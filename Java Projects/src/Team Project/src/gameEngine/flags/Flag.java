package gameEngine.flags;

/**
 * Interface that encapsulates the functionality for the 4 flags
 *
 */
public interface Flag {

	/**
	 * Get the number of turns the flag is active for
	 * @return
	 */
	public int getMovesLeft();
	
	
	/**
	 * Change the number of turns the flag is active for
	 * @param moves
	 */
	public void setMovesLeft(int moves);

	/**
	 * Makes the flag have its effect on the player
	 * @param scale
	 */
	public void execute(int scale);

	
	/**
	 * Get the type of flag that is used
	 * @return
	 */
	public String getType();

	

}
