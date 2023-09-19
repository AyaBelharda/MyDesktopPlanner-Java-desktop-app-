package myDpPlanner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.io.*;

import org.w3c.dom.Node;

public class Calendrier implements Serializable {
	
	private TreeSet<Journee> listeJournees;

	public Calendrier() {
		 listeJournees = new TreeSet<Journee>(); 
	}
	
	public Journee rechercherJournee(LocalDate date) {
		Journee jr = new Journee(date);
		Journee temp = listeJournees.ceiling(jr);
		if(temp == null || !temp.getDate().equals(date)) {
			return null;
		}
		return temp;

	}
	
	
	public Journee rechercherJournee(Creneau cr) {
		
		for(Journee jr: listeJournees) {
			if(jr.rechercherCreneau(cr)) {
				return jr;
			}
		}
		return null;

	}
	public void afficher() {
		
		for (Journee jr : listeJournees) {
			jr.afficher();
		}
	}
	
	private boolean valideDateDebut(LocalDate  date) {
		LocalDate today = LocalDate.now();
		if(date.isBefore(today) || (!listeJournees.isEmpty() && date.isBefore(listeJournees.last().getDate()))) {
			return false;//le cas ou la date specifié est avant la date d'aujordhui ou bien avant la derniere date du calendrier
		}
	    return true;
	}
	
	public boolean ajouterJournee(Journee jr) {
		return this.listeJournees.add(jr);
	}
	
	
	public boolean ajouterCreneau(Journee jr, Creneau cr) {
		if(listeJournees.contains(jr)) {
			return jr.ajouterCreneau(cr);
		}
		
		return false;
	}
	
	public TreeSet<Creneau> fixerJournees(LocalDate dateDebut, LocalDate  dateFin, LinkedList<HashSet<Creneau>> ensembleCreneaux) {
		TreeSet<Creneau> returnValue = new TreeSet<Creneau>();
		if(valideDateDebut(dateDebut)) {
			
			Iterator<HashSet<Creneau>> i = ensembleCreneaux.iterator();
			while (dateDebut.isBefore(dateFin) || dateDebut.isEqual(dateFin)) {
				Journee jr = new Journee(dateDebut);
				if(i.hasNext()) {
					//pour eviter les erreurs
					HashSet<Creneau> temp = i.next();
					jr.fixerCreneauxLibres(temp);
					returnValue.addAll(temp);
				}
			    listeJournees.add(jr);
			    dateDebut = dateDebut.plusDays(1);
			}
		}
		return returnValue;//si l'ensemble est vide veut dire que on n'a pas inserés des creneau
	}
	
	private boolean testTacheDecomposee(TacheDecomposable tache, Creneau creneau, Journee jr) {
		//pour tester si on peut inserer une tache decomposable pour eviter les suppression par la suite
		long dureeTache = tache.getDuree();
		long cptDuree = 0;
		Creneau iter = creneau;
		while (iter != null) {
			cptDuree += iter.calculerDuration();
			if(cptDuree >= dureeTache) {
				return true;
			}
			iter = jr.getListeCreneaux().higher(iter);
		}
		jr = listeJournees.higher(jr);
		if(tache.getDateLimite() == null) {
			while(jr != null) {
				for(Creneau cr : jr.getListeCreneaux()) {
					cptDuree += cr.calculerDuration();
					if(cptDuree >= dureeTache) {
						return true;
					}
				}
			}
		} else {
			while(jr != null) {
				for(Creneau cr : jr.getListeCreneaux()) {
			        LocalDateTime localDateTime = LocalDateTime.of(jr.getDate(), cr.getHeureDebut());

			        // Convert LocalDateTime to Date
			        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
					if(date.after(tache.getDateLimite())) {
						return false;
					}
					
					cptDuree += cr.calculerDuration();
					if(cptDuree >= dureeTache) {
						return true;
					}
				}
			}
		}

		return false;

	}
	

	private boolean testTacheDecomposee(TacheDecomposable tache) {
		//pour tester si on peut inserer une tache decomposable pour eviter les suppression par la suite
		long dureeTache = tache.getDuree();
		long cptDuree = 0;
		Iterator<Journee> iter = listeJournees.iterator();
		
		while(iter.hasNext()) {
			//on test si la tache peut etre plannifiee dans le calendrier
			for(Creneau cr : iter.next().getListeCreneaux()) {
				cptDuree += cr.calculerDuration();
				if(cptDuree >= dureeTache) {
					return true;
				}
			}
		}
		return false;

	}
	
