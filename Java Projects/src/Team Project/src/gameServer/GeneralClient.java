package gameServer;
/**
 * Interface for ServerClient and AIClient
 *
 */
public interface GeneralClient{
	
	/**
	 * Send a move(left/right) to the server
	 * @param ID Player ID
	 * @param direction The direction to move
	 */
	public void sendMove(int ID, String direction);
	
	/**
	 * Notify the server of completion (such as 
	 * updates)
	 */
	public void notifyComplete();
	
	/**
	 * Send a shot to the server
	 * @param ID Player ID
	 * @param weapon The weapon
	 * @param power The power of the shout
	 * @param mouseAngle The calculated mous angle of the shot
	 */
	public void sendShot(int ID, String weapon, double power,
			double mouseAngle);
	
	/**
	 * Wait for the server to send an update
	 * @return The entire string of the update
	 */
	public String serverUpdates();
	
	/**
	 * Start the client (connections)
	 */
	public void start();
	
	/**
	 * Stop the client (connections)
	 */
	public void stop();

	/**
	 * Send a string to the server
	 * @param selectMove
	 */
	public void send(String selectMove);
	
	
}
