package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * Class for the networking client
 */
public class ServerClient implements GeneralClient {
	private Socket socket = null;
	private DataInputStream input = null;
	private static DataOutputStream output = null;
	private String role;

	/**
	 * Create a new instance of the server client Connects to the given
	 * servername (IP address only, currently)
	 * 
	 * @param serverName
	 *            IP address in string format
	 * @param serverPort
	 *            The port to connect on
	 */
	public ServerClient(String serverName, int serverPort, String name,
			String role) {
		this.role = role;
		System.out.println("CLIENT: Attempting to join server. Hold on ...");
		try {
			socket = new Socket(InetAddress.getByName(serverName), serverPort);
			start();
			output.writeUTF("CHARACTER\t" + name + "\t" + role);
			output.flush();
		} catch (UnknownHostException e) {
			System.out.println("Host not valid");
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null, null, "Connection refused.", JOptionPane.CANCEL_OPTION);
			System.exit(1);
		}
	}

	public String getRole() {
		return role;
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
			System.out.println("Couldn't send a move.");
			System.exit(1);
		}
	}

	/**
	 * Notify the server that we're done updating/animating
	 */
	public void notifyComplete() {
		try {
			output.writeUTF("DONE");
			output.flush();
		} catch (IOException e) {
			JOptionPane.showConfirmDialog(null, null, "Connection lost.", JOptionPane.CANCEL_OPTION);
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
			System.out.println("Failed to send the server our shot");
			System.exit(1);
		}
	}

	/**
	 * Await and return a message from the server
	 * 
	 * @return
	 */
	public String serverUpdates() {
		String msg = "";
		try {
			msg = input.readUTF();
		} catch (IOException e) {
		}
		return msg;
	}

	/**
	 * Start the Client, setup threads
	 */
	public void start() {
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Stop the client
	 */
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
	public void send(String selectMove) {
		
	}



}
