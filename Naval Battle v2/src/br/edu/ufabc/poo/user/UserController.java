package br.edu.ufabc.poo.user;

import java.io.IOException;
import java.util.Scanner;

import br.edu.ufabc.poo.model.BattleMap;
import br.edu.ufabc.poo.model.Ship;
import br.edu.ufabc.poo.model.Shot;
import br.edu.ufabc.poo.model.Status;
import br.edu.ufabc.poo.model.Vector2D;
import br.edu.ufabc.poo.players.Player;

public class UserController {
	
	public static final String[] shipConfigExamples = 
		{"2,2,3,4","2,3,3,4,5","2,3,3,4,4,5","2,2,3,3,3,4,4,5,6","2,2,3,3,3,4,4,4,5,5,6","2,2,3,3,4,4,4,5,5,5,6,6,7"};

	private final Scanner scanner;

	private User[] users;
	
	public UserController() {
		scanner = new Scanner(System.in);
		users = new User[2];
	}
	
	private void clearScreen(){
	    //Clears Screen in java
	    try {
	        if (System.getProperty("os.name").contains("Windows"))
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        else
	            Runtime.getRuntime().exec("clear");
	    } catch (IOException | InterruptedException ex) {}
	}
	
	public boolean isShipConfigValid(int[] shipSizes, int mapSize) {
		
		int count = 0;
		
		for (int size : shipSizes) {
			if(size < 2 || size > mapSize)
				return false;
			count += size;
		}
		
		return count <= mapSize * mapSize / 2;
	}
	
	public boolean askMultiplayer() {
		
		clearScreen();
		
		System.out.println("Bem vindo ao jogo de batalha naval!\n"
				+ "Gostaria de jogar contra outro oponente ou o computador?");
		
		while(true) {
			
			System.out.println("Digite '0' para jogar contra outro jogador e '1' para o computador");
			String input = scanner.nextLine();
			if(input.equals("0"))
				return true;
			if(input.equals("1"))
				return false;
		}
	}
	
	public int askMapSize() {

		System.out.println("Qual o tamanho do grid?");
		
		while(true) {
			
			System.out.println("Escolha entre: '6x6', '8x8', '10x10','12x12', '14x14' ou '16x16'");
			String input = scanner.nextLine();
			
			switch (input) {
				case "6x6":
					return 6;
				case "8x8":
					return 8;
				case "10x10":
					return 10;
				case "12x12":
					return 12;
				case "14x14":
					return 14;
				case "16x16":
					return 16;
			}
			
		}
		
		
	}

	public int[] askShipSizes(int mapSize) {

		System.out.println("Configure os navios que serão usados: Insira o tamanho dos navios separados por vírgula");
		
		while(true) {
			
			System.out.println(" - Use somente vírgulas para separar os valores;\n"
							   + " - Os navios devem ter tamanho maior ou igual a 2;\n"
							   + " - Os navios não podem ser maiores que a lateral do mapa\n"
							   + " - Insira '0' para iniciar a configuração padrão");
			
			String input = scanner.nextLine();
			if(input.equals("0"))
				input = shipConfigExamples[(mapSize-6)/2];
			
			String[] ships = input.split(",");
			int[] shipSizes = new int[ships.length];
			boolean valid = false;
			try {
				for (int i = 0; i < ships.length; i++)
					shipSizes[i] = Integer.parseInt(ships[i]);
				
				valid = isShipConfigValid(shipSizes, mapSize);
			} catch (Exception e2) { }
			
			if(valid)
				return shipSizes;
			
			System.out.println("A configuração usada é inválida");
			System.out.println("Tente a configuração padrão:");
			System.out.println(shipConfigExamples[(mapSize-6)/2]);
		}
	}

	public boolean askRestart(Player player) {

		System.out.println("O jogador "+getUser(player).getName()+" ganhou! Deseja recomeçar?");
		
		while(true){
			
			System.out.println("Digite '0' para encerrar e '1' para recomeçar");
			String input = scanner.nextLine();
			if(input.equals("0"))
				return false;
			if(input.equals("1"))
				return true;
		}
	}

