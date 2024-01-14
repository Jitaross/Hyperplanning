package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.App;
import fr.utln.atlas.projethyp.daos.*;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.Formation;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.java.Log;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Log
public class PlanningController {
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Label labelCoursSelectionne;
	@FXML
	private Button btnAjouterCours;
	@FXML
    private HBox hboxGestionCours;
    @FXML
    private ChoiceBox<Formation> choiceBoxFormation;
    @FXML
    private ChoiceBox<Integer> choiceBoxGroupe;
    @FXML
    private BorderPane planningPane;

    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane planning;
    @FXML
    private VBox vbox;

    @FXML
    private Pagination paginationPlanning;
    @FXML
    private Text jour0;
    @FXML
    private Text jour1;
    @FXML
    private Text jour2;
    @FXML
    private Text jour3;
    @FXML
    private Text jour4;
    @FXML
    private Text jour5;
    @Getter
    @FXML
    private ChoiceBox<Integer> anneeChoice;

    private CoursDAO coursDAO;
    private MatiereDAO matiereDAO;
    private UtilisateurDAO utilisateurDAO;
    private SalleDAO salleDAO;

    private FormationDAO formationDAO;

    private static final int START_WEEK = 1;
    private static final int DAYS_IN_WEEK = 7;

    private Integer annee = LocalDate.now().getYear();

    int userId = MainController.getUserId();

    private int currentFormation = 1;
    private int currentGroupe = 0;
    private boolean gestionCoursMode = false;
    private Stage popupStage = null;
    private TextAreaCours coursTemporaireTextArea = null;
    private TextAreaCours coursSelectionneTextArea = null;
    private boolean gestionnaireMode = false;

    @FXML
    public void initialize() throws DataAccessException {

        this.planningPane.setVisible(false);


        this.coursDAO = InitDAOS.getCoursDAO();
        this.utilisateurDAO = InitDAOS.getUtilisateurDAO();
        this.formationDAO = InitDAOS.getFormationDAO();
        this.salleDAO = InitDAOS.getSalleDAO();

        paginationPlanning.setCurrentPageIndex(START_WEEK); // On initialise à la première semaine de l'année (plus simple pour les calculs suivants)
        paginationPlanning.setPageFactory(this::createPage); // Notre méthode de création de page est createPage
        paginationPlanning.setCurrentPageIndex(LocalDate.now().get(java.time.temporal.WeekFields.of(java.util.Locale.getDefault()).weekOfWeekBasedYear())-1); // On place la pagination sur la semaine actuelle
        createPage(paginationPlanning.getCurrentPageIndex()); // On crée la page avec comme index la semaine actuelle
        paginationPlanning.setOnMouseClicked(event -> createPage(paginationPlanning.getCurrentPageIndex())); // Ajout de l'évènement du clique sur la pagination pour générer les nouvelles pages


        ObservableList<Integer> items = FXCollections.observableArrayList(LocalDate.now().getYear()+1,LocalDate.now().getYear(),LocalDate.now().getYear()-1,LocalDate.now().getYear()-2);
        anneeChoice.setItems(items);
        anneeChoice.getSelectionModel().select(1);

        anneeChoice.setOnAction(event -> {this.annee=anneeChoice.getValue();
                                            createPage(paginationPlanning.getCurrentPageIndex());});

        // A EXECUTER SI GESTIONNAIRE EDT (ET/OU Admin ?)
        if(this.utilisateurDAO.findUtilisateur(this.userId).getTypeUser() == Utilisateur.TypeUser.Gestionnaire){
            this.gestionnaireMode = true;

            // Affichage de la a
            this.hboxGestionCours.setVisible(true);
            this.choiceBoxGroupe.setDisable(true); // pour le moment
			this.btnAjouterCours.setOnAction(event -> this.openGestionCours());
            this.btnSupprimer.setOnAction(event -> {
                ButtonType oui = new ButtonType("Oui");
                ButtonType non = new ButtonType("Non");
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer ce cours ?",
                        oui,non);
                confirmationAlert.showAndWait()
                        .filter(response -> response == oui)
                        .ifPresent(response -> this.supprimerCours());

            });

            // Initialisation de la ChoiceBox de formation
			Page<Formation> pageFormation = this.formationDAO.findAll(1,20);
            List<Formation> listeFormation = pageFormation.getResultList();
            this.choiceBoxFormation.getItems().addAll(listeFormation);
            this.choiceBoxFormation.setValue(listeFormation.get(0));
            this.choiceBoxFormation.setOnAction(event -> {
                this.currentFormation = choiceBoxFormation.getValue().getId();
                try {
                    Page<Cours> pageCoursSemaine = coursDAO.findCoursSemaineFormation(START_WEEK +
                            paginationPlanning.getCurrentPageIndex(), this.currentFormation, 1, 10, this.annee);
                    this.afficherCours(pageCoursSemaine);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            });
        }


    }

