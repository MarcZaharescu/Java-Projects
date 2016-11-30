package graphics.gameStates;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.tools.Coordinates;
import gameEngine.tools.GlobalMethods;
import gameEngine.weapons.SkipTurn;
import gameEngine.weapons.Weapon;
import gameServer.GeneralClient;
import gameServer.ServerClient;
import graphics.RunGame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * main gameplay screen, map at top, panel at bottom with pause, change weapon,
 * game controls etc.
 * 
 * @author B4 (we have all worked on different methods of this class)
 */
public class PlayGame implements GeneralGame, GameState {

	// The engine, used to draw the screen images
	private MainEngine engine;
	private BufferedImage map;
	private BufferedImage shotArrow;
	private Image weaponImg;

	// The characters associated with the game
	private Character[] characters;
	private Character activeCharacter;
	private Character myCharacter;
	private int myID;
	// The client (server client) associated
	private GeneralClient client;
	// The Client (class that created this)
	private RunGame gameClient;
	public static boolean helpActive;
	public static boolean weapInfo;
	public static boolean leftInfo;
	public static boolean rightInfo;
	public static boolean skipInfo;
	public static boolean fireInfo;
	private double mouseAngle; // /initial
	private double power;// initial
	private static double currWindSpeed = 3.0;
	private static double currWindAngle = 0.0;
	private boolean gameRunning = true;
	private Thread thread;
	private boolean myMoveComplete = false;
	private List<String> events;

	/**
	 * Get the current mouse angle
	 * 
	 * @return
	 */
	public double getMouseAngle() {
		return mouseAngle;
	}

	/**
	 * Start method, nothing required
	 */
	public void start() {

	}

	/**
	 * Set the current mouse angle.
	 * 
	 * @param mouseAngle
	 */
	public void setMouseAngle(double mouseAngle) {
		this.mouseAngle = mouseAngle;
	}

	/**
	 * Get the current power
	 * 
	 * @return
	 */
	public double getPower() {
		return power;
	}

	/**
	 * Set the current power.
	 * 
	 * @param power
	 */
	public void setPower(double power) {
		this.power = power;
	}

	/**
	 * Get the network client for this playgame
	 * 
	 * @return
	 */
	public GeneralClient getClient() {
		return client;
	}

	/**
	 * Check whether the playters move is complete
	 * 
	 * @return
	 */
	public boolean getMyMoveComplete() {
		return myMoveComplete;
	}

	/**
	 * After clicking fire, to ensure no duplicate moves can be made
	 * 
	 * @param myMoveComplete
	 */
	public void setMyMoveComplete(boolean myMoveComplete) {
		this.myMoveComplete = myMoveComplete;
	}

	/**
	 * Return id for the current instance
	 * 
	 * @return
	 */
	public int getMyID() {
		return myID;
	}

	/**
	 * Return the winning team.
	 */
	public int winningTeam() {
		int team1Hp = 0;
		int team2Hp = 0;
		for (Character character : characters) {
			if (character != null) {
				if (character.getTeamNumber() % 2 == 0) {
					team1Hp += character.getHp();
				} else if (character.getTeamNumber() % 2 == 1) {
					team2Hp += character.getHp();
				}
			}
		}
		if (team1Hp <= 0)
			return 2;
		if (team2Hp <= 0)
			return 1;
		return 0;
	}

	/**
	 * Returns whether it is your player's turn or not
	 * 
	 * @return true/false as to whether it is your turn to play
	 */
	public boolean getMyTurn() {
		return activeCharacter.equals(this.myCharacter);
	}

	/**
	 * Get the active character
	 */
	public Character getActiveCharacter() {
		return activeCharacter;
	}

	/**
	 * Set the active character
	 * 
	 * @param activeCharacter
	 */
	public void setActiveCharacter(Character activeCharacter) {
		this.activeCharacter = activeCharacter;
	}

