package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class ChangerMotDePasseController {

    @FXML
    PasswordField oldPassword;
    @FXML
    PasswordField newPassword;
    @FXML
    PasswordField newPasswordRepeated;
    @FXML
    Button cancelButton;
    @FXML
    Text statusText;


    @FXML
    private void initialize(){

    }

    @FXML
    private void cancel(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void confirm() throws DataAccessException {
        if((newPassword.getText()).equals(newPasswordRepeated.getText())) {
            UtilisateurDAO userDAO = new UtilisateurDAO();
            if(userDAO.modifyMDP(userDAO.getMailWithId(MainController.getUserId()), oldPassword.getText(), newPassword.getText())>=0){
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
            else{
                statusText.setText("L'ancien mot de passe n'est pas correct.");
            }

        }
        else{
            statusText.setText("Les deux mots de passe ne correspondent pas.");
        }
    }
}
