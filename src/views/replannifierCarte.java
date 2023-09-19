package views;

import java.io.IOException;
import java.time.LocalDate;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import myDpPlanner.Creneau;
import myDpPlanner.EtatCreneau;
import myDpPlanner.Journee;
import myDpPlanner.Tache;
import myDpPlanner.TacheDecomposable;
import myDpPlanner.TacheSimple;
import myDpPlanner.Utilisateur;

public class replannifierCarte {
	 static Stage stage;
	 static Scene scene;
	 static Parent root;
	
	 static Utilisateur user;
	 private Tache tache;

	 private AnchorPane container;
	 
	 @FXML
	 AnchorPane thisPane;
	 
	 @FXML
	 TextField dureeSup;
	 
	 @FXML 
	 DatePicker ddline;
	 
	 @FXML
	 CheckBox checkBox;
	 
	 
	 static TreeMap<Tache, CheckBox> selectionList = new TreeMap<Tache, CheckBox>();
	 static TreeSet<Journee> propositionList;
	 
	public void setAtts(Utilisateur utili, Tache tc, AnchorPane container) {

		user = utili;
		this.tache = tc;
		this.container = container;
		ddline.setDisable(true);
		//initScreen();
	}
	
	public void handleAnnuler() {
		container.getChildren().remove(thisPane);
	}
	
	public void handleReplannifier() throws IOException {
		
		long dureeSupp;
		try {
			dureeSupp = Integer.parseInt(dureeSup.getText());
		} catch(Exception e) {
			warning("Durée supplémentaire invalide");
			return;
		}
		int result;
		if(checkBox.isSelected()) {
			if(ddline == null) {
				warning("vous n'avez pas selectionné la date");
				return;
			}
			if(ddline.getValue().isBefore(LocalDate.now())) {
				warning("deadline invalide!");
				return;
			}
			
			if(tache instanceof TacheDecomposable) {
				result = user.getCalendrier().replanifier((TacheDecomposable) tache, dureeSupp, user.getDureeMinimale(), ddline.getValue());
			} else {
				result = user.getCalendrier().replanifier((TacheSimple) tache, dureeSupp, user.getDureeMinimale(), ddline.getValue());
			}

		} else {
			if(tache instanceof TacheDecomposable) {
				result = user.getCalendrier().replanifier((TacheDecomposable) tache, dureeSupp, user.getDureeMinimale());
			} else {
				result = user.getCalendrier().replanifier((TacheSimple) tache, dureeSupp, user.getDureeMinimale());
			}
		}

		
		switch(result) {
		case -1 :
			warning("vous ne pouvez pas replannifier cette tache car elle n'est pas en progrès");
			break;
		case 0 : 
			//handleAccepterProp();
			
			break;
		case 1:
			warning("date limite dépassée pour cette tache : "  + tache.getNom());
			//handleAccepterProp();
			break;
		
		case 3 : 
			propositionList = user.getCalendrier().proposer((TacheDecomposable) tache, user.getDureeMinimale());
			user.afficherTaches();
			//proposer
			uploadRefuserAccepter();
			break;
		
		case 2 :
			warning("pas de creneaux libres, impossible de continuer la plannification");
			break;
		
		
		case 4:
			warning("liste des journees est vide!");
			break;
		}
		
		
	}
	
	public void handleRefuserProp() throws IOException {
		stage.close();
		user.getCalendrier().refuserProposition(propositionList);
		
	}
	
	public void handleAccepterProp() throws IOException {
		stage.close();
	}
	
	public void uploadRefuserAccepter() throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("refuserPropositionReplannifier.fxml"));
		root = loader.load();
		scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
		loader.setController(this);
		stage.show();
		
	}
	
	public void handleCheckBox() {
		ddline.setDisable(!checkBox.isSelected()); 
	}
	
	 public void warning(String message) {
			// create a new Alert instance
		  Alert alert = new Alert(AlertType.ERROR);

		  // set the title and header text
		  alert.setTitle("Error");
		  alert.setHeaderText("An error has occurred");

		  // set the content text
		  alert.setContentText(message);

		  // show the alert
		  alert.showAndWait(); 
	 }
}
