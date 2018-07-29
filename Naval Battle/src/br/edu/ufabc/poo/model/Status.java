package br.edu.ufabc.poo.model;

public enum Status {
	UNKNOWN, MISS, HIT, SUNK;
	
	/*** Essas acima s�o as �nicas quatro inst�ncia poss�veis de Status, n�o se pode
	 * criar novas. Invoca-se elas estaticamente, por "Status.MISS", por exemplo */
	
	public boolean equalsAny(Status ... status) {
		
		for (Status stat : status)
			if(this == stat)
				return true;
		
		return false;
	}

}
