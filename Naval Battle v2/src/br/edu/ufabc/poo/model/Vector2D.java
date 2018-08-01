package br.edu.ufabc.poo.model;

public class Vector2D {

	public final int x, y;
	private boolean isHit = false;
	
	/* Simples encapsulador para uma coordenada */
	/** Eu irei usar este objeto para armazenar coordenadas */
	public Vector2D(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	/* Compara duas coordenadas (sobrescreve Object)*/
	public boolean equals(Vector2D v) {
		
		return x == v.x && y == v.y;
	}	
	
	/* Verifica se a coordenada está no retângulo de jogo. MIN incluso, MAX excluso */
	public boolean isInBounds(int minX, int minY, int maxX, int maxY) {
		
		return x >= minX && x < maxX && y >= minY && y < maxY;	
	}
	
	public boolean isHit() {
		return isHit;
	}
	
	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}
	
	//Cria uma nova coordenada, desvinculada da anterior
	public Vector2D clone() {
		return new Vector2D(x, y);
	}
}
