package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.controller.IdentificationController;
import fr.utln.atlas.projethyp.controller.MainController;
import fr.utln.atlas.projethyp.daos.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.Server;

import static fr.utln.atlas.projethyp.entities.DateSemaine.JourSemaine;

/**
 * Hello world!
 *
 */
public class App extends Application {


    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showAuthWindow();
    }

    // Méthode pour afficher la fenêtre d'authentification
    private void showAuthWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("identification.fxml"));
        Parent root = loader.load();

        IdentificationController identificationController = loader.getController();
        identificationController.setApp(this);

        primaryStage.setTitle("Hyper-planning | Authentification");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // Méthode appelée après une authentification réussie
    public void showMainWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();

        // Associer le contrôleur à la fenêtre principale
        MainController mainController = loader.getController();

        // Fournir les données nécessaires au contrôleur principal si nécessaire

        primaryStage.setTitle("Hyper-planning | Accueil");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