	private boolean testTacheDecomposee(TacheDecomposable tache, LocalDate dateDeb, LocalDate dateFin) {
		//pour tester si on peut inserer une tache decomposable pour eviter les suppression par la suite
		long dureeTache = tache.getDuree();
		long cptDuree = 0;
		Journee jourParc = listeJournees.ceiling(new Journee(dateDeb));
		while(jourParc != null && jourParc.getDate().isBefore(dateFin)) {
			//on test si la tache peut etre plannifiee dans le calendrier
			for(Creneau cr : jourParc.getListeCreneaux()) {
				cptDuree += cr.calculerDuration();
				if(cptDuree >= dureeTache) {
					return true;
				}
			}
			jourParc = listeJournees.higher(jourParc);
		}
		return false;

	}
	
	public boolean plannifierTacheManuellement(Tache tache, Creneau creneau, LocalDate date, long duree_min) {
		Journee tempJr = new Journee(date);
		Journee jr = listeJournees.ceiling(tempJr);
//		int jourDernier = Journee.nombreJours;
//		if(jour > jourDernier) {
//				return false;
//		} else {
//	
//			for(Journee jr : listeJournees) {
				if(jr != null && jr.getDate().equals(date)){//jr.getNumeroJournee() == jour) {
	
					if(tache instanceof TacheDecomposable && !testTacheDecomposee((TacheDecomposable) tache, creneau, jr)) {
						//pour eviter les insertions d'une tache decomposable puis la suppression 
						//dans le cas ou il y a pas d'espace suffisant dans le calendrier
					
						return false;
					}
					Tache tacheResult =  jr.plannifierTacheManuellement(tache, creneau, duree_min);
					if(tacheResult instanceof TacheSimple) {
						return false;
					}
					while(tacheResult != null) {
						jr = listeJournees.higher(jr);			
						System.out.println("debuging : " + jr.getDate());
						Iterator<Creneau> iter = jr.getListeCreneaux().iterator();
						Creneau tempCr = null;
						while(iter.hasNext()) {
							tempCr = iter.next();
							if(tempCr.getEtatCreneau() == EtatCreneau.Libre){
								break;
							}
							
						}
						 if(tempCr != null) {
								creneau = tempCr;
								tacheResult =  jr.plannifierTacheManuellement(tacheResult, creneau, duree_min);						 
						 }

					}
					
					if(tache instanceof TacheDecomposable) {
						this.evaluerTacheDomposable((TacheDecomposable) tache);
					}
					
					return true;
				}
				return false;
//			}
//			return false;
//		}
	}

	public boolean changerNomSousTaches(String nomTacheDecomposable, TreeMap<Integer, String> nomsSousTaches) {
		if (nomsSousTaches.size() == 0) {
			return false;
		}
		boolean result = false;
		int cpt = 1, numSousTache = 0;
		String nomSousTacheCourant;
		Iterator<Integer> it = nomsSousTaches.keySet().iterator();
		numSousTache = it.next();
		nomSousTacheCourant = nomsSousTaches.get(numSousTache);
		for(Journee jr : listeJournees) {
			//pas besoin de tester pour la premiere fois car le Map n'est pas vide
			for(Creneau cr : jr.getListeCreneaux()) {
				if(cr.getEtatCreneau() == EtatCreneau.Occupe && cr.getTache() instanceof TacheDecomposable && 
					cr.getTache().getNom().equals(nomTacheDecomposable)) {
						if(cpt == numSousTache) {
							cr.changerNomSousTaches(nomSousTacheCourant);
							if(it.hasNext()) {
								numSousTache = it.next();
								nomSousTacheCourant = nomsSousTaches.get(numSousTache);
							}
						}
						result = true;
						cpt++;//passer a la prochaine sous tache	
					}
				}
		}
		return result;
	}
	

	
	public boolean changerNomSousTaches(String nomTacheDecomposable, TreeMap<Integer, String> nomsSousTaches, Journee jr) {
		if (nomsSousTaches.size() == 0) {
			return false;
		}
		//pas besoin de tester pour la premiere fois car le Map n'est pas vide
		boolean result = false;
		int cpt = 1, numSousTache = 0;
		String nomSousTacheCourant;
		Iterator<Integer> it = nomsSousTaches.keySet().iterator();
		numSousTache = it.next();
		nomSousTacheCourant = nomsSousTaches.get(numSousTache);
		for(Creneau cr : jr.getListeCreneaux()) {
			if(cr.getEtatCreneau() == EtatCreneau.Occupe && cr.getTache() instanceof TacheDecomposable && 
				cr.getTache().getNom().equals(nomTacheDecomposable)) {
					if(cpt == numSousTache) {
						cr.changerNomSousTaches(nomSousTacheCourant);
						if(it.hasNext()) {
							numSousTache = it.next();
							nomSousTacheCourant = nomsSousTaches.get(numSousTache);
						}
					}
					result = true;
					cpt++;//passer a la prochaine sous tache	
				}
			}
		return result;
	}
	
