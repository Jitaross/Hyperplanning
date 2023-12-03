package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.entities.Cours;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Locale;

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



    private static final int START_WEEK = 1;
    private static final int DAYS_IN_WEEK = 7;

    public void initialize() {
        this.planningPane.setVisible(false);

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
        LocalDate startDate = LocalDate.of(2023, 01, 02); // Lundi 02 Janvier 2023 (date de référence pour les calculs)
        startDate = startDate.plusWeeks(currentWeek - 1); // Ajoute le nombre du numéro de la semaine sélectionnée


        // Calcul des nouvelles dates à afficher
        for (int i = 0; i < DAYS_IN_WEEK-1; i++) {

            LocalDate currentDate = startDate.plusDays(i);

            // On n'affiche pas les dimanches
            if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }

            // On définie le bon format des dates, dates courtes en français. Exemple : "lun 2 janv."
            String dayText = currentDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.FRENCH).toString().substring(0, 3) + " " +
                    currentDate.format(DateTimeFormatter.ofPattern("d MMM"));

            // Positionnement des jours aux bons emplacements (à modifier)
            joursSemaine.get(i).setText(dayText);
            joursSemaine.get(i).setTranslateY(70);

            // Placement des cours de la semaine
            Cours test = Cours.builder().date(Date.valueOf("2023-12-07"))
                    .description("Le cours du test")
                    .debut(Time.valueOf("10:15:00"))
                    .fin(Time.valueOf("12:15:00"))
                    .build();
            this.ajouterCours(test);
            }
        return gridPane;
    }

    private void ajouterCours(Cours cours){
        LocalTime debut = cours.getDebut().toLocalTime();
        LocalTime fin = cours.getFin().toLocalTime();
        int rowSpan = (int) (debut.until(fin, ChronoUnit.MINUTES)) / 15;
        int column = cours.getDate().getDay();
        int row = (debut.getHour() - 8) * 4 + (debut.getMinute() / 15) + 1; // + 1 cause 8:00 is row 1 not 0
        this.planning.add(new TextArea(cours.getDescription()), column, row, 1, rowSpan);
    }

    public void show(){
        this.planningPane.setVisible(true);
    }

    public void hide(){
        this.planningPane.setVisible(false);
    }
}
