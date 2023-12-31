package myDpPlanner;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


public abstract class Tache implements Comparable<Tache>, Cloneable, Serializable{
    protected String nom;
    protected long duree;
    protected Priorite priorite;
    protected Date dateLimite;
    protected Categorie categorie;
    protected EtatAvancement etatAvancement;
   // protected boolean decomposable;

   
    
    public void afficher() {
        System.out.println("Nom de la tâche: " + this.nom);
        System.out.println("Durée de la tâche: " + this.duree + " minutes");
        System.out.println("Priorité de la tâche: " + this.priorite);
        System.out.println("Date limite de la tâche: " + this.dateLimite);
        System.out.println("Catégorie de la tâche: " + this.categorie);
        System.out.println("État d'avancement de la tâche: " + this.etatAvancement);
   
    }
    
    public boolean delaiDepasse(LocalDate date) {
    	if(this.dateLimite == null) {
    		return false;
    	}
    	
    	LocalDate today = LocalDate.now();
    	return today.isAfter(date);
    }
    
    public void classerTache(Categorie categorie) {
        this.categorie = categorie;
    }

    public void renommer(String nouveauNom) {
        this.nom = nouveauNom;
    }

    public void changerEtat(EtatAvancement nouvelEtat) {
        this.etatAvancement = nouvelEtat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getDuree() {
        return duree;
    }

    public void setDuree(Long duree) {
        this.duree = duree;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public Date getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public EtatAvancement getEtatAvancement() {
        return etatAvancement;
    }

    public void setEtatAvancement(EtatAvancement etatAvancement) {
        this.etatAvancement = etatAvancement;
    }

    public int hashode() {
    	return (this.nom + this.categorie).hashCode();
    }
    
    public boolean equals(Object o) {
    	return this.hashCode() == ((Tache) o).hashCode();
    }

    public int compareTo(Tache tache) {
    	return this.getPriorite().compareTo(tache.getPriorite());
    }
}
