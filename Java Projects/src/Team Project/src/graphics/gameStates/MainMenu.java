package graphics.gameStates;

import gameEngine.Character;
import graphics.RunGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Main menu of game, with options to join or host a game
 *
 */
public class MainMenu implements GameState {

	private String[] menu = { "Join Game", "Host Game", "Help", "Quit" };
	private BufferedImage bg, buttonset;
	private BufferedImage[] button_frames = new BufferedImage[4];
	int joinButtonpos = 0, hostButtonpos = 1, helpButtonpos = 2, quitButtonpos = 3;
	public Rectangle joinR;
	public Rectangle hostR;
	public Rectangle quitR;
	public Rectangle helpR;

	/**
	 * Create a new instance of MainMenu
	 */
	public MainMenu() {
		try {
			buttonset = ImageIO.read(new File("res" + File.separator
					+ "Sprites" + File.separator + "OptionBar_Grid.png"));

			bg = ImageIO.read(new File("res" + File.separator + "Backgrounds"
					+ File.separator + "title_bg.png"));

			for (int i = 0; i < 4; i++) {
				button_frames[i] = buttonset.getSubimage(0, 20 * i, 110, 30);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		joinR = new Rectangle(RunGame.width / 2 - 220, RunGame.height / 2 - 87,
				(int) (button_frames[joinButtonpos].getWidth() * 3.5),
				(int) (button_frames[joinButtonpos].getHeight() * 2.5));
		hostR = new Rectangle(RunGame.width / 2 - 220, RunGame.height / 2 +10,
				(int) (button_frames[hostButtonpos].getWidth() * 3.5),
				(int) (button_frames[hostButtonpos].getHeight() * 2.5));
		helpR = new Rectangle(RunGame.width / 2 - 220, RunGame.height / 2 + 100,
				(int) (button_frames[helpButtonpos].getWidth() * 3.5),
				(int) (button_frames[helpButtonpos].getHeight() * 2.5));
		quitR = new Rectangle(RunGame.width / 2 - 220, RunGame.height / 2 + 190,
				(int) (button_frames[quitButtonpos].getWidth() * 3.5),
				(int) (button_frames[quitButtonpos].getHeight() * 2.5));

	}
	
	
	/**
	 * Render to the screen
	 */
	public void render(Graphics g) {
		//Draw the background
		g.drawImage(bg, 0, 0, RunGame.width, RunGame.height, null);
		
		

		g.setColor(Color.BLACK);
		

	}

	public void update() {
		//Nothing to update in here
	}

	/**
	 * No start method required
	 */
	public void start() {

	}

	/**
	 * No players in the main menu
	 */
	public Character[] getPlayers() {
		return null;
	}

	/**
	 * No player ID at this point
	 */
	public int getMyID() {
		return 0;
	}

	/**
	 * No winning team at the main menu
	 * Return a bogus value
	 */
	public int winningTeam() {
		return 5;
	}


	/**
	 * Nothing to close
	 */
	public void close() {
		
	}


	/**
	 * No file path at main menu
	 */
	public String getFilePath() {
		return null;
	}

}
