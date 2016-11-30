package gameEngine.weapons;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.flags.Healing;
import gameEngine.objects.MiscObject;
import gameEngine.tools.Coordinates;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * HealingBuff Weapon Class
 * 
 *
 */
public class HealingBuff implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private int squareSize = 20;
	private String name = "HealingBuff";
	private int damage = 300;
	private int mass = 1;
	private int explosiveRange = 200;
	private int accuracy = 100;
	private String description;
	private String details;

	private double healingAmp = 0;

	public HealingBuff() {
		readImg("img" + File.separator + "weapSprites" + File.separator
				+ "healingbuff.png");
		readPanelImg("img" + File.separator + "weapPanel" + File.separator
				+ "healingbuffpanel.png");
		description = "Fires a parabola missle that heals surrounding players by a set amount every turn";
		details = "HealingBuff - Fires a parabola missle that heals surrounding players by 100 hp every turn.\n"
				+ "The buff lasts for 5 turns. It refreshes when hit by another healing buff.\n\n";
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
		int xCoord = 0;
		int yCoord = 0;
		final double timeInterval = 0.1;
		double t = 0;// the time will increase every loop to calculate the path
						// and get all coordinates
		int teamNumber = map.getCoord(currCoord.getxCoord(),
				currCoord.getyCoord()) - 2;
		double windRadian = Math.toRadians(windAngle);
		double windXSpeed = windSpeed * Math.cos(windRadian);
		double windYSpeed = windSpeed * Math.sin(windRadian);

		boolean isCollide = false;
		double g = 9.81 + windYSpeed; // y direction acceleration (gravity)
		double a = 0 + windXSpeed; // x acceleration

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
		updateAfterShot(path.get(path.size() - 1), engine.getBitMap(),
				characters, explosiveRange, active, engine);
	}

	/**
	 * Add the healing buff to the characters in the blast radius
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
		// Map bitMap = dispy.getBitMap();
		int radius = i;
		int centerX = center.getxCoord();
		int centerY = center.getyCoord();

		int topLeftX = centerX - radius;
		int topLeftY = centerY - radius;

		int bottomRightX = centerX + radius;
		int bottomRightY = centerY + radius;

		ArrayList<Character> affectedCharacters = new ArrayList<Character>(8);
		for (int x = topLeftX; x <= bottomRightX; x++) {
			for (int y = topLeftY; y <= bottomRightY; y++) {
				if (isOnCircle(x, y, centerX, centerY, radius)) {

					// if a character is detected within radius, move it below
					// just outside circle
					for (Character character : characters) {
						if (character != null) {
							Coordinates coords = character.getCurrCoord();
							if (x == coords.getxCoord()
									&& y == coords.getyCoord()) {
								affectedCharacters.add(character);
								// Update character HP according to distance
								// from
								// center of blast??

							}
						}
					}
				}
			}
		}

		int teamNumber = active.getTeamNumber();
		for (Character affectedCharacter : affectedCharacters) {
			if (affectedCharacter.getTeamNumber() == teamNumber) {
				affectedCharacter.addFlag(new Healing(5, affectedCharacter));
			}

		}
		affectedCharacters.clear();
	}

	/**
	 * No explosion.
	 */
	public void animateExplosion(ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {
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

	@Override
	public int getHeal() {
		return 0;
	}

	public double getHealingAmp() {
		return healingAmp;
	}

	public double getMass() {
		return mass;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getWeaponDescription() {

		return description;
	}

	/**
	 * Test if a point is on the circle (for blast radius)
	 */
	public boolean isOnCircle(int x, int y, int centerX, int centerY, int radius) {
		return Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2) < Math
				.pow(radius, 2);
	}

	public void setHealingAmp(double healingAmp) {
		this.healingAmp = healingAmp;
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

	public String getDetails() {
		return details;
	}

	public void readImg(String path) {
		try {
			displaySprite = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readPanelImg(String path) {
		try {
			panelSprite = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public BufferedImage getPanelSprite() {
		return this.panelSprite;
	}

}
