package battleship;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player{
	private ArrayList<ArrayList<Integer>> targets = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> adjacent;
	
	public ComputerPlayer(int rows, int cols) {
		super(rows, cols);
	}
	
	public ComputerPlayer(int rows, int cols, String name, String[] str) {
		super(rows, cols, name, str);
	}


	//Searches for a valid location to shoot. Initially it fires at random. After a successful hit, the 4 adjacent squares' coordinates
	//are inserted in the ArrayList 'targets'. During its next move, the computer will prioritize these locations as they are more likely
	//to contain another target. Once the 'targets' is empty, it will fire at random again.
	public void move(int rows, int cols, Player opponent) throws InvalidInputException{
		try {
			Thread.sleep(500); //A small delay to simulate computer thinking (unnecessary).
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Random random = new Random();
		int candidateRow, candidateCol;
		boolean flag = true;
		try {
			while (flag) {
				if (targets.isEmpty()) { //Shoot randomly.
					candidateRow = random.nextInt(rows+1);
					candidateCol = random.nextInt(cols+1);
				} else { //Shoot the first candidate target (then remove it).
					candidateRow = targets.get(0).get(0);
					candidateCol = targets.get(0).get(1);
					targets.remove(0);
				}
						
				if (opponent.field.location[candidateRow-1][candidateCol-1].isMarked()) {
					throw new InvalidInputException("Invalid Location");
				} else {
					flag = false;
				}
				System.out.println(name + ": " + super.IntToChar(candidateRow) + candidateCol);
				shoot(candidateRow, candidateCol);
				if (isHit(candidateRow, candidateCol)) { //The shot hit a target.
					updateTargets(candidateRow, candidateCol);
				}
			}		
		} catch (Exception e) {
			throw new InvalidInputException("Invalid Location");
		}
	}
	
	
	//Returns true if the location contains a ship that has been hit there.
	private boolean isHit(int row, int col) {
		return ((opponent.field.location[row-1][col-1].state == 'x') || (Character.isUpperCase(opponent.field.location[row-1][col-1].state)));
	}
	
	
	//After a ship has been hit, all 4 (or less) adjacent slots should be targeted next, if possible.
	//This method fills in the list of next possible targets.
	private void updateTargets(int row, int col) {
		adjacent = new ArrayList<Integer>();
		adjacent.add(row+1);
		adjacent.add(col);
		targets.add(adjacent);
		adjacent = new ArrayList<Integer>();
		adjacent.add(row-1);
		adjacent.add(col);
		targets.add(adjacent);
		adjacent = new ArrayList<Integer>();
		adjacent.add(row);
		adjacent.add(col+1);
		targets.add(adjacent);
		adjacent = new ArrayList<Integer>();
		adjacent.add(row);
		adjacent.add(col-1);
		targets.add(adjacent);
	}	
}
