package br.edu.ufabc.poo;

import br.edu.ufabc.poo.model.*;
import br.edu.ufabc.poo.players.*;

public class GameController {
	
	private boolean isMultiplayer;
	private Player[] players;
	
	private int mapSize;
	private int[] shipSizes;

	public GameController() {
		players = new Player[2];
	}

	public static void main(String[] args) {
		
		(new GameController()).start();

	}

	private void start() {
		// TODO: Solicitar aos jogadores que configurem o jogo
		
		setup();
		play();
	}

	private void setup() {
		
		Ship[] player0Ships = new Ship[shipSizes.length];
		Ship[] player1Ships = new Ship[shipSizes.length];
		for (int i = 0; i < shipSizes.length; i++) {
			player0Ships[i] = new Ship(shipSizes[i], 2*i);
			player1Ships[i] = new Ship(shipSizes[i], 2*i + 1);
		}
		
		players[0] = new HumanPlayer(player0Ships, player1Ships, mapSize);
		if(isMultiplayer)
			players[1] = new HumanPlayer(player1Ships, player0Ships, mapSize);
		else
			players[1] = new IaPlayer(player1Ships, player0Ships, mapSize);
		
		
	}
	
	private void changeTurn(int p) {
		
		int p1 = p, p2 = p==1 ? 0 : 1;
		
		players[p2].endTurn();
		players[p1].startTurn();
	}
	
	private void play() {
		
		changeTurn(0);
		players[0].placeShips();
		changeTurn(1);
		players[1].placeShips();
		
		int p1 = 0, p2 = 1;
		while(!players[p2].isWinner()) {
			
			changeTurn(p1);
			
			Shot shot = players[p1].shoot();
			players[p2].takeShot(shot);
			players[p1].shotResults(shot);
			
			p1 = p2;
			p2 = p1==1 ? 0 : 1;
		}
		
		gameOver(p2);
		
	}

	private void gameOver(int p2) {
		
		// TODO: Player p2 ganhou. Perguntar se quer recomeçar.
		
	}
	
	

}
