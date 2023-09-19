package myDpPlanner;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class TacheSimple extends Tache implements Serializable {
   private int periodicite;
   
   public TacheSimple() {
	   
   }
   public TacheSimple(String nom, long duree, Priorite priorite, Date dateLimite, 
		   Categorie categorie, int periodicite) {
       this.nom = nom;
       this.duree = duree;
       this.priorite = priorite;
       this.dateLimite = dateLimite;
       this.categorie = categorie;
       this.etatAvancement =  EtatAvancement.Unscheduled;
       this.periodicite = periodicite;
   }
   
   public TacheSimple(String nom, long duree, Priorite priorite, 
		   Categorie categorie, int periodicite) {
       this.nom = nom;
       this.duree = duree;
       this.priorite = priorite;
       this.dateLimite = null;
       this.categorie = categorie;
       this.etatAvancement =  EtatAvancement.Unscheduled;
       this.periodicite = periodicite;
   }
   
   
   public  void afficher(){
	   	super.afficher();
        System.out.println("Périodicité:"+this.periodicite);
    };


    public void setPeriodicite(int p) {
        this.periodicite = p;
    }
    
    public int getPeriodicite() {
    	return this.periodicite;
    }

    public static TacheSimple creerTache(){
	    TacheSimple t = new TacheSimple();
	    String s; 
	    long l;
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("nom de la tache");
	    s=scanner.nextLine();
	    t.setNom(s);
	    System.out.println("durée de la tache");
	    l=scanner.nextInt();
	    t.setDuree(l);
	    int j=1;
	    System.out.println("Veuillez introduire sa priorité");
	    for (Priorite c : Priorite.values()) {
	    System.out.println(j+"-"+c);
	    j++;}
	    System.out.println("Entrez le numéro correspondant");
	    j=scanner.nextInt();
	    while(j<1 || j>3){
	        System.out.println("option non disponible, veuillez réessayer"); 
	        j=scanner.nextInt();  
	    }
	    t.setPriorite(Priorite.values()[j-1]);
	   
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false); // pour que le parsing lève une exception si le format est invalide
        
        Date date = null;

        System.out.println("Entrez une date valide du format jj/mm/aaaa :");
        scanner.nextLine();//je ne sais pas pourquoi il saute à l'exception si je ne mets pas cette ligne inutile
        String input = scanner.nextLine();
        
        try {
            date = format.parse(input);
        } catch (ParseException e) {
            System.out.println("Le format de la date est invalide. Veuillez réessayer.");
        }

        while (date == null) {
            System.out.println("Entrez une date au format jj/mm/aaaa :");
          input = scanner.nextLine();
            try {
                date = format.parse(input);
            } catch (ParseException e) {
                System.out.println("Le format de la date est invalide. Veuillez réessayer.");
            }

        }  
	      // System.out.println("La date saisie est : " + format.format(date)); pour  afficher la date sous format jj/mm/aaaa
	
	       t.setDateLimite(date);
	       System.out.println("Veuillez introduire une catégorie");
	    Categorie cat= new Categorie();
	    cat=Categorie.creerCategorie();
	    t.setCategorie(cat);
	  
	    //tester la validité du creneau
	    System.out.println("Voulez vous rendre cette tache périodique? (0 pour non et 1 pour oui)");
	    int opt,m;
	    opt= scanner.nextInt();
	while (opt<0||opt>1){
	    System.out.println("Option non disponible, veuillez réessayer");  
	    opt= scanner.nextInt();
	}
	    switch(opt){
	        case 0: t.setPeriodicite(1);
	        case 1: System.out.println("entrez sa périodicité en jours");
	        m = scanner.nextInt();
	        t.setPeriodicite(m);
	    }
	    t.setEtatAvancement(EtatAvancement.values()[2]);//affectation de l'état en cours par défaut
	    t.afficher();
	    
	    
	    return t;
	};
	
    @Override
    public Object clone() {
        try {
            TacheSimple cloned = (TacheSimple) super.clone();
            cloned.setCategorie(this.categorie);
            cloned.setDateLimite(this.dateLimite);
            cloned.setDuree(this.getDuree());
            cloned.setEtatAvancement(this.etatAvancement);
            cloned.setNom(this.getNom());     
            cloned.setPriorite(this.getPriorite());
            cloned.setPeriodicite(this.periodicite);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}







