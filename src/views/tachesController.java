package views;


import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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

public class tachesController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	static Utilisateur user;
	
	@FXML 
	TextField nom;
	
	@FXML 
	TextField duree;
	
	@FXML 
	TextField categorie;
	
	@FXML 
	TextField periodicite;
	
	@FXML
	TextField heurDb;
	
	@FXML
	TextField minDeb;
	
	@FXML
	DatePicker date;
	
	@FXML
	MenuButton couleur;
	
	@FXML
	MenuButton type;
	
	@FXML 
	MenuItem red;
	
	@FXML
	MenuItem yellow;
	
	@FXML 
	MenuItem rose;
	
	@FXML
	MenuItem purple;
	
	@FXML 
	MenuItem black;
	
	@FXML
	MenuItem brown;
	
	@FXML
	MenuItem cyan;
	
	@FXML
	MenuItem green;
	
	@FXML
	MenuItem blue;
	
	@FXML 
	MenuItem tSimple;
	
	@FXML
	MenuItem tDeco;
	
	@FXML
	MenuItem high;
	
	@FXML
	MenuItem medium;
	
	@FXML
	MenuItem low;
	
	@FXML
	MenuButton priorite;
	
	@FXML 
	AnchorPane tachePane;
	
	
	@FXML
	private ListView listTaches;
	static AnchorPane toQuitAnchor;
	static AnchorPane infoPane;
	
    static String nomT;
    static long dureeT;
    static Priorite prioriteT;
    static Date dateLimite;
    static Categorie categorieT;
    static EtatAvancement etatAvancement;
    static boolean isDecomposable;
    static int periodiciteT;
    static Couleur couleurT;
    
	public void setUser(Utilisateur user) {
		tachesController.user = user;
		initTaches();
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
	 
	 public void handleAjouterTache(ActionEvent event) {
		 	toQuitAnchor = tachePane;
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("infoTache.fxml"));
	            infoPane = loader.load();

	            // Set the positioning of the pane within the AnchorPane
	            AnchorPane.setTopAnchor(infoPane, (tachePane.getHeight() - infoPane.getPrefHeight()) / 2);
	            AnchorPane.setLeftAnchor(infoPane, (tachePane.getWidth() - infoPane.getPrefWidth()) / 2);

	            // Add the pane to the calendarPane
	            tachePane.getChildren().add(infoPane);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
	 
	 public void handleQuit(ActionEvent event) {
		 toQuitAnchor.getChildren().remove(infoPane);
	 }
	 
	 public void Evaluer(ActionEvent event) {
		 
	 }
	 
	 public void initTaches() {
		 ObservableList<AnchorPane> listAnchors = FXCollections.observableArrayList();
		 for(Tache tc : user.getListeTaches()) {
			 	if(tc.getEtatAvancement() == EtatAvancement.Cancelled) {
			 		continue;
			 	}
			    FXMLLoader loader = new FXMLLoader(getClass().getResource("tacheCarte.fxml"));
			    AnchorPane temp;
			    try {
					temp =  loader.load();
					tacheCarteController controller = loader.getController();
					controller.setUserTache(user, tc, null, tachePane, "tache");
					listAnchors.add(temp);
					
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
		
		 }
		 listTaches.setItems(listAnchors);
	 }
	 
	 public void ajouterTache(ActionEvent event) {
		 Tache tc;
		 Validations validate = new Validations();
		 nomT = nom.getText();
		 if(nomT == null) {
			 warning("nom n'est pas valide!");
			 return;
		 }
		 categorieT = new Categorie(categorie.getText(), couleurT); 
		 if(categorieT == null) {
			 warning("nom n'est pas valide!");
			 return;
		 }
		 //dateLimite = date.getValue();
		 try {
			 dureeT = Integer.parseInt(duree.getText());
		 }catch(Exception e) {
			 warning("Duree saisi est invalide");
		 }
		 
		 LocalDate dateLim = date.getValue();
		 if(dateLim == null) {
			 warning("vous n'avez pas saisi la date limite!" );
			 return;
		 }
		 
		 if(dateLim.isBefore(LocalDate.now())) {
			 warning("la date limite saisi est invalide, la date d'aujoudhui est :" + LocalDate.now());
			 return;
		 }
		 
		 LocalTime lclTm;
		 try {
			 int heurDeb = Integer.parseInt(heurDb.getText());
			 int minDb =  Integer.parseInt(minDeb.getText());
			 lclTm = LocalTime.of(heurDeb, minDb);
		 } catch(Exception e) {
			 warning("les horaires saisi ne sont pas correctes!");
			 return;
		 }
	        // Combine local time and local date to create LocalDateTime
	        LocalDateTime localDateTime = LocalDateTime.of(dateLim, lclTm);
	        
	        // Convert LocalDateTime to Instant
	        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
	        
	        // Convert Instant to java.util.Date
	        dateLimite = Date.from(instant);
		   
		 
		 if(isDecomposable) {
			 if(dateLimite == null) {
				 tc = new TacheDecomposable(nomT, dureeT, prioriteT, categorieT);
			 }
			 else {
				 tc = new TacheDecomposable(nomT, dureeT, prioriteT, dateLimite,  categorieT);
			 }
		 } else {
			 try {
				 periodiciteT = Integer.parseInt(periodicite.getText());
			 } catch(Exception e) {
				 warning("periodicite doit etre un entier");
				 return;
			 }
			 
			 if(dateLimite == null) {
				 tc = new TacheSimple(nomT, dureeT, prioriteT, categorieT, periodiciteT);
			 }
			 else {
				 tc = new TacheSimple(nomT, dureeT, prioriteT, dateLimite, categorieT, periodiciteT);
			 }	 
		 }
		 
		 if(prioriteT == null) {
			 warning("vous n'avez pas saisi la priorite");
			 return;
		 }
		 
		 if(couleurT == null) {
			 warning("vous n'avez pas saisi la couleur");
			 return;
		 }
		 
		 user.ajouterTache(tc);
		 
		 
	 }
	 
	 public void handleYellow(ActionEvent event) {
		 couleurT = Couleur.valueOf(yellow.getText());
		 couleur.setText(yellow.getText());
	 }
	 
	 public void handleRed(ActionEvent event) {
		 couleurT = Couleur.valueOf(red.getText());
		 couleur.setText(red.getText());	 
	 }
	 
	 public void handleViolet(ActionEvent event) {
		 couleurT = Couleur.valueOf(purple.getText());
		 couleur.setText(purple.getText()); 
	 }
	 
	 public void handleCyan(ActionEvent event) {
		 couleurT = Couleur.valueOf(cyan.getText());
		 couleur.setText(cyan.getText()); 
	 }
	 
	 public void handleBlue(ActionEvent event) {
		 couleurT = Couleur.valueOf(blue.getText());
		 couleur.setText(blue.getText());
	 }
	 
	 public void handleBrwon(ActionEvent event) {
		 couleurT = Couleur.valueOf(brown.getText());
		 couleur.setText(brown.getText());
	 }
	 
	 public void handleRose(ActionEvent event) {
		 couleurT = Couleur.valueOf(rose.getText());
		 couleur.setText(rose.getText());
	 }
	 
	 public void handleGreen(ActionEvent event) {
		 couleurT = Couleur.valueOf(green.getText());
		 couleur.setText(green.getText());
	 }
	 
	 public void handleBlack(ActionEvent event) {
		 couleurT = Couleur.valueOf(black.getText());
		 couleur.setText(black.getText());
	 }
	 
	 public void handleTsimple(ActionEvent event) {
		 type.setText(tSimple.getText());
		 isDecomposable = false;
		 periodicite.setDisable(false);
		 
	 }
	 
	 public void handleTDeco(ActionEvent event) {
		 type.setText(tDeco.getText());
		 isDecomposable = true;
		 periodicite.setDisable(true);
	 }
	 
	 public void handleHigh(ActionEvent event) {
		 priorite.setText(high.getText());
		 prioriteT = Priorite.valueOf(high.getText());
	 }
	 
	 public void handleMedium(ActionEvent event) {
		 priorite.setText(medium.getText());
		 prioriteT = Priorite.valueOf(medium.getText());
	 }
	 
	 public void handleLow(ActionEvent event) {
		 priorite.setText(low.getText());
		 prioriteT = Priorite.valueOf(low.getText());
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
