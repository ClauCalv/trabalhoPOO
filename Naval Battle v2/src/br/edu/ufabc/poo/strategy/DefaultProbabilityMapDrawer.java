package br.edu.ufabc.poo.strategy;

import br.edu.ufabc.poo.model.BattleMap;
import br.edu.ufabc.poo.model.Ship;
import br.edu.ufabc.poo.model.Status;

public class DefaultProbabilityMapDrawer extends ProbabilityMapDrawer {

	public DefaultProbabilityMapDrawer(BattleMap battleMap, Ship[] ships) {
		super(battleMap, ships);
	}

	public int[][] update() {

		clearMap();
		
		/** Diferente do modo de perseguição, no modo de procura eu testo todo ponto no mapa
		 *  
		 *  Como eu analiso todos os pontos no mapa válidos, não é necessário conferir as quatro
		 *  orientações possíveis, basta varrer pra baixo e pra esquerda, e todos as possibilidades
		 *  de encaixe já serão contempladas. */
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++) {
				
				if(!battleMap.getStatusAtPoint(i,j).equalsAny(Status.MISS, Status.SUNK))
					continue;
				
				for (Ship ship : ships)
					if(!ship.isSunk()) {						
						
						if(battleMap.isValidShipPosition(ship.size, i, j, 0))
							for (int k = 0; k < ship.size; k++)
								if(!battleMap.getStatusAtPoint(i+k,j).equalsAny(Status.HIT))
									probabilityMap[i+k][j]++;
							
						if(battleMap.isValidShipPosition(ship.size, i, j, 1))
							for (int k = 0; k < ship.size; k++)
								if(!battleMap.getStatusAtPoint(i,j+k).equalsAny(Status.HIT))
									probabilityMap[i][j+k]++;
						
					}
			}
		
		return probabilityMap;
	}

}
