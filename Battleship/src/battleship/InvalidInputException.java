package battleship;

//Handles the cases where the inserted command is invalid. For example: Unrecognized location, out of bounds, already hit, etc.
public class InvalidInputException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidInputException(String message) {
		super(message);
	}
}
