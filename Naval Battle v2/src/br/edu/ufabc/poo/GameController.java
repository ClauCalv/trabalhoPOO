package br.edu.ufabc.poo;

import br.edu.ufabc.poo.model.*;
import br.edu.ufabc.poo.players.*;
import br.edu.ufabc.poo.user.UserController;

public final class GameController {
	
	private static final GameController INSTANCE = new GameController(); 
	
	private boolean isMultiplayer;
	private Player[] players = new Player[2];
	
	private int mapSize;
	private int[] shipSizes;
	
	public UserController user;

	private GameController() {
	}
	
	public static GameController getInstance() {
		return INSTANCE;
		
	}

	public static void main(String[] args) {
		/** Eu acabei de inserir um Design Pattern: Singleton, vale ponto extra na nota
		 * https://en.wikipedia.org/wiki/Singleton_pattern
		 * 
		 * Singleton envolve garantir que s� haja a qualquer momento uma �nica inst�ncia
		 * por isso esta classe � final, o construtor � private, e tem uma constante est�tica
		 * iniciada com uma �nica inst�ncia que ser� acessada */
		GameController.getInstance().start();

	}

	public void start() {

		user = new UserController();
		isMultiplayer = user.askMultiplayer();
		
		mapSize = user.askMapSize();
		shipSizes = user.askShipSizes(mapSize);
		
		setup();
		play();
	}

	private void setup() {
		
		/** Aqui o c�digo instancia os navios a serem usados, repare que com IDs diferentes */
		
		Ship[] player0Ships = new Ship[shipSizes.length];
		Ship[] player1Ships = new Ship[shipSizes.length];
		for (int i = 0; i < shipSizes.length; i++) {
			player0Ships[i] = new Ship(shipSizes[i], 2*i);
			player1Ships[i] = new Ship(shipSizes[i], 2*i + 1);
		}
		
		/** Repare que players � um vetor de Player, e HumanPlayer e IAPlayer herdam desta*/		
		players[0] = new HumanPlayer(player0Ships, player1Ships, mapSize, user);
		if(isMultiplayer)
			players[1] = new HumanPlayer(player1Ships, player0Ships, mapSize, user);
		else
			players[1] = new IaPlayer(player1Ships, player0Ships, mapSize);
		
		
	}
	
	/* Controla qual oponente estar� jogando desta vez */
	private void changeTurn(int p) {		
		
		int p1 = p, p2 = p==1 ? 0 : 1; /* p2 � oposto ao p1 */
		
		players[p2].endTurn();
		players[p1].startTurn();
	}
	
	private void play() {
		
		/** Cada jogador posiciona seus navios pro jogo iniciar. 
		 * 
		 * Repare que o navio de cada jogador n�o tem mais rela��o com os navios inimigos outro */		
		changeTurn(0);
		players[0].placeShips();
		changeTurn(1);
		players[1].placeShips();
		
		/** Alterna todo fim de loop quem � o 1 e quem � o 0, assim poupa escrita 
		 * 
		 *  Se o jogador do turno passado n�o ganhou, o jogador desse turno atira,
		 *  depois o oponente avalia o tiro, e por fim o jogador deste turno o computa */
		int p1 = 0, p2 = 1;
		while(!players[p2].isWinner()) {
			
			changeTurn(p1);
			
			/** Repare que Shot � mantido de um m�todo pro outro, usaremos isso */
			Shot shot = players[p1].shoot();
			players[p2].takeShot(shot);
			players[p1].shotResults(shot);
			
			p1 = p2;
			p2 = p1==1 ? 0 : 1;
		}
		
		gameOver(p2);
		
	}

	private void gameOver(int p2) {
		
		boolean restart = user.askRestart(players[p2]);
		if(restart)
			start();
		else
			System.exit(0);
	}
	
	public boolean isMultiplayer() {
		return isMultiplayer;
	}

}
