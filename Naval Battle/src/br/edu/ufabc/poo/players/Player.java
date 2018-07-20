package br.edu.ufabc.poo.players;

import java.util.HashMap;

import br.edu.ufabc.poo.model.*;

public abstract class Player {
	
	protected static final int UNKNOWN = 0, MISS = 1, HIT = 2, SUNK = 3;
	
	protected final Ship[] myShips;
	protected final HashMap<Ship, Boolean> enemyShips;
	
	protected final int[][] battleMap;
	
	/* Cria para si uma cópia cega dos navios inimigos */
	public Player(Ship[] myShips, Ship[] enemyShips, int mapSize){
		
		battleMap = new int[mapSize][mapSize];
		for (int i = 0; i < mapSize; i++)
			for (int j = 0; j < mapSize; j++)
				battleMap[i][j] = UNKNOWN;
		
		this.myShips = myShips;
		
		this.enemyShips = new HashMap<Ship, Boolean>(enemyShips.length);
		for (Ship ship : enemyShips)
			this.enemyShips.put(ship.blindClone(), false);
	}
	
	public abstract void placeShips();
	public abstract Shot shoot();
	public abstract void startTurn();
	public abstract void endTurn();
	
	/* Atualiza a matriz de busca*/
	public void shotResults(Shot shot) {
		
		if(!shot.hit)
			battleMap[shot.target.x][shot.target.y] = MISS;
		else if(shot.sunkShip == null)
			battleMap[shot.target.x][shot.target.y] = HIT;
		else {
			for (Ship ship : enemyShips.keySet()) {
				if(shot.sunkShip.id == ship.id) enemyShips.replace(ship, true);
			}
			for (Vector2D pos : shot.sunkShip.getPositions()) {
				battleMap[pos.x][pos.y] = SUNK;
			}
		}
	}
	
	/* Confere se cada um dos navios foi atingido*/
	public void takeShot(Shot shot) {
		for (Ship ship : myShips) {
			ship.takeShot(shot);
			if(shot.hit) return;
		}
	}
	
	public boolean isWinner() {
		
		for (Ship ship : enemyShips.keySet())
			if(!enemyShips.get(ship)) 
				return false;
		
		return true;
	}

}
