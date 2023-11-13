package fr.utln.atlas.projethyp.planningview;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;

@Log
public class Controller {
    /**
     *
     */
    @FXML
    private TextField inputField;
    @FXML
    private Button addButton;
    @FXML
    private TextArea outputArea;

    private StringProperty name = new SimpleStringProperty();

    @FXML
    private void initialize() {
        addButton.disableProperty().bind(inputField.textProperty().isEmpty());
        inputField.textProperty().bindBidirectional(name);

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            //addButton.setDisable(inputField.textProperty().isEmpty().get());
            log.info("email changed from %s to %s.".formatted(oldValue, newValue));
        });

        inputField.setOnAction(event->{
            outputArea.appendText(inputField.getText()+System.getProperty("line.separator"));
            inputField.clear();
        });

        addButton.setOnAction(event->{
            outputArea.appendText(inputField.getText()+System.getProperty("line.separator"));
            inputField.clear();
        });

    }
}
