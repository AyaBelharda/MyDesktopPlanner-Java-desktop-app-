package views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myDpPlanner.EtatAvancement;
import myDpPlanner.Journee;
import myDpPlanner.Projet;
import myDpPlanner.Tache;
import myDpPlanner.TacheDecomposable;
import myDpPlanner.TacheSimple;
import myDpPlanner.Utilisateur;

public class tacheShortCutController {
	 static Stage stage;
	 static Scene scene;
	 static Parent root;
	 
	static Utilisateur user;
	static ArrayList<linkedListNode> selectionList = new ArrayList<linkedListNode>();
		
	@FXML
	AnchorPane listPane;
	
	static AnchorPane parent;
	
	@FXML
	ListView listTaches;
	
	@FXML 
	CheckBox dateExist;
	
	@FXML
	DatePicker dateDeb;
	
	@FXML
	DatePicker dateFin;
	
	
	
	static TreeSet<Journee> propositionList;
	static boolean dateExists;
	  
	
	private class linkedListNode{
		private Tache tache;
		private CheckBox checkBox;
		public Tache getTache() {
			return tache;
		}
		public void setTache(Tache tache) {
			this.tache = tache;
		}
		public CheckBox getCheckBox() {
			return checkBox;
		}
		public void setRadioButton(CheckBox checkBox) {
			this.checkBox = checkBox;
		}
	}
	
	
	private void insertIntoList(linkedListNode node, ArrayList<linkedListNode> arrayList) {
		Iterator<linkedListNode> iter = arrayList.iterator();
		int index = 0;
		while(iter.hasNext()) {
			if(iter.next().getTache().getPriorite().ordinal() > node.getTache().getPriorite().ordinal()) {
				break;
			}
			index++;
		}
		
		arrayList.add(index, node);
		
	}
	
	public void setUser(Utilisateur utilis, AnchorPane p) throws IOException {
		user = utilis;
		parent = p;
		dateExists = false;
		initList();
	}
	
	public void initList() throws IOException {
		ObservableList<AnchorPane> listAnchors = FXCollections.observableArrayList();
		for(Tache tc : user.getListeTaches()) {
			if(tc.getEtatAvancement() != EtatAvancement.Unscheduled) {
				continue;
			}
			
			AnchorPane carte = uploadTacheCart(tc);
			listAnchors.add(carte);
		}
		
		listTaches.setItems(listAnchors);
		
		
	}
	

	public AnchorPane uploadTacheCart(Tache tc) throws IOException {
		CheckBox checkBox = new CheckBox();
		linkedListNode node = new linkedListNode();
		node.setTache(tc);
		node.setRadioButton(checkBox);
		insertIntoList(node, selectionList);
		AnchorPane container = new AnchorPane();
        checkBox.setLayoutX(10);
        checkBox.setLayoutY(10);
        
        HBox hbox = new HBox(); 
        
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("tacheCarte.fxml"));
	    AnchorPane innerPane = loader.load();
		tacheCarteController controller = loader.getController();
		controller.setUserTache(user, tc, null, null, "shortCut");
		
        
        hbox.getChildren().add(checkBox);
//        AnchorPane.setTopAnchor(checkBox, 10.0);
//        AnchorPane.setLeftAnchor(checkBox, 10.0);

        hbox.getChildren().add(innerPane);
        hbox.setHgrow(innerPane, Priority.ALWAYS);
        container.getChildren().add(hbox);
       
//        AnchorPane.setTopAnchor(innerPane, (container.getPrefHeight() - innerPane.getPrefHeight()) / 2);
//        AnchorPane.setLeftAnchor(innerPane, (container.getPrefWidth() - innerPane.getPrefWidth()) / 2);

		return container;
	}
	
	 public void switchTocal() throws IOException {
		  //listPane.getParent()
		  parent.getChildren().remove(listPane);
	 }
	 
	public void handlePlannifierAutomatiquement() throws IOException {
		if(selectionList.size() == 0) {
			warning("Vous n'avez pas selectionné une tache");
			return;
		}
		linkedListNode node =  selectionList.get(selectionList.size() - 1);
		Tache tempTache = node.getTache();
		CheckBox cb = node.getCheckBox();
		selectionList.remove(node);
		
		switchTocal();
		if(cb.isSelected()) {
			int result;
			if(dateExists) {
				if(dateDeb.getValue() == null || dateFin.getValue() == null) {
					warning("une des date est vide!");
					return;
				}
				
				result = user.plannifierTacheAutomatiquement(tempTache, dateDeb.getValue(), dateFin.getValue());
			} else {
				result = user.plannifierTacheAutomatiquement(tempTache);
			}

			switch(result) {
			case 0 : 
				if(selectionList.size() > 0) {
					handlePlannifierAutomatiquement();
				}
				
				break;
			case 1:
				warning("date limite dépassée pour cette tache : " + tempTache.getNom());
				if(selectionList.size() > 0) {
					handlePlannifierAutomatiquement();
				}
				break;
			
			case 3 : 
				System.out.println("tache nom" + tempTache.getNom());
				propositionList = user.getCalendrier().proposer((TacheDecomposable) tempTache, user.getDureeMinimale());
				//proposer
				uploadRefuserAccepter(tempTache.getNom());
				break;
			
			case 2 :
				warning("pas de creneaux libres, impossible de continuer la plannification");
				break;
			
			
			case 4:
				warning("liste des journees est vide!");
				break;
			}
			
		}
		
		if(selectionList.size() > 0) {
			handlePlannifierAutomatiquement();
		}
		
		
	}
	
	public void handleRefuserProp() throws IOException {
		stage.close();
		user.getCalendrier().refuserProposition(propositionList);
		if(selectionList.size() != 0) {
			handlePlannifierAutomatiquement();
		}
		
	}
	
	public void handleAccepterProp() throws IOException {
		stage.close();
		if(selectionList.size() != 0) {
			handlePlannifierAutomatiquement();
		}
	}
	
	public void uploadRefuserAccepter(String nomTache) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("refuserProposition.fxml"));
		root = loader.load();
		tacheShortCutController controller = loader.getController();
		//loader.setController(this);
		scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void handleCheck(ActionEvent event) {
		dateDeb.setDisable(!dateExist.isSelected());
		dateFin.setDisable(!dateExist.isSelected());
		dateExists =  dateExist.isSelected();
		
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
