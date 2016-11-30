package graphics.gameStates;

import gameEngine.Character;

/**
 * Interface for PlayGame and AIGame.
 *
 */
public interface GeneralGame extends Runnable {
	public Character getActiveCharacter();
	public void run();
}
