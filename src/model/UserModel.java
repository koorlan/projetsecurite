package model;

import manager.UserManager;

public class UserModel {
	private UserManager manager = null;
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
		this.manager.update("nom");
	}

	public String getAffectation() {
		return affectation;
	}

	public void setAffectation(String affectation) {
		this.affectation = affectation;
		this.manager.update("affectation");
	}

	public String getGroupe() {
		return groupe;
	}

	public void setGroupe(String groupe) {
		this.groupe = groupe;
		this.manager.update("groupe");
	}


	private String nom = new String();
	private String affectation = new String();
	private String groupe = new String();
	
	
	public void setManager(UserManager manager){
		this.manager = manager;
		return;
	}
	
	
	
}
