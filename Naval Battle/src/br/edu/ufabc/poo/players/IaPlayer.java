package br.edu.ufabc.poo.players;

import java.util.LinkedList;
import java.util.Random;

import br.edu.ufabc.poo.model.*;

public class IaPlayer extends Player {

	private Random random;
	private int[][] probabilityMap;
	
	/** Esta classe também poderia ser modificada pra criar mais um Design Pattern: Strategy:
	 * 
	 * Se tirar toda a parte de atualizar a matriz de probabilidades e delegar isso pra uma interface
	 * que duas classes diferentes implementam, e fazer com que você escolha qual estratégia usar
	 * de acordo com essa mesma variável.
	 * 
	 * Não vi motivo pra deixar a construção do mapa de probablilidades em outra classe, mas isso iria
	 * deixar a UML do programa um pouco mais rica, além de contar ponto extra.
	 * 
	 * EDIT: Farei isso em uma terceira branch do GitHub e vocês vêem se ficou mais bonito, porque no
	 * fundo não muda nada*/
	private boolean seekAndDestroy = false;
	
	public IaPlayer(Ship[] myShips, Ship[] enemyShips, int mapSize) {
		super(myShips, enemyShips, mapSize);
		random = new Random();
		probabilityMap = new int[mapSize][mapSize];
	}

	/* Para que os navios sejam sempre aleatórios, tenta várias combinações e pega uma válida */
	public void placeShips() {
		
		/** Tá, toda essa sequência de métodos é uma grande gambiarra. Pra não ter que fazer uma
		 * coleção de matrizes de navios que ela começa jogando (seria uma péssima IA depois de se
		 * enfrentar algumas vezes), eu gero aleatoriamente uma matriz, e se não é válida, eu
		 * descarto e tento outra.
		 * 
		 * Isso pode demorar, por isso é importante que a soma dos pontos
		 * ocupados por navios não ultrapasse metade dos pontos do mapa, senão posicionar navios
		 * será impossível. Daí um bom motivo pra validadar a entrada do número e tipo de navios */
		boolean valid = false;
		while(!valid) 
			valid = placeShipsAtRandom();
		
		clearMap();
	}
	
	/* Para cada navio, sorteia um ponto no mapa e tenta encaixá-lo. Senão, desiste. */
	private boolean placeShipsAtRandom() {
		
		/** Para cada navio que deve ser encaixado, sorteie um ponto e tente encaixá-lo nas quatro
		 * orientações (cima, baixo, esquerda, direita) possíveis. Se não for possível, teremos que
		 * tirar todos os navios e tentar reencaixá-los um por um.
		 * 
		 * Para evitar de reiniciar muitas vezes, eu peço pra ele conferir no espelho desse ponto,
		 * o ponto (y,x), antes de anunciar que os navios não encaixam mais */
		for (Ship ship : myShips) {
			int xR = random.nextInt(battleMap.length), yR = random.nextInt(battleMap.length);
			int direction = random.nextInt(4);
			
			boolean valid = false;
			
			for(int i = 0; i < 8; i++) {
				
				direction = (direction + 1) % 4;
				
				if(i == 4) {
					int aux = yR;
					yR = xR;
					xR = aux;
				}
				
				valid = isValidShipPosition(ship.size, xR, yR, direction);
				if(valid) {
					putShipOnGrid(ship, xR, yR, direction);
					break;
				}
			}
			
			if(!valid) 
				return false;
			
		}
		
		return true;
	}
	
	/* Verifica a possibilidade de um navio estar posicionado naquela coordenada e direção */
	private boolean isValidShipPosition(int size, int x, int y, int direction) {
		
		/** Esse método é usado duas vezes, uma na colocação de navios no grid, e outra na
		 * criação do mapa de propabilidades pra IA poder disparar.
		 * 
		 * Ele analisa se o navio encaixa naquelas coordenadas iniciais e orientação. Repare que
		 * um navio só cabe se ele passa por UNKNOWN ou HIT. Se ele passasse por MISS, estaríamos
		 * dizendo que um navio pode ter um buraco dentro dele. Se ele passasse por SUNK,
		 * estaríamos dizendo que um navio pode estar em cima de outro.
		 * 
		 * Por isso, o método que coloca navios na mesa para validar, coloca como SUNK, para
		 * reaproveitar esse método.*/
		
		int newX, newY;
		for (int i = 0; i < size; i++) {
			newX = direction==0 ? x+i : direction==2 ? x-i : x;
			newY = direction==1 ? y+i : direction==3 ? y-i : y;
			
			if( ! (new Vector2D(newX,newY)).isInBounds(0, 0, battleMap.length, battleMap.length) )
				return false;
			
			if(battleMap[newX][newY] == MISS || battleMap[newX][newY] == SUNK)
				return false;
		}
		
		return true;
		
	}
	
	/* Armazena os navios no mapa para que seja validável. Também atualiza as infos dos navios */
	private void putShipOnGrid(Ship ship, int x, int y, int direction) {
		
		/** Coloca um navio no grid pra ser validado no final. Além disso, já informa ao navio
		 * que aquelas serão as coordenadas dele.
		 * 
		 * Não precisa limpar se a configuração atual de navios der errado, pois a nova vai
		 * sobrescrever a antiga. */
		
		int newX, newY;
		Vector2D[] positions = new Vector2D[ship.size];
		
		for (int i = 0; i < ship.size; i++) {
			newX = direction==0 ? x+i : direction==2 ? x-i : x;
			newY = direction==1 ? y+i : direction==3 ? y-i : y;
			
			battleMap[newX][newY] = SUNK;
			positions[i] = new Vector2D(newX,newY);
			
		}
		
		ship.placeShip(positions, battleMap.length);
		
	}
	
