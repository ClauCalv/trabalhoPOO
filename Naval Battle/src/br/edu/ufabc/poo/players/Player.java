package br.edu.ufabc.poo.players;

import br.edu.ufabc.poo.model.*;

public abstract class Player {
	
	/*** Eu fiz as modificações anunciadas no item 3) do comentário da última versão.
	 * Agora, battleMap não é mais um int[][], mas uma classe, e não temos mais um
	 * "protected static final int UNKNOWN = 0, MISS = 1, HIT = 2, SUNK = 3", mas em
	 * vez disso temos um ENUM. Um ENUM é um tipo de classe na qual todas as instâncias
	 * possíveis já foram criadas e são estáticas, sendo efetivamente uma lista pronta
	 * de objetos. 
	 * 
	 * Reparem na nova sintaxe usada ao mexer nos Status, que são Enums. */
	
	protected final Ship[] myShips;
	/** EDIT: Eu tirei todos os HashMaps do código substituindo por atributos boolean nas classes. */
	protected final Ship[] enemyShips;
	
	/** Eu fiz de battleMap que era um int[][] uma classe separada */
	protected final BattleMap battleMap;
	
	/* Cria para si uma cópia cega dos navios inimigos, para ser rastreada */
	public Player(Ship[] myShips, Ship[] enemyShips, int mapSize){
		
		battleMap = new BattleMap(mapSize);
		battleMap.clearMap();
		
		this.myShips = myShips;
		
		this.enemyShips = new Ship[enemyShips.length];
		for (int i = 0; i < enemyShips.length; i++)
			this.enemyShips[i] = enemyShips[i].blindClone();
			/* insere falso pois o navio não foi afundado */
			/** Os clones cegos servem pra mostrar pra um oponente um navio seu sem mostrar o
			 * interior. Esses clones são outro objeto, e portanto, os originais são
			 * inacessíveis através dele. */
	}
	
	/** Esses métodos são abstratos pois vão ser implementados diferente pelo Human e IA */
	public abstract void placeShips();
	public abstract Shot shoot();
	public abstract void startTurn();
	public abstract void endTurn();
	
	/* Atualiza a matriz de batalha com os resultados do tiro */
	public void shotResults(Shot shot) {
		
		/** Quatro valores possíveis para o estado daquele ponto são armazenados na matriz:
		 *  acerto, afundado, erro e desconhecido. No começo, tudo é desconhecido, conforme
		 *  se joga, se preenche com acerto ou erro para saber onde atirar depois
		 *  
		 *  Se o tiro afunda algum navio, todas as coordenadas daquele navio são marcadas
		 *  como afundadas, pois sabe-se que aqueles pontos, apesar de acertos, não pertencem
		 *  a outro navio */
		
		if(!shot.hit)
			battleMap.setStatusAtPoint(shot.target, Status.MISS);
		else if(shot.sunkShip == null)
			battleMap.setStatusAtPoint(shot.target, Status.HIT);
		else {
			/** Repare que aqui o shot.sunkShip é um dos navios que seu oponente armazena, e
			 * portanto possui coordenadas e informações. Agora que você o afundou, tem acesso
			 * à elas. Então você vai usar elas pra duas coisas.
			 * 
			 * Primeiro, compara qual das cópias que você mantém corresponde àquele navio, e
			 * marca ela como afundada, para ter controle do que você já afundou.*/
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
	
	/* Confere se o oponente está sem navios */
	/** Quem armazena os navios afundados é o afundador, portanto um jogador não tem como
	 * saber que ele perdeu, mas sim seu oponente sabe que ganhou. */
	public boolean isWinner() {
		
		for (Ship ship : enemyShips)
			if(!ship.isSunk()) 
				return false;
		
		return true;
	}

}
