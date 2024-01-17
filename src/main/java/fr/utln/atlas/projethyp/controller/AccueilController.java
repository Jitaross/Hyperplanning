package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.*;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.Devoir;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

public class AccueilController {
	@FXML
	private Pane accueil;
	@FXML
	private GridPane gridPane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private DatePicker datePicker;
	@FXML
	private GridPane notesPane;

	@FXML
	private void initialize() throws DataAccessException {

		this.accueil.setVisible(true);
		CoursDAO coursDAO = InitDAOS.getCoursDAO();
		List<Cours> cours = coursDAO.findCoursJourEtudiant(Date.valueOf(LocalDate.now()),MainController.getUserId());
		for(Cours c:cours){
			this.ajouterCours(c);

		}
		datePicker.setOnAction(event->{
			this.gridPane.getChildren().removeIf(TextArea.class::isInstance);
			try {
				List<Cours> lcours = coursDAO.findCoursJourEtudiant(Date.valueOf(datePicker.getValue()),MainController.getUserId());
				for(Cours c:lcours){
					this.ajouterCours(c);
				}
			} catch (DataAccessException e) {
				throw new RuntimeException(e);
			}
		});

		MatiereDAO matiereDAO = InitDAOS.getMatiereDAO();
		DevoirDAO devoirDAO = InitDAOS.getDevoirDAO();
		Page<Devoir> pageDevoirs = devoirDAO.findNotesUser(5,1,MainController.getUserId());
		List<Devoir> devoirs = pageDevoirs.getResultList();
		int i = 2;
		for(Devoir d:devoirs) {
			//Ajouter devoir
			TextArea matiere = new TextArea();
			TextArea type = new TextArea();
			TextArea note = new TextArea();

			matiere.setEditable(false);
			type.setEditable(false);
			note.setEditable(false);

			int span=0;

			if(matiereDAO.findMatId(d.getIdMatiere()).length()>10){
				matiere.setText(NotesEtudiantController.ajouterSautsDeLigne(matiereDAO.findMatId(d.getIdMatiere()),10));
				span=span+((matiereDAO.findMatId(d.getIdMatiere()).length())/10);


			}else{
				matiere.setText(matiereDAO.findMatId(d.getIdMatiere()));
			}

			type.setText("D" + d.getTypeDevoir().toString().substring(0, 1));
			note.setText(String.valueOf(d.getNote()));

			this.notesPane.add(matiere, 0, i,1,span);
			this.notesPane.add(type, 1, i,1,span);
			this.notesPane.add(note, 2, i,1,span);
			if(span>0)i--;
			i = i+1+span;
		}
	}

	private void ajouterCours(Cours cours){


		HashMap<String, String> couleurs = new HashMap<String, String>();
		couleurs.put("CM", "-fx-control-inner-background:#ffeb7a;");
		couleurs.put("TD", "-fx-control-inner-background:#9fff90;");
		couleurs.put("TP", "-fx-control-inner-background:#d790ff;");
		couleurs.put("CC", "-fx-control-inner-background:#DFCAD6;");
		couleurs.put("EXAMEN", "-fx-control-inner-background:#bf94a2;");
		couleurs.put("EXAMTP", "-fx-control-inner-background:#bf94a2;");
		couleurs.put("REUNION", "-fx-control-inner-background:#7f82b2;");

		LocalTime debut = cours.getDebut().toLocalTime();
		LocalTime fin = cours.getFin().toLocalTime();
		long duree = debut.until(fin, ChronoUnit.MINUTES);
		int rowSpan = (int) (duree) / 15;
		int row = (debut.getHour() - 8) * 4 + (debut.getMinute() / 15) + 1; // + 1 cause 8:00 is row 1 not 0


		TextArea coursTextArea = new TextArea(cours.getDescription());
		coursTextArea.setEditable(false);


		coursTextArea.setStyle(couleurs.get(cours.getTypeCours().toString()));


		this.gridPane.add(coursTextArea, 1, row, 1, rowSpan);
	}

	public void show(){
		this.accueil.setVisible(true);
	}

	public void hide(){
		this.accueil.setVisible(false);
	}
}
