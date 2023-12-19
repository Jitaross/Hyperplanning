package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.DevoirDAO;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.daos.MatiereDAO;
import fr.utln.atlas.projethyp.daos.Page;
import fr.utln.atlas.projethyp.entities.Devoir;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class NotesEtudiantController {

    @FXML
    private GridPane gridPane;
    @FXML
    private BorderPane notesPane;
    @FXML
    private StackedBarChart<String,Number> moyennes;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis;

    @FXML
    public void initialize() throws DataAccessException {
        this.notesPane.setVisible(false);

        MatiereDAO matiereDAO = InitDAOS.getMatiereDAO();

        // Affichages des notes
        DevoirDAO devoirDAO = InitDAOS.getDevoirDAO();
        Page<Devoir> pageDevoirs = devoirDAO.findNotesUser(10,1,MainController.getUserId());
        List<Devoir> devoirs = pageDevoirs.getResultList();
        int i = 1;
        for(Devoir d:devoirs){
            //Ajouter devoir
            TextArea matiere = new TextArea();
            TextArea type = new TextArea();
            TextArea note = new TextArea();
            TextArea commentaire = new TextArea();

            matiere.setEditable(false);
            type.setEditable(false);
            note.setEditable(false);
            commentaire.setEditable(false);

            matiere.setText(matiereDAO.findMatId(d.getIdMatiere()));
            type.setText("D"+d.getTypeDevoir().toString().substring(0,1));
            note.setText(String.valueOf(d.getNote()));

            int span=0;
            if((d.getCommentaire().length())>=75){
                span = (d.getCommentaire().length()/75)+1;
                commentaire.setText(ajouterSautsDeLigne(d.getCommentaire(),75));
                this.gridPane.add(commentaire,3,i,1,span);
                this.gridPane.add(matiere,0,i,1,span);
                this.gridPane.add(type,1,i,1,span);
                this.gridPane.add(note,2,i,1,span);
                span--;
            }else{
                commentaire.setText(d.getCommentaire());
                this.gridPane.add(commentaire,3,i);
                this.gridPane.add(matiere,0,i);
                this.gridPane.add(type,1,i);
                this.gridPane.add(note,2,i);
            }

            i = i+1+span;
        }

        // Affichages des moyennes
        //Map<String, Double> notes = Map.of("Maths", 15.46, "Physique", 13.5, "Chimie", 17.0,"Informatique",18.0,"IA",12.68,"Cybersécurité",13.5);
        Map<String, Double> notes = devoirDAO.findMoyennesUser(MainController.getUserId());
        for (Map.Entry<String, Double> entry : notes.entrySet()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            moyennes.getData().add(series);
        }

        categoryAxis.setCategories(FXCollections.observableArrayList(notes.keySet()));
        moyennes.setLegendVisible(false);

    }

    public String ajouterSautsDeLigne(String texte, int longueurLigne) {
        StringBuilder texteModifie = new StringBuilder(texte.length());
        int index = 0;

        while (index < texte.length()) {
            // Prendre un sous-ensemble de 80 caractères ou le reste si moins de 80 caractères
            if (index + longueurLigne < texte.length()) {
                texteModifie.append(texte.substring(index, index + longueurLigne));
                texteModifie.append("\n");
            } else {
                texteModifie.append(texte.substring(index));
            }
            index += longueurLigne;
        }

        return texteModifie.toString();
    }

    public void show(){
        this.notesPane.setVisible(true);
    }

    public void hide(){
        this.notesPane.setVisible(false);
    }
}
