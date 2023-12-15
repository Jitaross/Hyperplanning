package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.*;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.java.Log;

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

    private CoursDAO coursDAO;
    private MatiereDAO matiereDAO;
    private UtilisateurDAO utilisateurDAO;

    private static final int START_WEEK = 1;
    private static final int DAYS_IN_WEEK = 7;

    int userId = MainController.getUserId();



    public void initialize() throws DataAccessException {

        this.planningPane.setVisible(false);


        try{
            this.coursDAO = new CoursDAO();
            this.utilisateurDAO = new UtilisateurDAO();
            /*this.matiereDAO = new MatiereDAO();*/

        }catch(DataAccessException e) {
            e.printStackTrace();
        }

        paginationPlanning.setCurrentPageIndex(START_WEEK); // On initialise à la première semaine de l'année (plus simple pour les calculs suivants)
        paginationPlanning.setPageFactory(this::createPage); // Notre méthode de création de page est createPage
        paginationPlanning.setCurrentPageIndex(LocalDate.now().get(java.time.temporal.WeekFields.of(java.util.Locale.getDefault()).weekOfWeekBasedYear())-1); // On place la pagination sur la semaine actuelle
        createPage(paginationPlanning.getCurrentPageIndex()); // On crée la page avec comme index la semaine actuelle
        paginationPlanning.setOnMouseClicked(event -> createPage(paginationPlanning.getCurrentPageIndex())); // Ajout de l'évènement du clique sur la pagination pour générer les nouvelles pages
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
        startDate = startDate.withYear(LocalDate.now().getYear()).with(WeekFields.ISO.weekOfWeekBasedYear(), 1).with(DayOfWeek.MONDAY);
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
            Page<Cours> pageCoursSemaine = coursDAO.findCoursSemaineEtudiant(currentWeek,userId, 1, 10);
            List<Cours> cours = pageCoursSemaine.getResultList();
            for (Cours c : cours) {
                this.ajouterCours(c);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return gridPane;
    }

    private void ajouterCours(Cours cours){

        HashMap<String, String> couleurs = new HashMap<String, String>();
        couleurs.put("CM", "-fx-control-inner-background:#ffeb7a;");
        couleurs.put("TD", "-fx-control-inner-background:#9fff90;");
        couleurs.put("TP", "-fx-control-inner-background:#d790ff;");
        couleurs.put("CC", "-fx-control-inner-background:#9fff90;");
        couleurs.put("EXAM", "-fx-control-inner-background:#9fff90;");
        couleurs.put("EXAMTP", "-fx-control-inner-background:#9fff90;");
        couleurs.put("REUNION", "-fx-control-inner-background:#9fff90;");

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
        TextArea coursTextArea = new TextArea(cours.getDescription());
        coursTextArea.setEditable(false);


        coursTextArea.setStyle(couleurs.get(cours.getTypeCours().toString()));


        this.planning.add(coursTextArea, column, row, 1, rowSpan);
    }

    public void show(){
        this.planningPane.setVisible(true);
    }

    public void hide(){
        this.planningPane.setVisible(false);
    }

}
