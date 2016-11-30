package graphics;

import gameEngine.Character;
import gameServer.GeneralClient;
import gameServer.Server;
import graphics.MouseClick.GAMESTATE;
import graphics.gameStates.GameOver;
import graphics.gameStates.GameState;
import graphics.gameStates.HostGame;
import graphics.gameStates.JoinGame;
import graphics.gameStates.MainMenu;
import graphics.gameStates.PlayGame;
import graphics.gameStates.SelectMap;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * Main class for the game, handles the current game state and renders the
 * screen accordingly
 * 
 *
 */
@SuppressWarnings("serial")
public class RunGame extends Canvas implements Runnable {

	// Size variables for the screen
	public static int width = 1000;
	public static int height = 640;
	private JFrame window;
	// Thread for the client to run on
	private Thread clientThread;
	private boolean running = false;
	// Global game state
	private GameState currentState;
	// FPS limiting
	private long lastTime = System.nanoTime();
	private final double amountOfTicks = 60.0;
	private double ns = 1000000000 / amountOfTicks;
	private double delta = 0;
	private long now = 0;
	// To decide how to setup certain game states
	private boolean isHost = false;
	// Mouse listener object
	private MouseClick control;
	// Buffer strategy for the rendering
	private BufferStrategy strat;
	// To help restart a session
	private ArrayList<GameState> toClose = new ArrayList<GameState>();

	/**
	 * Create a new instance of client, setting the JFrame and the current state
	 */
	public RunGame() {
		setSize(width, height);
		window = new JFrame();
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Sheeps");
		window.add(this);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	public synchronized void restartClient() {
		for (GameState state : toClose) {
			try {
				state.close();
			} catch (Exception e) {

			}
		}
		stopClient();
		startClient();
	}

	/**
	 * Start the client thread
	 */
	public synchronized void startClient() {
		if (control == null) {
			control = new MouseClick(this);
			this.addMouseListener(control);
			this.addMouseMotionListener(control);

		}
		currentState = new MainMenu();
		control.setGameState(GAMESTATE.MAINMENU);
		running = true;
		clientThread = new Thread(this);
		clientThread.start();
	}

	/**
	 * Stop the client & thread
	 */
	@SuppressWarnings("deprecation")
	public synchronized void stopClient() {
		running = false;
		try {
			clientThread.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.exit(1);
	}

	/**
	 * Get the current state
	 * 
	 * @return A GameState object
	 */
	public GameState getCurrentState() {
		return currentState;
	}

	/**
	 * Set the current game state
	 * 
	 * @param currentState
	 *            The game state to switch to
	 */
	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}

	/**
	 * HostGame button was clicked, create a new HostGame object and change
	 * current state
	 */
	public void hostGame(String imagePath) {
		control.setGameState(MouseClick.GAMESTATE.HOSTMENU);
		toClose.add(currentState);
		currentState = new HostGame(this, imagePath);
		isHost = true;
		currentState.start();
	}

	public void selectScreen() {
		control.setGameState(MouseClick.GAMESTATE.SELECTMAP);
		toClose.add(currentState);
		currentState = new SelectMap();
		currentState.start();
	}

	/**
	 * Join game was clicked, create a new JoinGame and change game state (And
	 * start the JoinGame)
	 */
	public void joinGame() {
		toClose.add(currentState);
		currentState = new JoinGame(this);
		currentState.start();
		control.setGameState(MouseClick.GAMESTATE.PLAYGAME);
	}

	/**
	 * PlayGame initiated, change game state and update information accordingly
	 */
	public void playGame(GeneralClient generalClient, String mapPath) {
		toClose.add(currentState);
		control.setGameState(MouseClick.GAMESTATE.PLAYGAME);
		Character[] players = currentState.getPlayers();
		HostGame temp = null;
		if (isHost) {
			temp = (HostGame) currentState;
		}
		currentState = new PlayGame(this, players, generalClient,
				(currentState).getMyID(), mapPath);
		((PlayGame) currentState).setupGame(isHost);
		if (isHost) {
			Server.setAcceptingClients(false);
			temp.startGame(currentState.getPlayers());
		}
		toClose.add(currentState);
	}

	/**
	 * A game has finished display the end game dialog
	 * 
	 * @param msg
	 *            Generally the team number who won
	 */
	public void gameOver(String msg) {
		currentState = new GameOver(msg);
		control.setGameState(MouseClick.GAMESTATE.GAMEOVER);
	}

	public void end() {
		System.exit(1);
	}

	/**
	 * Method to update the game state variables This will in turn affect the
	 * render
	 */
	public void update() {
		currentState.update();
	}

	/**
	 * Method to render the graphics and draw it to the canvas
	 */
	public void render() {
		strat = getBufferStrategy();
		if (strat == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = strat.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(760, 575, 75, 40);
		g.fillRect(760, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("Help", 785, 600);
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(875, 575, 75, 40);
		g.fillRect(875, 575, 75, 40);
		g.setColor(Color.WHITE);
		g.drawString("Exit", 900, 600);
		currentState.render(g);
		
		g.dispose();
		strat.show();
	}

	/**
	 * Run method for the client
	 */
	public void run() {
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				delta--;
				render();
			}
		}
		stopClient();
	}

	/**
	 * Run method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RunGame client = new RunGame();
		client.startClient();
	}

}