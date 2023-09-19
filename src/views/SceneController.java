package views;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
public class SceneController {

	 private Stage stage;
	 private Scene scene;
	 private Parent root;
	 
	 public void switchToHome(ActionEvent event) throws IOException {
	  root = FXMLLoader.load(getClass().getResource("home.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	 }
	 
	 public void switchToLogin(ActionEvent event) throws IOException {
	  Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
	  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	  scene = new Scene(root);
	  stage.setScene(scene);
	  stage.show();
	 }
	 
	 public void switchToCreerCompte(ActionEvent event) throws IOException {
		  Parent root = FXMLLoader.load(getClass().getResource("creerCompte.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
		  System.out.print("aa");
	}
	 
	 public void switchToCompteOublie(ActionEvent event) throws IOException {
		  Parent root = FXMLLoader.load(getClass().getResource("CompteOublie.fxml"));
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
	}
}
