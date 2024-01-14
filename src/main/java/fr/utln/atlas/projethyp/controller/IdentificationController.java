package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.App;
import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class IdentificationController {

    private static App app;

    @FXML
    BorderPane identificationPane;
    @FXML
    TextField identifiant;
    @FXML
    PasswordField motDePasse;
    @FXML
    Button seConnecter;
    @FXML
    Text textErreurLogin;
    @FXML
    ProgressBar progressBar;

    private int iduser;


    @FXML
    public void initialize() throws IOException {
        seConnecter.setOnAction(event -> {
            try {
                login();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        identifiant.setOnAction(event -> {
            try {
                login();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        motDePasse.setOnAction(event -> {
            try {
                login();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setApp(App app) {
        this.app = app;
    }

    public App getApp(){
        return this.app;
    }

    private void login() throws IOException{
        seConnecter.setVisible(false);
        progressBar.setVisible(true);
        // Créer une Task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String identifiantValue = identifiant.getText();
                String motDePasseValue = motDePasse.getText();

                iduser = -1;

                Authentication auth = new Authentication(identifiantValue,motDePasseValue);
                try {
                    UtilisateurDAO utilisateurDAO = InitDAOS.getUtilisateurDAO();
                    iduser = utilisateurDAO.login(auth);
                    MainController.setUserId(iduser);
                } catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }


                return null;
            }
        };

        // Lier la progression de la tâche à la ProgressBar
        progressBar.progressProperty().bind(task.progressProperty());

        // Gestionnaires pour les états de la Task
        task.setOnSucceeded(event -> {
            if(iduser>=0){
                //Affichage de la scène Accueil
                try {
                    app.showMainWindow();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                textErreurLogin.setText("L'identifiant ou le mot de passe est incorrect, veuillez réessayer.");
                seConnecter.setVisible(true);
                progressBar.setVisible(false);
            }
        });

        task.setOnFailed(event -> {
            new Thread(task).start();
        });


        new Thread(task).start();




    }


    public void show(){
        this.identificationPane.setVisible(true);
    }

    public void hide(){
        this.identificationPane.setVisible(false);
    }




}
