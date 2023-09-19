package views;

import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myDpPlanner.Utilisateur;

public class homeController {
	 private Stage stage;
	 private Scene scene;
	 private Parent root;
	 
	 static Utilisateur user;
	 
	 @FXML
	 private Text welcome;
	 
	 public void setUser(Utilisateur user) {
		 welcome.setText("welcome " + user.getCompte().getPseudo());
		 homeController.user = user;
	 }
	 
	 public void switchToLogin(ActionEvent event) throws IOException {
		 homeController.user.getCompte().seDeconncter();
		  Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	  
	 }
	 
	 public void switcToCalender(ActionEvent event) throws IOException {
		 
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("calendrier.fxml"));
		  Parent root = loader.load();
		  calendarController controller = loader.getController();
		  controller.setUser(user);
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	  
	 }
	 
	 public void switchToBadgesList(ActionEvent event) throws IOException {
		 
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("badgesPage.fxml"));
		  Parent root = loader.load();
		  badgesStatsController controller = loader.getController();
		  controller.init(user);
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	  
	 }
	 
	 public void switchToTachesList(ActionEvent event) throws IOException {
		 
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("tachesList.fxml"));
		  Parent root = loader.load();
		  tachesController controller = loader.getController();
		  controller.setUser(user);
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	  
	 }
	 
	 
	 public void switchToAccountSettings(ActionEvent event) throws IOException {
		 
		  Parent root = FXMLLoader.load(getClass().getResource("account.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	  
	 }
	 
	 public void switchToProjectsList(ActionEvent event) throws IOException {
		 
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
	 
	 
}
