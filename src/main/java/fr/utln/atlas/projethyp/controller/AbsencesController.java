package fr.utln.atlas.projethyp.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class AbsencesController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private void initialize(){
        this.borderPane.setVisible(false);

    }

    @FXML
    private void handleFileChooser() {
        FileChooser fileChooser = new FileChooser();

        // Configurer le FileChooser (optionnel)
        fileChooser.setTitle("Choisir un justificatif");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tous les Fichiers", "*.*"),
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
                // Ajouter d'autres filtres si nécessaire
        );

        // Afficher la boîte de dialogue de sélection du justificatif
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Traiter le fichier sélectionné
            System.out.println("Fichier sélectionné: " + selectedFile.getAbsolutePath());
        } else {
            // Aucun fichier n'a été sélectionné
            System.out.println("Sélection de fichier annulée.");
        }
    }

    public void show(){
        this.borderPane.setVisible(true);
    }

    public void hide(){
        this.borderPane.setVisible(false);
    }
}
