package br.edu.ufabc.poo.players;

import br.edu.ufabc.poo.model.*;

public class IaPlayer extends Player {

	public IaPlayer(Ship[] myShips, Ship[] enemyShips, int mapSize) {
		super(myShips, enemyShips, mapSize);
	}

	public void placeShips() {
		// TODO: Lógica da IA para posicionar os navios: Aleatória?
	}

	public Shot shoot() {
		// TODO: Lógica da IA para disparar.
		return null;
	}

	public void startTurn() {
		
		// TODO: Não fazer nada? Exibir algo enquanto a IA joga?
		
	}

	public void endTurn() {
		
		// TODO: Fechar seja qual for a tela do turno da IA
		
	}

}
