package graphics.gameStates;

import gameEngine.Character;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * GameState to represent the completion of a game
 *
 */
public class GameOver implements GameState {

	private Image background;
	private Font font = new Font("Jokerman", Font.PLAIN, 120);
	private String displayText = "";

	/**
	 * Create a new instance of HostGame Loads in the background image and sets
	 * up the server and client
	 */
	public GameOver(String msg) {
		displayText = msg;
		try {
			background = ImageIO.read(new File("img" + File.separator + "misc" + File.separator
					+ "gameover.png"));
		} catch (IOException e) {
			System.out.println("Error reading background image");
			System.exit(1);
		}


	}
	
	/**
	 * Render to the screen
	 */
	public void render(Graphics g){
		g.drawImage(background, 0, 0,1000,550, null);
		g.setFont(font);
		g.drawString(displayText, 100, 100);
		
	}


	@Override
	public void update() {
		//nothing to update
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((GameOver) obj).displayText.equals(displayText);
		
	}


	@Override
	/**
	 * Nothing to start
	 */
	public void start() {
		
	}


	@Override
	/**
	 * No players in the gameover screen
	 */
	public Character[] getPlayers() {
		return null;
	}


	@Override
	/**
	 * No ID needed
	 */
	public int getMyID() {
		return 0;
	}


	@Override
	/**
	 * This has already been decided by the server
	 */
	public int winningTeam() {
		return 3;
	}


	@Override
	/**
	 * Nothing to close
	 */
	public void close() {
	}


	@Override
	/**
	 * No file name associated. 
	 */
	public String getFilePath() {
		return null;
	}

}
