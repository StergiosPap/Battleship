package battleship;

import java.util.List;
import java.util.Scanner;

public abstract class Player {
	public String name = "Player"; //Default name
	public int score; //Each time the player sinks an opposing ship, he/she gains points depending on the type.
	public Field field;
	private final static int totalScore = 22; //Max score for sinking all the ships. When a player reaches that score he/she wins the game.
	private static int player_number = 0; 
	private Scanner in;
	public Player opponent;
	public String history; //It contains the valid moves that have been made by this player in this game. Necessary for saving/loading the game.
	
	
	//Constructor for a new player.
	public Player(int rows, int cols)  {
		player_number++;
		if (this instanceof HumanPlayer) {setName();}
		field = new Field(rows, cols, this);
		score = 0;
		history = "";
	}
	
	
	//Constructor for a loaded player. "coords" contains the coordinates for the ships (starting location and direction). 
	public Player(int rows, int cols, String name, String[] coords/*, String history*/) {
		setName(name);
		score = 0;
		field = new Field(rows, cols, this, coords);
	}
	
	
	//Setters and getters.
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setOpponent(Player p) {
		this.opponent = p;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setName() {
		in = new Scanner(System.in);
		System.out.println("------------------------------------------------------------------------------");
		while (true) {
			System.out.println("Name for Player " + player_number + ": ");
			name = in.nextLine();
			if ((name == null ? 0 : name.replaceAll("[^ ]", "").length()) != 0) {
				System.out.println("Name cannot contain spaces\n");
			} else if (name.equals("")) {
				System.out.println("Name cannot be empty\n");
			} else {
				break;
			}
		}
	}
	
	public String getHistory() {
		return history;
	}
	
	
	public abstract void move(int rows, int cols, Player opponent) throws InvalidInputException, MoveIsCommandException;

	
	//Checks if a player has reached the maximum total score.
	public boolean hasWon() {
		return (score == totalScore);
	}
	
	
	//Necessary while loading a game. After placing the ships, this method will repeat every valid move that has been made by this player.
	public void recreateGame(List<String> history) {
		for (int i=0; i<history.size(); i++) {
			char rowchar = history.get(i).charAt(0);
			int row = CharToInt(rowchar);
			int col = Integer.parseInt(history.get(i).substring(1, history.get(i).length()));
			shoot(row, col);
		}		
	}
	
	
	//Takes a pair of valid coordinates and "shoots" at that location on the opposing field.
	public void shoot (int row, int col) {
		Ship target = opponent.field.location[row-1][col-1].ship;	
		if (target != null) {
			target.hit();
			if (target.isSinking()) {
				opponent.field.revealShip(target);
				score += target.points;
				System.out.println("Score: " + score + "\n");
			} else {
				opponent.field.location[row-1][col-1].state = 'x';
			}
		} else {
			opponent.field.location[row-1][col-1].state = 'o';
		}
		history += String.valueOf(IntToChar(row)) + col + " ";
	}
	
	
	//Translates an integer to the corresponding letter.
	public char IntToChar(int x) {
		char[] letters = {'A','B','C','D','E','F','G','H','I','K','L','M','N','O','P'};
		try {
			return letters[x-1];
		} catch (IndexOutOfBoundsException e) {
			return '0';
		}
	}
	
	//Translates a letter to the corresponding character.
	public int CharToInt(char s) {
		char[] lettersL = {'a','b','c','d','e','f','g','h','i','k','l','m','n','o','p'};
		char[] lettersC = {'A','B','C','D','E','F','G','H','I','K','L','M','N','O','P'};
		for (int i=0; i<lettersC.length; i++) {
			if (s == lettersC[i] || s == lettersL[i]) {
				return i+1;
			}
		}
		return 0;
	}
}