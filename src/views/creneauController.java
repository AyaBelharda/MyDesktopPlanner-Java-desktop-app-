package views;

import java.io.IOException;
import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myDpPlanner.Creneau;
import myDpPlanner.EtatAvancement;
import myDpPlanner.EtatCreneau;
import myDpPlanner.Journee;
import myDpPlanner.Projet;
import myDpPlanner.Tache;
import myDpPlanner.TacheDecomposable;
import myDpPlanner.TacheSimple;
import myDpPlanner.Utilisateur;

public class creneauController {
	 private Stage stage;
	 private Scene scene;
	 private Parent root;
	
	 static Utilisateur user;
	 private Creneau creneau;
	 private Journee journee;
	 @FXML
	 Text debutCr;
	 
	 @FXML
	 Text finCr;
	 
	 @FXML
	 Text etatCr;
	 
	 @FXML
	 Text nomTache;
	 
	 @FXML
	 Text nomSTache;
	 
	 @FXML
	 Text etatAv;
	 
	 @FXML
	 Text etatAvS;
	 
	 @FXML
	 Text categorieIn;
	 
	 @FXML
	 Text prioriteIn;
	 
	 @FXML
	 Text deadLineIn;
	 
	 @FXML
	 Text dureeIn;
	 
	 @FXML
	 Text dureeSIn;
	 
	 @FXML 
	 Text periodiciteIn;
	 
	 @FXML
	 Button buttonText;
	 
	 @FXML
	 AnchorPane tachePane;
	 
	 static TreeMap<Tache, CheckBox> selectionList = new TreeMap<Tache, CheckBox>();
	 static TreeSet<Journee> propositionList;
	 
	 private AnchorPane thisPane;
	 private AnchorPane container;
	 private EtatAvancement newEtatAv;
	 
	public void setAtts(Utilisateur user, Creneau creneau, Journee jr, AnchorPane container) {
		this.container = container;
		creneauController.user = user;
		journee = jr;
		this.creneau = creneau;
		if(creneau.getEtatCreneau() == EtatCreneau.Libre) {
			initScreenVide();
		} else if(creneau.getEtatCreneau() != EtatCreneau.Libre) {
			if(creneau.getTache() instanceof TacheDecomposable) {
				initScreenDeco();
			} else {
				initScreenSimp();
			}
		}
		//initScreen();
	}
	
	public void initScreenDeco() {
		
		nomTache.setText(creneau.getTache().getNom());
		categorieIn.setText(creneau.getTache().getCategorie().getCategorie().toString());
		prioriteIn.setText(creneau.getTache().getPriorite().toString());
		etatAv.setText(creneau.getTache().getEtatAvancement().toString());
		dureeIn.setText(creneau.getTache().getDuree().toString() + " minutes");
		dureeSIn.setText(((TacheDecomposable) creneau.getTache()).getDureeTacheDecomposable() + " minutes");
		nomSTache.setText(((TacheDecomposable) creneau.getTache()).getNomSousTache());
		System.out.println("nom de la ous tache :" + ((TacheDecomposable) creneau.getTache()).getNomSousTache());
		etatAvS.setText(((TacheDecomposable) creneau.getTache()).getEtatSousTache().toString());
		Date deadLn = creneau.getTache().getDateLimite();
		if(deadLn != null) {
			deadLineIn.setText(creneau.getTache().getDateLimite().toString());
		}
		
		debutCr.setText(this.creneau.getHeureDebut().toString());
		finCr.setText(this.creneau.getHeureFin().toString());
		etatCr.setText(this.creneau.getEtatCreneau().toString());
		
	}
	
	public void initScreenSimp() {
	
		nomTache.setText(creneau.getTache().getNom());
		categorieIn.setText(creneau.getTache().getCategorie().getCategorie().toString());
		prioriteIn.setText(creneau.getTache().getPriorite().toString());
		etatAv.setText(creneau.getTache().getEtatAvancement().toString());
		dureeIn.setText(creneau.getTache().getDuree().toString() + " minutes");
		Date deadLn = creneau.getTache().getDateLimite();
		if(deadLn != null) {
			deadLineIn.setText(creneau.getTache().getDateLimite().toString());
		}
		
		debutCr.setText(this.creneau.getHeureDebut().toString());
		finCr.setText(this.creneau.getHeureFin().toString());
		etatCr.setText(this.creneau.getEtatCreneau().toString());
		periodiciteIn.setText(((TacheSimple) this.creneau.getTache()).getPeriodicite() + " jours");
		
	}
	
	
	public void initScreenVide() {
		
		debutCr.setText(this.creneau.getHeureDebut().toString());
		finCr.setText(this.creneau.getHeureFin().toString());
		etatCr.setText(this.creneau.getEtatCreneau().toString());
		
	}
	