	private User getUser(Player player) {
		for (User user : users)
			if(user.getPlayer().equals(player))
				return user;
						
		return null;
	}

	public void startIATurn(Player iaPlayer) {
		
		if(users[1] == null)
			users[1] = new User(iaPlayer, "Computador", null);
		
		System.out.println("É a vez do computador!");
		
	}

	public void startTurn(Player humanPlayer, boolean isMultiplayer) {

		clearScreen();
		
		if(users[0] == null)
			createUser(0, humanPlayer, isMultiplayer);
		else if(users[1] == null)
			createUser(1, humanPlayer, isMultiplayer);
		
		User user = getUser(humanPlayer);
		System.out.println(user.getName()+", é o seu turno!");
		if(!isMultiplayer)
			return;
		
		while(true) {
			System.out.println("Insira sua senha pra continuar");
			if(user.isPasswordCorrect(scanner.nextLine()))
				return;
			System.out.println("Senha incorreta!");
		}
		
	}

	private void createUser(int i, Player player, boolean isMultiplayer) {

		System.out.println("Um novo jogador!");
		
		while(true) {
			System.out.println("Insira o seu nome:");
			String name = scanner.nextLine();
	
			String password = null;
			if(isMultiplayer) {
				System.out.println("Insira uma senha para seu turno:");
				password = scanner.nextLine();
			}
		
			System.out.println(isMultiplayer ? name+", certo, e esta é sua senha? "+password : name+", certo?");
			System.out.println("Insira '1' para confirmar nome" + (isMultiplayer?" e senha":""));
			
			if(scanner.nextLine().equals("1")) {
				users[i] = new User(player, name, password);
				return;
			}
		}		
	}

	public void warnLastShot(Shot shot) {
		
		String message = "Você recebeu um disparo em "+ (char)('A' + shot.target.y) + "" + (shot.target.x+1);
		if(shot.hit)
			message += " que acertou um navio aliado";
		if(shot.sunkShip!=null)
			message += ", afundando um navio de tamanho "+shot.sunkShip.size;
		
		System.out.println(message);
		
	}

	public void placeShips(BattleMap battleMap, Ship[] myShips) {

		System.out.println("Posicione seus navios um por vez. Insira suas cordenadas separadas por vírgulas,"
				+ " por exemplo, 'A1,A2,A3,A4' ou 'B3,C3,D3,E3,F3'.");
		
		while(true) {
		
			int remainingShips = 0;
			String remainingShipList = "";
			
			for (Ship ship : myShips)
				if(!ship.isSunk()) {
					remainingShipList += ship.size + " ";
					remainingShips++;
				}
			
			if(remainingShips == 0)
				break;
			
			System.out.println("Navios ainda disponíveis: " + remainingShipList);
			
			System.out.println("U = Vazio, S = Navio\n");
			printBattleMap(battleMap);
			
			System.out.println("\nInsira as coordenadas do navio");
			
			String input = scanner.nextLine();
			
			String[] shipCoords = input.split(",");
			Vector2D[] coords = new Vector2D[shipCoords.length];
			boolean valid = false;
			try {
				for (int i = 0; i < coords.length; i++) {
					int y = shipCoords[i].charAt(0) - 'A';
					int x = Integer.parseInt(shipCoords[i].substring(1)) - 1;
					coords[i] = new Vector2D(x, y);					
				}
				
				valid = isShipPositionValid(coords, myShips, battleMap);
			} catch (Exception e2) {
				System.out.println("Coordenadas inválidas");
			}
			
			if(!valid)
				while(true) {
					System.out.println("Manter a configuração atual? Digite '1' para manter ou '0' para recomeçar");
					String input2 = scanner.nextLine(); 
					if(input2.equals("1"))
						break;
					if(input2.equals("0")) {
						placeShips(battleMap, myShips);
						return;
					}
				}			
		}
		
		for (Ship ship : myShips)
			ship.setSunk(false);
		battleMap.clearMap();
		
	}

