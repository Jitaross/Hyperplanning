package fr.utln.atlas.projethyp.controller;
import fr.utln.atlas.projethyp.daos.*;
import fr.utln.atlas.projethyp.entities.*;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class GestionCoursController {
	@FXML
	private VBox vboxGestionCours;
	@FXML
	private Button btnPrevisualisation;
	@FXML
	private Button btnConfirmer;
	@FXML
	private Button btnAnnuler;
	@FXML
	private DatePicker datePicker;
	@FXML
	private ChoiceBox<Matiere> choiceBoxMatiere;
	@FXML
	private ChoiceBox<Integer> choiceBoxEnseignant;
	@FXML
	private ChoiceBox<Salle> choiceBoxSalle;
	@FXML
	private ChoiceBox<String> choiceBoxHeureDebut;
	@FXML
	private ChoiceBox<String> choiceBoxHeureFin;
	@FXML
	private ChoiceBox<Cours.TypeCours> choiceBoxTypeCours;
	private int formation;
	private int annee;
	private PlanningController planningController;
	private Cours cours = null;
	private MatiereDAO matiereDAO;
	private UtilisateurDAO utilisateurDAO;
	private SalleDAO salleDAO;
	private CoursDAO coursDAO;


	@FXML
	private void initialize(){
		this.matiereDAO = InitDAOS.getMatiereDAO();
		this.utilisateurDAO = InitDAOS.getUtilisateurDAO();
		this.salleDAO = InitDAOS.getSalleDAO();
		this.coursDAO = InitDAOS.getCoursDAO();
	}
	void init(int formation, int annee, PlanningController planningController){
		this.formation = formation;
		this.annee = annee;
		this.planningController = planningController;
		try{
			Page<Matiere> pageMatiere = this.matiereDAO.findMatFormation(formation,1,20);
			List<Matiere> listeMatiere = pageMatiere.getResultList();
			this.choiceBoxMatiere.getItems().addAll(listeMatiere);


			/* A CORRIGER
			Page<Utilisateur> pageUtilisateur = this.utilisateurDAO.findAll(1,50);
			List<Utilisateur> listeUtilisateur = pageUtilisateur.getResultList();*/
			this.choiceBoxEnseignant.getItems().add(1);

			Page<Salle> pageSalle = this.salleDAO.findAll(1,50);
			List<Salle> listeSalle = pageSalle.getResultList();
			this.choiceBoxSalle.getItems().addAll(listeSalle);
		}catch(DataAccessException e) {
			e.printStackTrace();
		}
		this.choiceBoxTypeCours.getItems().addAll(Cours.TypeCours.values());
		this.initChoiceBoxSalle();

		this.btnAnnuler.setCancelButton(true);
		this.btnAnnuler.setOnAction(event -> this.close());

		this.btnPrevisualisation.setOnAction(event -> {
			this.cours = creationCours();
			if(this.cours != null) this.planningController.ajouterCoursTemporaire(this.cours);
		});
		this.btnConfirmer.setOnAction((event -> {
			this.cours = creationCours();
			if(this.cours != null){
				try{
					coursDAO.persist(this.cours);
					this.planningController.ajouterCours(this.cours);
					this.showAlert(Alert.AlertType.CONFIRMATION, this.vboxGestionCours.getScene().getWindow(), "Ajout de cours réussi", "Le cours a bien été ajouté dans la base de donneés");
					this.close();
				}catch (DataAccessException e) {
					e.printStackTrace();
				}
			}
		}));

	}

	private void initChoiceBoxSalle(){
		String sMin;
		for(int h=8;h<20;h++){
			for(int m=0;m<60;m+=15){
				if(m == 0) sMin = "00";
				else sMin = String.valueOf(m);
				this.choiceBoxHeureDebut.getItems().add(h + ":" + sMin);
			}
		}
		// Ajuster la liste des heures de fin en fonction de l'heure de début choisie
		this.choiceBoxHeureDebut.setOnAction(event -> {
			this.choiceBoxHeureFin.getItems().clear();
			this.updateChoiceBoxHeureFin(this.choiceBoxHeureDebut.getSelectionModel().getSelectedItem());
		});
	}

	private void updateChoiceBoxHeureFin(String time){
		String sMin;
		String[] timeArr = time.split(":");
		int m;
		boolean firstTime = true;
		for(int h = Integer.parseInt(timeArr[0]);h<20;h++){
			if(firstTime) m = Integer.parseInt(timeArr[1]);
			else m = 0;
			for(;m<60;m+=15){
				if(firstTime) firstTime = false;
				else{
					if (m == 0) sMin = "00";
					else sMin = String.valueOf(m);
					this.choiceBoxHeureFin.getItems().add(h + ":" + sMin);
				}
			}
		}
		this.choiceBoxHeureFin.getItems().add("20:00");
	}

	private Cours creationCours(){
		if(choiceBoxMatiere.getSelectionModel().isEmpty()){
			showAlert(Alert.AlertType.ERROR,this.vboxGestionCours.getScene().getWindow(), "Erreur", "Veuillez selectionner une matière");
			return null;
		}
		if(choiceBoxEnseignant.getSelectionModel().isEmpty()){
			showAlert(Alert.AlertType.ERROR,this.vboxGestionCours.getScene().getWindow(), "Erreur", "Veuillez selectionner un(e) enseignant(e)");
			return null;
		}
		if(choiceBoxSalle.getSelectionModel().isEmpty()){
			showAlert(Alert.AlertType.ERROR,this.vboxGestionCours.getScene().getWindow(), "Erreur", "Veuillez selectionner une salle");
			return null;
		}
		if(choiceBoxHeureDebut.getSelectionModel().isEmpty()){
			showAlert(Alert.AlertType.ERROR,this.vboxGestionCours.getScene().getWindow(), "Erreur", "Veuillez selectionner une heure de début");
			return null;
		}
		if(choiceBoxHeureFin.getSelectionModel().isEmpty()){
			showAlert(Alert.AlertType.ERROR,this.vboxGestionCours.getScene().getWindow(), "Erreur", "Veuillez selectionner une heure de fin");
			return null;
		}

		if(choiceBoxTypeCours.getSelectionModel().isEmpty()){
			showAlert(Alert.AlertType.ERROR,this.vboxGestionCours.getScene().getWindow(), "Erreur", "Veuillez selectionner un type de cours");
			return null;
		}
		if(datePicker.getValue() == null){
			showAlert(Alert.AlertType.ERROR,this.vboxGestionCours.getScene().getWindow(), "Erreur", "Veuillez selectionner une date");
			return null;
		}
		int idMatiere = choiceBoxMatiere.getSelectionModel().getSelectedItem().getId();
		int idSalle = choiceBoxSalle.getSelectionModel().getSelectedItem().getId();
		Date date = Date.valueOf(datePicker.getValue());
		Time debut = Time.valueOf(choiceBoxHeureDebut.getSelectionModel().getSelectedItem() + ":00");
		Time fin = Time.valueOf(choiceBoxHeureFin.getSelectionModel().getSelectedItem() + ":00");
		Cours.TypeCours typeCours = choiceBoxTypeCours.getSelectionModel().getSelectedItem();

		return Cours.builder().idEnseignant(1).idMatiere(idMatiere).idSalle(idSalle)
				.date(date).debut(debut).fin(fin).typeCours(typeCours)
				.description(typeCours + " de " + choiceBoxMatiere.getSelectionModel().getSelectedItem().getNomMatiere()).build();

	}

	private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(owner);
		alert.show();
	}
	@FXML
	private void close(){
		this.planningController.retirerCoursTemporaire();
		this.planningController.setGestionCoursMode(false);
		Stage stage = (Stage) btnAnnuler.getScene().getWindow();
		stage.close();
	}
}
