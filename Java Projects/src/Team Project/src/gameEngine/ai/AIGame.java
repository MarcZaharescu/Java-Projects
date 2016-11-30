package gameEngine.ai;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.tools.Coordinates;
import gameEngine.tools.GlobalMethods;
import gameEngine.weapons.SkipTurn;
import gameEngine.weapons.Weapon;
import gameServer.GeneralClient;
import graphics.gameStates.GeneralGame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * AI player class
 * 
 *
 */
public class AIGame implements GeneralGame {
	private MainEngine engine;
	private Character[] characters;
	private Character activeCharacter;
	private Character myCharacter;
	private GeneralClient client;
	private Thread thread;
	private boolean gameRunning = true;
	private static double currWindSpeed = 3.0;
	private static double currWindAngle = 0.0;

	/**
	 * Create a new AIGame
	 * 
	 * @param players
	 *            Players in the game
	 * @param generalClient
	 *            The client connected to this instance
	 * @param myID The AI's Identification
	 * @param mapPath The image path for the map being used
	 */
	public AIGame(Character[] players, GeneralClient generalClient, int myID,
			String mapPath) {
		this.client = generalClient;
		characters = players;
		myCharacter = characters[myID];
		activeCharacter = characters[0];
		try {
			Map mappy = new Map(mapPath);
			engine = new MainEngine(characters, mappy, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread = new Thread(this);
		thread.start();
	}

	@Override
	/**
	 * Run method, handles the gameloop
	 * updates information and sends the
	 * AI move when requested.
	 */
	public void run() {
		client.notifyComplete();
		String update = client.serverUpdates();
		String[] updateDetails = update.split("\t");

		// Discard of unneeded information
		while (!updateDetails[0].equals("MOVE")) {
			update = client.serverUpdates();
			updateDetails = update.split("\t");
		}

		// While the game is still active
		while (gameRunning) {
			
			if (handleHeader(updateDetails)) {
				if (activeCharacter.equals(myCharacter)) {
					calcBestMove();
				} 
				// Refresh the information
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
			// And refresh information from the server
			update = client.serverUpdates();
			updateDetails = update.split("\t");
		}
	}

	/**
	 * Calculate the best move for a given engine and character
	 */
	public void calcBestMove() {
		client.send(AITools.selectMove(engine, myCharacter));
	}

	/**
	 * Get the active character for this game.
	 */
	public Character getActiveCharacter() {
		return activeCharacter;
	}

	/**
	 * Handle the header message recieved from the client
	 * 
	 * @param details
	 *            The full string represented as an array of information split
	 *            by tabs
	 * @return Whether
	 */
	private boolean handleHeader(String[] details) {
		if (details[0].equals("MOVE")) {
			activeCharacter = characters[Integer.parseInt(details[1])];
			return true;
		} else if (details[0].equals("ENDGAME")) {
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
	 * If the client has recieved a "shoot" action "Animate" the shot, which
	 * updates all character references in the engine
	 * 
	 * @param details
	 */
	private void handleShoot(String[] details) {
		characters = engine.getCharacters();
		// Character player = characters[Integer.parseInt(details[1])];
		Weapon wep = GlobalMethods.getWeapon(details[2]);
		activeCharacter.setCurrWeapon(wep);
		System.out
				.println("Current weapon of active player: " + wep.toString());
		Double power = Double.parseDouble(details[3]);
		Double mouseAngle = Double.parseDouble(details[4]);

		if (wep.getName() != "SkipTurn") {
			ArrayList<Coordinates> path = activeCharacter.fire(power, mouseAngle,
					engine.getCharacters(), currWindSpeed, currWindAngle, engine);
			engine.animateShot(wep, path);
		}else {
			engine.animateShot(new SkipTurn(), null);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * If the client has recieved a "walk" action "animate" (Just moves it) the
	 * sheep to update position.
	 * 
	 * @param details
	 */
	private void handleWalk(String[] details) {
		if (details[2].equals("LEFT")) {
			engine.animateMoveLeft(activeCharacter.getPlayerID());
		} else if (details[2].equals("RIGHT")) {
			engine.animateMoveRight(activeCharacter.getPlayerID());
		}
	}

	/**
	 * Get the current wind speed
	 * 
	 * @return The current wind value
	 */
	public static double getCurrWindSpeed() {
		return currWindSpeed;
	}

	/**
	 * Set the current wind speed
	 * 
	 * @param currWindSpeed
	 *            The new wind speed to set
	 */
	public void setCurrWindSpeed(double currWindSpeed) {
		AIGame.currWindSpeed = currWindSpeed;
	}

	/**
	 * Get the current wind angle
	 * 
	 * @return The wind angle
	 */
	public static double getCurrWindAngle() {
		return currWindAngle;
	}

	/**
	 * Set the wind angle
	 * 
	 * @param currWindAngle
	 *            The new wind angle
	 */
	public void setCurrWindAngle(double currWindAngle) {
		AIGame.currWindAngle = currWindAngle;
	}
}
