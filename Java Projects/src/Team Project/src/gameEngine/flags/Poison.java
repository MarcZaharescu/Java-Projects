package gameEngine.flags;

/**
 * Class that implements the Poison flag used by the normal class.
 * 
 *
 */
public class Poison implements Flag {

	private static final double damagePerTurn = 100;
	private int numberOfTurns;
	private gameEngine.Character character;

	private String type = "Poison";

	public Poison(int numberOfTurns, gameEngine.Character flaggedChar) {
		this.numberOfTurns = numberOfTurns;
		this.character = flaggedChar;
	}

	@Override
	public int getMovesLeft() {
		return numberOfTurns;
	}

	@Override
	public void setMovesLeft(int moves) {
		return;

	}

	/**
	 * Deal poison damage to the player over time
	 */
	@Override
	public void execute(int scale) {
		this.character.setHp((int) (this.character.getHp() - damagePerTurn
				/ scale));
		numberOfTurns--;
	}

	/**
	 * Returns the type of the flag
	 */
	public String getType() {
		return this.type;
	}

}
