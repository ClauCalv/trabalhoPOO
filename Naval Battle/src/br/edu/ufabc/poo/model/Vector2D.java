package br.edu.ufabc.poo.model;

public class Vector2D {

	public final int x, y;
	
	/* Simples encapsulador para uma coordenada */
	/** Eu irei usar este objeto para armazenar coordenadas */
	public Vector2D(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	/* Compara duas coordenadas (curiosidade: equals sobrescreve de Object) */
	public boolean equals(Vector2D v) {
		
		return x == v.x && y == v.y;
	}	
	
	/* Verifica se a coordenada está no retângulo de jogo. MIN incluso, MAX excluso */
	public boolean isInBounds(int minX, int maxX, int minY, int maxY) {
		
		return x >= minX && x < maxX && y >= minY && y < maxY;	
	}
}
