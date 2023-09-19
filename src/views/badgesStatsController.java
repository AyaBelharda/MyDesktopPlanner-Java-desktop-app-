package views;

import java.io.IOException;
import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import myDpPlanner.Categorie;
import myDpPlanner.Creneau;
import myDpPlanner.EtatAvancement;
import myDpPlanner.Journee;
import myDpPlanner.Projet;
import myDpPlanner.Tache;
import myDpPlanner.Utilisateur;

public class badgesStatsController {
	
	@FXML
	ListView listPj;
	
	@FXML
	ListView listJr;
	
	@FXML
	ListView listCat;
	
	@FXML
	Text veryGood;
	
	@FXML
	Text good;
	
	@FXML
	Text excellent;

	@FXML
	Text nbEnc;
	
	@FXML
	Text jrPlusRent;
	
    @FXML
    private PieChart pieChart;
    
    @FXML
    Button changerC;
    
    @FXML 
    Text cat;
    
	static Utilisateur user;
	private double count[];
	private  int what = 0;
	private double countPj[] = new double[]{0, 0, 0, 0, 0, 0};
	private double countCat[] = new double[]{0, 0, 0, 0, 0, 0};
	private double countJr[] = new double[]{0, 0, 0, 0, 0, 0};
	public void init(Utilisateur utili) {
		user = utili;

		
		for(Projet pj : user.getListeProjets()) {
			for(Tache tc : pj.getListeTaches()) {
				countPj[tc.getEtatAvancement().ordinal()]++;
				
			}
		}
	
		
		for(Journee jr : user.getCalendrier().getListeJourneest()) {
			for(Creneau cr : jr.getListeCreneaux()) {
				Tache tc = cr.getTache();
				if(tc == null) {
					continue;
				}
				countJr[tc.getEtatAvancement().ordinal()]++;
				
			}
		
		}
		
		for(Tache tache : user.getListeTaches()) {
			//categories
			HashMap<Categorie, Integer> listCats = new HashMap<Categorie, Integer>();
			if(listCats.containsKey(tache.getCategorie())) {
				//listCats.put(tache.getCategorie(), (int) listCats.get(tache.getCategorie())++);
			}
			
		}
		
		Journee tempJr = user.journeePlusRentable();
		if(tempJr != null) {
			
			jrPlusRent.setText(tempJr.getDate().toString()); 
		} else {
			jrPlusRent.setText("Il n y'a pas de journees dans le planning");
		}
		veryGood.setText(Integer.toString(user.nbVeryGood()));
		good.setText(Integer.toString(user.nbGood()));
		excellent.setText(Integer.toString(user.nbExcellent()));
		
		count = countCat;
		double sum = count[0] + count[1] + count[2] + count[3] + count[4] + count[5];
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Cancelled", 100 * count[0] / sum),
                        new PieChart.Data("Delayed", 100 * count[1] / sum),
                        new PieChart.Data("Unscheduled", 100 * count[2] / sum),
                        new PieChart.Data("notRealised", 100 * count[3] / sum),
                		new PieChart.Data("InProgress", 100 * count[4] / sum),
        				new PieChart.Data("Completed", 100 * count[5] / sum));


        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " amount: ", data.pieValueProperty()
                        )
                )
        );
        
        cat.setText("duree du temps passé par categorie");

        pieChart.getData().addAll(pieChartData);
		
	}
	
	public void handleGoBack(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
		  Parent root = loader.load();
		  homeController controller = loader.getController();
		  controller.setUser(user);
		  Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  Scene scene = new Scene(root);
		  stage.setScene(scene);
		  stage.setFullScreen(true);
		  stage.show();
	}
	
	public void handleClickSwitch() {
		if(what == 0) {
			count = countPj;
			what = 1;
			cat.setText("Etat de realisation des taches par projet");
		} else if(what == 1) {
			count = countJr;
			what = 2;
			cat.setText("Etat de realisation des taches par jour");
		} else {
			count = countCat;
			what = 0;
			cat.setText("duree du temps passé par categorie");
		}
		
		uploadChart();
		
	}
	
	public void uploadChart() {
		double sum = count[0] + count[1] + count[2] + count[3] + count[4] + count[5];
        PieChart.Data[] data = {
                new PieChart.Data("Cancelled", 100 * count[0] / sum),
                new PieChart.Data("Delayed", 100 * count[1] / sum),
                new PieChart.Data("Unscheduled", 100 * count[2] / sum),
                new PieChart.Data("notRealised", 100 * count[3] / sum),
        		new PieChart.Data("InProgress", 100 * count[4] / sum),
				new PieChart.Data("Completed", 100 * count[5] / sum)
        };
        ObservableList<PieChart.Data> chartData = pieChart.getData();
        chartData.clear();  // Clear the existing data

        // Add the updated data
        chartData.addAll(data);
	}
	
	
}