	public int plannifierTacheAutomatiquement(Tache tache, long duree_min) {
		int result = 4;//code erreur listeJournees est vide!
		for(Journee jr : listeJournees) {
			result = jr.plannifierTacheAutomatiquement(tache, duree_min);
			if(result != 2) {
				//on arret l etraitement si le resultat indique autre  chose que le manque de creneaux libres
				//dans le main on test si le resultat retourné est 3 on propose
				break;
			}
		}
		if(result == 0 && tache instanceof TacheDecomposable) {
			this.evaluerTacheDomposable((TacheDecomposable) tache);
		}
		return result;

	}
	
	public TreeSet<Journee> proposer(TacheDecomposable tache, long duree_min){
		//retourner une liste des creneaux du calendrier et des creneaux temporaire pour proposer
		
		Journee jrCopy ;
		TreeSet<Journee> resultList = new TreeSet<Journee>();
		if(!testTacheDecomposee((TacheDecomposable) tache)) {
			return null;// demander extension du calendrier
			
		}	
		
		TacheDecomposable tacheResult =  (TacheDecomposable) tache.clone();
		long duration;
		Iterator<Journee> iter = listeJournees.iterator();
		 do {	
			Journee temp = iter.next();
	        try {
	        	jrCopy = (Journee) temp.clone();

	        } catch (CloneNotSupportedException e) {
	            throw new RuntimeException(e);
	        }
			
			TreeSet<Creneau> list = temp.proposer(tacheResult, duree_min);
			if(list != null && list.size() > 0) {
				//on sauvgarde une copie de la journee avant la modification en cas de refus de la proposition
				//jrCopy.afficher();
				tache.setEtatSousTache(EtatAvancement.notRealised);
				resultList.add(jrCopy);
			} 
		} 
		while(iter.hasNext() && tacheResult != null &&  tacheResult.getEtatAvancement() == EtatAvancement.Unscheduled);
		
	    return resultList;//liste des journees consernées par la tache decomposabme avant la modification
	}
	
	public TreeSet<Journee> proposer(TacheDecomposable tache, long duree_min, LocalDate debut, LocalDate fin){
		//retourner une liste des creneaux du calendrier et des creneaux temporaire pour proposer
		
		Journee jrCopy ;
		TreeSet<Journee> resultList = new TreeSet<Journee>();
		if(!testTacheDecomposee((TacheDecomposable) tache, debut, fin)) {
			return null;// demander extension du calendrier
			
		}	
		
		TacheDecomposable tacheResult =  (TacheDecomposable) tache.clone();
		long duration;
		Iterator<Journee> iter = listeJournees.iterator();
		
		Journee temp = listeJournees.ceiling(new Journee(debut));//recuperer la premiere journee
		if(temp == null) {
			return null;
		}
		
		 do {	
	        try {
	        	jrCopy = (Journee) temp.clone();

	        } catch (CloneNotSupportedException e) {
	            throw new RuntimeException(e);
	        }
			
			TreeSet<Creneau> list = temp.proposer(tacheResult, duree_min);
			if(list != null && list.size() > 0) {
				//on sauvgarde une copie de la journee avant la modification en cas de refus de la proposition
				//jrCopy.afficher();
				tache.setEtatSousTache(EtatAvancement.notRealised);
				resultList.add(jrCopy);
			} 
			temp = listeJournees.higher(temp);
		} 
		while(temp != null && temp.getDate().isBefore(fin) && tacheResult != null &&  tacheResult.getEtatAvancement() == EtatAvancement.Unscheduled);
		
	    return resultList;//liste des journees consernées par la tache decomposabme avant la modification
	}
	
