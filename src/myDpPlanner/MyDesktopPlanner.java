package myDpPlanner;

import java.io.*;
import java.util.*;
public class MyDesktopPlanner {
	
	private HashSet<Utilisateur> listeUtilisateurs;
	private File fichierUtilisateurs = new File("C:\\Users\\Oualid_CHABANE\\eclipse-workspace\\CHABANE_BELHARDA_G01_TP2_POO\\src\\myDpPlanner\\Users.dat");//le fichier de nos utilisateurs
	
	
	public MyDesktopPlanner() {//il y avait le parametre String chemin
		// this.fichierUtilisateurs = new File(chemin);
		// if (!this.fichierUtilisateurs.exists()) {
        //     try {
        //         this.fichierUtilisateurs.createNewFile();
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
		listeUtilisateurs = new HashSet<Utilisateur>();
	}
	
//	public void  ajouterUtilisateur(Utilisateur utilisateur) {
//		ObjectOutputStream out; 
//		try { out = new ObjectOutputStream( new BufferedOutputStream( new FileOutputStream( new File("Point.dat"))));
//			//écriture de l'Utilisateur
//			out.writeObject(utilisateur);
//			}	
//		catch (FileNotFoundException e) { 
//			e.printStackTrace(); 
//		} catch (IOException e) { 
//			e.printStackTrace(); 
//		} 
//		
//	}
	
	
	 public void afficherContenuFichier(File fichier) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
			while (true) {
				Utilisateur utilisateur = (Utilisateur) ois.readObject();
				utilisateur.afficher();
			}
		} catch (EOFException e) {
			// fin de fichier atteinte, on sort de la boucle
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}




	// public void installer(Machine machin) {
	// 	System.out.println("Application installee sur la machine : " + machin.getName());
	// }
	
	 public boolean ajouterUtilisateur(Utilisateur us) {
	 	return listeUtilisateurs.add(us);
	 }
	
	// public boolean removeUtilisateur(Utilisateur us) {
	// 	if(listeUtilisateurs.contains(us)) {
	// 		listeUtilisateurs.remove(us);
	// 		return true;
	// 	} else {
	// 		return false;//utilisateur inexistant
	// 	}
	// }
	
	// public boolean editUtilisateur(int id, Utilisateur usr) {
	// 	for(Utilisateur us : listeUtilisateurs) {
	// 		if(us.getId() == id) {
	// 			listeUtilisateurs.remove(us);
	// 			if(listeUtilisateurs.add(usr)) {
	// 				return true;
	// 			} else {
	// 				listeUtilisateurs.add(us);
	// 				return false;
	// 			}
	// 		}
	// 	}
	// 	return false;
	// }
	public void afficherUtilisateursHashSet() {
		for (Utilisateur u : this.listeUtilisateurs) {
			u.afficher();
			// u.afficherCalendrier();
		}
	}

	public void afficherFichier() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierUtilisateurs));
			while (true) {
				try {
					Utilisateur utilisateur = (Utilisateur) in.readObject();
					utilisateur.afficher();
				} catch (EOFException e) {
					break; // Fin de fichier atteinte
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ouvrirr() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierUtilisateurs));
			while (true) {
				try {
					Utilisateur utilisateur = (Utilisateur) in.readObject();
					listeUtilisateurs.add(utilisateur);
				} catch (EOFException e) {
					break; // Fin de fichier atteinte
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fermer() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichierUtilisateurs));
			for (Utilisateur utilisateur : listeUtilisateurs) {
				out.writeObject(utilisateur);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



public void ouvrir(){//l'utlisateur s'authentifie ou bien se connecte
boolean found=false;
ObjectInputStream in;
ObjectOutputStream out;
System.out.println("avez vous déjà un compte? oui:1 / non:0");
Scanner scanner = new Scanner(System.in);
int opt= scanner.nextInt();
	while (opt<0||opt>1){
	    System.out.println("Option non disponible, veuillez réessayer");  
	    opt= scanner.nextInt();
	}
	//scanner.close();
switch(opt){
	//s'authentifier (on crée un compte temporaire pour le comparer avec les comptes des utilisateurs existatnts)
	case 1: 
	//c=Compte.creerCompte() ;
	// String input1, input2;
	// System.out.println("Entrez votre nom : ");
	// input1 = scanner.nextLine();
    // scanner.nextLine();
	// System.out.println("Entrez votre mot de passe : ");
	// input2 = scanner.nextLine();
    //Compte c= new Compte(input1,input2);
	 Compte c=new Compte("a", "a", "a", "a");

	 //parcourir le fichier des utilisateurs 
	 
	 try {
		 out = new ObjectOutputStream(
				 new BufferedOutputStream(
						 new FileOutputStream(
								 new File("Users.dat"))));
		 //écriture les objets dans un fichier
		 Utilisateur user = new Utilisateur(20);
		 user.creerCompte("a", "a", "a", "a");
		 out.writeObject(user);
		
		 
		 //Fermer le flux
		 out.close();  
	   
		 //Récupération des objets stockés
		 in = new ObjectInputStream(
				 new BufferedInputStream(
						 new FileInputStream(
								 new File("Users.dat"))));
		 
		 try {
			//Affichage de l'utilisateur s'il existe
			 
			 Utilisateur u;
			
			 while ((u = (Utilisateur)in.readObject()) != null || !found) {
				
				 if (u.getCompte().compareTo(c)==0) {
					 System.out.println("compte trouvé");
					 u.afficher();
					 found=true;
					 break;
				 }
			 }
			 
		 } catch (ClassNotFoundException e) {
			 e.printStackTrace();
		 } catch (EOFException e) {
			 // Fin du fichier atteinte
		 }
		 if(!found){
			System.out.print("compte non existant");
		 }
		 in.close();
		 
	 } catch (FileNotFoundException e) {
		 e.printStackTrace();
	 } catch (IOException e) {
		 e.printStackTrace();
	 }
	 break;
	 case 0://création d'un nouvel utilisateur et l'insérer dans le fichier
	 //Compte co=Compte.creerCompte();
	 Utilisateur user1=new Utilisateur(20);
	 user1.creerCompte("a", "a", "a", "a");
	  Utilisateur user2=new Utilisateur(20);
	  user2.creerCompte("a", "a", "a", "a");
	  Utilisateur user3=new Utilisateur(20);// a revoire
	  user3.creerCompte("a", "a", "a", "a");

//user.setCompte(co);
	 //ajout de l'utilisateur dans le fichier
	 try {
		 out = new ObjectOutputStream(
				 new BufferedOutputStream(
						 new FileOutputStream(
								 new File("Users.dat"), true))); // mode append
		 // mode append m3netha insertion men la fin du fichier
		 out.writeObject(user1);
		//   out.writeObject(user2);
		//   out.writeObject(user3);
		 // Fermer le flux
		 out.close();

		 // Récupération des Utilisateurs
		  in = new ObjectInputStream(
				 new BufferedInputStream(
						 new FileInputStream(
								 new File("Users.dat"))));
		 try {
			 Utilisateur u;
			 while ((u = (Utilisateur)in.readObject()) != null) {
				 u.afficher();
			 }
		 } catch (EOFException e) {
			 System.out.println("Fin de lecture");
		 } catch (ClassNotFoundException e) {
			 e.printStackTrace();
		 }
		 in.close();
	 } catch (FileNotFoundException e) {
		 e.printStackTrace();
	 } catch (IOException e) {
		 e.printStackTrace();
	 }
 }

  
}

	public Utilisateur rechercherUtilisateur(String userName, String passWord) {
		if(listeUtilisateurs == null) {
			return null;
		}
	    for (Utilisateur user : this.listeUtilisateurs) {
	        user.afficher();
	        if (user.rechercherUtilisateur(userName, passWord)) {
	            return user;
	        }
	    }
	    return null;
    }
	
	public Utilisateur rechercherUtilisateur(String userName) {
		if(listeUtilisateurs == null) {
			return null;
		}
	    for (Utilisateur user : this.listeUtilisateurs) {
	    	
	        if (user.rechercherUtilisateur(userName)) {
	            return user;
	        }
	    }
	    return null;
    }

	public HashSet<Utilisateur> getListeUtilisateurs() {
		return listeUtilisateurs;
	}

	public void setListeUtilisateurs(HashSet<Utilisateur> listeUtilisateurs) {
		this.listeUtilisateurs = listeUtilisateurs;
	}
	
}



