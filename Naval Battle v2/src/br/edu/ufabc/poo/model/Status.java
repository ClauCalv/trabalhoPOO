package br.edu.ufabc.poo.model;

public enum Status {
	UNKNOWN('U'), MISS('M'), HIT('H'), SUNK('S');
	
	public final char letter;
	
	private Status(char letter) {
		this.letter = letter;
	}
	
	/*** Essas acima s�o as �nicas quatro inst�ncia poss�veis de Status, n�o se pode
	 * criar novas. Invoca-se elas estaticamente, por "Status.MISS", por exemplo */
	
	public boolean equalsAny(Status ... status) {
		
		for (Status stat : status)
			if(this == stat)
				return true;
		
		return false;
	}

}
