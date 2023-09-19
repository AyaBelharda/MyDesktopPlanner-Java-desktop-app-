package myDpPlanner;

import java.util.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Journee implements Comparable<Journee>, Cloneable, Serializable{
	static int nombreJours = 0;
	private TreeSet<Creneau> listeCreneaux;
	private LocalDate date;
	private EtatAvancement etatAvancement;//est ce que cest dans journee oubien dans tache?
	//c dans tache, psq lzm à chaque jour ndirou le parcours des taches w l'utilisateur ybedel leur état
	//soit dans journee li fiha les taches planifiees 
	//wella nzidou meme ta3 les taches non planifiees ta3 l'utilisateur
	//avant dernier paragraphe ta3 page 2
	//private int nbTachesMinimales;//avant dernier paragraphe ta3 page 2

	
	public Journee(LocalDate date) {
		this.date = date;
		nombreJours++;
		listeCreneaux  = new TreeSet<Creneau>();
	}
	
	public boolean ajourParDefault() {
		if(listeCreneaux != null) {
			return false;
		}
		//un seul creneau par default
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		String str1 = "00:00";
		String str2 = "23:59";
		LocalTime heurDeb = LocalTime.parse(str1, format);
		LocalTime heurFin = LocalTime.parse(str2, format);
		Creneau cr = new Creneau(heurDeb, heurFin, EtatCreneau.Libre, null);
		
		listeCreneaux.add(cr);
		return true;
	}
	
	public void afficher() {
		System.out.println("Date de la journee : " + this.getDate().toString());
		System.out.println("Etat davancement : " + this.etatAvancement);
		for(Creneau cr : listeCreneaux) {
			cr.afficher();
		}
		System.out.println("=======");
	}
	
	public void fixerCreneauxLibres(HashSet<Creneau> creneauxLibres) {
		listeCreneaux.clear();
		for(Creneau cr : creneauxLibres) {
			listeCreneaux.add(cr);
		}
	}
	
	private int peutPlanifierTacheCreneau(Tache tache, Creneau creneau) {
		if(tache.delaiDepasse(this.date)) {
			return 1;//delai dépassé
		}
		
		if(creneau.getEtatCreneau() != EtatCreneau.Libre) {
			return 3;//creneau pas libre
		}
		
		if(tache instanceof TacheDecomposable) {
			if(((TacheDecomposable) tache).getDureeTacheDecomposable() > creneau.calculerDuration()){
				return 2;//probleme de durée
			}
			return 0;//oui on peut plannifier
		}
		
		if(tache.getDuree() > creneau.calculerDuration()){
			return 2;//probleme de durée
		}
		return 0;//oui on peut plannifier
	}
	
	public Tache plannifierTacheManuellement(Tache tacheEnt, Creneau creneau, long duree_min) {
		
			//si la tache est decomposable
		if(creneau.getEtatCreneau() == EtatCreneau.Libre) {
			if(tacheEnt instanceof TacheDecomposable) {
				TacheDecomposable tache = (TacheDecomposable) tacheEnt;
				if(creneau.estDecomposable(duree_min, tache.getDureeTacheDecomposable())) {

					Creneau reste = creneau.decomposer(tache, duree_min);
					listeCreneaux.add(reste);
			
					return null;
				} else {
					
					if(peutPlanifierTacheCreneau(tache, creneau) == 0) {
						creneau.planifierTache(tache);
						return null;
					} else {

							TacheDecomposable temp = tache, tacheDeco;
							int nombreSousTaches = temp.getNumeroTache();
							long duree = temp.getDuree() - temp.getDureeTacheDecomposable();
							do
							{
								tacheDeco = new TacheDecomposable(temp.getNom(), temp.getDuree(), 
										temp.getPriorite(), temp.getDateLimite(), temp.getCategorie());
								tacheDeco.setEtatSousTache(EtatAvancement.notRealised);
								
								if(tache.getDuree() - duree < creneau.calculerDuration()) {
									tacheDeco.setDureeTacheDecomposable(tache.getDuree() - duree);
									duree += tache.getDuree() - duree;
								} else {
									tacheDeco.setDureeTacheDecomposable(creneau.calculerDuration());
									duree += creneau.calculerDuration();
								}
								tacheDeco.setNomSousTache(tacheDeco.getNom() + nombreSousTaches);
								tacheDeco.setNumeroTache(nombreSousTaches);
								if(creneau.estDecomposable(duree_min, tacheDeco.getDureeTacheDecomposable())){
									Creneau reste = creneau.decomposer(tacheDeco, duree_min);
									listeCreneaux.add(reste);
									return null;
								}
								creneau.planifierTache(tacheDeco);
								nombreSousTaches++;
								creneau = listeCreneaux.higher(creneau);
							}
							while(creneau != null && duree < tache.getDuree());
							if( duree < tache.getDuree() ) {
								tacheDeco = new TacheDecomposable(temp.getNom(), temp.getDuree(), 
										temp.getPriorite(), temp.getDateLimite(), temp.getCategorie()
										);
								tacheDeco.setEtatSousTache(EtatAvancement.notRealised);
								tacheDeco.setDureeTacheDecomposable(tache.getDuree() - duree);
								tacheDeco.setNomSousTache(tacheDeco.getNom() + nombreSousTaches);
								tacheDeco.setNumeroTache(nombreSousTaches);
								tacheDeco.setEtatAvancement(EtatAvancement.Unscheduled);
								return tacheDeco;
							} else {
								//on a terminé, ils sont inserée dans la meme journée
								return null;
							}	
					}
					
				}
			}else {
				TacheSimple tache = (TacheSimple) tacheEnt;
				if(creneau.estDecomposable(duree_min, tache.getDuree())) {

					Creneau reste = creneau.decomposer(tache, duree_min);
					listeCreneaux.add(reste);

					return null;
				} else {
					
					if(peutPlanifierTacheCreneau(tache, creneau) == 0) {
						
						creneau.planifierTache(tache);
						return null;
					} else {			
						tache.setEtatAvancement(EtatAvancement.Unscheduled);
						return tache;
					}
					
				}
			}

		}
		tacheEnt.setEtatAvancement(EtatAvancement.Unscheduled);
		return tacheEnt;
	}
	
	public int plannifierTacheAutomatiquement(Tache tache, long duree_min) {
		if(tache.delaiDepasse(this.date)) {
			//on test si elle n'a pas depassé la date limite
			return 1;
		}
		
		boolean peutPlannifier = false;
		for(Creneau cr : listeCreneaux) {
			peutPlannifier = (peutPlanifierTacheCreneau(tache, cr) == 0) || peutPlannifier;
			if(cr.getEtatCreneau() == EtatCreneau.Libre && peutPlannifier) {
				if(cr.estDecomposable(tache.getDuree(), duree_min)) {
					Creneau crResult = cr.decomposer(tache, duree_min);
					listeCreneaux.add(crResult);
				} else {
					cr.planifierTache(tache);
				}
				return 0;//avec succes
			} 
		}
		tache.setEtatAvancement(EtatAvancement.Unscheduled);
		if(!peutPlannifier && tache instanceof TacheDecomposable) {
			return 3;//ici on proppose une décomposition pour la tache mais pas dans cette fonciton, dans la fonciton mere
		}
		return 2;//il y a pas des creneaux libres

	}
	
	public TreeSet<Creneau> proposer(TacheDecomposable tache, long duree_min){
		
		Creneau creneau = null;
		for(Creneau cr : listeCreneaux) {
			if(cr.getEtatCreneau() == EtatCreneau.Libre) {
				creneau = cr;
				break;
			}
		}
		if(creneau == null) {
			return null;//pas de creneaux libres
		}
		//subdiviser la tache sur les creneaux de la journée
		Creneau cr = creneau, tempCr = null;
		TacheDecomposable tacheDeco;
		TreeSet<Creneau> resultList = new TreeSet<Creneau>();
		 do{
			if(cr.getEtatCreneau() == EtatCreneau.Libre) {
				if(cr.estDecomposable(duree_min, tache.getDureeTacheDecomposable())) {
	
					try {
						tempCr = (Creneau) cr.clone();
						
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Creneau reste = cr.decomposer(tache, duree_min);
					listeCreneaux.add(reste);
					resultList.add(tempCr);
					return resultList;
				} 
				
				if(peutPlanifierTacheCreneau(tache, cr) == 0) {
				
					try {
						tempCr = (Creneau) cr.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println((tache == null) + "aaaaaaaaaaaaaaaa");
					cr.planifierTache(tache);
					resultList.add(tempCr);
					return resultList;
				}
				//si on nen peut pas la plannifier dans le creneau cr on la decompose
				tacheDeco = (TacheDecomposable) tache.decomposer(cr.calculerDuration());
				//la tache a plannifier ultirieurement est "tache"
				
				try {
					tempCr = (Creneau) cr.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cr.planifierTache(tacheDeco);
				resultList.add(tempCr);

			}
			cr = listeCreneaux.higher(cr);
		} while(cr != null);
		tache.setEtatAvancement(EtatAvancement.Unscheduled);
		return resultList;// il y a deux cas a distinguer, si la duree de tache decomposable passé en parametre est strictement 
		//positive alorss on n'a pas encore terminé la decomposition
	}
//	public boolean plannifierTacheAutomatiquement(Tache tache, long duree_min) {
//		if(tache.delaiDepasse(this.date)) {
//			//on test si elle n'a pas depassé la date limite
//			return false;
//		}
//		for(Creneau cr : listeCreneaux) {
//			if(cr.getEtatCreneau() == EtatCreneau.Libre && peutPlanifierTacheCreneau(tache, cr)) {
//				if(cr.estDecomposable(tache.getDuree(), duree_min)) {
//					Creneau crResult = cr.decomposer(tache, duree_min);
//					listeCreneaux.add(crResult);
//				} else {
//					cr.planifierTache(tache);
//				}
//				return true;
//			}
//		}
//		tache.setEtatAvancement(EtatAvancement.Unscheduled);
//		return false;
//
//	}

	public int rentabilite(){//compte le nombre de taches completees dans une journee
		SortedSet<Creneau> c=this.listeCreneaux;
		int cpt=0;
		for (Creneau cr:c){
			if(cr.getTache()!=null && cr.getTache().getEtatAvancement().equals(EtatAvancement.Completed)){
				cpt++;
			}
		
		}
		return cpt;
	}
	
	public boolean minDepasse(int min){//retourne true si on a accompli le nombre de taches minimales dans la journee
		return (rentabilite()> min);
	}
	
	public Creneau rechercherTache(Tache tache) {
		for(Creneau cr : listeCreneaux) {
			if(cr.rechercherTache(tache)) {
				return cr;
			}
		}
		return null;
	}
	
	
	public ArrayDeque<Creneau> rechercherTacheDecomposable(String nomTache){
		ArrayDeque<Creneau> listResult = new ArrayDeque<Creneau>();
		for(Creneau cr : listeCreneaux) {
			if(cr.rechercherTache(nomTache)) {
				// il faut c=verifier si la tache est de type decomposable
					listResult.addLast(cr);
			}
			
		}
		return listResult;
	}
	
	public boolean rechercherCreneau(Creneau cr) {
		for(Creneau creneau : listeCreneaux) {
			if(cr == creneau) {
				return true;
			}
		}
		
		return false;
	}
	
	public Creneau rechercherTacheSimple(String nom) {
		for(Creneau cr : listeCreneaux) {
			if(cr.rechercherTache(nom)) {
				return cr;
			}
		}
		return null;
	}
	
	//getters and setters
	public EtatAvancement getEtatAvancement() {
		return etatAvancement;
	}

	public void setEtatAvancement(EtatAvancement etatAvancement) {
		this.etatAvancement = etatAvancement;
	}

	public TreeSet<Creneau> getListeCreneaux() {
		return listeCreneaux;
	}
	
	public int hashCode() {
		return date.hashCode();
	}
	
	public boolean equals(Object o) {
		return this.hashCode() == ((Journee) o).hashCode();
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	@Override
	public int compareTo(Journee j) {

		return this.date.compareTo(j.getDate());
	}

    public Object clone() throws CloneNotSupportedException
    {
        try {
            Journee cloned = (Journee) super.clone();
            TreeSet<Creneau> temp  = new TreeSet<Creneau>();
            for(Creneau cr : listeCreneaux) {
            	temp.add((Creneau) cr.clone());
            }
            cloned.listeCreneaux = temp;
            cloned.date = LocalDate.from(this.date);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    

    
	public void setListeCreneaux(TreeSet treeSet) {
		// TODO Auto-generated method stub
		this.listeCreneaux.clear();
		this.listeCreneaux.addAll(treeSet);
	}

	public boolean ajouterCreneau(Creneau cr) {
		 return listeCreneaux.add(cr);
	}


    public boolean supprimerTache(Tache tache) {
        for (Creneau cr : listeCreneaux) {
            if (cr.getTache() != null && cr.getTache().equals(tache)) {
                cr.setEtatCreneau(EtatCreneau.Libre);// la tache sera écrasée après une autre planification
                return true;
            }
        }
        return false;
    }
    
    public int etatRealisationTache(EtatAvancement motif) {
        int cpt = 0;
        if (listeCreneaux == null) {
            return 0;
        }
        for (Creneau cr : listeCreneaux) {
            if (cr.etatRealisationTache(motif)) {
                cpt++;
            }
        }
        return cpt;
    }

    public long dureeCategorie(Categorie c) {// calcule la durée passée sur des taches de meme catégorie dans une
                                             // journee
        long d = 0;
        if (listeCreneaux == null) {
            return 0;
        }
        for (Creneau cr : listeCreneaux) {
            if (cr.memeCategorie(c)) {
                d++;
            }
        }
        return d;
    }

}
