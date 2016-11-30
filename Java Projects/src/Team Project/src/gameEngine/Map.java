package gameEngine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class that represents the underlying logical representation of the
 * map displayed on the screen: 
 * 0 sky 
 * 1 ground 
 * 2 characters in team 0 
 * 3 characters in team 1 
 * 4 crates/power-ups
 * 
 *
 */
public class Map {

	// the map is a 2d array of specified width and height
	int[][] coord;
	final int width = 1000;
	final int height = 550;

	/**
	 * Create a new instance of Map
	 * with a desired file
	 * @param fileName The file path to
	 * 					the desired bmp file.
	 */
	public Map(String fileName) {
		coord = new int[width][height];
		readImg(fileName);
	}

	/**
	 * Read in an image file that represents the map format Only covers ground
	 * and sky, the rest(characters, objects) is loaded later
	 * 
	 * @param img
	 */
	public void readImg(String img) {
		BufferedImage map;
		try {
			map = ImageIO.read(new File(img));
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (map.getRGB(i, j) == Color.BLACK.getRGB()) {
						coord[i][j] = 1;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the value of a pixel at
	 * position (x,y)
	 * @param x The x coordinate on the file
	 * @param y The y coordinate on the file
	 * @return The value of the specified pixel
	 */
	public int getCoord(int x, int y) {
		return coord[x][y];
	}

	/**
	 * Set the value of a pixel at
	 * position (x,y)
	 * @param x The x coordinate on the file
	 * @param y The y coordinate on the file
	 * @param value The desired value for the 
	 * 					specified pixel
	 */
	public void setCoord(int x, int y, int value) {
		coord[x][y] = value;
	}

	/**
	 * Calculate the first Y value to land on for
	 * a given x position
	 * @param xCoord The x position to check
	 * @return
	 */
	public int getFirstY(int xCoord) {
		for (int j = height - 1; j >= 0; j--) {
			if (getCoord(xCoord, j) != 1) {
				return j;
			}
		}
		return 0;
	}

	/**
	 * Calculate the last Y value to land on
	 * for a given X position
	 * @param xCoord The x position to check
	 * @return
	 */
	public int getLastY(int xCoord) {
		for (int j = 0; j < height; j++) {
			if (getCoord(xCoord, j) == 1) {
				return j - 1;
			}
		}
		return 0;
	}

	/**
	 * Get the height of this file
	 * @return
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Get the width of this file
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}

}