	/* Alterna entre o modo de procura e perseguição */
	public void shotResults(Shot shot) {
		
		/** A IA tem dois modos: procura e perseguição. Se há uma única coordenada com HIT no mapa,
		 * a IA para de procurar todos os pontos que podem ter navios, e procura somente aqueles
		 * que podem ter o navio atingido naquele ponto.
		 * 
		 * Por isso, sempre que um tiro atinge sem afundar, ela entra automaticamente em modo de
		 * perseguição. No entanto, se um navio for afundado, não significa que não há mais pontos
		 * com HIT no mapa, pois posso ter atingido outro navio perseguindo este. Pra isso, eu
		 * verifico. */
		
		super.shotResults(shot);
		
		if(shot.hit)
			if(shot.sunkShip == null)
				seekAndDestroy = true;
			else 
				seekAndDestroy = updateSnDStatus();
	}

	/* Descobre se há navios a serem perseguidos no mapa */
	private boolean updateSnDStatus() {
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++)			
				if(battleMap[i][j] == HIT) 
					return true;
				
		return false;
	}

	/* Atira no ponto de maior chance de acertar um navio, sorteando em caso de empate */
	public Shot shoot() {
		
		/** Os mapas de probabilidade constroem o número de navios que podem estar em cada ponto.
		 * O mais lógico, portanto, é atirar onde pode haver mais navios. */
	
		if(!seekAndDestroy)
			updateProbabilityMap();
		else	
			updateProbabilityMapOnSnD();
	
		/** Isso é uma espécie de fragmento de SelectionSort, mas em vez de pegar o primeiro maior,
		 * ele pega todos que estão empatados com maior número provável de navios */
		
		LinkedList<Vector2D> maximumPoints = new LinkedList<>();
		int maximumValue = 0;
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++) {
				
				if(probabilityMap[i][j] > maximumValue) {
					maximumPoints.clear();
					maximumValue = probabilityMap[i][j];
				}
				
				if(probabilityMap[i][j] == maximumValue)
					maximumPoints.add(new Vector2D(i, j));
				
			}
				
		/** No final, atira aleatoriamente dentre os mais prováveis */
		return new Shot(maximumPoints.get(random.nextInt(maximumPoints.size())));
	
	}
	
	/* Limpa o mapa de probabilidades, e o preenche com os pontos em torno dos acertos
	 * nos quais há chance de se acertar novamente */
	private void updateProbabilityMapOnSnD() {
		
		/** Primeiro, limpa o mapa da rodada anterior */
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++)
				probabilityMap[i][j] = 0;
		
		/** Repare que há 5 laços de repetição aqui dentro, dois pra percorrer a matriz, um
		 * para conferir cada navio, um para cada orientação possível, e um para conferir
		 * cada uma das coordenadas ao longo da orientação escolhida.
		 * 
		 *  O penúltimo não é um laço de fato, porque as orientações possíveis estão
		 *  explicitamente aberto nos quatro IFs em sequência abaixo.
		 *  
		 *  Repare que como eu analiso somente os pontos marcados com HIT, é necessário
		 *  conferir as quatro orientações. */
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++) {
				
				if(battleMap[i][j] != HIT)
					continue;
				
				for (Ship ship : enemyShips.keySet())
					/** Pergunta se o navio não foi afundado ainda, pegando o valor do HashMap
					 * que está associado com a chave "ship".*/
					if(!enemyShips.get(ship)) {						
						
						if(isValidShipPosition(ship.size, i, j, 0))
							for (int k = 1; k < ship.size; k++)
								if(battleMap[i+k][j] != HIT)
									probabilityMap[i+k][j]++;
							
						if(isValidShipPosition(ship.size, i, j, 1))
							for (int k = 1; k < ship.size; k++)
								if(battleMap[i][j+k] != HIT)
									probabilityMap[i][j+k]++;
						
						if(isValidShipPosition(ship.size, i, j, 2))
							for (int k = 1; k < ship.size; k++)
								if(battleMap[i-k][j] != HIT)
									probabilityMap[i-k][j]++;
							
						if(isValidShipPosition(ship.size, i, j, 3))
							for (int k = 1; k < ship.size; k++)
								if(battleMap[i][j-k] != HIT)
									probabilityMap[i][j-k]++;
						
					}
			}
		
	}

	/* Limpa o mapa de probabilidades, e o preenche com os pontos com maior chance de acerto */
	private void updateProbabilityMap() {
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++)
				probabilityMap[i][j] = 0;
		
		/** Diferente do modo de perseguição, no modo de procura eu testo todo ponto no mapa
		 *  
		 *  Como eu analiso todos os pontos no mapa válidos, não é necessário conferir as quatro
		 *  orientações possíveis, basta varrer pra baixo e pra esquerda, e todos as possibilidades
		 *  de encaixe já serão contempladas. */
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++) {
				
				if(battleMap[i][j] == MISS || battleMap[i][j] == SUNK)
					continue;
				
				for (Ship ship : enemyShips.keySet())
					if(!enemyShips.get(ship)) {						
						
						if(isValidShipPosition(ship.size, i, j, 0))
							for (int k = 0; k < ship.size; k++)
								probabilityMap[i+k][j]++;
							
						if(isValidShipPosition(ship.size, i, j, 1))
							for (int k = 0; k < ship.size; k++)
								probabilityMap[i][j+k]++;
						
					}
			}
		
	}
	

	public void startTurn() {
		
		// TODO: Não fazer nada? Exibir algo enquanto a IA joga?
		
	}

	public void endTurn() {
		
		// TODO: Fechar seja qual for a tela do turno da IA, ou nenhuma
		
	}

}