	public void refuserProposition(TreeSet<Journee> listeJournees) {
		//en cas de refus de la proposition on remplace les journees changées dans le calendrier par les journées retourné par
		//la méthode proposer qui sont les meme journées mais avant la modification
		Iterator<Journee> iter = listeJournees.iterator();
		while(iter.hasNext()) {
			Journee tempJr = iter.next();
			Journee searchJr = this.listeJournees.ceiling(tempJr);
			if(searchJr.getDate().compareTo(tempJr.getDate()) == 0) {
				this.listeJournees.remove(searchJr);
				this.listeJournees.add(tempJr);
				
			}
		}
	}
	
	public int plannifierTacheAutomatiquement(Tache tache, long duree_min, LocalDate dateDebPlan, LocalDate dateFinPlan) {
		Journee tempJr = new Journee(dateDebPlan);
		Journee jr = listeJournees.ceiling(tempJr);
		if(jr == null || jr.getDate().compareTo(dateFinPlan) > 0) {
			return 1;//code errer 1, plage non existante dans le planning
		}
		
		do {
			
			int result = jr.plannifierTacheAutomatiquement(tache, duree_min);
			if(result == 0) {
				if(tache instanceof TacheDecomposable) {
					this.evaluerTacheDomposable((TacheDecomposable) tache);
				}
				
				return 0;//succeded
			} 
			
			if(result == 3) {
				return 3;//il faut proposer
			}
			
		}
		while(jr != null && jr.getDate().compareTo(dateFinPlan) <= 0);
		return 2;//tache n'a pas était programmé
	}
	
	
	public int plannifierTacheAutomatiquement(PriorityQueue<Tache> tacheSet, long duree_minimale) {
		Journee jr = listeJournees.first();
		Tache tacheAPlannifier = tacheSet.poll();
		boolean stop = false;
		while(jr != null && !stop) {
			while(tacheAPlannifier != null && this.plannifierTacheAutomatiquement(tacheAPlannifier, duree_minimale) == 0) {
				tacheAPlannifier = tacheSet.poll();
			}
			if(tacheAPlannifier != null) {
				jr = listeJournees.higher(jr);
			} else {
				stop = true;
			}
		}
		if(tacheSet.size() > 0) {
			Tache tache = tacheSet.poll();
			while(tache != null) {
				tache.setEtatAvancement(EtatAvancement.Unscheduled);
				tache = tacheSet.poll();
			}
			return 1;//il y'a des taches qui n'ont pas été plannifiées
		}
		return 0;
	}
	
	public int plannifierTacheAutomatiquement(
			//Pramaters
			PriorityQueue<Tache> tacheSet, 
			long duree_minimale, 
			LocalDate dateDebPlan,
			LocalDate dateFinPlan
		) {
		Journee tempJr = new Journee(dateDebPlan);
		Journee jr = listeJournees.ceiling(tempJr);
		Tache tacheAPlannifier = tacheSet.poll();
		boolean stop = false;
		if(jr == null || jr.getDate().compareTo(dateFinPlan) > 0) {
			return 1;//code errer 1, plage non existante dans le planning, débordement de temps
		}
		while(jr != null && !stop) {
			while(tacheAPlannifier != null && jr.plannifierTacheAutomatiquement(tacheAPlannifier, duree_minimale) == 0) {
				tacheAPlannifier = tacheSet.poll();
			}
			if(tacheAPlannifier != null) {
				jr = listeJournees.higher(jr);
			} else {
				stop = true;
			}
		}
		if(tacheSet.size() > 0) {
			Tache tache = tacheSet.poll();
			while(tache != null) {
				tache.setEtatAvancement(EtatAvancement.Unscheduled);
				tache = tacheSet.poll();
			}
			return 2;//il y'a des taches qui n'ont pas été plannifiées
		}
		return 0;//avec succès
	}
	
	
	public TreeSet<Journee> ajouterHistorique() {
		Journee toFetch = new Journee(LocalDate.now());
		TreeSet<Journee> subSet = (TreeSet<Journee>) listeJournees.subSet(listeJournees.first(), true,listeJournees.floor(toFetch), true);
		for(Journee jr : listeJournees) {
			System.out.println(toFetch.compareTo(jr));
		}
		if(subSet != null &&  subSet.size() > 0) {
			listeJournees.removeAll(subSet);
			return subSet;
		}
		return null;
		
	}
	
