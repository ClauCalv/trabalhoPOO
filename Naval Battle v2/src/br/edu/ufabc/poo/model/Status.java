package br.edu.ufabc.poo.model;

public enum Status {
	UNKNOWN('U'), MISS('M'), HIT('H'), SUNK('S');
	
	public final char letter;
	
	//Cada Status j� leva em si a abrevia��o que levar� no mapa impresso ao usu�rio
	private Status(char letter) {
		this.letter = letter;
	}
	
	/*** Uma coordenada pode ter apenas 4 estados: Desconhecido, sem nenhum navio,
	 *  com navio e atingido, e com navio afundado */
	
	//Compara rapidamente se um Status � igual a outros.
	public boolean equalsAny(Status ... status) {
		
		for (Status stat : status)
			if(this == stat)
				return true;
		
		return false;
	}

}
