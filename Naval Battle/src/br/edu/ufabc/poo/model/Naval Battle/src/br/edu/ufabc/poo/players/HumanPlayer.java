package br.edu.ufabc.poo.players;

import br.edu.ufabc.poo.model.*;

public class HumanPlayer extends Player {

	public HumanPlayer(Ship[] myShips, Ship[] enemyShips, int mapSize) {
		super(myShips, enemyShips, mapSize);
		// TODO Inicialize aqui o que for precisar usar. Fique a vontade.
	}

	public void placeShips() {
		// TODO Solicitar ao jogador que posicione seus navios
		/** Dica: tentem fazer igual na IA e usar o battleMap pra posicionar seus navios, a� n�o
		 * tem que ficar criando vari�vel nova. S� lembrem de limpar depois, "battleMap.clearMap()",
		 * porque esse mapa vai ser usado ainda pras batalhas */
	}

	public Shot shoot() {
		// TODO Solicitar ao jogador que dispare
		return null;
	}
	
	public void takeShot(Shot shot) {
		super.takeShot(shot);
		// TODO: Exibir ao jogador que recebeu um tiro (OPCIONAL)
		/** Se quiserem, guardem uma vari�vel "protected Shot lastShot" pra guardar o �ltimo
		 * tiro recebido, e no m�todo "startTurn()", coloque uma mensagem do tipo "Seu oponente
		 * atirou em (8,4) e errou. � o seu turno agora!". Seria legal pois teoricamente um
		 * jogador n�o est� vendo o outro jogar, e n�o sabe portando se est� ganhando ou perdendo */
	}

	public void startTurn() {
		
		// TODO: Carregar o mapa do jogador na tela e dar os informes da rodada passada.
		// Seria bom solicitar uma senha a cada jogador para exibir suas informa��es, ou 
		// pelo menos um alerta de mudan�a de jogador.
		
	}

	public void endTurn() {
		
		// TODO: Limpar a tela e deslogar
		
	}

}
