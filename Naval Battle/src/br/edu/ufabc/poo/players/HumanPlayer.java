package br.edu.ufabc.poo.players;

import br.edu.ufabc.poo.model.*;

public class HumanPlayer extends Player {

	public HumanPlayer(Ship[] myShips, Ship[] enemyShips, int mapSize) {
		super(myShips, enemyShips, mapSize);
		// TODO Inicialize aqui o que for precisar usar. Fique a vontade.
	}

	public void placeShips() {
		// TODO Solicitar ao jogador que posicione seus navios
		/** Dica: tentem fazer igual na IA e usar o mapa do inimigo pra posicionar seus navios,
		 * aí não tem que ficar criando variável nova. Só lembrem de limpar depois, "clearMap()",
		 * porque esse mapa vai ser usado ainda pras batalhas */
	}

	public Shot shoot() {
		// TODO Solicitar ao jogador que dispare
		return null;
	}
	
	public void takeShot(Shot shot) {
		super.takeShot(shot);
		// TODO: Exibir ao jogador que recebeu um tiro (OPCIONAL)
		/** Se quiserem, guardem uma variável "protected Shot lastShot" pra guardar o último
		 * tiro recebido, e no método "startTurn()", coloque uma mensagem do tipo "Seu oponente
		 * atirou em (8,4) e errou. É o seu turno agora!". Seria legal pois teoricamente um
		 * jogador não está vendo o outro jogar, e não sabe portando se está ganhando ou perdendo */
	}

	public void startTurn() {
		
		// TODO: Carregar o mapa do jogador na tela e dar os informes da rodada passada.
		// Seria bom solicitar uma senha a cada jogador para exibir suas informações, ou 
		// pelo menos um alerta de mudança de jogador.
		
	}

	public void endTurn() {
		
		// TODO: Limpar a tela e deslogar
		
	}

}
