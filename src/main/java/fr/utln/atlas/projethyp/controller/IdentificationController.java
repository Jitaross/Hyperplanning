package fr.utln.atlas.projethyp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class IdentificationController {

    @FXML
    BorderPane identificationPane;
    @FXML
    TextField identifiant;
    @FXML
    PasswordField motDePasse;
    @FXML
    Button seConnecter;

    public void initialize(){
        seConnecter.setOnAction(event ->{
            String identifiantValue = identifiant.getText();
            String motDePasseValue = motDePasse.getText();
            System.out.println("Login = "+identifiantValue+" Mot de passe = "+motDePasseValue);
            System.out.println("Demande de connexion");

        });
    }

    public void connexionReussie(){

    }

    public void show(){
        this.identificationPane.setVisible(true);
    }

    public void hide(){
        this.identificationPane.setVisible(false);
    }

}
