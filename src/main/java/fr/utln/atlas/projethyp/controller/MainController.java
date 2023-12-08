package fr.utln.atlas.projethyp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.java.Log;

@Log
public class MainController {

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

	private Page currentPage;
	@FXML
	private void initialize(){
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
