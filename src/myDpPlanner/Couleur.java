package myDpPlanner;

import java.io.Serializable;

public enum Couleur implements Serializable{
	Rouge("#ee6055"),
	Vert("#7bf1a8"),
	Bleu("#004e64"),
	Violet("#3c1642"),
	Jaune("#ffef9f"),
	Cyan("#90f1ef"),
	Marron("#43291f"),
	Rose("#ffd6e0"),
	Noir("#00000090");
	
	private String color;
	private Couleur(String color) {
		this.color = color;
	}
	
	public String getCouleur() {
		return color;
	}
}
