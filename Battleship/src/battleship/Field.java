package battleship;

import java.util.Random;
import java.util.Scanner;

public class Field {
	private int rows;
	private int cols;
	private Player player; //The owner of this field/grid.
	public Location[][] location; //A 2-Dimensional array that contains all the (rows x cols) squares in the grid.
	private final char[] letters = {'A','B','C','D','E','F','G','H','I','K','L','M','N','O','P'};
	public AircraftCarrier A1, A2;
	public Destroyer D1, D2, D3;
	public Submarine S1, S2;
	private static Scanner in;
	private char direction;
	
	
	//Constructor. Creates the grid.
	public Field(int rows, int cols, Player p) {
		this.rows = rows;
		this.cols = cols;		
		this.player = p;
		location = new Location[rows][cols];
		for (int i=1; i<=rows; i++) {
			for (int j=1; j<=cols; j++) {
				location[i-1][j-1] = new Location(i, j);
			}
		}
		createShips();
		Ship[] ships = {A1, A2, D1, D2, D3, S1, S2};
		deployShips(ships);
	}
	
	
	//Constructor after loading game. It deploys the ships with the given coordinates.
	public Field(int rows, int cols, Player p, String[] coords) {
		this.rows = rows;
		this.cols = cols;		
		this.player = p;
		
		location = new Location[rows][cols];
		for (int i=1; i<=rows; i++) {
			for (int j=1; j<=cols; j++) {
				location[i-1][j-1] = new Location(i, j);
			}
		}
		createShips();
		Ship[] ships = {A1, A2, D1, D2, D3, S1, S2};
		int j=0;
		for (int i=0; i<21; i+=3) {
			placeShipWithCoordinates(ships[j], Integer.parseInt(coords[i]), Integer.parseInt(coords[i+1]), coords[i+2].charAt(0));
			j++;
		}	
	}
	
	
	private void createShips() {
		A1 = new AircraftCarrier("AircraftCarrier 1");
		A2 = new AircraftCarrier("AircraftCarrier 2");
		D1 = new Destroyer("Destroyer 1");
		D2 = new Destroyer("Destroyer 2");
		D3 = new Destroyer("Destroyer 3");
		S1 = new Submarine("Submarine 1");
		S2 = new Submarine("Submarine 2");
	}
	
	
	private void deployShips(Ship[] ships) {
		char method = deploymentMethod();
		if (method == 'A') { //automatic
			for (Ship ship : ships) {
				placeShipRandomly(ship);
			}
		} else  if (method == 'M') { //manual
			for (Ship ship : ships) {
				showField();
				placeShip(ship);
			}
			System.out.println("***********************************************************************************************\n");
		} else {
			System.out.println("Unrecognized method: " + method);
		}
	}
	
	
	//Asks for the deployment method (Automatic or Manual).
	private char deploymentMethod() {
		if (player instanceof HumanPlayer) {
			String method = "";
			in = new Scanner(System.in);
			while (true) {
				System.out.print("Ship deployment for " + player.name + " (A for automatic - M for manual): ");
				try {
					method = in.nextLine();
					if (method.equals("A") || method.equals("a") || method.equals("M") || method.equals("m")) {
						break;
					} else {
						throw new InvalidInputException("Invalid method");
					}
				} catch (Exception e) {
					System.out.print("Please enter A (for automatic) or M (for manual)\n");
				}		
			}
			return Character.toUpperCase(method.charAt(0));	
		} else {
			return 'A';
		}
	}
	
	
	//Places ship in the given coordinates.
	private void placeShipWithCoordinates(Ship s, int row, int col, char direction) {	
		if (direction == 'h') {
			for (int i=0; i<s.length; i++) {
				location[row-1][col+i-1].ship = s;
				location[row-1][col+i-1].state = s.letter;
			}
		} else if (direction == 'v') {
			for (int i=0; i<s.length; i++) {
				location[row+i-1][col-1].ship = s;
				location[row+i-1][col-1].state = s.letter;
			}
		}
		s.setCoords(row, col, direction);
	}