	public void handleQuit() throws IOException {
//		  FXMLLoader loader = new FXMLLoader(getClass().getResource("calendrier.fxml"));
//		  Parent root = loader.load();
//		  calendarController controller = loader.getController();
//		  controller.setUser(user);
//		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//		  scene = new Scene(root);
//		  scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
//		  stage.setScene(scene);
//		  stage.setFullScreen(true);
//		  stage.show();
		container.getChildren().remove(tachePane);
		
	}
	
	public void handleClickReplannifier(ActionEvent event) throws IOException {
		handleQuit();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("replannifier.fxml"));
        thisPane = loader.load();
        replannifierCarte controller = loader.getController();
        controller.setAtts(user, creneau.getTache(), container);
        AnchorPane.setTopAnchor(thisPane, (container.getPrefHeight() - thisPane.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(thisPane, (container.getPrefWidth() - thisPane.getPrefWidth()) / 2);
        container.getChildren().add(thisPane);
        
	}
	
	
	public void uploadRefuserAccepter(String nomTache) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("refuserProposition.fxml"));
		root = loader.load();
		scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void handleBloquer() {
		if(creneau.getEtatCreneau() != EtatCreneau.Occupe) {
			warning("tu ne peut pas bloquer ce creneau car il est soit libre soit bloqué");
			return;
		}
		creneau.setEtatCreneau(EtatCreneau.Bloque);
	}
	
	public void handleFinTache() throws IOException {
		if(creneau.getTache() instanceof TacheDecomposable){
			((TacheDecomposable) creneau.getTache()).setEtatSousTache(EtatAvancement.Completed);
		} else {
			creneau.getTache().setEtatAvancement(EtatAvancement.Completed);
		}
		
		handleQuit();
	}
	
	public void handlePlannifierManuellement() throws IOException {
		//user.plannifierTacheManuellement(, creneau, null)
		handleQuit();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tachesShortCutMan.fxml"));
        thisPane = loader.load();
        tachesShortCutManController controller = loader.getController();
        controller.setList(creneau, journee, user, container);
        AnchorPane.setTopAnchor(thisPane, (container.getPrefHeight() - thisPane.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(thisPane, (container.getPrefWidth() - thisPane.getPrefWidth()) / 2);
        container.getChildren().add(thisPane);
        
	}
	
	public void handleEditAvancement() throws IOException {
		handleQuit();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("etatAvancement.fxml"));
        thisPane = loader.load();
        creneauController controller = loader.getController();
        controller.container = container;
        controller.creneau = creneau;
        
        AnchorPane.setTopAnchor(thisPane, (container.getPrefHeight() - thisPane.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(thisPane, (container.getPrefWidth() - thisPane.getPrefWidth()) / 2);
        container.getChildren().add(thisPane);
	}
	
	public void handleEditEtat() throws IOException {
		if(creneau.getTache() instanceof TacheDecomposable){
			((TacheDecomposable) creneau.getTache()).setEtatSousTache(newEtatAv);
		} else {
			creneau.getTache().setEtatAvancement(newEtatAv);
		}
		handleQuit();
	}
	
	public void handleReporter() throws IOException {
		handleQuit();
		int result;
		if(creneau.getTache() instanceof TacheDecomposable) {
			result = user.getCalendrier().reporter((TacheDecomposable) creneau.getTache(), user.getDureeMinimale());
			switch(result) {
				case -1:
					warning("pas en progres");
					break;
				
				case -2:
					warning("tache inexistante");
					break;
					
				case -3 :
					warning("espace insuffisant pour reporter");
					break;
				
				case 1:
					warning("date limite dépassée pour cette tache : " + creneau.getTache().getNom());

					break;
				
				case 2 :
					warning("pas de creneaux libres, impossible de continuer la plannification");
					break;
				
				
				case 4:
					warning("liste des journees est vide!");
					break;
			}
		} else {
			result = user.getCalendrier().reporter((TacheSimple) creneau.getTache(), user.getDureeMinimale());
			
			switch(result) {
			case -1:
				warning("Tache n'existe pas dans le planning");
				break;
			
			case -2:
				warning("Tache n'est pas en progres");
				break;

			case 1:
				warning("date limite dépassée pour cette tache : " + creneau.getTache().getNom());

				break;
			
			case 2 :
				warning("pas de creneaux libres, impossible de continuer la plannification");
				break;
			
			
			case 4:
				warning("liste des journees est vide!");
				break;
			}
		}
	}
	
	public void changedToCancelled() {
		newEtatAv = EtatAvancement.Cancelled;
	}
	
	public void changedToNotRealized() {
		newEtatAv = EtatAvancement.notRealised;
	}
	
	public void changedToInProgress() {
		newEtatAv = EtatAvancement.InProgress;
	}
	
	public void changedToCompleted() {
		newEtatAv = EtatAvancement.Completed;
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
