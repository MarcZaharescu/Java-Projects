package gameServer;

import gameEngine.Character;
import gameEngine.ai.AIGame;
import gameEngine.tools.Coordinates;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A client for the AI to connect to the host.(Via localhost)
 * 
 *
 */
public class AIClient implements GeneralClient, Runnable {

	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;
	private Character[] players = new Character[8];
	private int myID;
	private String imagePath;

	/**
	 * Create a new instance of AIClient
	 * 
	 * @param name
	 *            Username (CPU+{int identifier})
	 * @param serverPort
	 *            The port to connect to the server
	 * @param role
	 *            The role of the character associated
	 */
	public AIClient(String name, int serverPort, String role) {
		// Connect to the server and send Character details
		try {
			socket = new Socket("localhost", serverPort);
			start();
			output.writeUTF("CHARACTER\t" + name + "\t" + role);
			output.flush();
			Thread thread = new Thread(this);
			thread.start();
		} catch (UnknownHostException e) {
			System.out.println("Host not valid");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("An AI client failed to write data");
			System.exit(1);
		}
	}

	/**
	 * Handle messages until the game is started
	 */
	public void waitForStartGame() {
		// Read information recieved from the server
		String update = serverUpdates();
		String[] updateDetail = update.split("\t");
		// Check that it's what we want
		if (updateDetail[0].equals("ID")) {
			myID = Integer.parseInt(updateDetail[1]);
			imagePath = updateDetail[2];
		} else {
			System.out
					.println("The server sent something wrong to an AI client.");
			System.exit(1);
		}

		update = serverUpdates();
		updateDetail = update.split("\t");
		while (!updateDetail[0].equals("STARTGAME")) {
			// If players recieved parse the information
			// And update current array of characters accordingly
			if (updateDetail[0].equals("PLAYERS")) {
				players = new Character[8]; // Reset the array incase anyone
											// left
				int counter = 0;
				for (int i = 1; i < updateDetail.length - 2; i += 4) { // For
																		// each
																		// character
					// Set the player Character
					players[counter] = new Character(
							Integer.parseInt(updateDetail[i]),
							updateDetail[i + 1], updateDetail[i + 2]);
					// Set the teamNumber
					players[counter].setTeamNumber(Integer
							.parseInt(updateDetail[i + 3]));
					counter++;
				}
			}
			// Refresh the information from the server
			update = serverUpdates();
			updateDetail = update.split("\t");
		}

		// "PLAYGAME" has been recieved so parse the information
		// And place the characters as told by the server
		int counter = 0;
		for (int i = 1; i < updateDetail.length - 1; i = i + 2) {
			int x = Integer.parseInt(updateDetail[i]);
			int y = Integer.parseInt(updateDetail[i + 1]);
			Coordinates coord = new Coordinates(x, y);
			players[counter].setCurrCoord(coord);
			counter++;
		}
		// Start the AI's game running
		new AIGame(players, this, myID, imagePath);
	}

	/**
	 * Send a Move that the player chose
	 * 
	 * @param ID
	 *            Player ID
	 * @param direction
	 *            the direction they want to move in
	 */
	public void sendMove(int ID, String direction) {
		try {
			output.writeUTF("WALK" + "\t" + ID + "\t" + direction);
			output.flush();
		} catch (IOException e) {
			System.out.println("AI Client Couldn't write move");
			System.exit(1);
		}
	}

	/**
	 * Notify the server that we have dealt with an update
	 */
	public void notifyComplete() {
		try {
			output.writeUTF("DONE");
			output.flush();
		} catch (IOException e) {
			System.out.println("AI Client couldn't contact the server");
			System.exit(1);
		}
	}

	/**
	 * Send the shot that the player chose
	 * 
	 * @param ID
	 *            Player ID
	 * @param weapon
	 *            The weapon name
	 * @param power
	 *            the power value
	 * @param mouseAngle
	 *            the mouseangle
	 */
	public void sendShot(int ID, String weapon, double power, double mouseAngle) {
		try {
			output.writeUTF("SHOOT" + "\t" + ID + "\t" + weapon + "\t" + power
					+ "\t" + mouseAngle);
			output.flush();
		} catch (IOException e) {
			System.out.println("AI Client couldn't send a shot.");
			System.exit(1);
		}
	}

	/**
	 * Get a string server update from the server
	 */
	public String serverUpdates() {
		String msg = "err";
		try {
			msg = input.readUTF();
		} catch (IOException e) {
			System.out
					.println("AI Client couldn't retrieve information from the server");
			System.exit(1);
		}
		return msg;
	}

	/**
	 * Start the input/output streams
	 */
	public void start() {
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		try {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			if (socket != null)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		waitForStartGame();
	}

	@Override
	/**
	 * Send a string to the server
	 */
	public void send(String selectMove) {
		try {
			output.writeUTF(selectMove);
			output.flush();
		} catch (IOException e) {
			System.out.println("Couldn't write move");
			System.exit(1);
		}

	}

}
