package fr.utln.atlas.projethyp.planningview;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;

@Log
public class Controller {
    /**
     *
     */
    @FXML
    private Pagination planning_menu_pages;


    @FXML
    private void initialize() {

        planning_menu_pages.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int currentPageIndex = newValue.intValue();
            System.out.println("Vous avez changé de page. Nouveau numéro de page : " + (currentPageIndex + 1));
        });


    }
}
