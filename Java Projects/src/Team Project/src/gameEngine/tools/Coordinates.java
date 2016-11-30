package gameEngine.tools;

import gameEngine.Map;


/**
 * Class that encapsulates the coordinate pair of a character on the map
 * 
 *
 */
public class Coordinates {

	private int xCoord;
	private int yCoord;

	/**
	 * Create a new instance of a pair
	 * of coordinates
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Coordinates(int x, int y) {
		xCoord = x;
		yCoord = y;
	}

	/**
	 * Set the x coordinate for a pair
	 * @param xCoord The desired X coordinate
	 */
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * Set the y coordinate for a pair
	 * @param yCoord The desired Y coordinate
	 */
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	/**
	 * Get the X coordinate for a pair
	 * @return
	 */
	public int getxCoord() {
		return xCoord;
	}

	/**
	 * Get the y coordinate for a pair
	 * @return
	 */
	public int getyCoord() {
		return yCoord;
	}

	/**
	 * Returns true if character is still on the map
	 * 
	 * @return
	 */
	public boolean isInMap(Map bitmap) {
		try {
			bitmap.getCoord(xCoord, yCoord);
			return true;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Override toString so coordinates get printed out nicely
	 */
	@Override
	public String toString() {
		return "X COORD: " + xCoord + " Y COORD: " + yCoord;
	}
	
	/**
	 * Override equals to easily compare 2 coordinates
	 */
	@Override
	public boolean equals(Object coord) {
		Coordinates coordinate = (Coordinates) coord;
		return this.xCoord == coordinate.xCoord
				&& this.yCoord == coordinate.yCoord;
	}
}
