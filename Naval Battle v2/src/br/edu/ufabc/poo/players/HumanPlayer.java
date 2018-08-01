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

	//Solicita ao usu�rio que insira seus navios
	public void placeShips() {
		
		user.placeShips(battleMap, myShips);
	}

	//Solicita ao usu�rio que atire
	public Shot shoot() {
		
		return user.shoot(battleMap);
	}
	
	//Informa ao usu�rio o sucesso de seus disparos
	public void shotResults(Shot shot) {
		super.shotResults(shot);
		user.shotResults(shot);
	}
	
	//Armazena o �ltimo disparo recebido para informar ao usu�rio que foi alvejado no turno do oponente
	public void takeShot(Shot shot) {
		super.takeShot(shot);
		lastEnemyShot = shot;
	}

	//Informa ao controlador para trocar o usu�rio que est� jogando. 
	//Tamb�m aproveita para informar sobre o turno passado
	public void startTurn() {
		
		user.startTurn(this, GameController.getInstance().isMultiplayer());
		
		if(lastEnemyShot!=null)
			user.warnLastShot(lastEnemyShot);
		
		lastEnemyShot = null;
		
	}

	public void endTurn() {}

}
