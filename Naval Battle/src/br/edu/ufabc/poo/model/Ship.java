package br.edu.ufabc.poo.model;

import java.util.HashMap;
import java.util.Set;

public class Ship {
	
	public final int size, id;
	
	/* Associa uma coordenada a seu estado de ter sido atingida*/
	/** Ver os comentários em Player */
	private HashMap<Vector2D, Boolean> positions;
	
	/** Um navio só tem tamanho e identificador. O identificador vai ser usado somente na 
	 * hora de saber qual dos navios inimigos que eu guardo no HashMap é aquele que foi
	 * afundado, na classe "Player" */
	public Ship(int size, int id) {		
		this.size = size;
		this.id = id;
	}
	
	/* Valida e associa o navio a sua posição no mapa */
	public boolean placeShip(Vector2D[] positions, int mapSize) {
		
		if(positions.length != size)
			return false;
		
		this.positions = new HashMap<Vector2D, Boolean>(size); /** Cria um novo */
		
		for (Vector2D pos : positions) {
			if(!pos.isInBounds(0,0,mapSize,mapSize)) 
				return false;
			this.positions.put(pos, false);
		}
			
		return true;
	}
	
	/** Um navio só é afundado se todas as suas coordenadas já foram afundadas */
	public boolean isSunk() {
		
		for (Vector2D pos : positions.keySet())
			if(!positions.get(pos)) 
				return false;
		
		return true;
	}
	
	
	/* configura 'hit = true' caso atingido e 'ship = this' caso afundado */
	/** Pra avaliar se tomou um tiro, compare a coordenada do tiro a cada uma das
	 * coordenadas que você guarda. Se atingiu, marque a coordenada como afundada
	 * e confira se o navio afundou, para avisar ao tiro que ele afundou um navio */
	public void takeShot(Shot shot) {
		
		for (Vector2D pos : positions.keySet()) {
			if(shot.target.equals(pos)) {
				/** Substitui o valor associado a "pos" no HashMap para "true" */
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
	
	/** Retorna as coordenadas no conjunto das chaves do HashMap*/
	public Set<Vector2D> getPositions() {
		return positions.keySet();
	}

}
