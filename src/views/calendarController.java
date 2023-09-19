package views;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myDpPlanner.Creneau;
import myDpPlanner.EtatCreneau;
import myDpPlanner.Journee;
import myDpPlanner.TacheDecomposable;
import myDpPlanner.Utilisateur;

public class calendarController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Text heur;
	
	@FXML
	private Text date;
	
	@FXML
	private AnchorPane calendarPane;
	
	@FXML 
	private DatePicker journee;
	
	@FXML
	private TextField heurDebCr;
	
	@FXML 
	private TextField heurFinCr;
	
	@FXML 
	private TextField minDebCr;
	
	@FXML 
	private TextField	minFinCr;
	
	@FXML
	private Text d1;
	
	@FXML
	private Text d2;
	
	@FXML
	private Text d3;
	
	@FXML
	private Text d4;
	
	@FXML
	private Text d5;
	
	@FXML
	private Text d6;
	
	@FXML
	private Text d7;
	
	@FXML
	private Text currentDate;
	
	@FXML 
	private GridPane gridPane;
	
	@FXML
	private GridPane hourScale;
	
	static Utilisateur user;
	static LocalDate whereCal;
	static AnchorPane pane;
	static AnchorPane toQuitAnchor;
	static Journee jr;
	
	public void setUser(Utilisateur user) {
        LocalDate today = LocalDate.now();
        whereCal = today;

        while (whereCal.getDayOfWeek() != DayOfWeek.SUNDAY) {
        	whereCal = whereCal.minusDays(1);
        }
        
        
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        String formattedDate = currentDate.format(formatter);
        date.setText(formattedDate);
        
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatterHour);
        heur.setText(formattedTime);
        
		calendarController.user = user;
		initCalendar();
		this.dateDidMount();
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
	 
	 public void handleAjouterJourneesManuellement(ActionEvent event) throws IOException {
		 	toQuitAnchor = calendarPane;
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("creneauxCarte.fxml"));
	            pane = loader.load();

	            // Set the positioning of the pane within the AnchorPane
	            AnchorPane.setTopAnchor(pane, (calendarPane.getHeight() - pane.getPrefHeight()) / 2);
	            AnchorPane.setLeftAnchor(pane, (calendarPane.getWidth() - pane.getPrefWidth()) / 2);

	            // Add the pane to the calendarPane
	            calendarPane.getChildren().add(pane);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	 }
	

	 public void handleAjouterJourneesAutomatiquement(ActionEvent event) throws IOException {
	        try {
	        	
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("creneauxCarteAuto.fxml"));
	            pane = loader.load();
	            
	            // Set the positioning of the pane within the AnchorPane
	            AnchorPane.setTopAnchor(pane, (calendarPane.getHeight() - pane.getPrefHeight()) / 2);
	            AnchorPane.setLeftAnchor(pane, (calendarPane.getWidth() - pane.getPrefWidth()) / 2);

	            // Add the pane to the calendarPane
	            calendarPane.getChildren().add(pane);
	           
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
	 
	 public void ajouterJournees(ActionEvent event) {
		 LocalDate temp;
		
		 Validations validate = new Validations();
		 temp = journee.getValue();
		 if(temp == null) {
			 warning("vous n'avez pas selectione une journee");
			 return;
		 }
		 if(!validate.validDay(temp)) {
			 warning("Journee saisi est avant de la date d'aujourdhui : " + LocalDate.now().toString());
			 return;
		 }
		 
		 Journee tempJournee = calendarController.user.getCalendrier().rechercherJournee(temp);
		 if(tempJournee == null) {
			 calendarController.jr = new Journee(temp);
			 calendarController.user.getCalendrier().ajouterJournee(jr);
			 
		 } else {
			 calendarController.jr = tempJournee;
		 }
		 calendarController.user.afficherCalendrier();
	 }
	 
	 public void ajouterCreneaux(ActionEvent event) throws IOException {
		 if(calendarController.jr == null) {
			 warning("Vous devez selectionner une journee et l'ajouter");
			 return;
		 }
		 
		 Validations validate = new Validations();
		 
		 if(!validate.validHour(heurDebCr.getText(), minDebCr.getText())) {
			 warning("Format invalide pour l'heur de debut du creneau :" + heurDebCr.getText() + " : " + minDebCr.getText());
		 }
		 
		 if(!validate.validHour(heurFinCr.getText(), minFinCr.getText())) {
			 warning("Format invalide pour l'heur de fin du creneau :" + heurDebCr.getText() + " : " + minFinCr.getText());
		 }
		 
		 
		 if(!validate.validCreneau(LocalTime.of(Integer.parseInt(heurDebCr.getText()), Integer.parseInt(minDebCr.getText())), 
			LocalTime.of(Integer.parseInt(heurFinCr.getText()), Integer.parseInt(minFinCr.getText())))) {
			 warning("Heur debut creneau est apres l'heur de fin!");
		 }
		 Creneau cr = new Creneau(LocalTime.of(Integer.parseInt(heurDebCr.getText()), Integer.parseInt(minDebCr.getText())), 
				 					LocalTime.of(Integer.parseInt(heurFinCr.getText()), Integer.parseInt(minFinCr.getText())), 
				 				  EtatCreneau.Libre, 
				 				  null
				 				  );
		 
		 calendarController.user.getCalendrier().ajouterCreneau(calendarController.jr, cr);
		 handleQuitCard();
		 calendarController.user.afficherCalendrier();
	 }
	 
	 public void handleQuitCard() {
//		  try {
			  toQuitAnchor.getChildren().remove(pane);	
			  //fillCalendar();
//			  FXMLLoader loader = new FXMLLoader(getClass().getResource("calendrier.fxml"));
//			  Parent root = loader.load();
//			  calendarController controller = loader.getController();
//			  controller.setUser(user);
//			  stage = (Stage) toQuitAnchor.getScene().getWindow();
//			  scene = new Scene(root);
//			  stage.setScene(scene);
//			  stage.setFullScreen(true);
//			  stage.show();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	 }
	 
	 public AnchorPane genererCreneau(Creneau creneau, Journee jr) {
		 AnchorPane cr = new AnchorPane();
         cr.setStyle("-fx-background-color: #ef233c;"
        		 
         		+ "-fx-background-radius : 15;"
        		 );
         //

		 if(creneau.getEtatCreneau() == EtatCreneau.Libre) {
			 cr.setStyle(cr.getStyle() +"-fx-background-color : #70e000;");
		 } else if(creneau.getEtatCreneau() == EtatCreneau.Bloque) {
			 cr.setStyle(cr.getStyle() +"-fx-background-color : #ffbc42;");
		 }
		 //cr.setPrefHeight((gridPane.getHeight() / gridPane.getColumnCount()) * creneau.calculerDuration() / 60);
		 cr.setPrefHeight(140 * creneau.calculerDuration() / 60.0);
		 cr.setPrefWidth(145);
		 cr.setId(creneau.getHeureDebut().toString());
		 cr.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent e) {
	        	if(creneau.getEtatCreneau() == EtatCreneau.Libre) {
	        		creneauClickedLibre(user, creneau, jr, "creneauTacheV.fxml");
	        	} else {
	        		if(creneau.getTache() instanceof TacheDecomposable) {
	        			creneauClickedLibre(user, creneau, jr, "creneauTache.fxml");
	        		} else {
	        			creneauClickedLibre(user, creneau, jr, "creneauTacheS.fxml");
	        		}
	        	}

	        }
	    });

		 return cr;
	 }
	 
	 public void creneauClickedLibre(Utilisateur utili, Creneau creneau, Journee jr,  String file) {
		 	toQuitAnchor = calendarPane;
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
	            pane = loader.load();
	            creneauController controller = loader.getController();
	            controller.setAtts(utili, creneau, jr, calendarPane);
	            
	            // Set the positioning of the pane within the AnchorPane
	            AnchorPane.setTopAnchor(pane, (calendarPane.getHeight() - pane.getPrefHeight()) / 2);
	            AnchorPane.setLeftAnchor(pane, (calendarPane.getWidth() - pane.getPrefWidth()) / 2);

	            // Add the pane to the calendarPane
	            calendarPane.getChildren().add(pane);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }

	 
	 public void fillCalendar() {
		 gridPane.getChildren().clear();
		 gridPane.setStyle(gridPane.getStyle() + "-fx-grid-lines-visible: true;");
		 int xCords , yCords;
		 for(Journee jr : user.getCalendrier().getListeJourneest()) {
			 xCords = (int) ChronoUnit.DAYS.between(whereCal, jr.getDate());
			 if(xCords < 0) {
				 continue;
			 }
			 
			 if(xCords > 6) {
				 break;
			 }
			
			 for(Creneau cr : jr.getListeCreneaux()) {
				 AnchorPane icone = this.genererCreneau(cr, jr);
				 yCords = cr.getHeureDebut().getHour() ;//a revoire
				 int yCordsFin = (int) (Math.ceil( cr.calculerDuration() / 60.0));
				 gridPane.add(icone, xCords, yCords, 1, yCordsFin);
				 double marginTop = 5 + 140 * cr.getHeureDebut().getMinute() / 60.0;
				 double marginBottom = 10 + 140 * yCordsFin - marginTop - 140 *  cr.calculerDuration() / 60.0;
				 gridPane.setMargin(icone, new Insets(marginTop, 5, marginBottom, 5));
				 
			 }
			
		 }
	 }
	 
	 public void handlePlannifierAutomatiquement(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("tachesShortCut.fxml"));
		  AnchorPane tableTaches = loader.load();
		  tacheShortCutController controller = loader.getController();
		  controller.setUser(user, calendarPane);
		  
          // Set the positioning of the pane within the AnchorPane
          AnchorPane.setTopAnchor(tableTaches, (calendarPane.getHeight() - tableTaches.getPrefHeight()) / 2);
          AnchorPane.setLeftAnchor(tableTaches, (calendarPane.getWidth() - tableTaches.getPrefWidth()) / 2);
		  calendarPane.getChildren().add(tableTaches);
	 }
	 
	 public void initCalendar() {
		 
		 for(int i = 1; i < 24; i++) {
			 Text currentHr = new Text();
			 LocalTime time = LocalTime.of(i, 0);
			 currentHr.setText(time.toString());
			 currentHr.setFont(Font.font("Lucida Console"));
			 currentHr.setFill(Color.WHITE);
			 GridPane.setHalignment(currentHr, HPos.CENTER);
			 GridPane.setValignment(currentHr, VPos.CENTER);
			 hourScale.add(currentHr, 0, i);
			
		 }
	 }
	 
	 public void handleAvancerCalendrier(ActionEvent event) {
	 		whereCal = whereCal.plusDays(7);
	 		DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
	        String monthYear = whereCal.format(formatter);
	        LocalDate localDate = whereCal;
	        currentDate.setText(monthYear);
	        d1.setText(Integer.toString(localDate.getDayOfMonth()));
	        d2.setText(Integer.toString(localDate.plusDays(1).getDayOfMonth()));
	        d3.setText(Integer.toString(localDate.plusDays(2).getDayOfMonth()));
	        d4.setText(Integer.toString(localDate.plusDays(3).getDayOfMonth()));
	        d5.setText(Integer.toString(localDate.plusDays(4).getDayOfMonth()));
	        d6.setText(Integer.toString(localDate.plusDays(5).getDayOfMonth()));
	        d7.setText(Integer.toString(localDate.plusDays(6).getDayOfMonth()));
	        fillCalendar();
	 }
	 
	 public void handleReculerCalendrier(ActionEvent event) {
		 	whereCal = whereCal.minusDays(7);
	 		DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
	        String monthYear = whereCal.format(formatter);
	        LocalDate localDate = whereCal;
	        currentDate.setText(monthYear);
	        d1.setText(Integer.toString(localDate.getDayOfMonth()));
	        d2.setText(Integer.toString(localDate.plusDays(1).getDayOfMonth()));
	        d3.setText(Integer.toString(localDate.plusDays(2).getDayOfMonth()));
	        d4.setText(Integer.toString(localDate.plusDays(3).getDayOfMonth()));
	        d5.setText(Integer.toString(localDate.plusDays(4).getDayOfMonth()));
	        d6.setText(Integer.toString(localDate.plusDays(5).getDayOfMonth()));
	        d7.setText(Integer.toString(localDate.plusDays(6).getDayOfMonth()));
	        fillCalendar();
	 }
	 
	 
	 public void dateDidMount() {
	 		DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
	        String monthYear = whereCal.format(formatter);
	        LocalDate localDate = whereCal;
	        currentDate.setText(monthYear);
	        d1.setText(Integer.toString(localDate.getDayOfMonth()));
	        d2.setText(Integer.toString(localDate.plusDays(1).getDayOfMonth()));
	        d3.setText(Integer.toString(localDate.plusDays(2).getDayOfMonth()));
	        d4.setText(Integer.toString(localDate.plusDays(3).getDayOfMonth()));
	        d5.setText(Integer.toString(localDate.plusDays(4).getDayOfMonth()));
	        d6.setText(Integer.toString(localDate.plusDays(5).getDayOfMonth()));
	        d7.setText(Integer.toString(localDate.plusDays(6).getDayOfMonth()));
	        fillCalendar();
	 }
	 
	 public void handleAjouterHistorique() {
		 int result =  user.ajouterHistrique();
		 switch(result) {
		 case 2:
			 warning("il n'y a pas de journées a archiver");
			 break;
			 
		 case 1 :
			 warning("probleme dans l'archivage");
			 break;
		 }
		
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
