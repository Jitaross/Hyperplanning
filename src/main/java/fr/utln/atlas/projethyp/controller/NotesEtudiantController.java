package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.DevoirDAO;
import fr.utln.atlas.projethyp.daos.Page;
import fr.utln.atlas.projethyp.entities.Devoir;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.util.List;

public class NotesEtudiantController {

    @FXML
    private ListView<String> devoirListView;
    @FXML
    private BorderPane notesPane;

    @FXML
    public void initialize() throws DataAccessException {
        this.notesPane.setVisible(false);
        DevoirDAO devoirDAO = new DevoirDAO();
        Page<Devoir> pageDevoirs = devoirDAO.findNotesUser(10,1,MainController.getUserId());
        List<Devoir> devoirs = pageDevoirs.getResultList();
        for(Devoir d:devoirs){
            devoirListView.getItems().add(d.getTypeDevoir().toString()+" "+d.getNote()+" "+d.getCommentaire());
        }
    }

    public void show(){
        this.notesPane.setVisible(true);
    }

    public void hide(){
        this.notesPane.setVisible(false);
    }
}
