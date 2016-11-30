package gameEngine.flags;

/**
 * Class that implements the Healing flag used by the healer.
 * 
 *
 */
public class Healing implements Flag {

	private static final double healingPerTurn = 100;
	private int numberOfTurns;
	private gameEngine.Character character;

	private String type = "Healing";

	public Healing(int numberOfTurns, gameEngine.Character flaggedChar) {
		this.numberOfTurns = numberOfTurns;
		this.character = flaggedChar;
	}

	/*
	 * Get/set the number of turns the flag is active for
	 */
	@Override
	public int getMovesLeft() {
		return numberOfTurns;
	}

	@Override
	public void setMovesLeft(int moves) {
		// TODO Auto-generated method stub

	}

	/**
	 * Set healing over time for a player
	 */
	@Override
	public void execute(int scale) {
		this.character.setHp((int) (this.character.getHp() + healingPerTurn
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
