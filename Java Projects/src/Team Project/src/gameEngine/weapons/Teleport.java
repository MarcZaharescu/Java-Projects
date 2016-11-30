package gameEngine.weapons;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.objects.MiscObject;
import gameEngine.tools.Coordinates;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Teleport Weapon Class
 * 
 *
 */
public class Teleport implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private int squareSize = 20;
	private String name = "Teleport";
	private int damage = 300;
	private int mass = 1;
	private int explosiveRange = 50;
	private int accuracy = 100;
	private String description;
	private String details;

	public Teleport() {
		readImg("img" + File.separator + "weapSprites" + File.separator + "teleport.png");
		readPanelImg("img" + File.separator + "weapPanel" + File.separator + "teleportpanel.png");
		description = "Fires a parabola missle that will teleport player to landing location.";
		details = "Teleport - Fires a parabola missile that will be affected by winds.\n"
				+ "Upon hit, it teleports the shooting player to landing location.\n"
				+ "This weapon does not deal any damage.\n"
				+ "Be careful u can teleport yourself out of the map\n\n";
	}

	/**
	 * Calculate the path of the shot
	 * 
	 * @param power
	 *            shot power
	 * @param angle
	 *            shot angle
	 * @param currCoord
	 *            current coordinates of active player
	 * @param active
	 *            the active player
	 * @param characters
	 *            the array of characters
	 * @param windSpeed
	 *            the wind speed
	 * @param windAngle
	 *            the wind angle
	 * @param engine
	 *            the MainEngine instance
	 * @return
	 */
	public ArrayList<Coordinates> fire(double power, double angle,
			Coordinates currCoord, Character active, Character[] characters,
			double windSpeed, double windAngle, MainEngine engine) {
		windSpeed = windSpeed/2;

		// to do calculations and set initial details of the shot
		if (power > 100) {
			power = 100;
		}
		Map map = engine.getBitMap();
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		double weaponMass = this.mass;
		double radians = Math.toRadians(angle);
		double initialSpeed = power / weaponMass;
		double initialYSpeed = initialSpeed * Math.sin(radians);
		double initialXSpeed = initialSpeed * Math.cos(radians);
		int initialXCoord = currCoord.getxCoord();
		int initialYCoord = currCoord.getyCoord() - MainEngine.getRadius();
		double windRadian = Math.toRadians(windAngle);
		double windXSpeed = windSpeed * Math.cos(windRadian);
		double windYSpeed = windSpeed * Math.sin(windRadian);

		int xCoord = 0;
		int yCoord = 0;
		final double timeInterval = 0.1;
		double t = 0;// the time will increase every loop to calculate the path
						// and get all coordinates
		int teamNumber = map.getCoord(currCoord.getxCoord(),
				currCoord.getyCoord()) - 2;

		boolean isCollide = false;
		double g = 9.81 + windYSpeed; // y direction acceleration (gravity)
		double a = windXSpeed; // x acceleration

		// get all coords of enemy team players
		ArrayList<Coordinates> enemiesCoord = new ArrayList<Coordinates>();

		for (Character character : characters) {
			if (character != null) {
				if (character.getTeamNumber() != teamNumber
						&& character.getHp() > 0) {
					enemiesCoord.addAll(character.getAllCoordinates());
				}
			}
		}
		// need to detect crates as well
		ArrayList<Coordinates> cratesCoord = new ArrayList<Coordinates>();
		for (MiscObject obj : engine.getObjects()) {
			if (obj != null) {
				cratesCoord.addAll(obj.getAllCoordinates());
			}
		}

		// the loop to calculate all coords along the shooting path
		while (!isCollide) {
			// equations to calculate flying distance of y and x
			// s = ut + 1/2*at^2

			double yDistance = ((initialYSpeed * t) + (0.5 * -g * t * t));
			double xDistance = ((initialXSpeed * t) + (0.5 * a * t * t)); // no
																			// air
																			// friction
			// we can calculate the acceleration due to the wind.

			xCoord = (int) Math.round(initialXCoord + xDistance);
			yCoord = (int) Math.round(initialYCoord - yDistance); // 0 is at top
			Coordinates result = new Coordinates(xCoord, yCoord);
			path.add(result);

			try {
				// if missile hits the ground or enemy players
				if (map.getCoord(xCoord, yCoord) == 1
						|| enemiesCoord
								.contains(new Coordinates(xCoord, yCoord))
						|| cratesCoord
								.contains(new Coordinates(xCoord, yCoord))) {
					isCollide = true;
				} else {
					t = t + timeInterval; // for every 0.1s check it is hit or
											// not
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				if (yCoord < 0) {// if its goes up to the sky keep going
					t += timeInterval;
				} else {
					isCollide = true;
				}
			}
		}

		return path;
	}

	/**
	 * Animate the path of the shot
	 * 
	 * @param fancyMap
	 * @param path
	 * @param active
	 * @param characters
	 * @param engine
	 */
	public void animateShot(BufferedImage fancyMap,
			ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {

		for (int i = 0; i < path.size(); i++) {
			drawWeaponSprite(fancyMap, path.get(i));
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		animateExplosion(path, active, characters, engine);
	}

	/**
	 * Change the location of player that fired the shot, checking for edge
	 * cases.
	 * 
	 * @param center
	 *            the center of the shot
	 * @param bitMap
	 *            the bitmap instance
	 * @param characters
	 *            the array of characters
	 * @param i
	 *            increment to the radius
	 * @param active
	 *            the active character
	 * @param engine
	 */
	public void updateAfterShot(Coordinates center, Map bitMap,
			Character[] characters, int i, Character active, MainEngine engine) {
		int telX = center.getxCoord();

		try {

			int telY = bitMap.getFirstY(telX) - active.getRadius() - 1;
			if (bitMap.getFirstY(telX) == bitMap.getHeight() - 1) {
				active.setHp(0);

			} else {
				active.setCurrCoord(new Coordinates(telX, telY));
			}
		} catch (ArrayIndexOutOfBoundsException e) {

			if (telX < 0) {
				telX = active.getRadius();
				int telY = bitMap.getFirstY(telX) - active.getRadius() - 1;
				if (bitMap.getFirstY(telX) == bitMap.getHeight() - 1) {
					active.setCurrCoord(new Coordinates(telX, telY));
				} else {
					active.setHp(0);
				}
			} else {
				telX = bitMap.getWidth() - active.getRadius();
				int telY = bitMap.getFirstY(telX) - active.getRadius() - 1;
				if (bitMap.getFirstY(telX) == bitMap.getHeight() - 1) {
					active.setCurrCoord(new Coordinates(telX, telY));
				} else {
					active.setHp(0);
				}
			}

		}
	}

	/**
	 * Animate the teleporting.
	 */
	public void animateExplosion(ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {

		Coordinates coord = path.get(path.size() - 1);
		updateAfterShot(coord, engine.getBitMap(), characters, 0, active, engine);

		// reset enraged flag
		if (active.getFlags().get(2) != null) {
			active.getFlags().get(2).setMovesLeft(0);
			active.resetModifiers();
		}
	}

	/**
	 * Draw the weapon sprite at specified coordinates
	 * 
	 * @param fancyMap
	 *            the map
	 * @param coord
	 *            coord of the bullet
	 */
	private void drawWeaponSprite(BufferedImage fancyMap, Coordinates coord) {
		int radius = squareSize / 2;
		for (int i = 0; i < squareSize; i++) {
			for (int j = 0; j < squareSize; j++) {
				int rgb = displaySprite.getRGB(i, j);
				// RGB colour codes for the sprites.
				// This ignores these colours and keeps background
				// transparent

				// transparent code
				int alpha = (rgb >> 24) & 0xff;
				if (alpha != 0) {
					try {
						fancyMap.setRGB(coord.getxCoord() - radius + i,
								coord.getyCoord() - radius + j, rgb);
					} catch (ArrayIndexOutOfBoundsException e) {

					}

				}
			}
		}
	}

	public int getAccuracy() {
		return accuracy;
	}

	public int getDamage() {
		return this.damage;
	}

	public BufferedImage getDisplaySprite() {
		return displaySprite;
	}

	public int getExplosiveRange() {
		return explosiveRange;
	}

	public int getHeal() {
		return 0;
	}

	public double getMass() {
		return mass;
	}

	public String getName() {
		return name;
	}

	public String getWeaponDescription() {

		return description;
	}

	@Override
	public boolean equals(Object weapon) {
		Weapon weaponC = (Weapon) weapon;
		return this.name.equals(weaponC.getName());
	}

	@Override
	public String toString() {
		return name;
	}

	public BufferedImage getPanelSprite() {
		return this.panelSprite;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * read the missile image
	 * 
	 * @param path
	 *            file path
	 */
	public void readImg(String path) {
		try {
			displaySprite = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * read the image to shown on screen
	 * 
	 * @param path
	 *            file path
	 */
	public void readPanelImg(String path) {
		try {
			panelSprite = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
