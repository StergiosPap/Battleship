package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class HumanPlayer extends Player{
	private Scanner in;
	private final String[] commands = {"help", "save", "load", "peek", "exit"};

	public HumanPlayer(int rows, int cols) {
		super(rows, cols);
	}
	
	
	public HumanPlayer(int rows, int cols, String name, String[] str) {
		super(rows, cols, name, str);
	}
	
	
	//Reads a move (or command) from the keyboard and executes it.
	public void move(int rows, int cols, Player opponent) throws InvalidInputException, MoveIsCommandException{
		in = new Scanner(System.in);
		try {
			System.out.print(name + " - Select location to shoot ");
			String answer = in.nextLine();
			
			//Checks for special command
			if (Arrays.asList(commands).contains(answer.toLowerCase())) {
				throw new MoveIsCommandException(answer.toLowerCase());
			}
			
			char rowchar = answer.charAt(0);
			int row = super.CharToInt(rowchar);
			int col = Integer.parseInt(answer.substring(1, answer.length()));
			
			
			//If already marked or out of bounds throw exception, else shoot!
			if (opponent.field.location[row-1][col-1].isMarked() || row>rows || col>cols) {throw new InvalidInputException("Invalid Location");}	
			shoot(row, col);
		} catch (MoveIsCommandException e) {
			throw new MoveIsCommandException(e.getMessage());
		} catch (Exception e) {
			throw new InvalidInputException("Invalid Location");
		}	
	}
}
