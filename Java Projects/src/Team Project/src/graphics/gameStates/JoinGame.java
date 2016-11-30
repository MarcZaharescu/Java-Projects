package graphics.gameStates;

import gameEngine.Character;
import gameEngine.tools.Coordinates;
import gameEngine.tools.GlobalMethods;
import gameServer.ServerClient;
import graphics.RunGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * GameState to represent a player who has chosen to join an existing game
 * @author jxr227
 *
 */
public class JoinGame implements GameState {

	// Some variables regarding display
	private Image background;
	private Font font = new Font("Arial", Font.PLAIN, 35);
	
	private final String[] ROLES = new String[]{"Normal", "Attacker", "Healer", "Tank"};
	
	// The players in the current lobby
	private Character[] players = new Character[8];
	
	private RunGame client;
	
	// The serverclient for the current player
	private ServerClient serverClient;

	// Unique identifier for current player
	private int playerID;
	
	private String mapPath;

	/**
	 * Get the player Id for this instance of joingame
	 * 
	 * @return
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Set the player ID for this instance of joingame
	 * 
	 * @param playerID
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public String getFilePath(){
		return mapPath;
	}

	/**
	 * Create a new instance of joingame, Read in the background image
	 */
	public JoinGame(RunGame client) {
		// Try to read in the background image
		this.client = client;
		try {
			background = ImageIO.read(new File("img" + File.separator + "misc" + File.separator
					+ "lobby.png"));
		} catch (IOException e) {
			System.out.println("Error reading background image");
			System.exit(1);
		}

	}

	/**
	 * Start method Sets up the connection and waits for the game to start
	 */
	public void start() {
		// Setup the connection
		String username = GlobalMethods.requestString("Enter username:");
		String ipAddress = GlobalMethods
				.requestString("Enter IP address of server:");
		String role = GlobalMethods.comboBox(ROLES);
		serverClient = new ServerClient(ipAddress, 1234, username, role);		
		serverClient.start();
		

		// Read information recieved from the server
		String update = serverClient.serverUpdates();
		String[] updateDetail = update.split("\t");
		// Check that it's what we want
		if (updateDetail[0].equals("ID")) {
			playerID = Integer.parseInt(updateDetail[1]);
			mapPath = updateDetail[2];
		} else {
			System.out.println("The server didn't send us our ID");
			System.exit(1);
		}

		// Keep recieving information from the server
		// Stopping once "STARTGAME" is recieved
		update = serverClient.serverUpdates();
		updateDetail = update.split("\t");
		while (!updateDetail[0].equals("STARTGAME")) {
			// If players recieved parse the information
			// And update current array of characters accordingly
			if (updateDetail[0].equals("PLAYERS")) {
				players = new Character[8]; // Reset the array incase anyone
											// left
				int counter = 0;
				for (int i = 1; i < updateDetail.length - 2; i += 4) { //For each character
					//Set the player Character
					players[counter] = new Character(Integer.parseInt(updateDetail[i]),
							updateDetail[i + 1], updateDetail[i + 2]);
					//Set the teamNumber
					players[counter].setTeamNumber(Integer.parseInt(updateDetail[i + 3]));
					counter++;
				}

			}
			//Refresh the information from the server
			update = serverClient.serverUpdates();
			updateDetail = update.split("\t");
		}
		
		//"PLAYGAME" has been recieved so parse the information
		//And place the characters as told by the server
		int counter = 0;
		for (int i = 1; i < updateDetail.length - 1; i = i + 2) {
			int x = Integer.parseInt(updateDetail[i]);
			int y = Integer.parseInt(updateDetail[i + 1]);
			Coordinates coord = new Coordinates(x, y);
			players[counter].setCurrCoord(coord);
			counter++;
		}
		client.playGame(serverClient, mapPath);
	}
	
	/**
	 * Return the array of characters associated
	 * with this instance
	 * @return
	 */
	public Character[] getPlayers() {
		return players;
	}
	/**
	 * Nothing to update
	 */
	public void update(){
		
	}
	
	/**
	 * Draw the information to the screen
	 * @param g
	 */
	public void render(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		renderPlayerSlots(g);
	
	}

	/**
	 * Draw the player slots
	 * 
	 * @param g
	 */
	public void renderPlayerSlots(Graphics g) {
		String label;
		int posX = 30;
		int posY;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null)
				label = "Awaiting Player...";
			else
				label = players[i].getPlayerName();

			if ((i+1)%2 == 0)
				posX = 570;
			else
				posX = 60;

			posY = ((i/2 * 100) + 90) + 90;
			g.setColor(Color.WHITE);
			g.drawString(label, posX, posY);
		}
		g.setColor(Color.DARK_GRAY);
	}

	@Override
	/**
	 * Return this players' ID
	 */
	public int getMyID() {
		return playerID;
	}

	@Override
	/**
	 * No winning team at this point,
	 * returns a bogus value
	 */
	public int winningTeam() {
		return 5;
	}

	@Override
	/**
	 * Close this gamestate
	 */
	public void close() {
		serverClient.stop();
	}

	

}