	/**
	 * Constructor for the PlayGame class
	 * 
	 * @param players
	 *            array of Characters in the game
	 * @param generalClient
	 *            ServerClient object
	 * @param myID
	 *            player ID
	 */
	public PlayGame(RunGame gameClient, Character[] players,
			GeneralClient generalClient, int myID, String mapPath) {

		this.gameClient = gameClient;
		characters = players;
		myCharacter = characters[myID];
		activeCharacter = characters[0];
		this.client = generalClient;
		try {
			Map mappy = new Map(mapPath);
			engine = new MainEngine(characters, mappy, this);


			shotArrow = ImageIO.read(new File("img" + File.separator + "misc" + File.separator 
					+ "firearrow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread = new Thread(this);
		thread.start();
		mouseAngle = 45;
		power = 70;
		if (myCharacter.getTeamNumber() == 1) {
			mouseAngle = 135;
		}
		events = new ArrayList<String>();

	}

	/**
	 * Run method, await information from the server and act on it
	 */
	public void run() {
		client.notifyComplete();
		String update = client.serverUpdates();
		String[] updateDetails = update.split("\t");

		// Discard unneeded information
		while (!updateDetails[0].equals("MOVE")) {
			update = client.serverUpdates();
			updateDetails = update.split("\t");
		}
		// While the game is still active
		while (gameRunning && !Thread.currentThread().isInterrupted()) {
			myMoveComplete = false;
			if (handleHeader(updateDetails)) {
				update = client.serverUpdates();
				updateDetails = update.split("\t");

				if (updateDetails[0].equals("SHOOT")) {
					handleShoot(updateDetails);
				} else if (updateDetails[0].equals("WALK")) {
					handleWalk(updateDetails);
				}
				int scale = 0;
				for (Character chara : characters) {
					if (chara != null) {
						scale++;
					}
				}
				for (Character chara : characters) {
					if (chara != null) {
						chara.dealWithFlags(scale);
					}
				}
			}
			client.notifyComplete(); // Notify the server we've finished
			update = client.serverUpdates();
			updateDetails = update.split("\t");
		}
	}

	/**
	 * Handle a header message from the server
	 * 
	 * @param details
	 * @return
	 */
	private boolean handleHeader(String[] details) {
		if (details[0].equals("MOVE")) {
			activeCharacter = characters[Integer.parseInt(details[1])];
			return true;
		} else if (details[0].equals("ENDGAME")) {
			gameClient.gameOver("Team " + details[1] + " wins!");
			gameRunning = false;
			return false;
		} else if (details[0].equals("CRATE")) {
			engine.animateCrateDrop(Integer.parseInt(details[2]),
					Integer.parseInt(details[3]), details[1],
					Integer.parseInt(details[4]));
			return false;
		} else if (details[0].equals("WIND")) {
			this.setCurrWindSpeed(Double.parseDouble(details[1]));
			this.setCurrWindAngle(Double.parseDouble(details[2]));
			return false;
		} else { // Somehow the messaging loop messed up
			System.out.println("Wrong information: " + details[0]);
			System.exit(1);
		}
		return false;
	}

	/**
	 * Add an event for the activity log
	 * 
	 * @param event
	 */
	public void addEvent(String event) {
		if (this.events.size() == 3) {
			this.events.remove(0);
		}
		this.events.add(event);
	}

	/**
	 * Handle a shot for a player
	 * 
	 * @param details
	 */
	private void handleShoot(String[] details) {
		// Character player = characters[Integer.parseInt(details[1])];
		Weapon wep = GlobalMethods.getWeapon(details[2]);
		activeCharacter.setCurrWeapon(wep);
		Double power = Double.parseDouble(details[3]);
		Double mouseAngle = Double.parseDouble(details[4]);
		if (wep.getName() != "SkipTurn") {

			ArrayList<Coordinates> path = activeCharacter.fire(power,
					mouseAngle, engine.getCharacters(), currWindSpeed,
					currWindAngle, engine);

			addEvent(activeCharacter.getPlayerName() + " fired "
					+ activeCharacter.getCurrWeapon().getName() + "!");

			engine.animateShot(wep, path);
		} else {
			addEvent(activeCharacter.getPlayerName() + " skipped their turn!");
			engine.animateShot(new SkipTurn(), null);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle walking for a player
	 * 
	 * @param details
	 */
	private void handleWalk(String[] details) {
		if (details[2].equals("LEFT")) {
			engine.animateMoveLeft(activeCharacter.getPlayerID());

		} else if (details[2].equals("RIGHT")) {
			engine.animateMoveRight(activeCharacter.getPlayerID());
		}
		addEvent(activeCharacter.getPlayerName() + " moved "
				+ details[2].toLowerCase() + "!");

		Set<String> toAdd = engine.getActiveCharacter().getPickedCrates();
		for (String event : toAdd) {
			addEvent(event);
		}

		engine.getActiveCharacter().resetCrates();

	}

	/**
	 * Return the engine associated with this game
	 * 
	 * @return
	 */
	public MainEngine getEngine() {
		return engine;
	}

	/**
	 * Setup a game
	 * 
	 * @param isHost
	 *            whether the current client is the host or not
	 */
	public void setupGame(boolean isHost) {
		if (isHost) {
			engine.placeCharacters();
		}
		characters = engine.getCharacters();
		engine.loadImage();
	}

	/**
	 * Return the character array associated with the current instance
	 * 
	 * @return array of Characters
	 */
	public Character[] getCharacters() {
		return characters;
	}

	/**
	 * Draw everything to the screen
	 * 
	 * @param g
	 */
	public void render(Graphics g) {
		map = engine.getFancyMap();
		g.drawImage(map, 0, 0, null);
		g.setColor(Color.BLACK);

		renderPanel(g);
		renderInfo(g);
		renderShotPower(g);
		renderLog(g);
		renderFireArrow(g);
		renderWindArrow(g);

	}

	/**
	 * Render the activity log
	 * 
	 * @param g
	 */
	private void renderLog(Graphics g) {

		g.setColor(Color.WHITE);

		g.drawRoundRect(20, 5, 300, 25, 50, 50);
		g.fillRoundRect(20, 5, 300, 25, 50, 50);
	
		g.drawRoundRect(20, 35, 300, 100, 50, 50);
		g.fillRoundRect(20, 35, 300, 100, 50, 50);

		g.setColor(Color.BLACK);
		g.drawString(activeCharacter.getPlayerName() + "'s turn", 35, 20);

		for (int i = 0; i < events.size(); i++) {

			this.drawString(g, events.get(i), 35, 50 + i * 40, 270);
		}

		g.setColor(Color.BLACK);

	}

	/**
	 * Render the tooltip when hovering over a button
	 * 
	 * @param g
	 */
	private void renderInfo(Graphics g) {
		if (weapInfo) {
			g.setColor(Color.DARK_GRAY);
			g.drawRect(470, 460, 250, 90);
			g.fillRect(470, 460, 250, 90);
			g.setColor(Color.WHITE);
			this.drawString(g, getActiveCharacter().getCurrWeapon()
					.getWeaponDescription(), 480, 485, 220);
		}
		if (skipInfo) {
			// 410, 575
			g.setColor(Color.DARK_GRAY);
			g.drawRect(310, 510, 250, 50);
			g.fillRect(310, 510, 250, 50);
			g.setColor(Color.WHITE);
			this.drawString(g, "Do nothing and skip your turn.", 320, 535, 220);
		}

		if (leftInfo) {
			g.setColor(Color.DARK_GRAY);
			g.drawRect(10, 510, 250, 50);
			g.fillRect(10, 510, 250, 50);
			g.setColor(Color.WHITE);
			this.drawString(g,
					"Moves left by a set distance.   Lose your turn to shoot.",
					20, 535, 220);
		}

		if (rightInfo) {
			g.setColor(Color.DARK_GRAY);
			g.drawRect(85, 510, 250, 50);
			g.fillRect(85, 510, 250, 50);
			g.setColor(Color.WHITE);
			this.drawString(g,
					"Moves right by a set distance. Lose your turn to shoot.",
					95, 535, 220);
		}

		if (fireInfo) {
			g.setColor(Color.RED);
			g.drawRect(200, 510, 250, 50);
			g.fillRect(200, 510, 250, 50);
			g.setColor(Color.WHITE);
			this.drawString(g, "Fire your carefully aimed shot!", 210, 535, 220);
		}

	}

	/**
	 * Update the information of the current game
	 */
	public void update() {
		characters = engine.getCharacters();
		engine.loadImage();
	}

	/**
	 * render the firing arrow
	 * 
	 * @param g
	 */
	private void renderFireArrow(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (myCharacter.equals(activeCharacter)&&myCharacter.getHp()>0) {
			int currX = activeCharacter.getCurrCoord().getxCoord();
			int currY = activeCharacter.getCurrCoord().getyCoord();
			double rotationRequired = -Math.toRadians(mouseAngle);
			g2d.rotate(rotationRequired, currX, currY);
			g2d.drawImage(shotArrow, currX, currY - 9, null);
			g2d.rotate(-rotationRequired, currX, currY);
		}
	}

	/**
	 * render the wind angle
	 */
	private void renderWindArrow(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawString("Wind Power: "
				+ String.valueOf(currWindSpeed).substring(0, 3), 900, 70);
		double rotationRequired = -Math.toRadians(currWindAngle);
		g2d.rotate(rotationRequired, 940 + shotArrow.getWidth() / 2,
				30 + shotArrow.getHeight() / 2);
		g2d.drawImage(shotArrow, 940, 30, null);
		g2d.rotate(-rotationRequired, 940 + shotArrow.getWidth() / 2,
				30 + shotArrow.getHeight() / 2);
	}

	/**
	 * render the shot power
	 * 
	 * @param g
	 */
	private void renderShotPower(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(635, 575, 100, 40);
		int powerLength = (int) Math.round(power);
		if (powerLength > 100) {
			powerLength = 100;
		}
		// change the color depends on power
		if (powerLength > 0 && powerLength <= 30) {
			g.setColor(Color.RED);
		} else if (powerLength > 30 && powerLength <= 70) {
			g.setColor(Color.yellow);
		} else if (powerLength > 70) {
			g.setColor(Color.green);
		}
		g.fillRect(635, 576, powerLength, 39);
		// line borders
		g.setColor(Color.BLACK);
		for (int i = 0; i <= 100; i += 20) {
			g.drawLine(635 + i, 575, 635 + i, 575 + 40);

		}

		g.drawString("Shot Power", 655, 630);
	}

	/**
	 * Code taken from coobird on stackoverflow
	 * http://stackoverflow.com/questions
	 * /400566/full-justification-with-a-java-graphics-drawstring-replacement
	 * 
	 * Essentially just wraps text drawn on the screen
	 * 
	 * @param g
	 * @param s
	 * @param x
	 * @param y
	 * @param width
	 */
	public void drawString(Graphics g, String s, int x, int y, int width) {
		// FontMetrics gives us information about the width,
		// height, etc. of the current Graphics object's Font.
		FontMetrics fm = g.getFontMetrics();

		int lineHeight = fm.getHeight();

		int curX = x;
		int curY = y;

		String[] words = s.split(" ");

		for (String word : words) {
			// Find out thw width of the word.
			int wordWidth = fm.stringWidth(word + " ");

			// If text exceeds the width, then move to next line.
			if (curX + wordWidth >= x + width) {
				curY += lineHeight;
				curX = x;
			}

			g.drawString(word, curX, curY);

			// Move over to the right for next word.
			curX += wordWidth;
		}
	}

	/**
	 * Render the game panel at the bottom of the screen
	 * 
	 * @param g
	 */
	private void renderPanel(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 550, 1000, 640);
		
		//Move left
		g.setColor(Color.DARK_GRAY);
		g.drawRect(50, 575, 75, 40);
		g.fillRect(50, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("Left", 75, 600);

		//Move right
		g.setColor(Color.DARK_GRAY);
		g.drawRect(165, 575, 75, 40);
		g.fillRect(165, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("Right", 185, 600);

		//Fire missile
		g.setColor(Color.RED);
		g.drawRect(280, 575, 75, 40);
		g.fillRect(280, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("FIRE", 305, 600);

		//Skip turn
		g.setColor(Color.DARK_GRAY);
		g.drawRect(395, 575, 75, 40);
		g.fillRect(395, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("SKIP", 420, 600);

		//Weapon select left
		g.setColor(Color.DARK_GRAY);
		g.drawRect(510, 575, 20, 40);
		g.fillRect(510, 575, 20, 40);
		g.setColor(Color.WHITE);
		g.drawString("<", 515, 600);

		//Choose weapon label
		g.setColor(Color.BLACK);
		g.drawString("Choose Weapon", 513, 630);
		g.drawString(engine.getActiveCharacter().getCurrWeapon().toString(),
				513, 570);

		//Current weapon
		weaponImg = activeCharacter.getCurrWeapon().getPanelSprite();
		g.drawImage(weaponImg, 550, 580, null);

		//Weapon select right
		g.setColor(Color.DARK_GRAY);
		g.drawRect(595, 575, 20, 40);
		g.fillRect(595, 575, 20, 40);
		g.setColor(Color.WHITE);
		g.drawString(">", 600, 600);

		//Help
		g.setColor(Color.DARK_GRAY);
		g.drawRect(760, 575, 75, 40);
		g.fillRect(760, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("Help", 785, 600);

		//Exit
		g.setColor(Color.DARK_GRAY);
		g.drawRect(875, 575, 75, 40);
		g.fillRect(875, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("Exit", 900, 600);
	}

	/**
	 * Return the players in the game
	 */
	public Character[] getPlayers() {
		return characters;
	}

	@SuppressWarnings("deprecation")
	/**
	 * Close the gamestate
	 */
	public void close() {
		thread.interrupt();
		thread.stop();
		thread = null;
		client.stop();
	}

	/**
	 * File path already in use.
	 */
	public String getFilePath() {
		return null;
	}

	/**
	 * Get the current wind speed
	 * 
	 * @return
	 */
	public static double getCurrWindSpeed() {
		return currWindSpeed;
	}

	/**
	 * Set the wind speed
	 * 
	 * @param currWindSpeed
	 */
	public void setCurrWindSpeed(double currWindSpeed) {
		this.currWindSpeed = currWindSpeed;
	}

	/**
	 * Get the current wind angle
	 * 
	 * @return
	 */
	public static double getCurrWindAngle() {
		return currWindAngle;
	}

	/**
	 * Set the wind angle
	 * 
	 * @param currWindAngle
	 */
	public void setCurrWindAngle(double currWindAngle) {
		this.currWindAngle = currWindAngle;
	}
	
	

}
