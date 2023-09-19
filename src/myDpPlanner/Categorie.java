package myDpPlanner;


import java.io.Serializable;
import java.util.Scanner;
public class Categorie implements Serializable {
	 private String categorie;
	 Couleur couleur; 
	 
	 public Categorie() {
		 
	 }
	 
	 public Categorie(String categorie, Couleur couleur) {
		 this.categorie = categorie;
		 this.couleur = couleur;
	 }
	 
	//setters 
	public void setCategorie(String categorie){
	this.categorie=categorie;
	}
	public void setCouleur(Couleur couleur){
	  this.couleur=couleur;
	  }
	//getters
	
	public String getCategorie(){
	  return this.categorie;
	}
	public Couleur getCouleur(){
	  return this.couleur;
	}
	
	public void afficher(){
	  System.out.println("la catégorie : "+this.categorie);
	  System.out.println("la couleur: "+this.couleur);
	}
	
	public static Categorie creerCategorie(){
		  Categorie c=new Categorie();
		  String s;
		  System.out.println("entrez la catégorie de votre tache");
		  Scanner scanner = new Scanner(System.in);
		  s=scanner.nextLine();
		  c.setCategorie(s);
		  int a=1;
		  for (Couleur co : Couleur.values()) {
		    System.out.println(a + "-" + co);
		    a++;
		    }
		  System.out.println("entrez la couleur de la catégorie en entrant le numéro correspondant");
		 
		  a=scanner.nextInt();
		  while(a<1 || a>9){
		      System.out.println("option non disponible, veuillez réessayer"); 
		      a=scanner.nextInt();  
		  }
		  c.setCouleur(Couleur.values()[a-1]);
		  c.afficher();
		 return c;
	}
    
	public boolean equals(Object c){
		return (this.categorie.equals(((Categorie) c).categorie) && this.couleur.equals(((Categorie) c).couleur) );}

	public int hashCode() {
		return this.getCategorie().hashCode();
	}

}