    private GridPane createPage(Integer weekIndex) {
        // Liste permettant de gérer simplement les attributs text de la page
        ArrayList<Text> joursSemaine = new ArrayList<Text>();
        joursSemaine.add(jour0);
        joursSemaine.add(jour1);
        joursSemaine.add(jour2);
        joursSemaine.add(jour3);
        joursSemaine.add(jour4);
        joursSemaine.add(jour5);

        int currentWeek = START_WEEK + weekIndex;

        // Mise à jour des jours de la semaine en fonction du numéro de la semaine sélectionné
        LocalDate startDate = LocalDate.now(); // Obtient la date actuelle
        startDate = startDate.withYear(this.annee).with(WeekFields.ISO.weekOfWeekBasedYear(), 1).with(DayOfWeek.MONDAY);
        //LocalDate startDate = LocalDate.of(2023, 01, 02); // Lundi 02 Janvier 2023 (date de référence pour les calculs)
        startDate = startDate.plusWeeks(currentWeek - 1); // Ajoute le nombre du numéro de la semaine sélectionnée


        // Calcul des nouvelles dates à afficher
        for (int i = 0; i < DAYS_IN_WEEK-1; i++) {

            LocalDate currentDate = startDate.plusDays(i);

            // On n'affiche pas les dimanches
            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }

            // On définie le bon format des dates, dates courtes en français. Exemple : "lun 2 janv."
            String dayText = currentDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.FRENCH).substring(0, 3) + " " +
                    currentDate.format(DateTimeFormatter.ofPattern("d MMM"));

