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
 * Rocket Launcher Weapon Class
 * 
 *
 */
public class RocketLauncher implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private String name = "RocketLauncher";
	private int damage = 210;
	private int accuracy = 100;
	private int heal = 0;
	private double mass = 0.1;
	private String details;

	public RocketLauncher() {
		readImg("img" + File.separator + "weapSprites" + File.separator+ "rocketlaucher.png");
		readPanelImg("img" + File.separator + "weapPanel" + File.separator+ "rocketlaucherpanel.png");
		details = "RocketLauncher - Fires a straight line missile that can pass through anything.\n"
				+ "Upon hitting an enemy, it deals 150 damage and knocks them back.\n\n";
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
			double ws, double wa, MainEngine engine) {
		Map map = engine.getBitMap();
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		power = 100;
		double weaponMass = this.mass;
		double radians = Math.toRadians(angle);
		double initialSpeed = power / weaponMass;
		double initialYSpeed = initialSpeed * Math.sin(radians);
		double initialXSpeed = initialSpeed * Math.cos(radians);
		boolean isCollide = false;
		int initialXCoord = currCoord.getxCoord();
		int initialYCoord = currCoord.getyCoord();
		int xCoord = 0;
		int yCoord = 0;
		final double timeInterval = 0.005;
		double t = 0;
		int teamNumber = map.getCoord(currCoord.getxCoord(),
				currCoord.getyCoord()) - 2;

		ArrayList<Coordinates> enemiesCoord = new ArrayList<Coordinates>();
		for (Character character : characters) {
			if (character != null) {
				if (character.getTeamNumber() != teamNumber
						&& character.getHp() > 0) {
					enemiesCoord.addAll(character.getAllCoordinates());
				}
			}
		}

		while (!isCollide) {
			double yDistance = (initialYSpeed * t);
			double xDistance = (initialXSpeed * t);
			xCoord = (int) Math.round(initialXCoord + xDistance);
			yCoord = (int) Math.round(initialYCoord - yDistance);
			Coordinates result = new Coordinates(xCoord, yCoord);
			if (result.isInMap(engine.getBitMap())) {
				if (enemiesCoord.contains(new Coordinates(xCoord, yCoord))) {// hits
																				// //
																				// enemies
					isCollide = true;

				} else {
					t = t + timeInterval;
				}
				path.add(result);
			} else {
				isCollide = true;
			}
		}
		return path;
	}

	private boolean isHit(Coordinates hit, Character character) {
		return character.getAllCoordinates().contains(hit);
	}

	/**
	 * Damage the affected players
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
		int teamNumber = active.getTeamNumber();
		for (Character character : characters) {
			if (character != null) {
				if (isHit(center, character)
						&& character.getTeamNumber() != teamNumber) {

					if (character.isBlocking() == false) {
						character
								.setHp(character.getHp()
										- (int) Math.round((double) (this.damage
												* (1 - character.getDamageRed()) * (1 + active
												.getDamageAmp()))));

					} else if (character.isBlocking() == true) {
						if (character.getFlags().get(3) != null) {
							character.getFlags().get(3).setMovesLeft(0);
							character.setBlocking(false);
						}
						break;
					}
				}
			}
		}
		if (active.getFlags().get(2) != null) {
			active.getFlags().get(2).setMovesLeft(0);
			active.resetModifiers();
		}

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
		ArrayList<Coordinates> cratesCoord = new ArrayList<Coordinates>();
		for (MiscObject obj : engine.getObjects()) {
			if (obj != null) {
				cratesCoord.addAll(obj.getAllCoordinates());
			}
		}
		for (int i = 0; i < path.size(); i += 2) {
			Coordinates coord = path.get(i);
			drawWeaponSprite(fancyMap, coord);
			// destroyAllCoords(coord);
			// set the destroy crates to hp 0
			for (MiscObject obj : engine.getObjects()) {
				if (obj != null) {
					if (obj.getAllCoordinates().contains(coord)) {
						obj.setHp(obj.getHp() - this.damage);
					}
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (path.size() > 35) {
				i += 1;
			}
		}
		updateAfterShot(path.get(path.size() - 1), engine.getBitMap(), characters,
				0, active, engine);
		animateExplosion(path, active, characters, engine);
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
		int radius = displaySprite.getHeight() / 2;
		for (int i = 0; i < radius * 2; i++) {
			for (int j = 0; j < radius * 2; j++) {
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

	/**
	 * Animate the knockback after it hits the enemy
	 * 
	 * @param path
	 *            firing path
	 * @param active
	 *            active character
	 * @param characters
	 *            all characters
	 * @param engine
	 *            the MainEngine instance
	 */
	@Override
	public void animateExplosion(ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {

		// calculate the flying angle

		Character getHit = null;
		Coordinates pCoord;
		Coordinates coord;
		if (path.size() < 2) {
			pCoord = path.get(0);
			coord = path.get(0);
		} else {
			pCoord = path.get(path.size() - 2);
			coord = path.get(path.size() - 1);
		}
		double yDistance = coord.getyCoord() - pCoord.getyCoord();
		double xDistance = pCoord.getxCoord() - coord.getxCoord();

		double missleAngle = Math.toDegrees((double) Math
				.atan((double) yDistance / (double) xDistance));
		double flyingAngle = 0;

		if (xDistance < 0) {
			missleAngle = missleAngle + 180;
		}
		if (xDistance > 0 && yDistance < 0) {
			missleAngle = missleAngle + 360;
		}
		flyingAngle = 180 - missleAngle;

		for (Character character : characters) {
			if (character != null)
				if (isHit(coord, character))
					getHit = character;
		}

		if (getHit != null) {
			ArrayList<Coordinates> flyingPath = flyingPath(50, flyingAngle,
					getHit.getCurrCoord(), active, engine);
			flyingloop: for (int i = 0; i < flyingPath.size(); i += 3) {
				Coordinates coordinates = flyingPath.get(i);

				try {
					for (MiscObject obj : engine.getObjects()) {// check for crates
															// while moveing

						ArrayList<Coordinates> objCoords = obj
								.getAllCoordinates();
						allCoordLoop: for (Coordinates allCoord : getHit
								.getAllCoordinates()) {

							if (objCoords.contains(allCoord)) {
								int action = obj.updateAfterCrate(getHit, engine);
								if (action == 0) {// if hits wall
									// fall
									int yCoord = coordinates.getyCoord();
									int xCoord = coordinates.getxCoord();
									int radius = getHit.getRadius();
									for (int y = yCoord; y <= engine.getBitMap()
											.getFirstY(xCoord) - radius; y++) {
										getHit.setCurrCoord(new Coordinates(
												xCoord, y));
									}
									break flyingloop;
								} else {
									obj.setHp(0);
									break allCoordLoop;
								}
							}
						}
					}
					getHit.setCurrCoord(coordinates);
				} catch (ArrayIndexOutOfBoundsException e) {
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Calculate knock back path
	 * 
	 * @param power
	 *            power similar thing as firing method
	 * @param angle
	 *            angle
	 * @param currCoord
	 *            coordinate of the player
	 * @param active
	 *            active player 
	 * @param engine the MainEngine instance
	 * @return
	 */
	private ArrayList<Coordinates> flyingPath(double power, double angle,
			Coordinates currCoord, Character active, MainEngine engine) {

		// to do calculations and set initial details of the shot
		Map map = engine.getBitMap();
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		double weaponMass = 1;
		double radians = Math.toRadians(angle);
		double initialSpeed = power / weaponMass;
		double initialYSpeed = initialSpeed * Math.sin(radians);
		double initialXSpeed = initialSpeed * Math.cos(radians);
		int initialXCoord = currCoord.getxCoord();
		int initialYCoord = currCoord.getyCoord() - MainEngine.getRadius();
		int xCoord = 0;
		int yCoord = 0;
		final double timeInterval = 0.02;
		double t = 0;// the time will increase every loop to calculate the path
						// and get all coordinates
		boolean isCollide = false;
		double g = 9.81; // y direction acceleration (gravity)
		double a = 0; // x acceleration

		// get all coords of enemy team players

		// the loop to calculate all coords along the shooting path
		int radius = active.getRadius();
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
				if (map.getCoord(xCoord, yCoord + active.getRadius()) == 1
						|| xCoord < radius
						|| xCoord > engine.getBitMap().getWidth() - radius) {
					isCollide = true;
					if (xCoord > engine.getBitMap().getWidth()) { // hits
																// right
						xCoord = engine.getBitMap().getWidth() - 1 - (radius + 1);
					} else if (xCoord < 0) {// hits left
						xCoord = radius + 1;
					}
					for (int y = yCoord; y <= engine.getBitMap().getFirstY(xCoord)
							- radius; y++) {
						path.add(new Coordinates(xCoord, y));
					}
				} else {
					t = t + timeInterval;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				if (yCoord < 0) {
					t = t + timeInterval;
				} else {
					isCollide = true;
				}
			}
		}
		return path;
	}

	@Override
	public boolean equals(Object weapon) {
		Weapon weaponC = (Weapon) weapon;
		return this.name.equals(weaponC.getName());
	}

	@Override
	public String getWeaponDescription() {
		return "Fires a rocket in a straight line, and knock back hitted enemies. Rocket can pass through everything.";
	}

	@Override
	public String toString() {
		return "RocketLauncher";
	}

	public String getDetails() {
		return details;
	}

	@Override
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

	@Override
	public BufferedImage getDisplaySprite() {
		return displaySprite;
	}

	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public double getMass() {
		return 0;
	}

	@Override
	public int getHeal() {
		return heal;
	}

	@Override
	public int getExplosiveRange() {
		return 0;
	}

	@Override
	public int getAccuracy() {
		return accuracy;
	}

	@Override
	public String getName() {
		return name;
	}
}
