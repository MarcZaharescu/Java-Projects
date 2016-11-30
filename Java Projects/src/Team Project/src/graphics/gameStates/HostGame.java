package graphics.gameStates;

import gameEngine.Character;
import gameEngine.Map;
import gameEngine.tools.GlobalMethods;
import gameServer.GeneralClient;
import gameServer.Server;
import gameServer.ServerClient;
import graphics.RunGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * GameState to represent a player who has chosen to host a game
 *
 */
public class HostGame implements GameState {
	// Variables for the display
	private Image background;
	private Font font = new Font("Arial", Font.PLAIN, 35);

	// The characters in the lobby
	private Character[] players = new Character[8];

	// Host runs both the server and a client
	private Server server;
	private static GeneralClient client;
	private String imagePath;

	private int myID;

	private final String[] ROLES = new String[] { "Normal", "Attacker",
			"Healer", "Tank" };
	private boolean mapChosen = false;


	/**
	 * Create a new instance of HostGame Loads in the background image and sets
	 * up the server and client
	 */
	public HostGame(RunGame _client, String imagePath) {
		this.imagePath = imagePath;
		try {

			background = ImageIO.read(new File("img" + File.separator + "misc" + File.separator
					+ "lobby.png"));


		} catch (IOException e) {
			System.out.println("Error reading background image");
			System.exit(1);
		}
		server = new Server(1234, _client, imagePath);
		client = new ServerClient("localhost", 1234,
				GlobalMethods.requestString("Enter Username:"),
				GlobalMethods.comboBox(ROLES));

	}
	
	/**
	 * Stop the gamestate
	 */
	public void stop() {
		server.stop();
	}

	/**
	 * Start the gamestate
	 */
	public void start() {
		String update = client.serverUpdates();
		String[] updateDetail = update.split("\t");
		// Check that it's what we want
		if (updateDetail[0].equals("ID")) {
			myID = Integer.parseInt(updateDetail[1]);
		} else {
			System.out.println("The server didn't send us our ID");
			System.exit(1);
		}

	}
	
	/**
	 * Return the host's ID
	 */
	public int getMyID() {
		return myID;
	}

	/**
	 * Start the game from the current host lobby Sends the notification to all
	 * connected players
	 * 
	 * @param characters
	 */
	public void startGame(Character[] characters) {
		String characterDetails = "";
		for (Character character : characters) {
			if (character != null) {
				characterDetails += "\t" + character.getCurrCoord().getxCoord()
						+ "\t" + character.getCurrCoord().getyCoord();
			}
		}
		server.handle("STARTGAME" + characterDetails);

	}

	/**
	 * Get the client used by this hostGame
	 * 
	 * @return
	 */
	public static GeneralClient getClient() {
		return client;
	}

	/**
	 * Get the players in the lobby
	 * 
	 * @return
	 */
	public Character[] getPlayers() {
		return players;
	}

	/**
	 * Method to update information On this screen all that needs to be updated
	 * is the player names that are connected
	 */
	public void update() {
		players = server.getCharacters();
	}

	/**
	 * Draw everything to the screen
	 * 
	 * @param g
	 */
	public void render(Graphics g) {

		g.drawImage(background, 0, 0, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		 InetAddress ip =null;
		  try {
	 
			ip = InetAddress.getLocalHost();
			
	 
		  } catch (UnknownHostException e) {
	 
			e.printStackTrace();
	 
		  }
		  

		renderPlayerSlots(g);
		font = new Font("Arial", Font.PLAIN, 30);
		g.setFont(font);
		g.drawString(ip.getHostAddress(), 370, 120);

	}

	/**
	 * Draw the player slots to the screen
	 * 
	 * @param g
	 */
	public void renderPlayerSlots(Graphics g) {
		String label;
		int posX;
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
		// Start game rect
		g.fillRect(400, 575, 200, 40);
		// Add AI rect
		g.fillRect(50, 575, 200, 40);
		// Text on buttons
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.PLAIN, 14));
		// Start game String
		g.drawString("Start game", 460, 600);
		// Add AI string
		g.drawString("Add CPU", 110, 600);
	}

	@Override
	/**
	 * No winning team at this point
	 */
	public int winningTeam() {
		return 5;
	}

	@Override
	/**
	 * Close connections
	 */
	public void close() {
		client.stop();
		server.stop();

	}

	@Override
	/**
	 * Get the bitmap image file path.
	 */
	public String getFilePath() {
		return imagePath;
	}
	
	/**
	 * Get the actual player count 
	 * (Number of non null players in the array)
	 * @return
	 */
	public int getActualPlayerCount(){
		int counter = 0;
		for(int i = 0; i < players.length; i++){
			if(players[i] != null){
				counter++;
			}
		}
		return counter; 
	}

}