	private boolean isShipPositionValid(Vector2D[] coords, Ship[] myShips, BattleMap battleMap) {
		
		Ship availableShip = null;
		for (Ship ship : myShips)
			if((!ship.isSunk()) && ship.size == coords.length)
				availableShip = ship;
		if(availableShip == null) {
			System.out.println("Não há navio disponível com esse tamanho");
			return false;
		}
		
		boolean isInsideMap = true;
		for (Vector2D point : coords)
			if(!point.isInBounds(0, 0, battleMap.size, battleMap.size))
				isInsideMap = false;
		if(!isInsideMap) {
			System.out.println("As coordenadas não estão contidas no mapa. Escolha valores válidos");
			return false;
		}
		
		boolean isAligned = true;
		Vector2D[] deltas = new Vector2D[coords.length - 1];
		for (int i = 0; i < deltas.length; i++) {
			int deltaX = coords[i+1].x - coords[i].x;
			int deltaY = coords[i+1].y - coords[i].y;
			deltas[i] = new Vector2D(deltaX, deltaY);
		}
		for (int i = 0; i < deltas.length; i++) {
			if(Math.abs(deltas[i].x) + Math.abs(deltas[i].y) != 1)
				isAligned = false;
			if(!deltas[i].equals(deltas[0]))
				isAligned = false;
		}
		
		if(!isAligned) {
			System.out.println("O navio não está alinhado. Escolha cordenadas consecutivas em uma reta");
			return false;
		}
		
		boolean isEmpty = true;
		for (Vector2D point : coords)
			if(battleMap.getStatusAtPoint(point).equalsAny(Status.SUNK))
				isEmpty = false;
		
		if(!isEmpty) {
			System.out.println("O navio está ocupando uma coordenada já ocupada. Reposicione o navio");
			return false;
		}
		
		for (Vector2D coord : coords)
			battleMap.setStatusAtPoint(coord, Status.SUNK);
		availableShip.placeShip(coords, battleMap.size);
		availableShip.setSunk(true);
		return true;
	}

	public void printBattleMap(BattleMap battleMap) {

		for (int i = -1; i < battleMap.size; i++) {
			System.out.print(i==-1 ? "   " : i<9 ? " "+(i+1)+" " : (i+1)+" ");
			for (int j = 0; j < battleMap.size; j++) 
				System.out.print((i == -1 ? (char)('A' + j) : battleMap.getStatusAtPoint(i,j).letter)+" ");
			System.out.println();
		}
		
	}

	public Shot shoot(BattleMap battleMap) {

		while(true) {
			System.out.println("Escolha uma coordenada no mapa para disparar");
			
			System.out.println("U = Vazio, M = Erro, H = Acerto, S = Navio Afundado\n");
			printBattleMap(battleMap);
			
			System.out.println("\nInsira as coordenadas do navio");
			
			String input = scanner.nextLine();
			Vector2D target = null;
			boolean valid = false;
			try {
				int y = input.charAt(0) - 'A';
				int x = Integer.parseInt(input.substring(1)) - 1;
				target = new Vector2D(x, y);					
				
				valid = isShotValid(target, battleMap);
			} catch (Exception e2) {
				System.out.println("Coordenadas inválidas");
			}
			
			if(valid)
				return new Shot(target);
		}
	}

	private boolean isShotValid(Vector2D target, BattleMap battleMap) {

		if(!target.isInBounds(0,0,battleMap.size,battleMap.size)) {
			System.out.println("Coordenada fora do mapa. Atire em uma coordenada válida");
			return false;
		}
		
		if(!battleMap.getStatusAtPoint(target).equalsAny(Status.UNKNOWN)) {
			System.out.println("Você já atirou nessa coordenada antes! Escolha outra.");
			return false;
		}
		
		return true;
	}

	public void shotResults(Shot shot) {
		
		String message = "";
		if(shot.hit) {
			message = "Você acertou o tiro";
			if(shot.sunkShip!=null)
				message += " e afundou um navio de tamanho " + shot.sunkShip.size;
		}
		else
			message = "Você errou";
		
		System.out.println(message+"!");
	}

}
