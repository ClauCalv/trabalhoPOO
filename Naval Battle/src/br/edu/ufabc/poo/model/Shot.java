package br.edu.ufabc.poo.model;

public class Shot {
	
	public final Vector2D target;
	public boolean hit;
	public Ship sunkShip;

	public Shot(Vector2D target) {
		this.target = target;
		hit = false;
		sunkShip = null;
	}
}
