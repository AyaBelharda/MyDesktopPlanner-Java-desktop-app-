package myDpPlanner;

import java.io.Serializable;
import java.util.*;

public class Projet implements Comparable<Projet>, Serializable {

		private String nomProjet;
		private String description;
		private HashSet<Tache> listeTaches;
		private EtatAvancement etatAvancement;
		
		public Projet(String nomProjet, String description) {
			this.nomProjet = nomProjet;
			this.description = description;
			this.listeTaches = new HashSet<Tache>();
		}
		
		public static Projet creerProjet(){
			Projet pj;
			Scanner scanner = new Scanner(System.in);
			System.out.print("nom Projet : ");
			String nom = scanner.nextLine();
			pj = new Projet(nom, null);
			return pj;
		}
		
		public boolean supprimerTache(Tache tc) {
			if (listeTaches.contains(tc)) {
				listeTaches.remove(tc); // remove the element from the set
				return true;
			} else {
			    return false;//Tache non existante
			}
		}
		
		public boolean ajouterTache(Tache tc) {
			return listeTaches.add(tc);
		}
		
		public boolean modifierTache(Tache tc, Tache toReplace) {
			if (listeTaches.contains(tc)) {
				listeTaches.remove(tc); // remove the element from the set
				return listeTaches.add(toReplace);
			} else {
			    return false;//Tache non existante
			}
		}
		
		public Tache rechercherTache(String cleRecherche) {
			//La cle de recherche est "nomTache + category tache"
			for(Tache tc : listeTaches) {
				if((tc.getNom() + tc.getCategorie()).equals(cleRecherche)) {
					return tc;
				}
			}
			return null;
			
		}
		
		//getters and setters
		public String getNomProjet() {
			return nomProjet;
		}

		public void setNomProjet(String nomProjet) {
			this.nomProjet = nomProjet;
		}
		
		public int hashCode() {
			return this.nomProjet.hashCode();
		}
		
		public boolean equals(Object o) {
			return this.hashCode() == ((Projet) o).hashCode();
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public HashSet<Tache> getListeTaches() {
			return listeTaches;
		}

		@Override
		public int compareTo(Projet pj) {
			return this.getNomProjet().compareTo(pj.getNomProjet());
		}


}