            // Positionnement des jours aux bons emplacements (à modifier)
            joursSemaine.get(i).setText(dayText);
            joursSemaine.get(i).setTranslateY(70);


        }
        // Placement des cours de la semaine

        // Enlève tous les cours présent à l'écran
        this.planning.getChildren().removeIf(TextArea.class::isInstance);
        try{
            Page<Cours> pageCoursSemaine;
            if(gestionnaireMode) {
                pageCoursSemaine = coursDAO.findCoursSemaineFormation(currentWeek, currentFormation, 1, 10, this.annee);
            }else{
                pageCoursSemaine = coursDAO.findCoursSemaineEtudiant(currentWeek,userId, 1, 10,this.annee);
            }

            this.afficherCours(pageCoursSemaine);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return gridPane;
    }
    // Placement des cours de la semaine
    private void afficherCours(Page<Cours> pageCoursSemaine){
        this.planning.getChildren().removeIf(TextArea.class::isInstance);
        List<Cours> cours = pageCoursSemaine.getResultList();
        for (Cours c : cours) {
            this.ajouterCours(c);
        }
    }

    // Placement d'un cours sur lz planning
    public TextAreaCours ajouterCours(Cours cours){

        HashMap<String, String> couleurs = new HashMap<String, String>();
        couleurs.put("CM", "-fx-control-inner-background:#ffeb7a;");
        couleurs.put("TD", "-fx-control-inner-background:#9fff90;");
        couleurs.put("TP", "-fx-control-inner-background:#d790ff;");
        couleurs.put("CC", "-fx-control-inner-background:#DFCAD6;");
        couleurs.put("EXAMEN", "-fx-control-inner-background:#bf94a2;");
        couleurs.put("EXAMTP", "-fx-control-inner-background:#bf94a2;");
        couleurs.put("REUNION", "-fx-control-inner-background:#7f82b2;");

        // Calcul de la taille d'un cours sur l'emploi du temps
        LocalTime debut = cours.getDebut().toLocalTime();
        LocalTime fin = cours.getFin().toLocalTime();
        long duree = debut.until(fin, ChronoUnit.MINUTES);
        int rowSpan = (int) (duree) / 15;
        int column = cours.getDate().getDay();
        int row = (debut.getHour() - 8) * 4 + (debut.getMinute() / 15) + 1; // + 1 cause 8:00 is row 1 not 0
        TextAreaCours coursTextArea = null;
        try{
            coursTextArea = new TextAreaCours(cours,cours.getDescription()+"\n"+utilisateurDAO.findUtilisateur(cours.getIdEnseignant()).getNom()+"\n"+salleDAO.find(5).get().getNomSalle());
        }catch(DataAccessException e){
            e.printStackTrace();
        }
        coursTextArea.setEditable(false);
        coursTextArea.setStyle(couleurs.get(cours.getTypeCours().toString()));

        // Le gestionnaire peut selectionner un cours pour le supprimer
        if(gestionnaireMode){
            TextAreaCours finalCoursTextArea = coursTextArea;
            coursTextArea.setOnMouseClicked(event ->{
                this.selectionnerCours(finalCoursTextArea);
            });
        }

        this.planning.add(coursTextArea, column, row, 1, rowSpan);
        return coursTextArea;
    }

    // Ajoute un cours sur l'emploi du temps lors de la prévisualition d'un ajout de cours par un gestionnaire
    public void ajouterCoursTemporaire(Cours cours){
        this.retirerCoursTemporaire();
        this.coursTemporaireTextArea = this.ajouterCours(cours);
    }
    public void retirerCoursTemporaire(){
        if(this.coursTemporaireTextArea != null){
            this.planning.getChildren().remove(this.coursTemporaireTextArea);
            this.coursTemporaireTextArea = null;
        }
    }

    // Affiche les informations d'un cours et un bouton pour le supprimer
    private void selectionnerCours(TextAreaCours cours){
        this.coursSelectionneTextArea = cours;
        this.labelCoursSelectionne.setVisible(true);
        String s = "ID: " + cours.getCours().getId() + " | " + cours.getCours().getDescription() +
                " | " + cours.getCours().getDate();
        this.labelCoursSelectionne.setText("Cours selectionné : " + s);
        this.btnSupprimer.setVisible(true);
    }
    private void deselectionnerCours(){
        this.coursSelectionneTextArea = null;
        this.labelCoursSelectionne.setVisible(false);
        this.btnSupprimer.setVisible(false);
    }

    // Ouvre une fentre pop-up pour l'ajout de cours
    @FXML
    private void openGestionCours(){
        if(!this.gestionCoursMode){
            try {
				// Permet de passer en mode "Gestion cours" et de désactiver le changement de formation et d'années
				this.setGestionCoursMode(true);

                //Création de la fenetre popup
                InputStream fxmlStream = App.class.getResourceAsStream("gestionCours.fxml");
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(fxmlStream);
                GestionCoursController controller = loader.getController();
                controller.init(this.currentFormation, this.annee, this);
				this.popupStage = new Stage();
                this.popupStage.setAlwaysOnTop(true);
                this.popupStage.setOnHiding(event -> {
                    this.setGestionCoursMode(false);
                    this.retirerCoursTemporaire();
                });
                this.popupStage.setResizable(false);
                Scene scene = new Scene(root);
                this.popupStage.setScene(scene);
                this.popupStage.setTitle("Ajouter un cours");
                this.popupStage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'exception de chargement du FXML
            }
        }
    }
    // Bloque le changement d'année, semaine et formation lors de l'ajout de cours
    public void setGestionCoursMode(boolean bool){
        this.gestionCoursMode = bool;
        this.anneeChoice.setDisable(bool);
        this.choiceBoxFormation.setDisable(bool);
        this.paginationPlanning.setDisable(bool);
    }

	private void supprimerCours(){
        try {
            System.out.println("fesfes");
            int id = this.coursSelectionneTextArea.getCours().getId();
            this.coursDAO.deleteCoursById(id);
            this.coursSelectionneTextArea.setVisible(false);
            this.deselectionnerCours();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
	@FXML
    public void exporterEmploiDuTemps() throws Exception {
        // Créer un nouveau calendrier
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Mon Emploi du Temps//iCal4j 1.0//FR"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        // Générateur d'UID pour les événements
        RandomUidGenerator ug = new RandomUidGenerator();
        List<Cours> lcours = coursDAO.findAllById(userId);

        // Pour chaque événement de l'emploi du temps
        for (Cours c:lcours) {
            java.util.Date date = c.getDate();
            Time time = c.getDebut();

            java.util.Calendar cal_deb = java.util.Calendar.getInstance();
            cal_deb.setTime(date);
            cal_deb.set(java.util.Calendar.HOUR_OF_DAY, time.getHours());
            cal_deb.set(java.util.Calendar.MINUTE, time.getMinutes());
            cal_deb.set(java.util.Calendar.SECOND, time.getSeconds());

            Time time_fin = c.getDebut();

            java.util.Calendar cal_fin = java.util.Calendar.getInstance();
            cal_fin.setTime(date);
            cal_fin.set(java.util.Calendar.HOUR_OF_DAY, time_fin.getHours());
            cal_fin.set(java.util.Calendar.MINUTE, time_fin.getMinutes());
            cal_fin.set(java.util.Calendar.SECOND, time_fin.getSeconds());



            // Créer un nouvel événement
            VEvent event = new VEvent(new DateTime(cal_deb.getTimeInMillis()),
                    new DateTime(cal_fin.getTimeInMillis()),
                    c.getDescription());

            // Ajouter un identifiant unique
            event.getProperties().add(ug.generateUid());

            // Ajouter l'événement au calendrier
            calendar.getComponents().add(event);

            // Ajoutez d'autres propriétés comme la description, le lieu, etc. si nécessaire
        }

        // Utiliser un FileChooser pour enregistrer le fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'emploi du temps");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("iCalendar Files", "*.ics"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileOutputStream fout = new FileOutputStream(file)) {
                CalendarOutputter outputter = new CalendarOutputter();
                outputter.output(calendar, fout);
            }
        }
    }

    public void show(){
        this.planningPane.setVisible(true);
    }

    public void hide(){
        this.deselectionnerCours();
        this.retirerCoursTemporaire();
        if(this.gestionCoursMode){
            this.popupStage.close();
            this.setGestionCoursMode(false);
        }
        this.planningPane.setVisible(false);
    }

}
