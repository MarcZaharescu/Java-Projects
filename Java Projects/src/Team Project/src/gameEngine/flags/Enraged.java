package gameEngine.flags;

/**
 * Class that implements the Enraged flag used by the attacker.
 * 
 *
 */
public class Enraged implements Flag {

	private static final double damageAmp = 1;
	private int numberOfTurns = 3;
	private gameEngine.Character character;

	public static String type = "Enraged";

	public Enraged(gameEngine.Character flaggedChar) {
		this.character = flaggedChar;
	}

	/**
	 * Get the number of turns left
	 * for this flag to remain active
	 */
	@Override
	public int getMovesLeft() {
		return numberOfTurns;
	}

	/**
	 * Set the number of turns left
	 * for this flag to remain active
	 * @param moves The number to update to
	 */
	public void setMovesLeft(int moves) {
		this.numberOfTurns = moves;
	}

	/**
	 * Increase damage of the player up to a certain limit
	 * @param scale  the scale to apply
	 */
	@Override
	public void execute(int scale) {
		if (character.getDamageAmp() < 1.5) {
			this.character.setDamageAmp((1 + character.getDamageAmp())
					* (1 + damageAmp) - 1);
		}
	}

	/**
	 * Returns the type of the flag
	 */
	public String getType() {
		return Enraged.type;
	}

}
