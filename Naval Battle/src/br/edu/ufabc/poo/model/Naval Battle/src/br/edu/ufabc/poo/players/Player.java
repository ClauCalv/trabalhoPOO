package br.edu.ufabc.poo.players;

import br.edu.ufabc.poo.model.*;

public abstract class Player {
	
	/*** Eu fiz as modifica��es anunciadas no item 3) do coment�rio da �ltima vers�o.
	 * Agora, battleMap n�o � mais um int[][], mas uma classe, e n�o temos mais um
	 * "protected static final int UNKNOWN = 0, MISS = 1, HIT = 2, SUNK = 3", mas em
	 * vez disso temos um ENUM. Um ENUM � um tipo de classe na qual todas as inst�ncias
	 * poss�veis j� foram criadas e s�o est�ticas, sendo efetivamente uma lista pronta
	 * de objetos. 
	 * 
	 * Reparem na nova sintaxe usada ao mexer nos Status, que s�o Enums. */
	
	protected final Ship[] myShips;
	/** EDIT: Eu tirei todos os HashMaps do c�digo substituindo por atributos boolean nas classes. */
	protected final Ship[] enemyShips;
	
	/** Eu fiz de battleMap que era um int[][] uma classe separada */
	protected final BattleMap battleMap;
	
	/* Cria para si uma c�pia cega dos navios inimigos, para ser rastreada */
	public Player(Ship[] myShips, Ship[] enemyShips, int mapSize){
		
		battleMap = new BattleMap(mapSize);
		battleMap.clearMap();
		
		this.myShips = myShips;
		
		this.enemyShips = new Ship[enemyShips.length];
		for (int i = 0; i < enemyShips.length; i++)
			this.enemyShips[i] = enemyShips[i].blindClone();
			/* insere falso pois o navio n�o foi afundado */
			/** Os clones cegos servem pra mostrar pra um oponente um navio seu sem mostrar o
			 * interior. Esses clones s�o outro objeto, e portanto, os originais s�o
			 * inacess�veis atrav�s dele. */
	}
	
	/** Esses m�todos s�o abstratos pois v�o ser implementados diferente pelo Human e IA */
	public abstract void placeShips();
	public abstract Shot shoot();
	public abstract void startTurn();
	public abstract void endTurn();
	
	/* Atualiza a matriz de batalha com os resultados do tiro */
	public void shotResults(Shot shot) {
		
		/** Quatro valores poss�veis para o estado daquele ponto s�o armazenados na matriz:
		 *  acerto, afundado, erro e desconhecido. No come�o, tudo � desconhecido, conforme
		 *  se joga, se preenche com acerto ou erro para saber onde atirar depois
		 *  
		 *  Se o tiro afunda algum navio, todas as coordenadas daquele navio s�o marcadas
		 *  como afundadas, pois sabe-se que aqueles pontos, apesar de acertos, n�o pertencem
		 *  a outro navio */
		
		if(!shot.hit)
			battleMap.setStatusAtPoint(shot.target, Status.MISS);
		else if(shot.sunkShip == null)
			battleMap.setStatusAtPoint(shot.target, Status.HIT);
		else {
			/** Repare que aqui o shot.sunkShip � um dos navios que seu oponente armazena, e
			 * portanto possui coordenadas e informa��es. Agora que voc� o afundou, tem acesso
			 * � elas. Ent�o voc� vai usar elas pra duas coisas.
			 * 
			 * Primeiro, compara qual das c�pias que voc� mant�m corresponde �quele navio, e
			 * marca ela como afundada, para ter controle do que voc� j� afundou.*/
			for (Ship ship : enemyShips)				
				if(shot.sunkShip.id == ship.id) 
					ship.setSunk(true);
			
			for (Vector2D pos : shot.sunkShip.getPositions())
				battleMap.setStatusAtPoint(pos, Status.SUNK);
		}
	}
	
	/* Confere se algum de seus navios foi atingido pelo tiro */
	/** Para saber se foi atingido, basta perguntar a cada um de seus navios se eles o foram */
	public void takeShot(Shot shot) {
		for (Ship ship : myShips) {
			ship.takeShot(shot);
			if(shot.hit) return;
		}
	}
	
	/* Confere se o oponente est� sem navios */
	/** Quem armazena os navios afundados � o afundador, portanto um jogador n�o tem como
	 * saber que ele perdeu, mas sim seu oponente sabe que ganhou. */
	public boolean isWinner() {
		
		for (Ship ship : enemyShips)
			if(!ship.isSunk()) 
				return false;
		
		return true;
	}

}