	public ArrayDeque<Creneau> rechercherTacheDecomposable(String nomTache){
		ArrayDeque<Creneau> listResult = new ArrayDeque<Creneau>();
		for(Journee jr : listeJournees) {
			ArrayDeque<Creneau> listResultTemp = jr.rechercherTacheDecomposable(nomTache);
			listResult.addAll(listResultTemp);
		}
		return listResult;
	}
	
	public int replanifier(TacheSimple tache, long dureeSupp, long dureeMin) {
		if(tache.getEtatAvancement() != EtatAvancement.InProgress) {
			return -1;//la tache ne peut pas etre replanfiée a cause de létat d'avancement
		}
		TacheSimple tacheReplannifier = (TacheSimple) tache.clone();
		tacheReplannifier.setPeriodicite(0);
		tacheReplannifier.setDuree(dureeSupp);
		return this.plannifierTacheAutomatiquement(tacheReplannifier, dureeMin);
	}
	
	public int replanifier(TacheSimple tache, long dureeSupp, long dureeMin, LocalDate deadline) {
		if(tache.getEtatAvancement() != EtatAvancement.InProgress) {
			return -1;//la tache ne peut pas etre replanfiée a cause de létat d'avancement
		}
		TacheSimple tacheReplannifier = (TacheSimple) tache.clone();
		tacheReplannifier.setPeriodicite(0);
		tacheReplannifier.setDuree(dureeSupp);
		LocalDate today = LocalDate.now();
		return this.plannifierTacheAutomatiquement(tacheReplannifier, dureeMin, today, deadline);
	}
	
	
	public int replanifier(TacheDecomposable tache, long dureeSupp, long dureeMin) {
		if(tache.getEtatSousTache() != EtatAvancement.InProgress ||(tache.getEtatSousTache() == null && tache.getEtatAvancement() != EtatAvancement.InProgress)) {
			return -1;//la tache ne peut pas etre replanfiée a cause de létat d'avancement
		}
		TacheDecomposable tacheReplanifier = new TacheDecomposable(tache.getNom(), dureeSupp, tache.getPriorite()
				, tache.getCategorie());
		if(this.testTacheDecomposee(tacheReplanifier)) {
			ArrayDeque<Creneau> sousTaches = rechercherTacheDecomposable(tache.getNom());
			if(sousTaches.size() == 0) {
				return -2;//sous taches introuvables
			}
			tacheReplanifier.extend((TacheDecomposable) sousTaches.getLast().getTache());
			int result =  this.plannifierTacheAutomatiquement(tacheReplanifier, dureeMin);
			
			if(result == 0) {
				this.evaluerTacheDomposable(tache);
			}
			
			return result;
			
		}

		return -1;//probleme de planning
		
	}
	
	public int replanifier(TacheDecomposable tache, long dureeSupp, long dureeMin, LocalDate dateLimite) {
		if(tache.getEtatSousTache() != EtatAvancement.InProgress ||(tache.getEtatSousTache() == null && tache.getEtatAvancement() != EtatAvancement.InProgress)) {
			return -1;//la tache ne peut pas etre replanfiée a cause de létat d'avancement
		}
		TacheDecomposable tacheReplanifier = new TacheDecomposable(tache.getNom(), dureeSupp, tache.getPriorite()
				, tache.getCategorie());
		if(this.testTacheDecomposee(tacheReplanifier)) {
			//on peut la replanifier plannifier
			ArrayDeque<Creneau> sousTaches = rechercherTacheDecomposable(tache.getNom());
			if(sousTaches.size() == 0) {
				return -2;//sous taches introuvables
			}
			tacheReplanifier.extend((TacheDecomposable) sousTaches.getLast().getTache());
			LocalDate today = LocalDate.now();
			int result =  this.plannifierTacheAutomatiquement(tacheReplanifier, dureeMin, today, dateLimite);
			
			if(result == 0) {
				this.evaluerTacheDomposable(tache);
			}
			
			return result;
		}
		return -1;//probleme de planning
		
	}
	
	public Creneau rechercherTacheSimple(String nom) {
		Creneau cr;
		for(Journee jr : listeJournees) {
			cr = jr.rechercherTacheSimple(nom);
			if(cr != null) {
				return cr;
			}
		}
		
		return null;
	}
	
