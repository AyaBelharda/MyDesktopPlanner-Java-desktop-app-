package myDpPlanner;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) {
      /*   LocalTime heure1 = LocalTime.of(10, 30);
        LocalTime heure2 = LocalTime.of(13, 30);
        Creneau [] tab= new Creneau [2];
        Creneau creneau= new Creneau(heure1,heure2,30,true);
     
        boolean b=creneau.estDecomposable(16);
        System.out.println(b);
      
       tab=Creneau.decomposer(creneau,50);
        System.out.println("cr1");
        tab[0].afficher();
        System.out.println("cr2");
        tab[1].afficher();*/
    	
//		User user1 = new User(1);
//		User user2 = new User(2);
//		User user3 = new User(3);
//		User user4 = new User(5);
//		User user5 = new User(6);
//		User user6 = new User(7);
//		User user7 = new User(4);
//		
//		MyDesktopPlanner app = new MyDesktopPlanner();
//		app.addUser(user1);
//		app.addUser(user2);
//		app.addUser(user3);
//		app.addUser(user4);
//		app.addUser(user5);
//		app.addUser(user5);
//		app.removeUser(user6);
//		app.addUser(user7);
//		app.afficher();
		
//      TacheSimple ta= new TacheSimple();
//      ta=TacheSimple.creerTache();
//      ta.afficher();
     // Creneau creneau= Creneau.creerCreneau();
      //creneau.afficher();

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
    	
    	Utilisateur user = new Utilisateur(20);
    	user.creerCompte("a", "a", "a", "a");
    	LocalDate today = LocalDate.now();
    	
    	LocalDate date1 = today.plusDays(0); // one week from today
    	LocalDate date2 = today.plusDays(2); // one month from today
    	

    	user.fixerPeriodePlannification(date1, date2, ensembleCreneaux);
//    	user.afficherCalendrier();
    	
//    	 Création d'une tâche simple
    	TacheSimple tache1 = new TacheSimple("Faire les courses", 30, Priorite.Medium, 
    	        new Date(2023, 4, 30), new Categorie("entretaitement", Couleur.Bleu), 0);

    	// Création d'une autre tâche simple
    	TacheSimple tache2 = new TacheSimple("Répondre aux e-mails", 30, Priorite.Low, 
    	        new Date(2023, 5, 5), new Categorie("entretaitement", Couleur.Bleu), 0);
    	
    	Date dateLimite = new Date(2023, 5, 29); // Exemple de date limite
    	TacheDecomposable tacheDecomposable = new TacheDecomposable("Tache 1 Dec", 110, Priorite.High, dateLimite,
    			new Categorie("entretaitement", Couleur.Bleu));

//==========test clone pour tache====================
//    	TacheDecomposable tc = (TacheDecomposable) tacheDecomposable.clone();
//			tc.setPriorite(Priorite.Medium);
//			tc.afficher();
//			tacheDecomposable.afficher();
//    ======================================	
    	//user.plannifierTacheManuellement(tache1, creneau1, date1);
    	//user.afficherCalendrier();
    	//System.out.println(" resultatttttttttttttttttttttttttt : " + user.getCalendrier().reporter(tache1, 20));
    	user.plannifierTacheManuellement(tacheDecomposable, creneau2, date1);
    //	user.plannifierTacheAutomatiquement(tacheDecomposable, LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
    	//user.getCalendrier().proposer(tacheDecomposable, 20,  LocalDate.now().plusDays(1),  LocalDate.now().plusDays(3));
    //	user.afficherCalendrier();
    	tacheDecomposable.setEtatAvancement(EtatAvancement.InProgress);
    	tacheDecomposable.setEtatSousTache(EtatAvancement.InProgress);
    	user.getCalendrier().reporter(tacheDecomposable, 20);
    	
    	user.afficherCalendrier();
 //   	tache1.setEtatAvancement(EtatAvancement.InProgress);
//    	
//    	tacheDecomposable.setEtatAvancement(EtatAvancement.InProgress);
//    	tacheDecomposable.setEtatSousTache(EtatAvancement.InProgress);
//    	if(user.replanifier(tacheDecomposable, 35) == 3) {
//    		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//    		user.getCalendrier().proposer(tacheDecomposable, 20);
//    	}
//    	user.afficherCalendrier();
//
//        TreeMap<Integer, String> nomsSousTaches = new TreeMap<>();
//
//        // Add three entries to the map
//        nomsSousTaches.put(1, "Subtask 1");
//        nomsSousTaches.put(2, "Subtask 2");
//        nomsSousTaches.put(3, "Subtask 3");
//        user.changerNomSousTaches("Tache 1 Dec", nomsSousTaches);
//        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//        user.afficherCalendrier();
//        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//        if(user.plannifierTacheAutomatiquement(tacheDecomposable, 0) == 0) {
//        	user.afficherCalendrier();
//        } else {
//        	System.out.println("error");
//        }
//        user.ajouterHistrique();
//        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//        System.out.println("calender after history");
//        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//        user.afficherCalendrier();
//        user.afficherCalendrier();
//        System.out.println("eeeeeeeeeeeeeeeeeee");
//        System.out.println("eeeeeeeeeeeeeeeeeee" );
//          TreeSet<Journee> tr = user.getCalendrier().proposer(tacheDecomposable, user.getDureeMinimale());
//          user.afficherCalendrier();
//          user.getCalendrier().refuserProposition(tr);
//          //tr.first().afficher();
//          System.out.println("eeeeeeeeeeeeeeeeeee" + tr.size());
//          System.out.println("eeeeeeeeeeeeeeeeeee" + tr.size());
//          user.afficherCalendrier();
// Create a DateTimeFormatter to parse date strings
//    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//    	// Create a Journee instance for May 10th, 2023
//    	LocalDate date1 = LocalDate.parse("10/05/2023", formatter);
//    	Journee journee1 = new Journee(date1);
//
//    	// Create a list of Creneau for May 10th, 2023
//    	List<Creneau> creneaux1 = new ArrayList<>();
//    	LocalTime debut1 = LocalTime.of(8, 0);
//    	LocalTime fin1 = LocalTime.of(10, 0);
//    	Creneau creneau11 = new Creneau(debut1, fin1, EtatCreneau.Libre, null);
//    	creneaux1.add(creneau1);
//    	// add more creneaux for May 10th, 2023
//
//    	// Set the list of creneaux for journee1
//    	journee1.setListeCreneaux(new TreeSet<>(creneaux1));
//
//    	// Create a Journee instance for May 11th, 2023
//    	LocalDate date2 = LocalDate.parse("11/05/2023", formatter);
//    	Journee journee2 = new Journee(date2);
//
//    	// Create a list of Creneau for May 11th, 2023
//    	List<Creneau> creneaux2 = new ArrayList<>();
//    	LocalTime debut2 = LocalTime.of(14, 0);
//    	LocalTime fin2 = LocalTime.of(16, 0);
//    	Creneau creneau22 = new Creneau(debut2, fin2, EtatCreneau.Libre, null);
//    	creneaux2.add(creneau2);
//    	// add more creneaux for May 11th, 2023
//
//    	// Set the list of creneaux for journee2
//    	journee2.setListeCreneaux(new TreeSet<>(creneaux2));
//=========tester le clone pour journee=================
//    	try {
//			Journee jr = (Journee) journee2.clone();
//
//			jr.setEtatAvancement(EtatAvancement.Completed);
//			jr.afficher();
//			journee2.afficher();
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    

        // TreeMap<Integer, String> nomsSousTaches = new TreeMap<>();

        // // Add three entries to the map
        // nomsSousTaches.put(1, "Subtask 1");
        // nomsSousTaches.put(2, "Subtask 2");
        // nomsSousTaches.put(3, "Subtask 3");
        // user.changerNomSousTaches("Tache 1 Dec", nomsSousTaches);
        // System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        // user.afficherCalendrier();
        // System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        // System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        // System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        // if(user.plannifierTacheAutomatiquement(tache2, 0)) {
        // 	user.afficherCalendrier();
        // } else {
        // 	System.out.println("error");
        // }
		//user.afficherEtatRealisationTache();
		// System.out.println(user.afficherEtatRealisationTache());
		// Categorie x=user.plusDeTemps();
		// x.afficher();
    }
}










