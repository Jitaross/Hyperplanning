package fr.utln.atlas.projethyp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import fr.utln.atlas.projethyp.daos.CoursDAO;
import fr.utln.atlas.projethyp.daos.Page;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.DateSemaine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import org.h2.tools.Server;

import static fr.utln.atlas.projethyp.entities.DateSemaine.JourSemaine;

import java.io.IOException;
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("main.fxml"));
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);

        primaryStage.setTitle("Hyper-planning");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
  public static void main(String[] args) {
        /*
        try {
            // Création d'une instance de CoursDAO
            CoursDAO coursDAO = new CoursDAO();

            // Appel de la méthode findCoursSemaine depuis l'instance de CoursDAO
            Page<Cours> pageCoursSemaine = coursDAO.findCoursSemaine(47, 1, 10);

            // Utilisation des résultats ou traitement de la page de cours obtenue
            List<Cours> cours = pageCoursSemaine.getResultList();
            for (Cours c : cours) {
                // Faites quelque chose avec chaque cours
                System.out.println(c.getDescription());
            }
            Time debut = Time.valueOf("09:00:00");
            Time fin = Time.valueOf("10:30:00");
            Date date = Date.valueOf("2023-11-27");
            coursDAO.persist("Cours de fun en barre",1,1, 1, debut,  fin,  date );
            coursDAO.persist("Cours de fun en barre2",101,101, 101, debut,  fin,  date );
            coursDAO.persist("Cours de fun en barre3",1,1, 1, debut,  fin,  date );
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

         */
        Time debut = Time.valueOf("09:00:00");
        Time fin = Time.valueOf("10:30:00");
        Duration diff = Duration.between((Temporal) debut, (Temporal) fin);
        System.out.println(diff);

    }

    static void loadProperties(String propFileName) throws IOException {
        Properties properties = new Properties();
        InputStream inputstream = App.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputstream == null) throw new FileNotFoundException();
        properties.load(inputstream);
        System.setProperties(properties);
    }

    static void configureLogger() {
        //Regarder src/main/ressources/logging.properties pour fixer le niveau de log
        String path;
        path = Objects.requireNonNull(App.class.getClassLoader().getResource("logging.properties")).getFile();
        System.setProperty("java.util.logging.config.file", path);
    }
}
