package br.edu.ufabc.poo.model;

public class Shot {
	
	public final Vector2D target;
	public boolean hit;
	public Ship sunkShip;

	/* Simples encapsulador para um tiro */
	/** A inten��o � que em TakeShot, esse objeto seja configurado, e em ShotResults, seja lido
	 * 
	 * Tudo � p�blico, e s� a coordenada alvo � final, n�o h� necessidade de proteger */
	public Shot(Vector2D target) {
		this.target = target;
		hit = false;
		sunkShip = null;
	}
}
