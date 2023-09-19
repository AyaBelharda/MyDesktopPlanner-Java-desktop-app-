package views;

import java.io.IOException;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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

public class tacheCarteController {
	static Utilisateur user;
	static Projet project;
	private Tache tache;
	private AnchorPane parentPane;
	private String couleur;
	@FXML
	AnchorPane carte;
	
	@FXML
	Text categorieC;
	
	@FXML
	Text typeC;
	
	@FXML
	Text prioriteC;
	
	@FXML
	Text etatAv;
	
	@FXML
	Text ddLine;
	
	@FXML
	Text nomT;
	
	@FXML 
	Text dureeT;
	
	@FXML
	AnchorPane headerPane;
	
	@FXML
	AnchorPane leftPane;
	
	@FXML
	AnchorPane rightPane;
	
	@FXML
	AnchorPane pane;
	
	@FXML 
	Button buttonDelete;
	
    static String nom;
    static long duree;
    static Priorite prioriteT;
    static Date dateLimite;
    static Categorie categorieT;
    static EtatAvancement etatAvancement;
    static boolean isDecomposable;
    static int periodiciteT;
    static Couleur couleurT;
	
    private String weAreIn;
    
	public void setUserTache(Utilisateur user, Tache tache, Projet pj, AnchorPane pane, String where) {
		this.tache = tache;
		project = pj;
		this.parentPane = pane;
		tacheCarteController.user = user;
		categorieC.setText(tache.getCategorie().getCategorie());
		nomT.setText(tache.getNom());
		if(tache instanceof TacheDecomposable) {
			typeC.setText("Tache decomposable");
		} else {
			typeC.setText("Tache simple");
		}
		
		prioriteC.setText(tache.getPriorite().toString());
		etatAv.setText(tache.getEtatAvancement().toString());
		if(tache.getDateLimite() != null) {
			ddLine.setText("Date limite :" + tache.getDateLimite().toString());
		}
		
		dureeT.setText(tache.getDuree().toString());
		weAreIn = where;
		if(weAreIn == "shortCut") {
			buttonDelete.setVisible(false); 
		}
		
		headerPane.setStyle(headerPane.getStyle() + "-fx-background-color:" + tache.getCategorie().getCouleur().getCouleur() + ";");
		leftPane.setStyle(headerPane.getStyle() + "-fx-background-color:" + tache.getCategorie().getCouleur().getCouleur() + ";");
		rightPane.setStyle(headerPane.getStyle() + "-fx-background-color: " +  tache.getCategorie().getCouleur().getCouleur()  + ";");
	}
	
	 public void handleSupprimerTache(ActionEvent event) throws IOException {
		 if(weAreIn == "project") {
			 project.supprimerTache(tache);
		 } else if(weAreIn == "tache") {
			 user.supprimerTache(tache);
		 } 
		 else {
			 return;
		 }
		 pane.setVisible(false);
	 }
	 
	 public void handleModifierTache(ActionEvent event) throws IOException {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("infoTache.fxml"));
	            pane = loader.load();

	            // Set the positioning of the pane within the AnchorPane
	            AnchorPane.setTopAnchor(pane, (parentPane.getHeight() - pane.getPrefHeight()) / 2);
	            AnchorPane.setLeftAnchor(pane, (parentPane.getWidth() - pane.getPrefWidth()) / 2);

	            // Add the pane to the calendarPane
	            parentPane.getChildren().add(pane);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  
	 }
	 
}
