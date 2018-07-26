package br.edu.ufabc.poo.players;

import java.util.HashMap;

import br.edu.ufabc.poo.model.*;

public abstract class Player {
	
	/** Isso são constantes numéricas. Constantes (static final) se escrevem em CAPSLOCK 
	 * O professor pediu para que cada coordenada do mapa fosse um objeto, mas em vez disso eu
	 * coloquei números. Sugiro três coisas que resolvem isso
	 * 
	 * 1) Criem uma classe com essas constantes numéricas e uma variável chamada ESTADO, que diz
	 * como ela está no momento, e o método agora teria que chamar comparar atributos de objetos
	 * e não mais variável, então ficaria por exemplo:
	 * -- protected final MapTile[][] battleMap;
	 * -- battleMap[i][j].setState(MapTile.UNKNOWN);
	 * -- if(battleMap[i][j].getStatus() == MapTile.HIT) 
	 * 
	 * 2) Reutilizem a classe Vector2D, colocando essa funcionalidade nela, e verão que dá pra
	 * simplificar até mais código ainda, e não teríamos que usar hashmaps, só vetores, porque as
	 * próprias coordenadas já guardam seu estado
	 * 
	 * 3) Criem um ENUM, é tipo um objeto só que todas suas instancias já existem e são estáticas
	 * Essa soluçao pode ainda ser misturada com as duas primeiras, ou implementada sozinha nesta
	 * mesma classe, e agora, seja "public enum TileState" uma '''classe'''', basta fazer
	 * "protected final TileState[][] battleMap;" uma matriz de enums. Minha solução predileta
	 * seria implementar a 2 e a 3 juntas, porém deixo escolherem como querem fazer ou se querem
	 * que eu faça essa correção no código 
	 * 
	 * EDIT: Vou subir em outra branch no Github a correção que eu faria, fiquem a vontade pra
	 * não usar, porque ela usa coisa que vocês não verão em aula.*/
	protected static final int UNKNOWN = 0, MISS = 1, HIT = 2, SUNK = 3;
	
	protected final Ship[] myShips;
	/* Associa um navio inimigo a seu estado de ter sido afundado*/
	/** Um HashMap trabalha com pares de valores <K,V>, ou Keys e Values, chaves e valores
	 * Pra cada elemento na lista de chaves, que não pode ter repetições, há um único elemento
	 * na lista de valores para o qual ele corresponde. Isso permite fazer que cada navio tenha
	 * associado consigo um valor booleano que diz se ele já foi afundado.
	 * 
	 * Repare que myShips é só um vetor porque não preciso associar nada a eles, não me faz
	 * diferença se meus navios foram afundados, só precisa saber quais navios eu tenho */
	protected final HashMap<Ship, Boolean> enemyShips;
	
	/** Aqui está a matriz que deve ser arrumada. Lembrem que tudo envolvido com ela tem que
	 * ser arrumado também! */
	protected final int[][] battleMap;
	
	/* Cria para si uma cópia cega dos navios inimigos, para ser rastreada */
	public Player(Ship[] myShips, Ship[] enemyShips, int mapSize){
		
		battleMap = new int[mapSize][mapSize];
		clearMap();
		
		this.myShips = myShips;
		
		this.enemyShips = new HashMap<Ship, Boolean>(enemyShips.length);
		for (Ship ship : enemyShips)
			this.enemyShips.put(ship.blindClone(), false);
			/* insere falso pois o navio não foi afundado */
			/** Os clones cegos servem pra mostrar pra um oponente um navio seu sem mostrar o
			 * interior. Esses clones são outro objeto, e portanto, os originais são
			 * inacessíveis através dele. */
	}
	
	/* Limpa a matriz de batalha, para começar o jogo */
	protected void clearMap() {
		for (int i = 0; i < battleMap.length; i++)
			for (int j = 0; j < battleMap.length; j++)
				battleMap[i][j] = UNKNOWN;
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
			battleMap[shot.target.x][shot.target.y] = MISS;
		else if(shot.sunkShip == null)
			battleMap[shot.target.x][shot.target.y] = HIT;
		else {
			/** Repare que aqui o shot.sunkShip é um dos navios que seu oponente armazena, e
			 * portanto possui coordenadas e informações. Agora que você o afundou, tem acesso
			 * à elas. Então você vai usar elas pra duas coisas.
			 * 
			 * Primeiro, compara qual das cópias que você mantém corresponde àquele navio, e
			 * marca ela como afundada, para ter controle do que você já afundou.*/
			for (Ship ship : enemyShips.keySet())				
				if(shot.sunkShip.id == ship.id) 
					enemyShips.replace(ship, true);
			
			for (Vector2D pos : shot.sunkShip.getPositions())
				battleMap[pos.x][pos.y] = SUNK;
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
		
		for (Ship ship : enemyShips.keySet())
			if(!enemyShips.get(ship)) 
				return false;
		
		return true;
	}

}
