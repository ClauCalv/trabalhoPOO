package br.edu.ufabc.poo.players;

import br.edu.ufabc.poo.model.*;

public class HumanPlayer extends Player {

	public HumanPlayer(Ship[] myShips, Ship[] enemyShips, int mapSize) {
		super(myShips, enemyShips, mapSize);
		// TODO
	}

	public void placeShips() {
		// TODO Solicitar ao jogador que posicione seus navios
	}

	public Shot shoot() {
		// TODO Solicitar ao jogador que dispare
		return null;
	}
	
	public void takeShot(Shot shot) {
		super.takeShot(shot);
		// TODO: Exibir ao jogador que recebeu um tiro
	}

	public void startTurn() {
		
		// TODO: Carregar o mapa do jogador na tela e dar os informes da rodada passada
		
	}

	public void endTurn() {
		
		// TODO: Limpar a tela e deslogar
		
	}

}
