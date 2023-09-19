package myDpPlanner;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.io.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Utilisateur implements Serializable {
	static int lastId = 0;
	private int id;
	//private String pseudo;
	private Calendrier calendrier;
	private SortedSet<Projet> listeProjets = new TreeSet<Projet>();
	private Compte compte;
	private long dureeMinimale;//ta3 le créneau
    private int encouragement;//compter le nombre de fois ou l'utilisateur a accompli la realisation du nombre minimal des taches par journee 
    private TreeSet<Creneau> listeCreneauxLibres;
	private TreeSet<Journee> historique;
	private final String HISTORY_FILE_NAME = "somthing.bin";
	private LinkedList<Badge> listeBadges=new LinkedList<Badge>();//pour l'évaluation des badges
	private int nbTachesMinimales;
    //private int encouragement;//compter le nombre de fois ou l'utilisateur a accompli la realisation du nombre minimal des taches par journee 

    private HashSet<Tache> listeTaches = new HashSet<Tache>();
    public HashSet<Tache> getListeTaches() {
		return listeTaches;
	}

	public void setListeTaches(HashSet<Tache> listeTaches) {
		this.listeTaches = listeTaches;
	}

	private int badges[] = new int[3];
    // badges[0]: nb Good badges[1]: nb VeryGood badges[2]: nb Excellent
    // private int encouragement;//compter le nombre de fois ou l'utilisateur a
    // accompli la realisation du nombre minimal des taches par journee
	
	
	
	public Utilisateur(long dureeMinimal) {
		this.compte = null;
		this.id = lastId;
		this.dureeMinimale = dureeMinimal;
		lastId++;
		listeCreneauxLibres = new TreeSet<Creneau>();
		this.historique = new TreeSet<Journee>();
		this.calendrier = new Calendrier();
	}
	
	public Compte getCompte(){
		return this.compte;
	}

	public void setCompte(Compte c){
		this.compte = c;
	}

	public boolean creerCompte(String userName, String passWd, String questSecrete, String response) {
		if(this.compte != null) {
			return false;
		}
		this.compte = new Compte(userName, passWd, questSecrete, response);//pour les testes
		return true;
	}
	
//	public static Utilisateur creerUtilisateur() {
//		Utilisateur ut;
//		ut = new Utilisateur();
//		return ut;
//	}
	
	public void afficher(){
		System.out.println(this.id);
	}
	public void afficherCalendrier() {
		if(this.compte.isConnected()) {
			this.calendrier.afficher();
		} else {
			//il faut connecter
		}
	}
	
	public boolean fixerPeriodePlannification(LocalDate dateDeb, LocalDate dateFin, LinkedList<HashSet<Creneau>> ensembleCreneaux) {
		//pour fixer une periode dans le calendrier
		
		listeCreneauxLibres.addAll(this.calendrier.fixerJournees(dateDeb, dateFin, ensembleCreneaux));
		return listeCreneauxLibres.size() > 0;
	}
	
	public boolean creerProjet(String nomPj, String description) {
		Projet pj = new Projet(nomPj, description);
		return this.listeProjets.add(pj);//si il n'y a pas un autre projet qui contient le meme nom
	}
	
	public boolean supprimerProjet(Projet pj) {
		if (listeProjets.contains(pj)) {
			listeProjets.remove(pj); // remove the element from the set
			return true;
		} else {
		    return false;//Projet non existante
		}
	}
	
	public boolean ajouterProjet(Projet pj) {
		return listeProjets.add(pj);
	}
	
	public boolean modifierProjet(Projet pj, Projet toReplace) {
		if (listeProjets.contains(pj)) {
			listeProjets.remove(pj); // remove the element from the set
			return listeProjets.add(toReplace);
		} else {
		    return false;//Projet non existante
		}
	}
	
	public boolean plannifierTacheManuellement(Tache tache, Creneau creneau, LocalDate date) {
		if(this.compte.isConnected()) {
			return calendrier.plannifierTacheManuellement(tache, creneau, date, this.dureeMinimale);
		}
		
		return false;

	}
	
	public boolean changerNomSousTaches(String nomTacheDecomposable, TreeMap<Integer, String> nomsSouTaches) {
		//si le nombre des noms intoduits est > au nombre des taches
		return  this.calendrier.changerNomSousTaches(nomTacheDecomposable, nomsSouTaches);
	}
	
	public boolean changerNomSousTaches(String nomTacheDecomposable, TreeMap<Integer, String> nomsSouTaches, Journee jr) {
		//ici on peut specifier la journée de debut de la décomposition de la tache décomposable
		return  this.calendrier.changerNomSousTaches(nomTacheDecomposable, nomsSouTaches, jr);
	}
	

	public int plannifierTacheAutomatiquement(Tache tache) {
		
		return this.calendrier.plannifierTacheAutomatiquement(tache, this.dureeMinimale);
	}

	public int plannifierTacheAutomatiquement(Tache tache, LocalDate dateDebPlan, LocalDate dateFinPlan) {
		
		return this.calendrier.plannifierTacheAutomatiquement(tache, this.dureeMinimale, dateDebPlan, dateFinPlan);
		
	}
	
	public int plannifierTacheAutomatiquement(PriorityQueue<Tache> tacheSet) {
		return this.calendrier.plannifierTacheAutomatiquement(tacheSet, this.dureeMinimale);
	}
	
	public int plannifierTacheAutomatiquement(PriorityQueue<Tache> tacheSet, LocalDate dateDebPlan, LocalDate dateFinPlan) {
		return this.calendrier.plannifierTacheAutomatiquement(tacheSet, this.dureeMinimale, dateDebPlan, dateFinPlan);
	}
//	
//	public int replanifier(TacheDecomposable tache, long dureeSupp) {
//		if(tache.getEtatSousTache() != EtatAvancement.InProgress ||(tache.getEtatSousTache() == null && tache.getEtatAvancement() != EtatAvancement.InProgress)) {
//			return -1;//la tache ne peut pas etre replanfiée a cause de létat d'avancement
//		}
//		
//		return this.calendrier.replanifier(tache, dureeSupp, this.dureeMinimale);
//	}
//	
//	public int replanifier(TacheSimple tache, long dureeSupp) {
//		if(tache.getEtatAvancement() != EtatAvancement.InProgress) {
//			return -1;//la tache ne peut pas etre replanfiée a cause de létat d'avancement
//		}
//		return this.calendrier.replanifier(tache, dureeSupp, this.dureeMinimale);
//	}
	
	public int ajouterHistrique() {
		TreeSet<Journee> temp = this.calendrier.ajouterHistorique();
		if(temp != null) {
			this.historique.addAll(temp);
			//ajouter au fichier
			  try (FileOutputStream fileOutputStream = new FileOutputStream(HISTORY_FILE_NAME);
			             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
			            objectOutputStream.writeObject(temp);
		        } catch (IOException e) {
		        	System.out.print("Erreur lors de l'insertion dans le fichier d'historique");
		            e.printStackTrace();
		            return 1;//code 1 erreur dans historique
		        }
			return 0;//ça marche avec success
		}
		return 2;//code erreur 2 si il y'a pas des journees a archiver 
	}
	
	public boolean rechercherUtilisateur(String username, String password) {
        //return (this.compte.getPseudo().equals(username) && this.compte.getMotDePasse().equals(username));
		return this.compte.verifierCompte(username, password);
    }
	
	public boolean rechercherUtilisateur(String username) {
        //return (this.compte.getPseudo().equals(username) && this.compte.getMotDePasse().equals(username));
		return this.compte.verifierCompte(username);
    }

	//Getters and Setters

	public Calendrier getCalendrier() {
		return calendrier;
	}

	public SortedSet<Projet> getListeProjets() {
		return listeProjets;
	}
	
	public int getId() {
		return id;
	}
	
	public int hashCode() {
		Integer integer = Integer.valueOf(this.getId());
		return integer.hashCode();
	}
	
	public boolean equals(Object o) {
		return this.hashCode() == ((Utilisateur) o).hashCode();
	}


	public TreeSet<Creneau> getListeCreneauxLibres() {
		return listeCreneauxLibres;
	}

	public void setListeCreneauxLibres(TreeSet<Creneau> listeCreneauxLibres) {
		this.listeCreneauxLibres = listeCreneauxLibres;
	}

    public int getEncouragement() {
        return encouragement;
    }

    public void setEncouragement(int encouragement) {
        this.encouragement = encouragement;
    }

    public long getDureeMinimale() {
        return dureeMinimale;
    }

    public void setDureeMinimale(long dureeMinimale) {
        this.dureeMinimale = dureeMinimale;
    }

    // statistiques
    public int etatRealisationTache(Journee jour, EtatAvancement motif) {
        // retourne le nombre de taches réalisées selon un motif dans une journée donnée
        return calendrier.etatRealisationTache(jour, motif);
    }

    public int tachesCompletees(LocalDate debut, LocalDate fin) {
        return (calendrier.etatRealisationTachePeriode(debut, fin, EtatAvancement.Completed));
    }

    public int tachesPrevues(LocalDate debut, LocalDate fin) {
        // retourne le nombre de taches prévues dans une période (non completees)
        return (calendrier.etatRealisationTachePeriode(debut, fin, EtatAvancement.Unscheduled) +
                calendrier.etatRealisationTachePeriode(debut, fin, EtatAvancement.InProgress) +
                calendrier.etatRealisationTachePeriode(debut, fin, EtatAvancement.Delayed) +
                calendrier.etatRealisationTachePeriode(debut, fin, EtatAvancement.notRealised));

    }

    public double rendementJournalier(LocalDate debut, LocalDate fin) {
        return calendrier.rendementJournalier(debut, fin);
    }

    public double moyenneRendementJournalier() {// retourne le rendement journalier à partir de la date d'aujourd'hui

        return calendrier.moyenneRendementJournalier();

    }

    public int encouragement() {// le nombre de jours ou il a dépassé le nombre minimale de taches
        if (calendrier == null) {
            return 0;
        }
        return calendrier.encouragement(nbTachesMinimales);
    }

    public Journee journeePlusRentable() {// retourne la journee dans laquelle il a ete le plus rentable
        if (calendrier == null) {
            return null;
        }
        return (calendrier.journeePlusRentable());
    }

    public long dureeCategorie(Categorie c) {// calcule la durée passée sur des taches de meme catégorie dans une
                                             // journee
        if (calendrier == null) {
            return 0;
        }
        return (calendrier.dureeCategorie(c));
    }

    // public Categorie plusDeTemps() {// retourne la categorie sur laquelle il
    // passe le plus de temps
    // long max = 0;
    // Categorie ct = null;
    // Calendrier cal = this.calendrier;
    // TreeSet<Journee> j = cal.getListeJourneest();
    // Iterator<Journee> iterator = j.iterator();
    // while (iterator.hasNext()) {
    // Journee jour = iterator.next();
    // // parcourir les créneaux
    // TreeSet<Creneau> lc = jour.getListeCreneaux();
    // for (Creneau cr : lc) {
    // if (cr.getTache() != null && dureeCategorie(cr.getTache().categorie) > max) {
    // max = dureeCategorie(cr.getTache().categorie);
    // ct = cr.getTache().categorie;
    // }
    // }
    // }
    // return ct;
    // }

    public void ajouterExcellent() {
        badges[2]++;
    }

    public void ajouterVeryGood() {
        badges[1]++;
    }

    public void ajouterGood() {
        badges[0]++;
    }

    public int nbExcellent() {
        return (badges[2]);
    }

    public int nbVeryGood() {
        return (badges[1]);
    }

    public int nbGood() {
        return (badges[0]);
    }

    public boolean ajouterTache(Tache tache) {
        return (listeTaches.add(tache));
    }

    public boolean supprimerTache(Tache tache) {
        for (Tache t : listeTaches) {
            if (t.equals(tache)) {
                t.setEtatAvancement(EtatAvancement.Cancelled);
                return supprimerTacheCalendrier(t);
            }
        }
        
        return false;
    }

    public boolean supprimerTacheCalendrier(Tache tache) {
        return (calendrier.supprimerTache(tache));
    }

    public void afficherTaches() {
        for (Tache t : listeTaches) {
            t.afficher();
        }
    }
}