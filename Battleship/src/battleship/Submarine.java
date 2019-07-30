package battleship;

public class Submarine extends Ship {
	public Submarine(String name) {
		super(name);
		length = 1;
		points = 3;
		letter = 's';
	}
}
