package br.edu.ufabc.poo.players;

import java.util.LinkedList;
import java.util.Random;

import br.edu.ufabc.poo.GameController;
import br.edu.ufabc.poo.model.*;
import br.edu.ufabc.poo.strategy.*;

public class IaPlayer extends Player {

	private Random random;
	
	/*** Implantei aqui o DesignPattern: Strategy, mas em vez de interface, fiz com classe abstrata
	 * https://en.wikipedia.org/wiki/Strategy_pattern 
	 * 
	 * Movi vários métodos de uma classe pra outra:
	 * -- IsValidShipPosition foi pra BattleMap
	 * -- updateProbebilityMap foi pra DefaultProbabilityMapDrawer
	 * -- updateProbebilityMapOnSnD foi pra SnDProbabilityMapDrawer */
	private ProbabilityMapDrawer probabilityMap;
	
	public IaPlayer(Ship[] myShips, Ship[] enemyShips, int mapSize) {
		super(myShips, enemyShips, mapSize);
		random = new Random();
		probabilityMap = new DefaultProbabilityMapDrawer(battleMap, enemyShips);
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
		
		battleMap.clearMap();
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
			int xR = random.nextInt(battleMap.size), yR = random.nextInt(battleMap.size);
			int direction = random.nextInt(4);
			
			boolean valid = false;
			
			for(int i = 0; i < 8; i++) {
				
				direction = (direction + 1) % 4;
				
				if(i == 4) {
					int aux = yR;
					yR = xR;
					xR = aux;
				}
				
				valid = battleMap.isValidShipPosition(ship.size, xR, yR, direction);
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
			
			battleMap.setStatusAtPoint(newX, newY, Status.SUNK);
			positions[i] = new Vector2D(newX,newY);
			
		}
		
		ship.placeShip(positions, battleMap.size);
		
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
		
		boolean seekAndDestroy = false;
		
		if(shot.hit)
			if(shot.sunkShip == null)
				seekAndDestroy = true;
			else
				for (int i = 0; i < battleMap.size; i++)
					for (int j = 0; j < battleMap.size; j++)			
						if(battleMap.getStatusAtPoint(i, j).equalsAny(Status.HIT))
							seekAndDestroy = true;
				
		updateStrategy(seekAndDestroy);
	}

	/* Descobre se há navios a serem perseguidos no mapa */
	private void updateStrategy(boolean seekAndDestroy) {
		probabilityMap = seekAndDestroy ? new SnDProbabilityMapDrawer(battleMap, enemyShips) : 
					new DefaultProbabilityMapDrawer(battleMap, enemyShips);
	}

	/* Atira no ponto de maior chance de acertar um navio, sorteando em caso de empate */
	public Shot shoot() {
		
		/** Os mapas de probabilidade constroem o número de navios que podem estar em cada ponto.
		 * O mais lógico, portanto, é atirar onde pode haver mais navios. */
	
		int[][] probabilityMap = this.probabilityMap.update();
	
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

	public void startTurn() {
		
		GameController.getInstance().user.startIATurn(this);
		
	}

	public void endTurn() {}

}
