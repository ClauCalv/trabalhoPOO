package br.edu.ufabc.poo.strategy;

import br.edu.ufabc.poo.model.*;

public class SnDProbabilityMapDrawer extends ProbabilityMapDrawer {

	public SnDProbabilityMapDrawer(BattleMap battleMap, Ship[] ships) {
		super(battleMap, ships);
	}

	/*** Este � o antigo m�todo updateProbabilityMapOnSnD() da classe IAPlayer*/
	public int[][] update() {
		
		clearMap();
		
		/** Repare que h� 5 la�os de repeti��o aqui dentro, dois pra percorrer a matriz, um
		 * para conferir cada navio, um para cada orienta��o poss�vel, e um para conferir
		 * cada uma das coordenadas ao longo da orienta��o escolhida.
		 * 
		 *  O pen�ltimo n�o � um la�o de fato, porque as orienta��es poss�veis est�o
		 *  explicitamente aberto nos quatro IFs em sequ�ncia abaixo.
		 *  
		 *  Repare que como eu analiso somente os pontos marcados com HIT, � necess�rio
		 *  conferir as quatro orienta��es. */
		
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
