package battleship;

public class Location {
	public int row;
	public int col;
	public Ship ship;
	public char state; 
	//States: '.' for unmarked, 'o' for missed, 'x' for hit (not sinking), 'A/D/S' for sinking (depending on the ship), 
	//'a/d/s' (only visible in manual deployment, peek mode and final reveal) means that there is a ship but it has not been hit. 
	
	
	//Constructor. Each location represents a 'square' in the player's field.
	public Location(int row, int col){
		this.row = row;
		this.col = col;
		this.state = '.';
	}
	
	
	//Checks if this location has been chosen before. 
	public boolean isMarked() {
		if (this.state == 'x' || this.state == 'o' || Character.isUpperCase(this.state)) {
			return true;
		}
		return false;
	}
}
