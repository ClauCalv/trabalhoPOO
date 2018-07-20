package br.edu.ufabc.poo.model;

public class Vector2D {

	public final int x, y;
	
	public Vector2D(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public boolean equals(Vector2D v) {
		
		return x == v.x && y == v.y;
	}	
	
	/* MIN incluso, MAX excluso */
	public boolean isInBounds(int minX, int maxX, int minY, int maxY) {
		
		return x >= minX && x < maxX && y >= minY && y < maxY;	
	}
}
