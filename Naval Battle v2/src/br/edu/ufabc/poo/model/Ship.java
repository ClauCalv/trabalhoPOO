package br.edu.ufabc.poo.model;


public class Ship {
	
	public final int size, id;
	
	private boolean isSunk = false;
	/** Escolhi, em vez de armazenar navios no mapa, armazenar as coordenadas diretamente no navio,
	 * poupando assim o esfor�o de associar cada coordenada do mapa a um navio*/
	private Vector2D[] positions;
	
	public Ship(int size, int id) {		
		this.size = size;
		this.id = id;
	}
	
	/* Valida e associa o navio a sua posi��o no mapa */
	public boolean placeShip(Vector2D[] positions, int mapSize) {
		
		if(positions.length != size)
			return false;
		
		this.positions = new Vector2D[size]; /** Cria um novo */
		
		for (int i = 0; i < positions.length; i++) {
			if(!positions[i].isInBounds(0,0,mapSize,mapSize)) 
				return false;
			this.positions[i] = positions[i].clone();
			this.positions[i].setHit(false);
			
		}
			
		return true;
	}
	
	/** Um navio s� � afundado se todas as suas coordenadas j� foram afundadas */
	public boolean isSunk() {
		
		if(positions!=null)
			if(!isSunk) {	
				isSunk = true;
			
				for (Vector2D pos : positions)
					if(!pos.isHit()) 
						isSunk = false;
			}
		
		return isSunk;
	}
	
	public void setSunk(boolean isSunk) {
		this.isSunk = isSunk;
	}
	
	/* configura 'hit = true' caso atingido e 'ship = this' caso afundado */
	/** Pra avaliar se tomou um tiro, compare a coordenada do tiro a cada uma das
	 * coordenadas que voc� guarda. Se atingiu, marque a coordenada como afundada
	 * e confira se o navio afundou, para avisar ao tiro que ele afundou um navio */
	public void takeShot(Shot shot) {
		
		for (Vector2D pos : positions) {
			if(shot.target.equals(pos)) {
				pos.setHit(true);
				shot.hit = true;
				break;
			}
		}
		
		if(shot.hit) shot.sunkShip = isSunk() ? this : shot.sunkShip;
		
	}
	
	/* retorna uma c�pia do navio, mas sem informa��es de sua posi��o*/
	public Ship blindClone() {
		return new Ship(size, id);
	}
	
	public Vector2D[] getPositions() {
		return positions;
	}

}
