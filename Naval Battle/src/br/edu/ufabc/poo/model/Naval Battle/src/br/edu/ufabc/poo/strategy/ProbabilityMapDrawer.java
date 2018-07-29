package br.edu.ufabc.poo.strategy;

import br.edu.ufabc.poo.model.*;

public abstract class ProbabilityMapDrawer {

	/*** Parte do Design Pattern, � uma classe abstrata onde cada implementa��o faz a l�gica
	 * do m�todo update de forma diferente, e a classe contexto invoca esse m�todo, guardando
	 * uma estrat�gia das poss�veis que vai se alternando, mas o m�todo tem sempre a mesma
	 * assinatura. */
	protected int[][] probabilityMap;
	protected BattleMap battleMap;
	protected Ship[] ships;
	
	public ProbabilityMapDrawer(BattleMap battleMap, Ship[] ships) {
		probabilityMap = new int[battleMap.size][battleMap.size];
		this.battleMap = battleMap;
		this.ships = ships;
	}
	
	protected void clearMap() {
		
		for (int i = 0; i < probabilityMap.length; i++)
			for (int j = 0; j < probabilityMap.length; j++)
				probabilityMap[i][j] = 0;
		
	}
	
	public abstract int[][] update();
	
}
