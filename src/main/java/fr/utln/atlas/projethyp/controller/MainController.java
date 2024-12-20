package fr.utln.atlas.projethyp.controller;

import fr.utln.atlas.projethyp.App;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.daos.UtilisateurDAO;

import fr.utln.atlas.projethyp.entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;

@Log
public class MainController {
	private static int userId = -1;

	private enum Page{
		ACCUEIL, PLANNING, ABSENCES, NOTES, APPEL
	}
	@FXML
	private AccueilController accueilController;
	@FXML
	private PlanningController planningController;
	@FXML
	private NotesEtudiantController notesEtudiantController;
	@FXML
	private AbsencesController absencesController;
	@FXML
	private AppelController appelController;

	@FXML
	private Button btnAccueil;
	@FXML
	private Button btnPlanning;
	@FXML
	private Button btnNotes;
	@FXML
	private Button btnAbsences;
	@FXML
	private Button btnAppel;
	@FXML
	private Text textInfos;
	@FXML
	private Button btnDeconnexion;

	private Page currentPage;

	public static void setUserId(int value){
		userId = value;
	}

	public static int getUserId(){
		return userId;
	}

	@FXML
	private void initialize() throws Exception {
		UtilisateurDAO userdao = InitDAOS.getUtilisateurDAO();
		Utilisateur user = userdao.findUtilisateur(userId);
		if(user.getTypeUser()== Utilisateur.TypeUser.Etudiant){
			textInfos.setText("Espace "+ user.getTypeUser()+" | "+user.getNom() + " " + user.getPrenom() + " ("+userdao.getNomFormationWithId(userId)+")");
		}
		else{
			textInfos.setText("Espace "+ user.getTypeUser()+" | "+user.getNom() + " " + user.getPrenom());
		}



		IdentificationController identificationController = new IdentificationController();


		this.btnDeconnexion.setOnAction(event -> {
			try {
				identificationController.getApp().showAuthWindow();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		this.currentPage = Page.ACCUEIL;
		this.btnAccueil.setOnAction(event -> showNewPage(Page.ACCUEIL));
		this.btnPlanning.setOnAction(event -> showNewPage(Page.PLANNING));
		this.btnNotes.setOnAction(event -> showNewPage(Page.NOTES));
		this.btnAbsences.setOnAction(event->showNewPage(Page.ABSENCES));
		if (user.getTypeUser() == Utilisateur.TypeUser.Enseignant) {
			this.btnAppel.setVisible(true);
			this.btnAppel.setDisable(false);
			this.btnAppel.setOnAction(actionEvent -> showNewPage(Page.APPEL));
		}
	}

	private void hideCurrentPage(){
		switch (this.currentPage){
			case ACCUEIL -> accueilController.hide();
			case PLANNING -> planningController.hide();
			case NOTES -> notesEtudiantController.hide();
			case ABSENCES -> absencesController.hide();
			case APPEL -> appelController.hide();
		}
	}

	private void showNewPage(Page nextPage){
		this.hideCurrentPage();
		this.currentPage = nextPage;
		switch (nextPage){
			case ACCUEIL -> accueilController.show();
			case PLANNING -> planningController.show();
			case NOTES -> notesEtudiantController.show();
			case ABSENCES -> absencesController.show();
			case APPEL -> appelController.show();
		}
	}

	@FXML
	private void openChangerMotDePasse() {
		try {
			InputStream fxmlStream = App.class.getResourceAsStream("changerMotDePasse.fxml");
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(fxmlStream);
			Stage popupStage = new Stage();
			popupStage.setResizable(false);
			popupStage.initModality(Modality.APPLICATION_MODAL);


			// Charger la scène depuis le fichier FXML
			Scene scene = new Scene(root);

			popupStage.setScene(scene);
			popupStage.setTitle("Changer mon mot de passe");
			popupStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace(); // Gérer l'exception de chargement du FXML
		}
	}


}
