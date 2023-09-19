package myDpPlanner;

import java.io.Serializable;
import java.util.Date;

public class TacheDecomposable extends Tache implements Decomposable, Serializable {
 
    private long dureeTacheDecomposable;
    private int numeroTache;
    private String nomSousTache;
    private EtatAvancement etatSousTache;
    
    public TacheDecomposable(String nom, long duree, Priorite priorite, Date dateLimite, Categorie categorie) {
        this.nom = nom;
        this.duree = duree;
        this.priorite = priorite;
        this.dateLimite = dateLimite;
        this.categorie = categorie;
        this.etatAvancement = EtatAvancement.Unscheduled;
        this.etatSousTache = EtatAvancement.Unscheduled;
        this.numeroTache = 0;
        this.dureeTacheDecomposable = duree;
   
    }
    
    public TacheDecomposable(String nom, long duree, Priorite priorite, Categorie categorie) {
         this.nom = nom;
         this.duree = duree;
         this.priorite = priorite;
         this.dateLimite = null;
         this.categorie = categorie;
         this.etatAvancement = EtatAvancement.Unscheduled;
         this.etatSousTache = EtatAvancement.Unscheduled;
         this.numeroTache = 0;
         this.dureeTacheDecomposable = duree;
  
     }
    
    public  void afficher(){
    	super.afficher();
        System.out.println("dureeTacheDecomposable = " + this.dureeTacheDecomposable);
        System.out.println("numeroTache = " + this.numeroTache);
        System.out.println("nomSousTache = " + this.nomSousTache);
        System.out.println("etat = " + this.etatAvancement);
        
    };
    
    public Object decomposer(long duree) {
    	
    	
    	if(this.dureeTacheDecomposable > duree) {
        	TacheDecomposable result = new TacheDecomposable(this.getNom(), this.getDuree(), this.getPriorite(), this.getDateLimite()
        			,this.getCategorie());
        	
        	result.setEtatSousTache(EtatAvancement.notRealised);
        	result.setNumeroTache(numeroTache);
        	result.setNomSousTache(this.getNom() + numeroTache);
        	this.numeroTache++;
        	this.setNumeroTache(numeroTache);
        	this.setNomSousTache(this.getNom() + numeroTache);
        	result.setDureeTacheDecomposable(duree);
        	this.setDureeTacheDecomposable(this.dureeTacheDecomposable - duree);
        	return result;//on retourne une nouvelle tache qui precede this tache
    	} 
        return null;//si la tache ne peut pas etre plannifier on a pas besoin de decomposer	
    }
    
    public void extend(TacheDecomposable tc) {
    	this.setNumeroTache(tc.getNumeroTache());
    	this.setNomSousTache(tc.getNom() + tc.getNumeroTache());
    }
    
    public  void ajouterSousTache(Tache sousTache){};
    public  void supprimerSousTache(Tache sousTache){}
	public int getNumeroTache() {
		return numeroTache;
	}
	public void setNumeroTache(int numeroTache) {
		this.numeroTache = numeroTache;
	}
	public long getDureeTacheDecomposable() {
		return dureeTacheDecomposable;
	}
	public void setDureeTacheDecomposable(long dureeTacheDecomposable) {
		this.dureeTacheDecomposable = dureeTacheDecomposable;
	}

	public String getNomSousTache() {
		return nomSousTache;
	}

	public void setNomSousTache(String nomSousTache) {
		this.nomSousTache = nomSousTache;
	}

    @Override
    public Object clone() {
        try {
            TacheDecomposable cloned = (TacheDecomposable) super.clone();
            cloned.setCategorie(this.categorie);
            cloned.setDateLimite(this.dateLimite);
            cloned.setDuree(this.getDuree());
            cloned.setDureeTacheDecomposable(this.getDureeTacheDecomposable());
            cloned.setEtatAvancement(this.etatAvancement);
            cloned.setNom(this.getNom());
            cloned.setNomSousTache(this.getNomSousTache());
            cloned.setNumeroTache(this.getNumeroTache());
            cloned.setPriorite(this.getPriorite());
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

	public EtatAvancement getEtatSousTache() {
		return etatSousTache;
	}

	public void setEtatSousTache(EtatAvancement etatSousTache) {
		this.etatSousTache = etatSousTache;
	}
}
