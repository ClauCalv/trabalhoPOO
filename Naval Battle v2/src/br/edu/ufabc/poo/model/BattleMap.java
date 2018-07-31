package br.edu.ufabc.poo.model;

public class BattleMap {

	public final int size;
	
	/*** Ele � um mapa de Status, que � um Enum. Repare que Enums ainda s�o objetos.*/
	protected final Status[][] map;
	
	public BattleMap(int mapSize) {
		size = mapSize;
		map = new Status[mapSize][mapSize];
	}
	
	/*** Este m�todo estava na classe Player, mas veio pra c�. */
	public void clearMap() {
				
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				map[i][j] = Status.UNKNOWN;
			}
		}
	}
	
	/* Verifica a possibilidade de um navio estar posicionado naquela coordenada e dire��o */
	/*** Este m�todo estava na classe IAPlayer, mas veio pra c�. */
	public boolean isValidShipPosition(int size, int x, int y, int direction) {
		
		/** Esse m�todo � usado duas vezes, uma na coloca��o de navios no grid, e outra na
		 * cria��o do mapa de propabilidades pra IA poder disparar.
		 * 
		 * Ele analisa se o navio encaixa naquelas coordenadas iniciais e orienta��o. Repare que
		 * um navio s� cabe se ele passa por UNKNOWN ou HIT. Se ele passasse por MISS, estar�amos
		 * dizendo que um navio pode ter um buraco dentro dele. Se ele passasse por SUNK,
		 * estar�amos dizendo que um navio pode estar em cima de outro.
		 * 
		 * Por isso, o m�todo que coloca navios na mesa para validar, coloca como SUNK, para
		 * reaproveitar esse m�todo.*/
		
		int newX, newY;
		for (int i = 0; i < size; i++) {
			newX = direction==0 ? x+i : direction==2 ? x-i : x;
			newY = direction==1 ? y+i : direction==3 ? y-i : y;
			
			if( ! (new Vector2D(newX,newY)).isInBounds(0, 0, this.size, this.size) )
				return false;
			
			if(getStatusAtPoint(newX, newY).equalsAny(Status.MISS, Status.SUNK))
				return false;
		}
		
		return true;
		
	}
	
	public Status getStatusAtPoint(Vector2D point) {
		return map[point.x][point.y];
	}
	
	public Status getStatusAtPoint(int x, int y) {
		return map[x][y];
	}
	
	public void setStatusAtPoint(Vector2D point, Status status) {
		map[point.x][point.y] = status;
	}
	
	public void setStatusAtPoint(int x, int y, Status status) {
		map[x][y] = status;
	}
	
}
