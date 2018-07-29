package br.edu.ufabc.poo.model;

public enum Status {
	UNKNOWN, MISS, HIT, SUNK;
	
	/*** Essas acima são as únicas quatro instância possíveis de Status, não se pode
	 * criar novas. Invoca-se elas estaticamente, por "Status.MISS", por exemplo */
	
	public boolean equalsAny(Status ... status) {
		
		for (Status stat : status)
			if(this == stat)
				return true;
		
		return false;
	}

}
