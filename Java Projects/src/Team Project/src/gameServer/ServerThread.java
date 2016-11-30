package gameServer;

import gameEngine.Character;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class for a single connection to the server
 * Handles all communication with the specific
 * user.
 *
 */
public class ServerThread extends Thread {
	private Socket socket = null;
	private Server server = null;
	private Character character = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;

	/**
	 * Create a new serverThread (A client connected to the server
	 * 
	 * @param server
	 *            The server it's using
	 * @param socket
	 *            The connecting socket
	 */
	public ServerThread(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.character = new Character(9999,"Player joining", "NORMAL");
		try {
			open();
			this.character = awaitCharacter();
			this.character.setTeamNumber(Server.getCurrentTeam());
			Server.setCurrentTeam();
			send("ID\t"+server.getClientCount()+"\t"+server.getFilePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Await a message from the server.
	 * @return
	 */
	public String awaitMessage(){
		String msg = "";
		try {
			msg = input.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * Simply waits for a character from the player
	 * @return
	 */
	public Character awaitCharacter(){
		String characterDetails;		
		try {
			characterDetails = input.readUTF();
			String[] details = characterDetails.split("\t");
			if(details[0].equals("CHARACTER"))
				return new Character(server.getClientCount(),details[1], details[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Wait for a move (A message) from a player
	 * @return
	 */
	public String awaitMove(){
		String playerMove = null;	
		try {
			playerMove = input.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return playerMove;	
	}

	/**
	 * Run method
	 * currently empty
	 */
	public void run() {

	}

	/**
	 * Send an object
	 * 
	 * To a single connection
	 * 
	 * @param obj
	 */
	@SuppressWarnings("deprecation")
	public void send(String msg) {
		try{
			output.writeUTF(msg);
			output.flush();
		}catch(IOException e){
			System.out.println("SERVER: Couldn't send a message.");
			System.exit(1);
		}
	}


	/**
	 * Get the character (Player) associated with this connection
	 * 
	 * @return
	 */
	public Character getCharacter() {
		return this.character;
	}

	/**
	 * Set the character (player) for the current connection
	 * 
	 * @param character
	 */
	public void setCharacter(Character character) {
		this.character = character;
	}

	/**
	 * Open the connection
	 * 
	 * @throws IOException
	 */
	public void open() throws IOException {
		input = new DataInputStream(new BufferedInputStream(
				socket.getInputStream()));
		output = new DataOutputStream(new BufferedOutputStream(
				socket.getOutputStream()));
	}

	/**
	 * Close the connection
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (input != null)
			input.close();
	}

}
