package battleship;

//Handles the different types of special commands a (human) player can enter, such as help, save, exit, etc.
public class MoveIsCommandException extends Exception{
	private static final long serialVersionUID = 1L;

	public MoveIsCommandException(String message) {
		super(message);
	}
}
