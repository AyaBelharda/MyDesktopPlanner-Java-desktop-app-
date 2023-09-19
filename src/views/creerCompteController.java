package views;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import javafx.scene.control.PasswordField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;

import myDpPlanner.*;
public class creerCompteController {
	 

	 
	 private Stage stage;
	 private Scene scene;
	 private Parent root;
	 
	 @FXML
	 private TextField userName;
	 
	 @FXML
	 private PasswordField passWord;
	 
	 @FXML
	 private TextField response;

	 @FXML
	 private MenuButton ques;
	 @FXML 
	 private MenuItem firstMenuItem;
	 
	 @FXML 
	 private MenuItem secondMenuItem;	 
	 
	 @FXML 
	 private MenuItem thirdMenuItem;	
	 
	 @FXML 
	 private MenuItem fourthMenuItem;
	 
	 @FXML
	 TextField dureeMin;
	 
	 private String securityQuestion = null;
	 
	 public void handle1(ActionEvent event) {
		 this.securityQuestion = firstMenuItem.getText();
		 System.out.println(securityQuestion);
		 ques.setText(securityQuestion);
	 }
	 public void handle2(ActionEvent event) {
		 this.securityQuestion = secondMenuItem.getText();
		 System.out.println(securityQuestion);
		 ques.setText(securityQuestion);
	 }
	 public void handle3(ActionEvent event) {
		 this.securityQuestion = thirdMenuItem.getText();
		 System.out.println(securityQuestion);
		 ques.setText(securityQuestion);
	 }
	 public void handle4(ActionEvent event) {
		 this.securityQuestion = fourthMenuItem.getText();
		 System.out.println(securityQuestion);
		 ques.setText(securityQuestion);
	 }
	 
	 public void switchToLogin(ActionEvent event) throws IOException {
		 
		  Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
	  
	 }

	 public void handleCreateAccounte(ActionEvent event) throws IOException {
		  Validations validate = new Validations();
		  String userName, passWord, rep, ques;
		  long duree_min;
		  try {
			  duree_min = Long.parseLong(dureeMin.getText());
		  } catch (Exception e) {
				// create a new Alert instance
			  Alert alert = new Alert(AlertType.ERROR);

			  // set the title and header text
			  alert.setTitle("Error");
			  alert.setHeaderText("An error has occurred");

			  // set the content text
			  alert.setContentText("Duree minimale invalide!");

			  // show the alert
			  alert.showAndWait();  
			  return;
		  }
		  
		  try {
			  userName = this.userName.getText();
			  if(!validate.validUsername(userName)) {
				  throw new Exception();
			  }
		  } catch(Exception e) {
				// create a new Alert instance
			  Alert alert = new Alert(AlertType.ERROR);

			  // set the title and header text
			  alert.setTitle("Error");
			  alert.setHeaderText("An error has occurred");

			  // set the content text
			  alert.setContentText("Nom d'utilisateur non saisi!");

			  // show the alert
			  alert.showAndWait();  
			  return;
		  }
		  
		  try {
			  passWord = this.passWord.getText();
			  if(!validate.validPasWord(passWord)) {
				  throw new Exception();
			  }
		  } catch(Exception e) {
				// create a new Alert instance
			  Alert alert = new Alert(AlertType.ERROR);

			  // set the title and header text
			  alert.setTitle("Error");
			  alert.setHeaderText("An error has occurred");

			  // set the content text
			  alert.setContentText("Mot de passe invalide");

			  // show the alert
			  alert.showAndWait();  
			  return;
		  }
		  
		  try {
			  rep = this.response.getText();
			  if(!validate.validResponse(rep)) {
				  throw new Exception();
			  }
		  } catch(Exception e) {
				// create a new Alert instance
			  Alert alert = new Alert(AlertType.ERROR);

			  // set the title and header text
			  alert.setTitle("Error");
			  alert.setHeaderText("An error has occurred");

			  // set the content text
			  alert.setContentText("Reponse invalide");

			  // show the alert
			  alert.showAndWait();  
			  return;
		  }
		  if(this.securityQuestion == null) {
				// create a new Alert instance
			  Alert alert = new Alert(AlertType.ERROR);

			  // set the title and header text
			  alert.setTitle("Error");
			  alert.setHeaderText("An error has occurred");

			  // set the content text
			  alert.setContentText("Champ de question de securite est vide!");

			  // show the alert
			  alert.showAndWait();  
			  return;
	 		}
		  Utilisateur user = new Utilisateur(duree_min);
		  user.creerCompte(userName, passWord, this.securityQuestion, rep);
		  Main.app.ajouterUtilisateur(user);
//		  String userName = this.userName.getText();
//		  String passWord = this.passWord.getText();
//		  String rep = this.response.getText();
//		  String ques = this.securityQuesiton.getText();
//		  if(validate.validUsername(userName) ) {
//			  if(validate.validPasWord(passWord)) {
//				  if(validate.validResponse(rep)) {
//					//ajouter au myDpPlanner
//					  Utilisateur user = new Utilisateur(20);
//					  user.creerCompte(userName, passWord, ques, rep);
//					  Main.app.ajouterUtilisateur(user);
//				  }
//				  else {
//						// create a new Alert instance
//					  Alert alert = new Alert(AlertType.ERROR);
//
//					  // set the title and header text
//					  alert.setTitle("Error");
//					  alert.setHeaderText("An error has occurred");
//
//					  // set the content text
//					  alert.setContentText("Reponse invalide");
//
//					  // show the alert
//					  alert.showAndWait();  
//				  }
//				  
//			  }
//			  
//			  else {
//					// create a new Alert instance
//				  Alert alert = new Alert(AlertType.ERROR);
//
//				  // set the title and header text
//				  alert.setTitle("Error");
//				  alert.setHeaderText("An error has occurred");
//
//				  // set the content text
//				  alert.setContentText("Mot de passe invalide");
//
//				  // show the alert
//				  alert.showAndWait();
//			  } 
//			  
//		  } else {
//			// create a new Alert instance
//			  Alert alert = new Alert(AlertType.ERROR);
//
//			  // set the title and header text
//			  alert.setTitle("Error");
//			  alert.setHeaderText("An error has occurred");
//
//			  // set the content text
//			  alert.setContentText("Nom d'utilisateur invalide");
//
//			  // show the alert
//			  alert.showAndWait();
//		  }
	  
	 }
	 
	
	 public void handleStrock(KeyEvent event) {
//		 String userName = this.userName.getText();
//		 Validations validat = new Validations();
//		 System.out.println("b");
//		 if(!validat.validUsername(userName)) {
//			 System.out.println("a");
//			 this.userName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
//			 this.userName.requestLayout();
//			 this.userName.layout();
//
//		 }else {
//			 System.out.println("a");
//			 this.userName.setStyle(this.userName.getStyle() + "-fx-border-color: black; -fx-border-width: 2px;");
//			 this.userName.requestLayout();
//			 this.userName.layout(); 
//		 }
	 }
}
