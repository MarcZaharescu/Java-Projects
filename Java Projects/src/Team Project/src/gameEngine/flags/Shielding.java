package gameEngine.flags;

/**
 * Class that implements the Shielding flag used by the tank
 * 
 *
 */
public class Shielding implements Flag {

	private int numberOfTurns = 3;
	private gameEngine.Character character;

	public static String type = "Shielding";

	public Shielding(gameEngine.Character flaggedChar) {
		this.character = flaggedChar;
	}

	/*
	 * Get/set the number of turns the flag is active for
	 */
	@Override
	public int getMovesLeft() {
		return numberOfTurns;
	}

	public void setMovesLeft(int moves) {
		this.numberOfTurns = moves;
	}

	/**
	 * Set the character to block the shot
	 */
	@Override
	public void execute(int scale) {
		character.setBlocking(true);
	}

	/**
	 * Returns the type of the flag
	 */
	public String getType() {
		return Shielding.type;
	}

}