	//Asks for coordinates and places the ship there, if able.
		private void placeShip(Ship s) {
			in = new Scanner(System.in);
			int length = s.length;
			boolean flag = true;
			boolean flag2;
			while (flag) {
				System.out.print("\nSelect row and column for ship: " + s.name + " (Length: " + s.length + ") ");
				String answer = in.nextLine();
				if (answer.toLowerCase().equals("help")) {help();}
				if (answer.toLowerCase().equals("exit")) {exit();}
				int row, col;
				try {
					char rowchar = answer.charAt(0);
					row = CharToInt(rowchar);
					col = Integer.parseInt(answer.substring(1, answer.length()));
				} catch (Exception e) {
					System.out.println("Please enter a valid location");
					continue;
				}		
				flag2 = true;
				if (length != 1) {
					System.out.print("Select direction (h for horizontal - v for vertical): ");
					direction = in.nextLine().charAt(0);
				} else {
					direction = 'h';
				}
				while (flag2) {
					if (direction == 'H' || direction == 'h' || direction == 'V' || direction == 'v') {
						flag2 = false;
					} else {
						System.out.print("Please enter h (for horizontal) or v (for vertical): ");
						direction = in.nextLine().charAt(0);
					}
				}
				if (checkValidation(row, col, direction, length)) {
					if (direction == 'h' || direction == 'H') {
						for (int i=0; i<length; i++) {
							location[row-1][col+i-1].ship = s;
							location[row-1][col+i-1].state = s.letter;
						}
					} else if (direction == 'v' || direction == 'V') {
						for (int i=0; i<length; i++) {
							location[row+i-1][col-1].ship = s;
							location[row+i-1][col-1].state = s.letter;
						}
					}
					s.setCoords(row, col, direction);
					flag = false;
				} else {
					System.out.println("Please enter a valid location.");
				}
			}
		}
			
	
	//Translates a letter to the corresponding character.
	private int CharToInt(char s) {
		for (int i=0; i<letters.length; i++) {
			if (s == letters[i] || Character.toUpperCase(s) == letters[i]) {
				return i+1;
			}
		}
		return 0;
	}
	