	public int reporter(TacheSimple tache, long dureeMin) {
		if( tache.getEtatAvancement() != EtatAvancement.InProgress) {
			return -2;//pas en progres
		}
		
		Creneau cr = rechercherTacheSimple(tache.getNom());
		if(cr == null) {
			//la tache n'existe pas dans le planning
			return -1;
		}
		
		cr.liberer();
		int result =  this.plannifierTacheAutomatiquement(tache, dureeMin, LocalDate.now().plusDays(1), listeJournees.last().getDate());
		if(result != 0) {
			cr.setEtatCreneau(EtatCreneau.Bloque);
		}
		
		return result;
	}
	
	public int reporter(TacheSimple tache, long dureeMin, LocalDate dateLimite) {
		if( tache.getEtatAvancement() != EtatAvancement.InProgress) {
			return -2;//pas en progres
		}
		Creneau cr = rechercherTacheSimple(tache.getNom());
		if(cr == null) {
			//la tache n'existe pas dans le planning
			return -1;
		}
		
		cr.liberer();
		int result =  this.plannifierTacheAutomatiquement(tache, dureeMin, LocalDate.now().plusDays(1), dateLimite);
		if(result != 0) {
			cr.setEtatCreneau(EtatCreneau.Bloque);
		}
		
		return result;
	}
	
	public int reporter(TacheDecomposable tache, long dureeMin) {
		if(tache.getEtatSousTache() != EtatAvancement.InProgress ||(tache.getEtatSousTache() == null && tache.getEtatAvancement() != EtatAvancement.InProgress)) {
			return -1;//pas en progres
		}
		ArrayDeque<Creneau> listCrTc = rechercherTacheDecomposable(tache.getNom());
		//restreindre le domaine des sous taches a ce qu'on a besoin
		while(!listCrTc.isEmpty() && listCrTc.getFirst().getTache() != tache) {
			listCrTc.poll();
		}
		if(listCrTc.isEmpty()) {
			return -2;//tache inexistante
		}
		
		
		TacheDecomposable newTache;
		if(tache.getDateLimite() == null) {
			newTache = new TacheDecomposable(tache.getNom(), tache.getDuree(), tache.getPriorite(), tache.getCategorie());
	
		} else {
			newTache = new TacheDecomposable(tache.getNom(), tache.getDuree(), tache.getPriorite(), tache.getDateLimite(), tache.getCategorie());		
		}
		
		newTache.setEtatAvancement(EtatAvancement.Delayed);
		
		long duree = 0;
		//calculer la duree
		Iterator<Creneau> iter = listCrTc.iterator();
		while(iter.hasNext()) {
			Creneau temp = iter.next();
			duree += ((TacheDecomposable) temp.getTache()).getDureeTacheDecomposable();
			temp.setEtatCreneau(EtatCreneau.Libre);
		}
		newTache.setDuree(duree);
		newTache.setDureeTacheDecomposable(newTache.getDuree());
		
		Journee jr = this.rechercherJournee(listCrTc.getFirst());
		jr = listeJournees.higher(jr);
		if(jr != null) {
			if(tache.getDateLimite() != null) {
				  LocalDate localDate = tache.getDateLimite().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				int result = this.plannifierTacheAutomatiquement(newTache, dureeMin, jr.getDate(), localDate);
				if(result == 3) {
					this.proposer(newTache, dureeMin, jr.getDate(), localDate);
				} else if(result == 0) {
					this.evaluerTacheDomposable(tache);
				} else {
					iter = listCrTc.iterator();
					while(iter.hasNext()) {
						Creneau temp = iter.next();
						duree += ((TacheDecomposable) temp.getTache()).getDureeTacheDecomposable();
						temp.setEtatCreneau(EtatCreneau.Bloque);
					}
				}
				return result;
			} else {
				int result =  this.plannifierTacheAutomatiquement(newTache, dureeMin, jr.getDate(), listeJournees.last().getDate());
				if(result == 3) {
					this.proposer(newTache, dureeMin, jr.getDate(), listeJournees.last().getDate());
				}
				else if(result == 0) {
					this.evaluerTacheDomposable(tache);
				}
				else {
					iter = listCrTc.iterator();
					while(iter.hasNext()) {
						Creneau temp = iter.next();
						duree += ((TacheDecomposable) temp.getTache()).getDureeTacheDecomposable();
						temp.setEtatCreneau(EtatCreneau.Bloque);
					}
				}
				return result;
			}
			
		}
		return -3;//espace insuffisant pour reporter 
	}
	
