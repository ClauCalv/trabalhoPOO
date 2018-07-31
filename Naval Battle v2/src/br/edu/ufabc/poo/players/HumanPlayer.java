package br.edu.ufabc.poo.players;

import br.edu.ufabc.poo.GameController;
import br.edu.ufabc.poo.model.*;
import br.edu.ufabc.poo.user.UserController;

public class HumanPlayer extends Player {

	private Shot lastEnemyShot = null;
	private UserController user;
	
	public HumanPlayer(Ship[] myShips, Ship[] enemyShips, int mapSize, UserController user) {
		super(myShips, enemyShips, mapSize);
		this.user = user;
		
	}

	public void placeShips() {
		
		user.placeShips(battleMap, myShips);
	}

	public Shot shoot() {
		
		return user.shoot(battleMap);
		
	}
	
	public void shotResults(Shot shot) {
		super.shotResults(shot);
		user.shotResults(shot);
	}
	
	public void takeShot(Shot shot) {
		super.takeShot(shot);
		lastEnemyShot = shot;
	}

	public void startTurn() {
		
		user.startTurn(this, GameController.getInstance().isMultiplayer());
		
		if(lastEnemyShot!=null)
			user.warnLastShot(lastEnemyShot);
		
		lastEnemyShot = null;
		
	}

	public void endTurn() {
		
		// TODO: Limpar a tela e deslogar
		
	}

}
