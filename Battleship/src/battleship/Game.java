package battleship;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
	private static int mode;
	private static int rows;
	private static int cols;
	private static int number_of_rounds;
	private static Scanner in, sc;
	private static Player p1, p2;
	
	
	public static void main(String[] args) throws InvalidInputException {
		System.out.println("Welcome to Battleship!\n------------------------------------------------------------------------------");
		int initMode;
		do {
			initMode = setInitMode();
			if (initMode == 1) { //New Game
				initSettings();
				initPlayers();		
				playGame();
				showResult();
			} else if (initMode == 2){ //Load Game
				loadGame();
			} else { //Help
				help();
			}
			System.out.println();
		} while(initMode!=1 || initMode!=2);
	}
	
	
	//Initializes starting mode (new or existing game).
	private static int setInitMode() {
		in = new Scanner(System.in);
		while (true) {
			System.out.println("1 - New Game\n"
					+ "2 - Load Game\n"
					+ "3 - Help");
			try {
				mode = Integer.parseInt(in.nextLine());
				if (mode>0 && mode<4) {
					return mode;
				} else {
					throw new InvalidInputException("Invalid mode");
				}
			} catch (Exception e) {
				System.out.println("\nPlease select a valid mode (1-2-3)\n");
			}
		}
	}
	
	
	//Creates the players.
	private static void initPlayers() {
		System.out.println("------------------------------------------------------------------------------");
		setMode();
		if (mode == 1) {
			p1 = new HumanPlayer(rows, cols);
			p2 = new HumanPlayer(rows, cols);
		} else if (mode == 2) {
			p1 = new HumanPlayer(rows, cols);
			p2 = new ComputerPlayer(rows, cols);
			p2.setName("CPU");
		} else {
			p1 = new ComputerPlayer(rows, cols);
			p2 = new ComputerPlayer(rows, cols);
			p1.setName("CPU1");
			p2.setName("CPU2");
		}
		p1.setOpponent(p2);
		p2.setOpponent(p1);
	}
	

	//Initializes the number of rows, columns and rounds in the new game.
	private static void initSettings() {
		in = new Scanner(System.in);
		System.out.println("Settings:\nSelect number of rows and columns (each between 10 and 15 inclusive)");
		setRows();
		System.out.println("------------------------------------------------------------------------------");
		setCols();
		System.out.println("------------------------------------------------------------------------------");
		setRounds();
	}
	
	
	//Sets the number of rows (between 10 and 15).
	private static void setRows() {
		while (true) {
			System.out.print("Rows: ");
			try {
				rows = Integer.parseInt(in.nextLine());
				if (rows>=10 && rows<=15) {
					break;
				} else {
					throw new InvalidInputException("Invalid rows");
				}
			} catch (Exception e) {
				System.out.println("Row number must be an integer between 10 and 15 (inclusive)!\n");
			}
		}	
	}
	
	
	//Sets the number of columns (between 10 and 15).
	private static void setCols() {
		while (true) {
			System.out.print("Columns: ");
			try {
				cols = Integer.parseInt(in.nextLine());
				if (cols>=10 && cols<=15) {
					break;
				} else {
					throw new InvalidInputException("Invalid columns");
				}
			} catch (Exception e) {
				System.out.println("Column number must be an integer between 10 and 15 (inclusive)!\n");
			}
		}	
	}
	
	
	//Sets the number of rounds. If '0' is inserted, the game continues until there is a winner. 
	private static void setRounds() {
		while (true) {
			System.out.print("Select number of rounds (0 to play until the end): ");
			try {
				number_of_rounds = Integer.parseInt(in.nextLine());
				if (number_of_rounds == 0) {
					number_of_rounds = rows*cols; //Worst case scenario: Someone will have to shoot every single slot.
					break;
				} else if (number_of_rounds > 0) {
					break;
				} else {
					throw new InvalidInputException("Invalid rounds");
				}
			} catch (Exception e) {
				System.out.println("Round number must be a positive integer\n");
			}
		}	
	}
	
	
	//Sets the game mode (Player vs Player, Player vs Computer, Computer vs Computer).
	private static void setMode() {
		while (true) {
			System.out.println("Select mode:\n1 - Player vs Player\n"
					+ "2 - Player vs Computer\n"
					+ "3 - Computer vs Computer");
			try {
				mode = Integer.parseInt(in.nextLine());
				if (mode>0 && mode<4) {
					break;
				} else {
					throw new InvalidInputException("Invalid mode");
				}
			} catch (Exception e) {
				System.out.println("\nPlease select a valid mode (1-2-3)\n");
			}
		}
	}
	
	
	//Executes the game procedure. The game ends after each player has made number_of_rounds moves or whenever someone has sunk his opponent's last ship.
	private static void playGame() {
		showFields();
		for (int i=0; i<number_of_rounds; i++) {
			System.out.println("***********************************************************************************************\nRound " + (i+1) + ":\n");
			play(p1);
			if (p1.hasWon()) {showResult();}
			showFields();
			play(p2);
			if (p2.hasWon()) {showResult();}
			showFields();
		}
	}

	
	//Tries to execute a single move (automatic or manual depending on the player) until a valid one has been found.
	private static void play(Player p) {
		here: do {
			try {
				p.move(rows, cols, p.opponent);
				break here;
			} catch (MoveIsCommandException e){ 
				executeCommand(e.getMessage());
			} catch (InvalidInputException e) {
				if (p instanceof HumanPlayer) {
					System.out.println("Invalid location. Try again.");
				}
			}
		} while(true);
	}
	
	
	//Executes the given special command (Help-Peek-Save-Load-Exit).
	private static void executeCommand(String command) {
		if (command.equals("help")) {
			help();
		} else if (command.equals("peek")) {
			p1.field.showField();
			p2.field.showField();
		} else if (command.equals("save")) {
			saveGame();
		} else if (command.equals("load")) {
			loadGame();
		} else if (command.equals("exit")) {
			exit();
		} else {
			System.out.println("Unrecognized Command");
		}
	}
	
		
	//Saves the current state of the game in a txt file (board size, rounds, player's names and moves, ships' coordinates).
	private static void saveGame() {
		in = new Scanner(System.in);
		String stringbuilder;
		System.out.print("Select file to open (without .txt). You can also 'cancel' or 'exit': ");
		String answer = in.next();
		if (answer.toLowerCase().equals("cancel")) {return;}
		if (answer.toLowerCase().equals("exit")) {exit();}
		try {
			PrintWriter outputstream = new PrintWriter(answer + ".txt");
			String instance1 = (p1 instanceof HumanPlayer) ? "h" : "c";
			String instance2 = (p2 instanceof HumanPlayer) ? "h" : "c";
			stringbuilder = rows + " " + cols + " " + number_of_rounds + "\n"  +
					p1.name + " " + instance1 + "\n" + shipCoords(p1) + "\n" + p1.history + "\n" +
					p2.name + " " + instance2 + "\n" + shipCoords(p2) + "\n" + p2.history;	
			outputstream.println(stringbuilder);
			System.out.println("Saved!");
			outputstream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	//Reads a txt file and creates a game with the given parameters.
	private static void loadGame() {
		in = new Scanner(System.in);
		System.out.print("Select file to open (without .txt). You can also 'cancel' or 'exit': ");
		String answer = in.next();
		if (answer.toLowerCase().equals("cancel")) {return;}
		if (answer.toLowerCase().equals("exit")) {exit();}
		try {
			File file = new File(answer + ".txt");
			sc = new Scanner(file);
			//Settings
			rows = sc.nextInt();
			cols = sc.nextInt();
			number_of_rounds = sc.nextInt();
			if (number_of_rounds==0) {number_of_rounds = rows*cols;}
			
			//Player 1
			String name = sc.next();
			String instance = sc.next();
			String[] str = new String[21];			
			for (int i=0; i<21; i++) {
				str[i] = sc.next();
			}
			sc.nextLine();
			String history1 = sc.nextLine();			
			List<String> historyList1;
			if (history1.equals("")) {
				historyList1 = new ArrayList<String>();
			} else {
				historyList1 = new ArrayList<String>(Arrays.asList(history1.split("\\s")));
			}		
			if (instance.equals("h")){
				p1 = new HumanPlayer(rows, cols, name, str);
			} else {
				p1 = new ComputerPlayer(rows, cols, name, str);
			}
			
			//Player 2
			name = sc.next();
			instance = sc.next();
			for (int i=0; i<21; i++) {
				str[i] = sc.next();
			}
			sc.nextLine();
			String history2 = sc.nextLine();
			sc.close();
			List<String> historyList2;
			if (history2.equals("")) {
				historyList2 = new ArrayList<String>();
			} else {
				historyList2 = new ArrayList<String>(Arrays.asList(history2.split("\\s")));
			}
			if (instance.equals("h")){
				p2 = new HumanPlayer(rows, cols, name, str);
			} else {
				p2 = new ComputerPlayer(rows, cols, name, str);
			}
			
			//Setting each other's new opponent
			p1.setOpponent(p2);
			p2.setOpponent(p1);
			
			if (!history1.equals("")) {p1.recreateGame(historyList1);}
			if (!history2.equals("")) {p2.recreateGame(historyList2);}
			
			showFields();
			//If Player 2 chose to save the game, then the current round has not been completed (p1 has made one additional move).
			if ((historyList1.size() > historyList2.size())) {
				play(p2);
				showFields();
			}

			//Resumes the game.
			for (int i=0; i<(number_of_rounds-historyList1.size()); i++) {
				System.out.println("***********************************************************************************************\nRound " + (i+historyList1.size()+1) + ":\n");
				play(p1);
				showFields();
				play(p2);
				showFields();
			}
			showResult();
			System.exit(0);
		} catch (Exception e) {
			System.out.println("The game could not be loaded. Please try again.");
		}
		
	}
	
	
	//Creates a string with the location of all ships for the given player (for saveGame).
	private static String shipCoords(Player p) {
		return p.field.A1.row + " " + p.field.A1.col + " " + p.field.A1.direction + "\n" +
				p.field.A2.row + " " + p.field.A2.col + " " + p.field.A2.direction + "\n" +
				p.field.D1.row + " " + p.field.D1.col + " " + p.field.D1.direction + "\n" +
				p.field.D2.row + " " + p.field.D2.col + " " + p.field.D2.direction + "\n" +
				p.field.D3.row + " " + p.field.D3.col + " " + p.field.D3.direction + "\n" +
				p.field.S1.row + " " + p.field.S1.col + " " + p.field.S1.direction + "\n" +
				p.field.S2.row + " " + p.field.S2.col + " " + p.field.S2.direction;
	}
	
	
	//Prints both fields (filtered).
	private static void showFields() {
		p1.field.showFilteredField();
		p2.field.showFilteredField();
		System.out.println(p1.name + "'s points: " + p1.score);
		System.out.println(p2.name + "'s points: " + p2.score + "\n");
	}
	
	
	//After the final round, it shows both fields (without filter) and finds the winner. 
	private static void showResult() {
		System.out.println("***********************************************************************************************\nResults:\n");
		p1.field.showField();
		p2.field.showField();
		System.out.println(p1.name + ": " + p1.score + " points");
		System.out.println(p2.name + ": " + p2.score + " points\n");
		if (p1.score > p2.score) {
			System.out.println(p1.name + " Won!");
		} else if (p1.score < p2.score) {
			System.out.println(p2.name + " Won!");
		} else {
			System.out.println("Draw!");
		}
		System.exit(0);
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
		in = new Scanner(System.in);
		System.out.print("This will end the current game. Are you sure? (Yes/No) ");
		String answer = in.next();
		if (answer.charAt(0)=='Y' || answer.charAt(0)=='y') {
			System.exit(0);
		}
	}
}