	public boolean evaluerTacheDomposable(TacheDecomposable tache) {
		ArrayDeque<Creneau> listCreneau =  this.rechercherTacheDecomposable(tache.getNom());
		if(listCreneau == null) {
			return false;
		}
		Iterator<Creneau> iter = listCreneau.iterator();
		if(iter.hasNext()) {
			tache.setEtatAvancement(((TacheDecomposable) iter.next().getTache()).getEtatSousTache());
		}
		while(iter.hasNext()){
			Creneau temp = iter.next();
			if(((TacheDecomposable) temp.getTache()).getEtatSousTache().ordinal() < tache.getEtatSousTache().ordinal()) {
				((TacheDecomposable) temp.getTache()).getEtatSousTache();
			}
		}
		
		if(tache.getEtatAvancement() == EtatAvancement.Unscheduled) {
			tache.setEtatAvancement(EtatAvancement.notRealised);//dans le cas ou l atache est totallement plannifié
		}
		
		return true;
	}
	
	//Getters and Setters
	public TreeSet<Journee> getListeJourneest() {
		return listeJournees;
	}
	public void setListeJournees(TreeSet<Journee> set) {
		this.listeJournees = set;
	}
	
    public boolean supprimerTache(Tache tache) {
        for (Journee j : listeJournees) {
            if (j.supprimerTache(tache)) {
                return true;
            }
        }
        return false;
    }

    public int etatRealisationTache(Journee jour, EtatAvancement motif) {
        if (listeJournees.size() == 0 || listeJournees == null) {
            return 0;
        }
        for (Journee j : listeJournees) {
            if (j.getDate().equals(jour.getDate())) {
                return (j.etatRealisationTache(motif));
            }
        }
        return 0;
    }

    public int etatRealisationTachePeriode(LocalDate deb, LocalDate fin, EtatAvancement motif) {
        int cpt = 0;
        if (listeJournees.size() == 0 || listeJournees == null) {
            return 0;
        }
        for (Journee j : listeJournees) {
            if (j.getDate().isAfter(deb) && j.getDate().isBefore(fin)) {
                cpt += j.etatRealisationTache(motif);
            }
        }
        return cpt;
    }

    public int tachesCompletees(LocalDate debut, LocalDate fin) {
        return (etatRealisationTachePeriode(debut, fin, EtatAvancement.Completed));
    }

    public int tachesPrevues(LocalDate debut, LocalDate fin) {
        // retourne le nombre de taches prévues dans une période (non completees)
        return (etatRealisationTachePeriode(debut, fin, EtatAvancement.Unscheduled) +
                etatRealisationTachePeriode(debut, fin, EtatAvancement.InProgress) +
                etatRealisationTachePeriode(debut, fin, EtatAvancement.Delayed) +
                etatRealisationTachePeriode(debut, fin, EtatAvancement.notRealised));

    }

    public double rendementJournalier(LocalDate debut, LocalDate fin) {
        if (tachesPrevues(debut, fin) != 0) {
            return (tachesCompletees(debut, fin) / tachesPrevues(debut, fin));
        }
        return -1;

    }

    public double moyenneRendementJournalier() {// retourne le rendement journalier à partir de la date d'aujourd'hui
        if (listeJournees == null) {
            return -1;
        } else {
            return rendementJournalier(LocalDate.now(), listeJournees.last().getDate());
        }
    }

    public int encouragement(int min) {// le nombre de jours ou il a dépassé le nombre minimal de taches
        int e = 0;
        for (Journee j : this.listeJournees) {
            if (j.minDepasse(min)) {
                e++;
            }

        }
        return e;

    }

    public Journee journeePlusRentable() {// retourne la journee dans laquelle il a ete le plus rentable
        int max = 0;// contient la rentabilité maximale de la journee pour tout le calendrier
        if(listeJournees == null || listeJournees.size() == 0) {
        	return null;
        }
        Journee jo = listeJournees.first();
        Iterator<Journee> iterator = listeJournees.iterator();
        if (iterator == null) {
            return null;
        }
        while (iterator.hasNext()) {
        	Journee temp = iterator.next();
            if (temp.rentabilite() > max) {
                max = temp.rentabilite();
                jo = temp;
                System.out.println("aaaaaaaaaaa");
            }
        }

        return jo;
    }

    public long dureeCategorie(Categorie c) {// calcule la durée passée sur des taches de meme catégorie dans une
                                             // journee
        long d = 0;
        if (listeJournees == null) {
            return 0;
        }
        for (Journee jr : listeJournees) {
            d += jr.dureeCategorie(c);
        }
        return d;
    }
}
;