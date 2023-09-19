package views;

import java.io.IOException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myDpPlanner.Categorie;
import myDpPlanner.Couleur;
import myDpPlanner.EtatAvancement;
import myDpPlanner.Priorite;
import myDpPlanner.Projet;
import myDpPlanner.Tache;
import myDpPlanner.TacheDecomposable;
import myDpPlanner.TacheSimple;
import myDpPlanner.Utilisateur;

public class projectTachesController {
	
	 private Stage stage;
	 private Scene scene;
	 private Parent root;
	 
	static Projet project;
	static Utilisateur user;
	
	@FXML
	Text pjName;
	
	@FXML
	ListView tacheListView; 
	
	@FXML
	AnchorPane tachePane;
	
	
	public void setPj(Projet pj, Utilisateur utilis) {
		project = pj;
		user = utilis;
		pjName.setText(project.getNomProjet());
		initTaches();
	}
	
	 public void switchToPj(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("projectsList.fxml"));
		  Parent root = loader.load();
		  projectsController controller = loader.getController();
		  controller.setUser(user);
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	  
	 }
	 
	 public void Evaluer(ActionEvent event) {
		 
	 }
	 
	 public void initTaches() {
		 ObservableList<AnchorPane> listAnchors = FXCollections.observableArrayList();
		 for(Tache tc : project.getListeTaches()) {
			 
			    FXMLLoader loader = new FXMLLoader(getClass().getResource("tacheCarte.fxml"));
			    AnchorPane temp;
			    try {
					temp =  loader.load();
					tacheCarteController controller = loader.getController();
					controller.setUserTache(user, tc, project, tachePane, "project");
					listAnchors.add(temp);
					
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
		
		 }
		 tacheListView.setItems(listAnchors);
	 }
	 
	 public void handleAjouterTaches(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("tachesShortCutPj.fxml"));
		  AnchorPane tableTaches = loader.load();
		  tachesShortCutPjController controller = loader.getController();
		  controller.setProject(project, user, tachePane);
		  
         // Set the positioning of the pane within the AnchorPane
         AnchorPane.setTopAnchor(tableTaches, (tachePane.getHeight() - tableTaches.getPrefHeight()) / 2);
         AnchorPane.setLeftAnchor(tableTaches, (tachePane.getWidth() - tableTaches.getPrefWidth()) / 2);
         tachePane.getChildren().add(tableTaches);
	 }
}
