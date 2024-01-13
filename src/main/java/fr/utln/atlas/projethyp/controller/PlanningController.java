package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.*;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import lombok.extern.java.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
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
import java.util.List;
import java.util.Locale;
import java.util.HashMap;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;

@Log
public class PlanningController {

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

    @FXML
    private ChoiceBox<Integer> anneeChoice;

    private CoursDAO coursDAO;
    private MatiereDAO matiereDAO;
    private UtilisateurDAO utilisateurDAO;
    private SalleDAO salleDAO;


    private static final int START_WEEK = 1;
    private static final int DAYS_IN_WEEK = 7;

    private Integer annee = LocalDate.now().getYear();

    int userId = MainController.getUserId();


    @FXML
    public void initialize() throws DataAccessException {

        this.planningPane.setVisible(false);


        this.coursDAO = InitDAOS.getCoursDAO();
        this.utilisateurDAO = InitDAOS.getUtilisateurDAO();
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
            Page<Cours> pageCoursSemaine = coursDAO.findCoursSemaineEtudiant(currentWeek,userId, 1, 10,this.annee);
            List<Cours> cours = pageCoursSemaine.getResultList();
            for (Cours c : cours) {
                this.ajouterCours(c);
            }
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }
        return gridPane;
    }

    private void ajouterCours(Cours cours) throws DataAccessException, SQLException {

        HashMap<String, String> couleurs = new HashMap<String, String>();
        couleurs.put("CM", "-fx-control-inner-background:#ffeb7a;");
        couleurs.put("TD", "-fx-control-inner-background:#9fff90;");
        couleurs.put("TP", "-fx-control-inner-background:#d790ff;");
        couleurs.put("CC", "-fx-control-inner-background:#DFCAD6;");
        couleurs.put("EXAMEN", "-fx-control-inner-background:#bf94a2;");
        couleurs.put("EXAMTP", "-fx-control-inner-background:#bf94a2;");
        couleurs.put("REUNION", "-fx-control-inner-background:#7f82b2;");

        LocalTime debut = cours.getDebut().toLocalTime();
        LocalTime fin = cours.getFin().toLocalTime();
        long duree = debut.until(fin, ChronoUnit.MINUTES);
        int rowSpan = (int) (duree) / 15;
        int column = cours.getDate().getDay();
        int row = (debut.getHour() - 8) * 4 + (debut.getMinute() / 15) + 1; // + 1 cause 8:00 is row 1 not 0

        /* A REPRENDRE QUAND IL Y AURA LES DAOS FONCTIONNELLES
        String matiere = "";
        String nomProf = "";
        LocalTime dureeLT = LocalTime.ofSecondOfDay(duree);
        String dureeCours =  dureeLT.getHour() + "h" + dureeLT.getMinute();
        try{
            matiere = this.matiereDAO.findMatId(cours.getIdMatiere());
            nomProf = this.utilisateurDAO.findUtilisateurNom(cours.getIdEnseignant());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        String text = matiere + "\n\n" +
                    nomProf + "\n" +
                    dureeCours;
        */
        TextArea coursTextArea = new TextArea(cours.getDescription()+"\n"+utilisateurDAO.findUtilisateur(cours.getIdEnseignant()).getNom()+"\n"+salleDAO.find(5).get().getNomSalle());
        coursTextArea.setEditable(false);

        coursTextArea.setStyle(couleurs.get(cours.getTypeCours().toString()));


        this.planning.add(coursTextArea, column, row, 1, rowSpan);
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
        this.planningPane.setVisible(false);
    }

}
