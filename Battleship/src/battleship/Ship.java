package battleship;

public abstract class Ship {
	protected int length;
	protected int points;
	protected char letter;
	public char direction;
	public int row, col;
	public String name; //Each ship has a unique name in order to tell it apart from other ships of the same type.
	//private boolean isDamaged = false;
	private int damagePoints = 0; //Basically how many times the ship has been hit.
	
	
	//Constructor-Setter
	public Ship(String name) {
		this.name = name;
	}
	
	
	public void setCoords(int row, int col, char dir) {
		this.row = row;
		this.col = col;
		this.direction = dir;
	}
	
	
	//It is called when a ship has been hit. It prints a message, then checks if it's sinking.
	public void hit() {
		damagePoints++;
		getHitMessage();
		if (isSinking()) {getSinkMessage();}
	}

	
	public boolean isSinking() {
		return (damagePoints == length);
	}
	
	
	//This does NOT give away the type of the ship.
	public void getHitMessage() {
		System.out.println("Hit!");
	}
	
	
	//It gives away the name of the sinking ship.
	public void getSinkMessage() {
		System.out.println("Sink! (" + name + ")");
	}
}
