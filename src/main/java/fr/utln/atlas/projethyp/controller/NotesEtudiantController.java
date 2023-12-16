package fr.utln.atlas.projethyp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class NotesEtudiantController {

    @FXML
    private ListView<String> devoirListView;
    @FXML
    private BorderPane notesPane;

    @FXML
    public void initialize() {
        this.notesPane.setVisible(false);
        // Initialisation des données de la liste des devoirs (remplacez cela par vos propres données)
        devoirListView.getItems().addAll("Devoir 1: 90%", "Devoir 2: 85%", "Devoir 3: 92%");
    }

    public void show(){
        this.notesPane.setVisible(true);
    }

    public void hide(){
        this.notesPane.setVisible(false);
    }
}
