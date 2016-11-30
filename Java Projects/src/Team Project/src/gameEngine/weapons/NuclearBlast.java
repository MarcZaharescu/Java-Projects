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
 * NuclearBlast Weapon Class
 * 
 *
 */
public class NuclearBlast implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private int squareSize = 20;
	private String name = "NuclearBlast";
	private int damage = 500;
	private int mass = 1;
	private int explosiveRange = 150;
	private int accuracy = 100;
	private String description;
	private String details;

	public NuclearBlast() {
		readImg("img" + File.separator + "weapSprites" + File.separator+ "nuclearblast.png");
		readPanelImg("img" + File.separator + "weapPanel" + File.separator+ "nuclearblastpanel.png");
		description = "Fires a massive parabola missle that destroys the ground and damages nearby enemies";
		details = "NuclearBlast - Fires a massive parabola missile that will be affected by winds.\n"
				+ "Upon hit it destroys the ground with a 150px radius and damages nearby enemies.\n"
				+ "Deals damage to enemies depending on the distance from the blast radius.\n"
				+ "Maximum 500 damage.\n\n";
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
	 * Damage enemies in the blast radius
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
					// destroy the terrain on the radius
					try {
						if (bitMap.getCoord(x, y) != 2
								|| bitMap.getCoord(x, y) != 3) {
							bitMap.setCoord(x, y, 0);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

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
		ArrayList<MiscObject> affectedObject = new ArrayList<MiscObject>();
		for (MiscObject obj : engine.getObjects()) {
			if (isOnCircle(obj.getCurrCoord().getxCoord(), obj.getCurrCoord()
					.getyCoord(), centerX, centerY, radius)
					|| obj.getAllCoordinates().contains(center)) {
				affectedObject.add(obj);
			}
		}

		// hp decrease part
		// just do once
		int teamNumber = active.getTeamNumber();
		// for destroy crates
		if (radius == explosiveRange) {
			engine.getObjects().iterator();
			for (MiscObject obj : affectedObject) {
				if (obj != null) {
					// hp change once only
					obj.setHp((int) (obj.getHp() - (damage)));
				}
			}

		}

		for (Character affectedCharacter : affectedCharacters) {
			int currX = affectedCharacter.getCurrCoord().getxCoord();
			int currY = affectedCharacter.getCurrCoord().getyCoord();
			if (affectedCharacter.isBlocking() == false) {

				double xDis = currX - centerX;
				double yDis = currY - centerY;

				double distance = Math.sqrt(yDis * yDis + xDis * xDis);

				double percentage = (1 - (distance / damage));

				int playerNewHP = affectedCharacter.getHp()
						- (int) Math.round(percentage * damage
								* (1 - affectedCharacter.getDamageRed())
								* (1 + active.getDamageAmp()));

				// this is to avaoid the characters to get decrease hp for
				// second time due to the loop
				if (radius == explosiveRange) { // hp change once only
					if (affectedCharacter.getTeamNumber() != teamNumber
							&& affectedCharacter.getHp() > 0) {
						affectedCharacter.setHp(playerNewHP);
					}
				}

			}
			affectedCharacter.setCurrCoord(new Coordinates(currX, bitMap
					.getFirstY(currX) - characters[0].getRadius()));
			// detected players within radius have to be reinitialised on
			// bitmap

		}
		if (radius == explosiveRange) {
			for (Character affectedCharacter : affectedCharacters) {
				if (affectedCharacter.getFlags().get(3) != null) {
					affectedCharacter.getFlags().get(3).setMovesLeft(0);
					affectedCharacter.setBlocking(false);
				}
			}
		}
		affectedCharacters.clear();
	}

	/**
	 * Animate the shot after it lands
	 * 
	 * @param path
	 *            the path of the shot
	 * @param active
	 *            the active character
	 * @param characters
	 *            the array of characters
	 * @param engine
	 *            the MainEngine instance
	 */
	public void animateExplosion(ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {

		Coordinates coord = path.get(path.size() - 1);
		for (int i = 0; i <= explosiveRange; i += 5) {
			// we keep doing update the ground and character position by
			// increasing the radius size
			updateAfterShot(coord, engine.getBitMap(), characters, i, active, engine);
		}

		// reset shielding flag

		// reset enraged flag
		if (active.getFlags().get(2) != null) {
			active.getFlags().get(2).setMovesLeft(0);
			active.resetModifiers();
		}
	}

	/**
	 * Draw the weapon sprite at specified coordinates.
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

	/**
	 * Test if a point is on the circle (for blast radius)
	 */
	public boolean isOnCircle(int x, int y, int centerX, int centerY, int radius) {
		return Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2) < Math
				.pow(radius, 2);
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

}
