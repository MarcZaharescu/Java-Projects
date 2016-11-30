package gameServer;

import gameEngine.Character;
import graphics.RunGame;
import graphics.gameStates.PlayGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

/**
 * Server class to handle connections with all clients.
 *
 */
public class Server implements Runnable {
	private ServerSocket server = null;
	private Thread thread = null;
	private ServerThread clients[] = new ServerThread[8]; // 8 Players maximum
	private static boolean acceptingClients = true;
	private int clientCount = 0;
	private static int currentTeam = 0;
	private static int cpuPlayers = 0;
	private boolean gamePlay = true;
	private RunGame client;
	private String filePath;
	private boolean gameBroken = false;
	
	/**
	 * Create a new instance of a server
	 * 
	 * @param port
	 *            The port to run the server on
	 */
	public Server(int port, RunGame client, String imagePath) {
		filePath = imagePath;
		this.client = client;
		try {
			System.out.println("SERVER: Creating server on port " + port
					+ ", please wait...");
			server = new ServerSocket(port);
			server.setSoTimeout(1000); // Set timeout
			start();
		} catch (Exception e) {
			System.out.println("SERVER: Couldn't start the server."
					+ e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Get the team to allocate next client
	 * 
	 * @return
	 */
	public static int getCurrentTeam() {
		return currentTeam;
	}
	
	/**
	 * Get the next name for a CPU player and increment
	 * it
	 * 
	 * @return
	 */
	public static String nextCPU(){
		return "CPU"+cpuPlayers++;
	}

	/**
	 * Set the team to allocate to the next team
	 */
	public static void setCurrentTeam() {
		if (currentTeam == 0) {
			currentTeam = 1;
		} else if (currentTeam == 1) {
			currentTeam = 0;
		}
	}

	
	/**
	 * Get the file path for the bitmap
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Set the file path for the bitmap
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Set the boolean of whether to accept new clients or not
	 * 
	 * @param state
	 */
	public static void setAcceptingClients(boolean state) {
		acceptingClients = state;
	}

	/**
	 * Return the characters for all connected sockets
	 * 
	 * @return
	 */
	public Character[] getCharacters() {
		Character[] characters = new Character[8];
		for (int i = 0; i < clientCount; i++) {
			characters[i] = clients[i].getCharacter();
		}
		return characters;
	}

	/**
	 * Starts the server and initialises the thread.
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Stops the server
	 */
	@SuppressWarnings("deprecation")
	public void stop() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	/**
	 * Get the clientCount
	 * 
	 * @return
	 */
	public int getClientCount() {
		return clientCount;
	}

	/**
	 * Run method Continuously accepts clients.
	 */
	public void run() {
		while (thread != null && acceptingClients) {
			try {
				addThread(server.accept());
			} catch (SocketTimeoutException e) {
				// Time out, silently fail as this will happen every second.
			} catch (IOException e) {
				System.out.println("SERVER: Error accepting clients");
				System.exit(1);
			}
		} // Once this section is reached the game has started
		System.out.println("No longer accepting clients");
		// Main game loop
		System.out.println("waiting for players to confirm they're ready");
		waitForPlayers();
		while (gamePlay) {
			for (ServerThread _client : clients) {
				if (client.getCurrentState().winningTeam() > 0) {
					handle("ENDGAME\t" + ((PlayGame) client.getCurrentState()).winningTeam());
					gamePlay = false;
					break;
				}
				if(gameBroken){
					handle("ENDGAME\t" + ((PlayGame) client.getCurrentState()).winningTeam());
				}

				if (_client != null) {
					int clientHp = client.getCurrentState().getPlayers()[_client.getCharacter().getPlayerID()].getHp();
					if (clientHp > 0) {
						// Tell all clients who's move it is
						handle("MOVE\t" + _client.getCharacter().getPlayerID());
						// Wait for the client to say what they want to do
						String move = _client.awaitMove();
						String[] moveDetails = move.split("\t");
						// React based on what the clients wants to do
						if (moveDetails[0].equals("SHOOT")) {
							handle(move);
						} else if (moveDetails[0].equals("WALK")) {
							handle(move);
						} else {
							System.out
									.println("Client gave us unknown information");
							// We shouldn't be here, the client sent something
							// incorrectly
						}
						waitForPlayers();
					}
				}
				// Send the information to everyone
			}			
			Random rand = new Random();
			
			int x = rand.nextInt(1000);
			int hp = rand.nextInt(20);
			int c = rand.nextInt(4);
			if(c==0) {
				handle("CRATE\tHpCrate\t"+x+"\t"+(((PlayGame) client.getCurrentState()).getEngine().getBitMap().getLastY(x)-10)+"\t" + hp);
			}
			else if(c==1) {
				handle("CRATE\tWeaponCrate\t"+x+"\t"+(((PlayGame) client.getCurrentState()).getEngine().getBitMap().getLastY(x)-10)+"\t" + hp);
			}
			else if(c==2) {
				handle("CRATE\tDmgBoostCrate\t"+x+"\t"+(((PlayGame) client.getCurrentState()).getEngine().getBitMap().getLastY(x)-10)+"\t" + hp);
			}
			else {
				handle("CRATE\tDmgReductionCrate\t"+x+"\t"+(((PlayGame) client.getCurrentState()).getEngine().getBitMap().getLastY(x)-10)+"\t" + hp);
			}
			
			waitForPlayers();
			double y = (double)rand.nextInt(5);
			double wa = (double)rand.nextInt(361);
			
			y+= rand.nextDouble();
			wa +=rand.nextDouble();
			handle("WIND\t" + y + "\t"+wa);
			
			waitForPlayers();
		}

	}

	/**
	 * Add a thread to the Server
	 * 
	 * @param socket
	 *            given socket
	 */
	public void addThread(Socket socket) {
		if (clientCount < clients.length) {
			System.out.println("SERVER: Client Accepted");
			clients[clientCount] = new ServerThread(this, socket);
			try {
				setupThread();
			} catch (IOException e) {
				System.out.println("SERVER: Failed to add a new server thread");
			}
		} else {
			System.out.println("SERVER: Client refused, too many clients.");
		}
	}

	/**
	 * Wait for all players to confirm
	 * an update has been completed
	 */
	public void waitForPlayers() {
		String msg;
		for (int i = 0; i < clientCount; i++) {
			msg = clients[i].awaitMessage();
			if (!msg.equals("DONE")) {
				System.exit(1);
			}
		}
	}

	/**
	 * Set the thread up, opens the connection, starts the connection and sends
	 * the client the allocated ID. (followed by all players)
	 * 
	 * @throws IOException
	 */
	public void setupThread() throws IOException {
		clients[clientCount].open();
		clients[clientCount].start();
		clients[clientCount].send("ID\t" + clientCount);
		clientCount++;
		sendPlayers();
	}

	/**
	 * Send all of the players in game to all connected clients
	 */
	public void sendPlayers() {
		String msg = "PLAYERS";
		for (ServerThread character : clients) {
			if (character != null) {
				msg = msg + "\t" + character.getCharacter().toString();
			}
		}
		handle(msg);
	}

	/**
	 * Find a client by name
	 * 
	 * @param name
	 * @return
	 */
	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getCharacter().getPlayerID() == ID)
				return i;
		return -1;
	}

	/**
	 * Handle a message from a client
	 * 
	 * @param name
	 *            name of the client sending the object
	 * @param input
	 *            the input being sent
	 */
	public synchronized void handle(String input) {
		for (int i = 0; i < clientCount; i++) {
			if (clients[i] != null) {
				clients[i].send(input);
			}
		}
	}

	/**
	 * Remove a player from the server
	 * 
	 * @param name
	 *            Player name
	 */
	@SuppressWarnings("deprecation")
	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		gameBroken = true;
		if (pos >= 0) {
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID);
			if (pos < clientCount - 1) {
				for (int i = pos + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
				clientCount--;
				try {
					toTerminate.close();
				} catch (IOException e) {
					System.out.println("Couldn't remove client");
				}
				toTerminate.stop();
			}
		}
		System.exit(0);
	}

}