	//Checks if a ship can be placed in the given coordinates (no collisions or out of bounds).
	private boolean checkValidation(int row, int col, char direction, int length) {
		boolean valid = true;
		try {
			if (direction == 'h' || direction == 'H') {
				for (int i=0; i<length; i++) {
					if (location[row-1][col+i-1].state != '.') {
						valid = false;
						break;
					}
				}			
			} else if (direction == 'v' || direction == 'V') {
				for (int i=0; i<length; i++) {
					if (location[row+i-1][col-1].state != '.') {
						valid = false;
						break;
					}
				}	
			}
		} catch (IndexOutOfBoundsException e) {
			valid = false;
		}	
		return valid;
	}
	
	
	//You guessed it! It places the ship randomly in the grid (always in a valid location).
	private void placeShipRandomly(Ship s) {
		int randomRow, randomCol;
		char randomDirection;
		Random random = new Random();
		for (int i=0; i<500; i++) {
			randomRow = random.nextInt(rows+1);
			randomCol = random.nextInt(cols+1);
			if(random.nextInt(2) == 0) {
				randomDirection = 'h';
			} else {
				randomDirection = 'v';
			}
			if (checkValidation(randomRow, randomCol, randomDirection, s.length)) {
				placeShipWithCoordinates(s, randomRow, randomCol, randomDirection);
				break;
			}
		}
	}
	
	
	//Displays the field (censored - without revealing the ships, unless they are sinking).
	public void showField() {
		System.out.print(player.name + "'s Field\n\t");
		for (int i=1; i<=cols; i++) {
			System.out.print(i + "  ");
			if (i<10) {System.out.print(" ");}
		}
		System.out.print("\n----------------------------------------------------------------------\n");		
		for (int i=1; i<=rows; i++) {
			System.out.print("   " + letters[i-1] + " |  ");
			for (int j=1; j<=cols; j++) {
				System.out.print(location[i-1][j-1].state + "   ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	
	//Displays the field (uncensored - shows all the ships regardless if they have been hit or not).
	public void showFilteredField() {
		System.out.print(player.name + "'s Field\n\t");
		for (int i=1; i<=cols; i++) {
			System.out.print(i + "  ");
			if (i<10) {System.out.print(" ");}
		}
		System.out.print("\n----------------------------------------------------------------------\n");		
		for (int i=1; i<=rows; i++) {
			System.out.print("   " + letters[i-1] + " |  ");
			for (int j=1; j<=cols; j++) {
				if (location[i-1][j-1].state == 'x') {
					System.out.print("x   ");
				} else if (location[i-1][j-1].state == 'o') {
					System.out.print("o   ");
				} else if (location[i-1][j-1].state == '.' || Character.isLowerCase(location[i-1][j-1].state)) {
					System.out.print(".   ");
				} else {
					System.out.print(location[i-1][j-1].state + "   ");
				}
				
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
		
	//If a ship is sinking, this method capitalizes the locations' state and "reveals" that ship when showing the field.
	public void revealShip(Ship s) {
		for (int i=1; i<=rows; i++) {
			for (int j=1; j<=cols; j++) {
				if (location[i-1][j-1].ship == s) {
					//location[i-1][j-1].ship = null;
					location[i-1][j-1].state = Character.toUpperCase(s.letter);
				}
			}
		}
	}
	
	
	//Prints a text with the game's features and supported commands.
	private static void help() {
		System.out.println("\nBattleship is a guessing game for two players. It is played on ruled grids on which each player's fleet (battleships) are marked.\n"
				+ "The locations of the ships are concealed from the other player. Players alternate turns calling \"shots\" at the other player's\n"
				+ "ships, and the objective of the game is to destroy the opponent's entire fleet.\n\n"
				+ "In this version, each player's fleet consists of 7 ships, with different lengths and values:\n"
				+ "2 Aircraft Carriers (Length: 5) - 5 Points\n"
				+ "3 Destroyers (Length: 3) - 2 Points\n"
				+ "2 Submarines (Length: 1) - 3 Points\n\n"
				+ "Settings:\n"
				+ "At the start of the game, the size of the grid must be assigned. Both rows and columns must be between 10 and 15.\n"
				+ "In addition, the players must set the number of rounds for the current game. If the players wish to play until the end they must enter '0'.\n"
				+ "Also the game mode must be selected (PvP, PvC, CvC).\n"
				+ "Each player can choose to deploy their ships manually or automatically (obviously a computer-controlled player always deploys automatically).\n"
				+ "In manual deployment, for each ship, the player must choose a location for the front and the direction. The ships can be adjacent to each other\n"
				+ "but they cannot collide or be placed outside of the grid. Each location has a unique letter for its row and a unique number for its column.\n"
				+ "In order to select a location, the player must type the corresponding letter and number (without space in between).\n\n"
				+ "Game:\n"
				+ "The players take turns calling out row and column coordinates on the other player's grid in an attempt to identify a location that contains a ship.\n"
				+ "The selection happens by typing the grid's coordinates (as mentioned above). In each round, both players make a move. After the last round,\n"  
				+ "the player with the most points wins the game. Of course the game can end earlier if someone has destroyed his opponent's entire fleet.\n\n"
				+ "Commands:\n"
				+ "A player can type in a special command instead of taking their turn. These commands are the following:\n"
				+ "Save: Saves the current state of the game in the specified '.txt' file. After that, the current game continues.\n"
				+ "Load: Loads an existing game from the specified '.txt' file. Careful! The loaded game will overwrite the current one!\n"
				+ "Help: Displays the current help text.\n"
				+ "Peek: Displays both players' fields unfiltered (useful for revising)."
				+ "Exit: Closes the game.\n\n"
				+ "Location marks:\n"
				+ ". - This location has not been selected.\n"
				+ "o - Missed shot.\n"
				+ "x - Target was hit (but is not sinking yet).\n"
				+ "A/D/S - The ship is sinking. The letter is the initial of the corresponding ship.\n"
				+ "a/d/s - A ship that has not been hit there.\n");
	} 	
	
	
	//Terminates the program.
	private static void exit() {
		System.out.print("This will end the current game. Are you sure? (Yes/No) ");
		in = new Scanner(System.in);
		String answer = in.next();
		if (answer.charAt(0)=='Y' || answer.charAt(0)=='y') {
			System.exit(0);
		}
	}
}
