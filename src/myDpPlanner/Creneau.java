package myDpPlanner;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Creneau implements Comparable<Creneau>, Cloneable, Serializable {
	private Tache tache;
    private LocalTime heureDebut;
    private LocalTime heureFin;  
    private EtatCreneau etatCreneau;
    //rani dert enum EtatCreneau
    //psq kayen 3 cas possibles w créneau bloqué m3netha on peut pas replanifier une autre tache fih

    public Creneau(LocalTime heure1, LocalTime heure2, EtatCreneau etCr, Tache tache) {
        this.heureDebut = heure1;
        this.heureFin = heure2; 
        this.tache = tache;
        this.etatCreneau = etCr;
    }
    
    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

	public Tache getTache() {
		return tache;
	}

	public void setTache(Tache tache) {
		this.tache = tache;
	}
	
	public EtatCreneau getEtatCreneau() {
		return etatCreneau;
	}

	public void setEtatCreneau(EtatCreneau etatCreneau) {
		this.etatCreneau = etatCreneau;
	}
	public void afficher(){
	    System.out.println("l'heure du début de ce créneau:"+this.heureDebut);
	    System.out.println("l'heure de la fin de ce créneau:"+this.heureFin);
	    System.out.println("libre (true), occupé (false):"+this.etatCreneau);
	    if(tache != null) {
	    	tache.afficher();
	    }
	    System.out.println("----------");
	    
	}
	
	public static Creneau creerCreneau(){
	    Creneau cr=new Creneau(null, null, EtatCreneau.Occupe, null);
	    Scanner scanner = new Scanner(System.in);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime heure = null;
        do {
            System.out.println("Entrez l'heure de début de format HH:mm :");
            String input = scanner.nextLine();
            try {
                heure = LocalTime.parse(input, format);
            } catch (Exception e) {
                System.out.println("L'heure saisie est invalide. Veuillez réessayer.");
            }
        } while (heure == null);
		cr.setHeureDebut(heure);
		do {
		    System.out.println("Entrez l'heure de fin de format HH:mm :");
		    String input = scanner.nextLine();
		    try {
		        heure = LocalTime.parse(input, format);
		    } catch (Exception e) {
		        System.out.println("L'heure saisie est invalide, Veuillez réessayer");
		    }
		} while (heure == null);
		cr.setHeureFin(heure);
		System.out.println("veuillez entrer la durée minimale du créneau en minutes");
		long a = scanner.nextInt();
		     cr.setEtatCreneau(EtatCreneau.Occupe);;
		return cr;
	}
	
	
	
	public boolean estDecomposable(long dureeTache, long duree_min) { //méthode qui indique si on peut décomposer un creneau donné lors de l'introduction d'une tache
	        
	        Duration duree = Duration.between(this.heureDebut, this.heureFin);
	        return duree.toMinutes() - dureeTache > duree_min;
	        //on peut decomposer quand la duree du creneau - duree tache > duree minimale
	    }
	
	//on utilise cette méthode seulement après s'assurer que le créneau est décomposable par la fonction précédente
    public Creneau decomposer(Tache tache, long duree_min){
    	long dureeTache;
    	if(tache instanceof TacheDecomposable ) {
    		 dureeTache = ((TacheDecomposable) tache).getDureeTacheDecomposable();
    	} else {
    		dureeTache = tache.getDuree();
    	}
    	
        Creneau cr1, cr2;
        cr1 = this;
        LocalTime h = cr1.getHeureDebut();
        
        // ajout de la durée en minutes à l'heure de départ
        LocalTime heureArrivee = h.plusMinutes(dureeTache);
    
        cr2 = new Creneau(heureArrivee, this.getHeureFin(), EtatCreneau.Libre, null);
        
        cr1.setHeureFin(heureArrivee);
        cr1.planifierTache(tache);
        
        return cr2;
     }
    
    public boolean planifierTache(Tache tache) {
    	if(this.etatCreneau == EtatCreneau.Libre) {
        	this.tache = tache;
        	this.etatCreneau = EtatCreneau.Occupe;
        	this.tache.setEtatAvancement(EtatAvancement.notRealised);
        	return true;
    	}
    	return false;
    }
    public long calculerDuration(){
    	Duration duree = Duration.between(this.heureDebut, this.heureFin);
        return duree.toMinutes();
     } 
    
    public void changerNomSousTaches(String nom) {
    	//on est sur que la tache est décomposable
    	TacheDecomposable td = (TacheDecomposable) tache;
    	//System.out.println("entred");
    	td.setNomSousTache(nom);
    }
    
    public boolean rechercherTache(Tache tache) {
    	if(tache == null || this.tache == null) {
    		return this.tache == null;
    	}
    
    	return tache.getNom() == this.tache.getNom();
    }
    
    public boolean rechercherTache(String nom) {
    	if(this.tache == null) {
    		return false;
    	}
    
    	return nom.equals(this.tache.getNom());
    }
    
    
    public void liberer() {
    	this.etatCreneau = EtatCreneau.Libre;
    	this.tache = null;
    }
    public void bloquer() {
    	this.etatCreneau = EtatCreneau.Bloque;
    }
    
	public int hashCode() {
		return this.getHeureDebut().hashCode();
	}
	
	public boolean equals(Object o) {
		return this.hashCode() == ((Creneau) o).hashCode();
	}
	
    @Override
    public int compareTo(Creneau cr) {
    	return this.getHeureDebut().compareTo(cr.getHeureDebut());
    }

    public Object clone() throws CloneNotSupportedException
    {
        try {
            Creneau cloned = (Creneau) super.clone();
            if(this.tache != null) {
                if(this.tache instanceof TacheDecomposable) {
                	cloned.setTache((TacheDecomposable) ((TacheDecomposable)this.tache).clone());
                } else {
                	cloned.setTache((TacheSimple) ((TacheSimple)this.tache).clone());
                }
            } else {
            	cloned.tache = null;
            }
            cloned.heureDebut = LocalTime.from(this.heureDebut);
            cloned.heureFin = LocalTime.from(this.heureFin);
            cloned.etatCreneau = this.etatCreneau;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


	public boolean etatRealisationTache(EtatAvancement motif) {
		if (this.tache.getEtatAvancement().equals(motif)) {
			return true;
		}
		return false;
	}

	public boolean memeCategorie(Categorie cat) {
		return (this.tache.getCategorie().equals(cat));
	}

 }


