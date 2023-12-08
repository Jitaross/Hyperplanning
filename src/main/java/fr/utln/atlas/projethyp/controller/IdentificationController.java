package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.App;
import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Data;

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
    public void initialize() throws IOException {
        seConnecter.setOnAction(event ->{
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


        });
    }

    public void setApp(App app) {
        this.app = app;
    }


    public void show(){
        this.identificationPane.setVisible(true);
    }

    public void hide(){
        this.identificationPane.setVisible(false);
    }



}
