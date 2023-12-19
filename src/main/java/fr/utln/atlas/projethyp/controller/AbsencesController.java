package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.AbsenceDAO;
import fr.utln.atlas.projethyp.daos.CoursDAO;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.entities.Absence;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class AbsencesController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane;
    private AbsenceDAO absenceDAO;
    @FXML
    private void initialize() throws DataAccessException, SQLException {
        this.borderPane.setVisible(false);
        absenceDAO = InitDAOS.absenceDAO;
        afficherAbsences();


    }

    private void afficherAbsences() throws DataAccessException, SQLException {
        CoursDAO coursDAO = InitDAOS.coursDAO;
        Cours c;
        List<Absence> absences = absenceDAO.findAbsenceEtudiant(MainController.getUserId());
        int i=0;
        for(Absence abs:absences){
            c = coursDAO.findCoursById(abs.getIdCours());


            TextArea date = new TextArea();
            TextArea cours = new TextArea();
            TextArea motif = new TextArea();

            date.setEditable(false);
            cours.setEditable(false);
            motif.setEditable(false);

            date.setText(c.getDate().toString());
            cours.setText(c.getDescription());
            motif.setText(abs.getMotif());

            this.gridPane.add(date,0,i);
            this.gridPane.add(cours,2,i);
            this.gridPane.add(motif,3,i);

            Button justifier = new Button();
            justifier.setText("Justifier");
            justifier.setId(String.valueOf(abs.getId()));
            justifier.setOnAction(event -> {
                try {
                    justifier(Integer.valueOf(justifier.getId()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (DataAccessException | IOException e) {
                    throw new RuntimeException(e);
                }
            });

            this.gridPane.add(justifier,4,i);

            i++;
        }

    }

    public byte[] fileToByteArray(File file) throws IOException {
        byte[] fileContent = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(fileContent);
        }
        return fileContent;
    }


    private void justifier(int id) throws IOException, SQLException, DataAccessException {
        File file = handleFileChooser();
        byte[] bytes = fileToByteArray(file);
        absenceDAO.insertJustificatif(id,bytes);
    }

    @FXML
    private File handleFileChooser() {
        FileChooser fileChooser = new FileChooser();

        // Configurer le FileChooser (optionnel)
        fileChooser.setTitle("Choisir un justificatif");
        fileChooser.getExtensionFilters().addAll(
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

        return selectedFile;
    }


    public void show(){
        this.borderPane.setVisible(true);
    }

    public void hide(){
        this.borderPane.setVisible(false);
    }
}
