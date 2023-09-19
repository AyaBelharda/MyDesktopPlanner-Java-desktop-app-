package views;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import myDpPlanner.Projet;
import myDpPlanner.Utilisateur;

public class projectsController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	static Utilisateur user;
	
	@FXML
	private TextField nom;
	
	@FXML
	TextArea description;
	
	@FXML 
	AnchorPane projectPane;
	
	@FXML
	ListView projectList;
	

	
	static AnchorPane projectPaneSt;
	static AnchorPane infoPaneSt;
	
	
	
	public void setUser(Utilisateur user) {
		projectsController.user = user;
		fillList();
	}
	
    public void switchToHome(ActionEvent event) throws IOException {
		 
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
		  Parent root = loader.load();
		  homeController controller = loader.getController();
		  controller.setUser(user);
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	  
	 }
    
    public void handleAddPj(ActionEvent event) {
    	projectPaneSt = projectPane;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterProject.fxml"));
            infoPaneSt = loader.load();

            // Set the positioning of the pane within the AnchorPane
            AnchorPane.setTopAnchor(infoPaneSt, (projectPane.getHeight() - infoPaneSt.getPrefHeight()) / 2);
            AnchorPane.setLeftAnchor(infoPaneSt, (projectPane.getWidth() - infoPaneSt.getPrefWidth()) / 2);

            // Add the pane to the calendarPane
            projectPane.getChildren().add(infoPaneSt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	 
	 public void handleQuit() {
		 projectPaneSt.getChildren().remove(infoPaneSt);
	 }
	 
	 public void fillList() {
		 ObservableList<AnchorPane> listAnchors = FXCollections.observableArrayList();

		 for(Projet pj : user.getListeProjets()) {
			 
			    FXMLLoader loader = new FXMLLoader(getClass().getResource("projectIcon.fxml"));
			    AnchorPane temp;
			    try {
					temp =  loader.load();
					componentPjController controller = loader.getController();
					controller.setAtts(pj.getNomProjet(), pj.getDescription(), user, pj);
					listAnchors.add(temp);
					
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
		
		 }
		    projectList.setItems(listAnchors);
		   
	 }
	 
    public void addPj(ActionEvent event) {
    	
    	if(nom.getText() == null || nom.getText().length() == 0) {
    		warning("Vous n'avez pas asaisi le nom du projet");
    		return;
    	}
    	if(description.getText() == null || nom.getText().length() == 0) {
    		warning("Vous n'avez pas saisi une description");
    		return;
    	}
    	
    	if(!user.creerProjet(nom.getText(), description.getText())) {
    		warning("projet existant !");
    	}
    	handleQuit();
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
