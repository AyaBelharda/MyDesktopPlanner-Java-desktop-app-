package views;

import myDpPlanner.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        // Create a new Scene with the loaded FXML file as the root node
        Scene scene = new Scene(root);
//        String css = this.getClass().getResource("creerCompt.css").toExternalForm();
//        scene.getStylesheets().add(css);
        // Set the title of the Stage
        primaryStage.setTitle("My JavaFX App");

        // Set the Scene for the Stage
        primaryStage.setScene(scene);
        
        primaryStage.setFullScreen(true);
        // Show the Stage
        primaryStage.show();
    }
    
    static  MyDesktopPlanner app;
    public static void main(String[] args) {
    	app = new MyDesktopPlanner();
//    	app.ouvrirr();
//    	app.afficherFichier();
    	for(Utilisateur user : app.getListeUtilisateurs()) {
    		System.out.println("aaaaa" + user.getCompte().getPseudo() + " " + user.getCompte().getMotDePasse());
    	}
    	
    	//Utilisateur user = new Utilisateur(20);
    	//Iterator<Utilisateur> iter = app.getListeUtilisateurs().iterator();
    	//iter.next();
    	//Utilisateur user = iter.next();
    	//user.creerCompte("a", "a", "azul", "a");
    	Utilisateur user = new Utilisateur(20);
    	user.creerCompte("Oualid CHABANE", "Walid2004", "....Somthing", "kiki");
    	app.ajouterUtilisateur(user);
       	Creneau creneau1 = new Creneau(LocalTime.of(9, 0), LocalTime.of(10, 0), EtatCreneau.Libre, null);
    	Creneau creneau2 = new Creneau(LocalTime.of(10, 0), LocalTime.of(11, 0), EtatCreneau.Libre, null);
    	Creneau creneau3 = new Creneau(LocalTime.of(11, 0), LocalTime.of(12, 0), EtatCreneau.Libre, null);
    	Creneau creneau4 = new Creneau(LocalTime.of(14, 0), LocalTime.of(15, 0), EtatCreneau.Libre, null);
    	Creneau creneau5 = new Creneau(LocalTime.of(15, 0), LocalTime.of(16, 0), EtatCreneau.Libre, null);
    	Creneau creneau6 = new Creneau(LocalTime.of(8, 0), LocalTime.of(11, 0), EtatCreneau.Libre, null);
    	Creneau creneau7 = new Creneau(LocalTime.of(12, 0), LocalTime.of(14, 0), EtatCreneau.Libre, null);
//============test clone pour creneau===================
//    	try {
//			Creneau cr = (Creneau) creneau5.clone();
//
//			cr.setEtatCreneau(EtatCreneau.Bloque);
//			cr.afficher();
//			creneau5.afficher();
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
// ========================================   	
    	LinkedList<HashSet<Creneau>> ensembleCreneaux = new LinkedList<>();

    	// Create the first HashSet and add two Creneaux objects to it
    	HashSet<Creneau> set1 = new HashSet<>();
    	set1.add(creneau1);
    	set1.add(creneau2);

    	// Create the second HashSet and add two different Creneau objects to it
    	HashSet<Creneau> set2 = new HashSet<>();
    	set2.add(creneau4);
    	set2.add(creneau5);

    	HashSet<Creneau> set3 = new HashSet<>();
    	set3.add(creneau6);
    	set3.add(creneau7);
    	// Add the two HashSets to the LinkedList
    	ensembleCreneaux.add(set1);
    	ensembleCreneaux.add(set2);
    	ensembleCreneaux.add(set3);
    	
    	//user.creerCompte("a", "a", "a", "a");
    	LocalDate today = LocalDate.now();
    	
    	LocalDate date1 = today.plusDays(0); // one week from today
    	LocalDate date2 = today.plusDays(2); // one month from today
    	

    	user.fixerPeriodePlannification(date1, date2, ensembleCreneaux);
    	TacheSimple tache1 = new TacheSimple("Faire les courses", 30, Priorite.Medium, 
    	        new Date(2023, 6, 30), new Categorie("entretaitement", Couleur.Bleu), 0);
    	TacheSimple tache2 = new TacheSimple("Faire du sport", 30, Priorite.High, 
    	        new Date(2023, 7, 10), new Categorie("entretaitement", Couleur.Rouge), 0);
    	TacheSimple tache3 = new TacheSimple("Faire le projet 2CP", 30, Priorite.Medium, 
    	         new Categorie("entretaitement", Couleur.Jaune), 0);
    	TacheSimple tache4 = new TacheSimple("Faire quelque chose", 30, Priorite.Low, 
    	        new Date(2023, 7, 15), new Categorie("entretaitement", Couleur.Violet), 0);
    	TacheSimple tache5 = new TacheSimple("Faire le tp", 30, Priorite.High, 
    	         new Categorie("entretaitement", Couleur.Vert), 0);
    	// Création d'une autre tâche simple
    	TacheSimple tache6 = new TacheSimple("Répondre aux e-mails", 20, Priorite.Low, 
    	        new Date(2023, 8, 5), new Categorie("entretaitement", Couleur.Rouge), 0);
    	Date dateLimite = new Date(2023, 5, 15); // Exemple de date limite
    	
    	TacheDecomposable tacheDecomposable = new TacheDecomposable("Apprendre java", 110, Priorite.High, 
    			new Categorie("entretaitement", Couleur.Vert));
    	
       	TacheDecomposable tacheDecomposable2 = new TacheDecomposable("s'exercer en mathématiques", 120, Priorite.Medium, new Date(2023, 7, 16),
    			new Categorie("entretaitement", Couleur.Vert));
       	
       	TacheDecomposable tacheDecomposable3 = new TacheDecomposable("Réparer la voiture", 90, Priorite.High, new Date(2023, 7, 17),
    			new Categorie("entretaitement", Couleur.Vert));
       	
       	TacheDecomposable tacheDecomposable4 = new TacheDecomposable("Aider arezki dans ses exercices", 50, Priorite.Low, new Date(2023, 8, 17),
    			new Categorie("entretaitement", Couleur.Vert));
    	
//    	TacheDecomposable tacheDecomposable2 = new TacheDecomposable("Tache 2 Dec", 110, Priorite.Medium, dateLimite,
//    			new Categorie("entretaitement", Couleur.Bleu));

//==========test clone pour tache====================
//    	TacheDecomposable tc = (TacheDecomposable) tacheDecomposable.clone();
//			tc.setPriorite(Priorite.Medium);
//			tc.afficher();
//			tacheDecomposable.afficher();
//    ======================================	
    	//user.plannifierTacheManuellement(tache1, creneau1, date1);
    	//TreeSet<Journee> tr = user.getCalendrier().proposer(tacheDecomposable, user.getDureeMinimale());
    	//user.afficherCalendrier();
    	user.creerProjet("Langages de pg", "Ce projet rassemble les taches qu iont relation avec l'apprentissage des langages de programmation");
    	user.creerProjet("Mechanique", "random text");
    	user.creerProjet("Aide et bénévole", "random text");
//    	user.getListeProjets().first().ajouterTache(tacheDecomposable);
//    	user.getListeProjets().first().ajouterTache(tache1);
    	user.ajouterTache(tacheDecomposable);
    	user.ajouterTache(tache1);
    	user.ajouterTache(tache2);
    	user.ajouterTache(tacheDecomposable2);
    	user.ajouterTache(tacheDecomposable3);
    	user.ajouterTache(tacheDecomposable4);
    	user.ajouterTache(tache2);
    	user.ajouterTache(tache3);
    	user.ajouterTache(tache4);
    	user.ajouterTache(tache5);
    	user.ajouterTache(tache6);
    	
        launch(args);
        app.fermer();
        
    }
}