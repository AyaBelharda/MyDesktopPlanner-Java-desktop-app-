package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myDpPlanner.Projet;
import myDpPlanner.Utilisateur;

public class componentPjController {
	
	 private Stage stage;
	 private Scene scene;
	 private Parent root;
	 
	 
	@FXML
	Text pjName;
	
	@FXML
	Text pjDescription;
	
	@FXML
	AnchorPane cartePj;
	
	static Utilisateur user;
	private Projet project;
	public void setAtts(String nom, String description, Utilisateur user, Projet project) {
		pjName.setText(nom);
		pjDescription.setText(description);
		componentPjController.user = user;
		this.project = project;
	}
	
	public void handleDelete() {
		user.supprimerProjet(project);
		cartePj.setVisible(false);
	}
	
	public void handleButtonClick(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("projectTaches.fxml"));
		  Parent root = loader.load();
		  projectTachesController controller = loader.getController();
		  controller.setPj(project, user);
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	}
}
