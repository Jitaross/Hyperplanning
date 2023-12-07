package fr.utln.atlas.projethyp.entities;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

public class DateSemaine {
    public static List<Date> JourSemaine(int numeroSemaine, int annee) {
        List<Date> joursSemaine = new ArrayList<>();
        LocalDate date = LocalDate.now(); // Obtient la date actuelle
        date = date.withYear(annee).with(WeekFields.ISO.weekOfWeekBasedYear(), numeroSemaine).with(DayOfWeek.MONDAY); // Obtient le lundi de la semaine spécifiée

        // Boucle pour obtenir tous les jours de la semaine
        for (int i = 0; i < 6; i++) { // Boucle du lundi au samedi
            joursSemaine.add(Date.valueOf(date)); // Ajoute la date à la liste
            date = date.plusDays(1); // Passe au jour suivant
        }

        return joursSemaine;
    }
}