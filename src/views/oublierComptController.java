package views;

import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import myDpPlanner.Utilisateur;
public class oublierComptController {

	 private Stage stage;
	 private Scene scene;
	 private Parent root;
	 
	 public void switchToLogin(ActionEvent event) throws IOException {
		 
		  Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
	  
	 }
	 
	 @FXML
	 TextField userName;
	 @FXML
	 TextField response;
	 @FXML
	 PasswordField passWord;
	 @FXML 
	 Text questSecrete;
	 private Utilisateur user = null;
	 private boolean allowed = false;
	 public void handleSearch(){
		 Validations validate = new Validations();
		 String userName;
		  try {
			  System.out.println("aaaaaa");
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
		  this.user = Main.app.rechercherUtilisateur(userName);
		  if(this.user == null) {
				// create a new Alert instance
			  Alert alert = new Alert(AlertType.ERROR);

			  // set the title and header text
			  alert.setTitle("Error");
			  alert.setHeaderText("An error has occurred");

			  // set the content text
			  alert.setContentText("Utilisateur non existant!");

			  // show the alert
			  alert.showAndWait();  
			  return;
		  }
		  this.allowed = true;
		  questSecrete.setText(this.user.getCompte().getQuestSecrete());
	 }
	 
	 public void restore(ActionEvent event) throws IOException{
		 if(!allowed) {
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
		  Validations validate = new Validations();
		  String passWord;
		  String rep;
		  
		  try {
			  System.out.println("Entred" + this.passWord.getText());
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
		//on doit verifier si la reponse est l ameme que celle qui etait enregistrée
		  if(!rep.equals(this.user.getCompte().getResponse())) {
				// create a new Alert instance
			  Alert alert = new Alert(AlertType.ERROR);

			  // set the title and header text
			  alert.setTitle("Error");
			  alert.setHeaderText("An error has occurred");

			  // set the content text
			  alert.setContentText("Votre reponse n'est pas juste");

			  // show the alert
			  alert.showAndWait();  
			  return; 
		  }
		  //mot de passe changé
		  this.user.getCompte().setMotDePasse(passWord);
	 }

}
