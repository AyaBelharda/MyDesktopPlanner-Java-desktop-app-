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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import myDpPlanner.Utilisateur;

public class LoginController {
		
		
		 private Stage stage;
		 private Scene scene;
		 private Parent root;
		 
		 @FXML
		 private TextField userName;
		 
		 @FXML
		 private PasswordField passWord;
		 
		public void handleLogin(ActionEvent event) throws IOException {
			
			String userName, passWord;
		
			try {
				 userName = this.userName.getText();
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
			} catch(Exception e) {
				// create a new Alert instance
				  Alert alert = new Alert(AlertType.ERROR);

				  // set the title and header text
				  alert.setTitle("Error");
				  alert.setHeaderText("An error has occurred");

				  // set the content text
				  alert.setContentText("Mot de passe non saisi!");

				  // show the alert
				  alert.showAndWait();  
				  return;
			}
			Utilisateur user = Main.app.rechercherUtilisateur(userName, passWord);
			if(user != null) {
				  user.getCompte().seConnecter();
				  FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
				  Parent root = loader.load();
				  homeController controller = loader.getController();
				  controller.setUser(user);
				  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				  scene = new Scene(root);
				  stage.setScene(scene);
				  stage.setFullScreen(true);
				  stage.show();
				//switch to a new page
			}
			else {
				// create a new Alert instance
				  Alert alert = new Alert(AlertType.ERROR);

				  // set the title and header text
				  alert.setTitle("Error");
				  alert.setHeaderText("An error has occurred");

				  // set the content text
				  alert.setContentText("Utilisateur inexistant");

				  // show the alert
				  alert.showAndWait();  
			}
			
		}
	
		 public void switchToHome(ActionEvent event) throws IOException {

			  root = FXMLLoader.load(getClass().getResource("home.fxml"));
			  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			  scene = new Scene(root);
			  stage.setScene(scene);
			  stage.setFullScreen(true);
			  stage.show();

		 }
	
		 public void switchToCreerCompte(ActionEvent event) throws IOException {	
			  Parent root = FXMLLoader.load(getClass().getResource("creerCompte.fxml"));
			  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			  scene = new Scene(root);
			  stage.setScene(scene);
			  stage.setFullScreen(true);
			  stage.show();
		}
		
		 public void switchToCompteOublie(ActionEvent event) throws IOException {
			  Parent root = FXMLLoader.load(getClass().getResource("CompteOublie.fxml"));
			  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			  scene = new Scene(root);
			  stage.setScene(scene);
			  stage.setFullScreen(true);
			  stage.show();
		}
}
