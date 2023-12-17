package fr.utln.atlas.projethyp.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class AbsencesController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private void initialize(){
        this.borderPane.setVisible(false);
    }

    public void show(){
        this.borderPane.setVisible(true);
    }

    public void hide(){
        this.borderPane.setVisible(false);
    }
}
