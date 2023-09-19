package views;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import myDpPlanner.Creneau;
import myDpPlanner.EtatAvancement;
import myDpPlanner.Journee;
import myDpPlanner.Projet;
import myDpPlanner.Tache;
import myDpPlanner.TacheDecomposable;
import myDpPlanner.Utilisateur;

public class tachesShortCutManController {



	 static Stage stage;
	 static Scene scene;
	 static Parent root;
	 
	static Utilisateur user;
	private Creneau creneau;
	private Journee journee;
	
	private HashMap<Tache, RadioButton> selectionList = new HashMap<Tache, RadioButton>();
	
	@FXML
	AnchorPane listPane;
	
	private AnchorPane parent;
	
	@FXML
	ListView listTaches;
	
	@FXML 
	CheckBox dateExist;
	
	@FXML
	DatePicker dateDeb;
	
	@FXML
	DatePicker dateFin;
	

	
	public void setList(Creneau cr, Journee jr, Utilisateur utili, AnchorPane parent) throws IOException {
		user = utili;
		journee = jr;
		creneau = cr;
		this.parent = parent;
		initList();
	}
	
	public void initList() throws IOException {
		ObservableList<AnchorPane> listAnchors = FXCollections.observableArrayList();
		ToggleGroup toggleGroup = new ToggleGroup();
		for(Tache tc : user.getListeTaches()) {

			if(tc.getEtatAvancement() != EtatAvancement.Unscheduled) {
				continue;
			}
			
			AnchorPane carte = uploadTacheCart(tc, toggleGroup);
			listAnchors.add(carte);
		}
		System.out.println("selection lit size" + user.getListeTaches().size());
		
		listTaches.setItems(listAnchors);
	}
	

	public AnchorPane uploadTacheCart(Tache tc, ToggleGroup toggleGroup) throws IOException {
		RadioButton radio = new RadioButton();
		selectionList.put(tc, radio);
		AnchorPane container = new AnchorPane();
		radio.setLayoutX(10);
		radio.setLayoutY(10);
        radio.setToggleGroup(toggleGroup);
        radio.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px;");
        HBox hbox = new HBox(); 
        
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("tacheCarte.fxml"));
	    AnchorPane innerPane = loader.load();
		tacheCarteController controller = loader.getController();
		controller.setUserTache(user, tc, null, null, "shortCut");
		
        
        hbox.getChildren().add(radio);
//        AnchorPane.setTopAnchor(checkBox, 10.0);
//        AnchorPane.setLeftAnchor(checkBox, 10.0);

        hbox.getChildren().add(innerPane);
        hbox.setHgrow(innerPane, Priority.ALWAYS);
        container.getChildren().add(hbox);
       
//        AnchorPane.setTopAnchor(innerPane, (container.getPrefHeight() - innerPane.getPrefHeight()) / 2);
//        AnchorPane.setLeftAnchor(innerPane, (container.getPrefWidth() - innerPane.getPrefWidth()) / 2);

		return container;
	}
	
	 public void switchToLtc() throws IOException {
		  parent.getChildren().remove(listPane);

	 }
	 
	 public void handlePlanifierManuellement() throws IOException {
		 if(selectionList.size() == 0) {
			 warning("liste est vide");
			 return;
		 }
		 for(Tache tc : selectionList.keySet()) {
			 if(selectionList.get(tc).isSelected()) {
				 boolean result = user.plannifierTacheManuellement(tc, creneau, journee.getDate());
				 if(!result) {
					 warning("on ne peut pas plannifier cette tache!");
				 }
				 break;
			 }
		 }
		 switchToLtc();
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
