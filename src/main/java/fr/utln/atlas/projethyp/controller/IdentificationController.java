package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.App;
import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class IdentificationController {

    private App app;

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

    private void login() throws IOException{
            String identifiantValue = identifiant.getText();
            String motDePasseValue = motDePasse.getText();
            System.out.println("Login = "+identifiantValue+" Mot de passe = "+motDePasseValue);
            System.out.println("Demande de connexion");

            int iduser = -1;

            Authentication auth = new Authentication(identifiantValue,motDePasseValue);
            try {
                UtilisateurDAO utilsateurDAO = new UtilisateurDAO();
                iduser = utilsateurDAO.login(auth);
                MainController.setUserId(iduser);
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
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
            }



    }


    public void show(){
        this.identificationPane.setVisible(true);
    }

    public void hide(){
        this.identificationPane.setVisible(false);
    }



}
