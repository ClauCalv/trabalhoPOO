package br.edu.ufabc.poo.model;

import java.util.HashMap;
import java.util.Set;

public class Ship {
	
	public final int size, id;
	
	/* Associa uma coordenada a seu estado de ter sido atingida*/
	private HashMap<Vector2D, Boolean> positions;
	
	public Ship(int size, int id) {		
		this.size = size;
		this.id = id;
	}
	
	/* Valida e associa o navio a sua posição no mapa */
	public boolean placeShip(Vector2D[] positions, int mapSize) {
		
		if(positions.length != size)
			return false;
		
		this.positions = new HashMap<Vector2D, Boolean>(size);
		
		for (Vector2D pos : positions) {
			if(!pos.isInBounds(0,0,mapSize,mapSize)) 
				return false;
			this.positions.put(pos, false);
		}
			
		return true;
	}
	
	public boolean isSunk() {
		
		for (Vector2D pos : positions.keySet())
			if(!positions.get(pos)) 
				return false;
		
		return true;
	}
	
	/* configura 'hit = true' caso atingido e 'ship = this' caso afundado */
	public void takeShot(Shot shot) {
		
		for (Vector2D pos : positions.keySet()) {
			if(shot.target.equals(pos)) {
				positions.replace(pos, true);
				shot.hit = true;
				break;
			}
		}
		
		if(shot.hit) shot.sunkShip = isSunk() ? this : shot.sunkShip;
		
	}
	
	/* retorna uma cópia do navio, mas sem informações de sua posição*/
	public Ship blindClone() {
		return new Ship(size, id);
	}
	
	public Set<Vector2D> getPositions() {
		return positions.keySet();
	}

}
