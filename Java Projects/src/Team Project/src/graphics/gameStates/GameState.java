package graphics.gameStates;

import gameEngine.Character;

import java.awt.Graphics;

/**
 * Interface for gameStates
 *
 */
public interface GameState {
	
	public void update();
	public void render(Graphics g);
	public void start();
	public Character[] getPlayers();
	public int getMyID();
	public int winningTeam();
	public void close();
	public String getFilePath();

}
