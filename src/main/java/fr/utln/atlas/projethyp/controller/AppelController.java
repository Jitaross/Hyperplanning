package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.CoursDAO;
import fr.utln.atlas.projethyp.daos.EnseignantDAO;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.entities.*;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class AppelController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label labelCoursSel;
    @FXML
    private Label labelDateCours;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Button validateAppel;
    private CoursDAO coursDAO;
    private List<Cours> coursList;
    private String heureCours;

    @FXML
    private void initialize() throws DataAccessException {
        this.anchorPane.setVisible(false);
        this.coursDAO = InitDAOS.getCoursDAO();
        EnseignantDAO enseignantDAO = InitDAOS.getEnseignantDAO();
        this.coursList = enseignantDAO.findAllCoursIdDay(MainController.getUserId(), Date.valueOf(LocalDate.now()));
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Cours cours : this.coursList)
            items.add(String.valueOf(cours.getDebut()));
        this.heureCours = String.valueOf(coursList.get(0).getDebut());
        this.labelCoursSel.setText(coursList.get(0).getDescription());
        this.labelDateCours.setText(String.valueOf(coursList.get(0).getDate()));
        afficherAppel(coursList.get(0).getId());

        choiceBox.setItems(items);

        choiceBox.getSelectionModel().select(0);

        choiceBox.setOnAction(event -> {
            this.heureCours = choiceBox.getValue();
            try {
                this.labelCoursSel.setText(coursList.get(choiceBox.getSelectionModel().getSelectedIndex()).getDescription());
                this.labelDateCours.setText(String.valueOf(coursList.get(choiceBox.getSelectionModel().getSelectedIndex()).getDate()));
                afficherAppel(coursList.get(choiceBox.getSelectionModel().getSelectedIndex()).getId());
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void afficherAppel(int idCours) throws DataAccessException {

        if(this.validateAppel.isDisable()) this.validateAppel.setDisable(false);

        List<Etudiant> etudiantList = this.coursDAO.findAllEtudiant(idCours);
        Appel appel = new Appel(idCours, etudiantList);
        int i = 0;
        for (Etudiant etudiant : etudiantList) {
            Utilisateur utilisateur = InitDAOS.getUtilisateurDAO().findUtilisateur(etudiant.getId());

            TextArea name = new TextArea(utilisateur.getNom());
            TextArea firstName = new TextArea(utilisateur.getPrenom());
            TextArea dateBirth = new TextArea(String.valueOf(utilisateur.getDateNaissance()));

            CheckBox checkBox = new CheckBox();

            checkBox.setIndeterminate(false);

            checkBox.setOnAction(actionEvent -> {
                if (checkBox.isSelected()) appel.addEtudiantAbsent(etudiant);
                else appel.removeEtudiantAbsent(etudiant);
            });

            dateBirth.setEditable(false);
            name.setEditable(false);
            firstName.setEditable(false);

            this.gridPane.add(checkBox, 0, i);
            this.gridPane.add(name,1,i);
            this.gridPane.add(firstName,2,i);
            this.gridPane.add(dateBirth,3,i);

            i++;

            this.validateAppel.setOnAction(actionEvent -> {
                try {
                    appel.etudiantAbsentConfirme();
                    this.validateAppel.setDisable(true);
                } catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void show(){
        this.anchorPane.setVisible(true);
    }

    public void hide(){
        this.anchorPane.setVisible(false);
    }

}
