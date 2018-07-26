package br.edu.ufabc.poo.model;

public class Shot {
	
	public final Vector2D target;
	public boolean hit;
	public Ship sunkShip;

	/* Simples encapsulador para um tiro */
	/** A intenção é que em TakeShot, esse objeto seja configurado, e em ShotResults, seja lido
	 * 
	 * Tudo é público, e só a coordenada alvo é final, não há necessidade de encapsular */
	public Shot(Vector2D target) {
		this.target = target;
		hit = false;
		sunkShip = null;
	}
}
