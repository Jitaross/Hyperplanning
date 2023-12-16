package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.CoursDAO;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
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
	private void initialize() throws DataAccessException {

		this.accueil.setVisible(true);
		CoursDAO coursDAO = new CoursDAO();
		List<Cours> cours = coursDAO.findCoursJourEtudiant(Date.valueOf(LocalDate.now()),MainController.getUserId());
		for(Cours c:cours){
			this.ajouterCours(c);

		}
	}

	private void ajouterCours(Cours cours){

		HashMap<String, String> couleurs = new HashMap<String, String>();
		couleurs.put("CM", "-fx-control-inner-background:#ffeb7a;");
		couleurs.put("TD", "-fx-control-inner-background:#9fff90;");
		couleurs.put("TP", "-fx-control-inner-background:#d790ff;");
		couleurs.put("CC", "-fx-control-inner-background:#9fff90;");
		couleurs.put("EXAM", "-fx-control-inner-background:#9fff90;");
		couleurs.put("EXAMTP", "-fx-control-inner-background:#9fff90;");
		couleurs.put("REUNION", "-fx-control-inner-background:#9fff90;");

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
