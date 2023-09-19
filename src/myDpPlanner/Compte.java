package myDpPlanner;

import java.io.Serializable;
import java.util.*;

public class Compte implements Comparable<Compte>,Serializable{
  
    private String pseudo;
    private String motDePasse;
    private String questSecrete;
    private String response;
    private boolean connected;
    
    public Compte(String pseudo, String motDePass, String questSecrete, String response) {
    	this.setPseudo(pseudo);
    	this.setMotDePasse(motDePass);
    	this.setQuestSecrete(questSecrete);
    	this.setResponse(response);
    	this.setConnected(true);
    }
    
	public void afficher(){
		System.out.println(this.pseudo);
	}
    public static Compte creerCompte() {
//        Scanner scanner = new Scanner(System.in);
//        String input;
//        
//        if (this.compteExiste == false) {
//            System.out.println("Entrez votre nom : ");
//            input = scanner.nextLine();
//            this.pseudo = input;
//            
//            System.out.println("Entrez votre mot de passe : ");
//            input = scanner.nextLine();
//            this.motDePasse = input;
//            
//            System.out.println("Votre compte a été créé avec succès.");
//            this.compteExiste = true;
//        } else {
//            // S'authentifier
//            System.out.println("Entrez votre nom : ");
//            input = scanner.nextLine();
//            while (!this.pseudo.equals(input)) {
//                System.out.println("Il semble que votre nom d'utilisateur n'est pas correct. Veuillez réessayer : ");
//                input = scanner.nextLine();
//            }
//            
//            System.out.println("Entrez votre mot de passe : ");
//            input = scanner.nextLine();
//            while (!this.motDePasse.equals(input)) {
//                System.out.println("Mot de passe incorrect. Veuillez réessayer : ");
//                input = scanner.nextLine();
//            }
//            
//            System.out.println("Authentification faite avec succès");
//        }
	      Scanner scanner = new Scanner(System.in);
	      String input1, input2;
	      System.out.println("Entrez votre nom : ");
	      input1 = scanner.nextLine();
		 
	     // scanner.nextLine();
	      System.out.println("Entrez votre mot de passe : ");
	      input2 = scanner.nextLine();
	     // scanner.nextLine();
	      Compte cmp = new Compte(input1, input2, null, null);
	      return cmp;
    }
    
    public boolean verifierCompte(String username, String password) {
    	if(password == null || username == null) {
    		return false;
    	}
    
    	return (this.pseudo.equals(username) && this.motDePasse.equals(password));
    }
    
    public boolean verifierCompte(String username) {
    	if( username == null) {
    		return false;
    	}
    	
    	return this.pseudo.equals(username);
    }
    
    public void seConnecter() {
    	setConnected(true);
    }
    public void seDeconncter() {
    	setConnected(false);
    }
    public void changerMotDePasse(String nouveauMotDePasse) {
        setMotDePasse(nouveauMotDePasse);
    }

    public void changerPseudo(String nouveauPseudo) {
        setPseudo(nouveauPseudo);
    }

    
    //getters and setters

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public boolean equals(Compte co){
		return (this.pseudo.equals(co.pseudo) && this.motDePasse.equals(co.motDePasse));
	}

	@Override
	public int compareTo(Compte o) {
		if (this.pseudo.equals(o.pseudo) && !this.motDePasse.equals(o.motDePasse)){
			return 1; //si le mot de passe est incorrect et le pseudo est correct
		}
			if(!this.pseudo.equals(o.pseudo) && this.motDePasse.equals(o.motDePasse)){
             return 2;//si le mot de passe est correct et le pseudo est incorrect
			}
			if(this.pseudo.equals(o.pseudo) && this.motDePasse.equals(o.motDePasse)){
				return 0;//si le mot de passe est correct et le pseudo est correct
			}
			return -1;//si les deux sont incorrectes
			
		}

	public String getQuestSecrete() {
		return questSecrete;
	}

	public void setQuestSecrete(String questSecrete) {
		this.questSecrete = questSecrete;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
		
	}
    

