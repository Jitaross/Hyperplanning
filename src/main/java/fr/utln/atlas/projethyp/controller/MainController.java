package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lombok.extern.java.Log;

@Log
public class MainController {
	private static int userId = -1;

	private enum Page{
		ACCUEIL, PLANNING, ABSENCES, NOTES
	}
	@FXML
	private AccueilController accueilController;
	@FXML
	private PlanningController planningController;

	@FXML
	private Button btnAccueil;
	@FXML
	private Button btnPlanning;
	@FXML
	private Text textInfos;

	private Page currentPage;

	public static void setUserId(int value){
		userId = value;
	}

	public static int getUserId(){
		return userId;
	}

	@FXML
	private void initialize() throws DataAccessException {
		UtilisateurDAO userdao = new UtilisateurDAO();
		textInfos.setText(userdao.findUtilisateur(userId).getNom()+" "+userdao.findUtilisateur(userId).getPrenom()+" ("+")");
		this.currentPage = Page.ACCUEIL;
		this.btnAccueil.setOnAction(event -> showNewPage(Page.ACCUEIL));
		this.btnPlanning.setOnAction(event -> showNewPage(Page.PLANNING));
	}

	private void hideCurrentPage(){
		switch (this.currentPage){
			case ACCUEIL -> accueilController.hide();
			case PLANNING -> planningController.hide();
		}
	}

	private void showNewPage(Page nextPage){
		this.hideCurrentPage();
		this.currentPage = nextPage;
		switch (nextPage){
			case ACCUEIL -> accueilController.show();
			case PLANNING -> planningController.show();
		}
	}
}
