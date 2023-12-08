package fr.utln.atlas.projethyp.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class AccueilController {
	@FXML
	private Pane accueil;

	@FXML
	private void initialize(){
		this.accueil.setVisible(true);
	}

	public void show(){
		this.accueil.setVisible(true);
	}

	public void hide(){
		this.accueil.setVisible(false);
	}
}
