package br.edu.ufabc.poo.user;

import br.edu.ufabc.poo.players.Player;

public class User {

	private final Player player;
	private String name, password;
	
	public User(Player player, String name, String password) {
		
		this.player = player;
		this.name = name;
		this.password = password;
	}

	public Player getPlayer() {
		return player;
	}

	public String getName() {
		return name;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isPasswordCorrect(String password) {
		return this.password.equals(password);
	}

}
