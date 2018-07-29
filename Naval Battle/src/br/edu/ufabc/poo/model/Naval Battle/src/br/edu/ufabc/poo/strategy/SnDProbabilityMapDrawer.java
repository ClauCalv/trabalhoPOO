package br.edu.ufabc.poo.strategy;

import br.edu.ufabc.poo.model.*;

public class SnDProbabilityMapDrawer extends ProbabilityMapDrawer {

	public SnDProbabilityMapDrawer(BattleMap battleMap, Ship[] ships) {
		super(battleMap, ships);
	}

	/*** Este é o antigo método updateProbabilityMapOnSnD() da classe IAPlayer*/
	public int[][] update() {
		
		clearMap();
		
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
				
				if(!battleMap.getStatusAtPoint(i,j).equalsAny(Status.HIT))
					continue;
				
				for (Ship ship : ships)
					if(!ship.isSunk()) {						
						
						if(battleMap.isValidShipPosition(ship.size, i, j, 0))
							for (int k = 1; k < ship.size; k++)
								if(!battleMap.getStatusAtPoint(i+k,j).equalsAny(Status.HIT))
									probabilityMap[i+k][j]++;
							
						if(battleMap.isValidShipPosition(ship.size, i, j, 1))
							for (int k = 1; k < ship.size; k++)
								if(!battleMap.getStatusAtPoint(i,j+k).equalsAny(Status.HIT))
									probabilityMap[i][j+k]++;
						
						if(battleMap.isValidShipPosition(ship.size, i, j, 2))
							for (int k = 1; k < ship.size; k++)
								if(!battleMap.getStatusAtPoint(i-k,j).equalsAny(Status.HIT))
									probabilityMap[i-k][j]++;
							
						if(battleMap.isValidShipPosition(ship.size, i, j, 3))
							for (int k = 1; k < ship.size; k++)
								if(!battleMap.getStatusAtPoint(i,j-k).equalsAny(Status.HIT))
									probabilityMap[i][j-k]++;
						
					}
			}
		
		return probabilityMap;
	}

}
