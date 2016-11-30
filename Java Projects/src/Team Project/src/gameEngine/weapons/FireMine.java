package gameEngine.weapons;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.objects.Mine;
import gameEngine.objects.MiscObject;
import gameEngine.tools.Coordinates;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * FireMine weapon class
 * 
 *
 */
public class FireMine implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private String name = "FireMine";
	private int squareSize = 20;
	private String description;
	private String details;
	private double mass = 1.2;
	private int damage = 500;

	public FireMine() {
		readImg("img" + File.separator + "weapSprites" + File.separator
				+ "firemine.png");
		readPanelImg("img" + File.separator + "weapPanel" + File.separator
				+ "fireminepanel.png");
		description = "Plant a mine on the ground. Enemies that pass thorugh the mine will be knocked back and lose HP.";
		details = "FireMine - Plant a mine on the ground.\n"
				+ "Enemies that pass thorugh the mine will be knocked back and lose HP.\n"
				+ "Mine deals 500 damage in total.\n\n";
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
	 * Draw weapon sprite at specified coordinates
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

	/**
	 * Place the mine on the ground at the right coordinates
	 */
	public void updateAfterShot(Coordinates center, Map bitMap,
			Character[] characters, int i, Character active, MainEngine engine) {
		// TODO Auto-generated method stub
		try {
			engine.getObjects().add(
					new Mine(center.getxCoord(), center.getyCoord() - 10));
		} catch (ArrayIndexOutOfBoundsException e) {
			// if projectile falls off the map, don't do anything.
		}

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
		updateAfterShot(path.get(path.size() - 1), engine.getBitMap(),
				characters, 0, active, engine);
	}

	/**
	 * animate the knock back after it hits enemy
	 * 
	 * @param path
	 *            firing path
	 * @param active
	 *            active character
	 * @param characters
	 *            all characters
	 */
	public void animateKnockBack(Character active, MainEngine engine,
			double angle) {

		// calculate the flying angle
		ArrayList<Coordinates> path = flyingPath(30, angle,
				active.getCurrCoord(), active, engine);
		Coordinates pCoord = path.get(path.size() - 2);
		Coordinates coord = path.get(path.size() - 1);

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

		if (active != null) {
			ArrayList<Coordinates> flyingPath = flyingPath(50, flyingAngle,
					active.getCurrCoord(), active, engine);
			flyingloop: for (int i = 0; i < flyingPath.size(); i += 3) {
				Coordinates coordinates = flyingPath.get(i);

				try {
					for (MiscObject obj : engine.getObjects()) {// check for
																// crates
						// while moveing

						ArrayList<Coordinates> objCoords = obj
								.getAllCoordinates();
						allCoordLoop: for (Coordinates allCoord : active
								.getAllCoordinates()) {

							if (objCoords.contains(allCoord)) {
								int action = obj.updateAfterCrate(active,
										engine);
								if (action == 0) {// if hits wall
									// fall
									int yCoord = coordinates.getyCoord();
									int xCoord = coordinates.getxCoord();
									int radius = active.getRadius();
									for (int y = yCoord; y <= engine
											.getBitMap().getFirstY(xCoord)
											- radius; y++) {
										active.setCurrCoord(new Coordinates(
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
					active.setCurrCoord(coordinates);
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
	 * the knock back path when a player is knocked back
	 * 
	 * @param power
	 *            power similar thing as firing method
	 * @param angle
	 *            angle
	 * @param currCoord
	 *            coordinate of the player
	 * @param active
	 *            active player
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
						xCoord = engine.getBitMap().getWidth() - 1
								- (radius + 1);
					} else if (xCoord < 0) {// hits left
						xCoord = radius + 1;
					}
					for (int y = yCoord; y <= engine.getBitMap().getFirstY(
							xCoord)
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
	public int getDamage() {
		// TODO Auto-generated method stub
		return damage;
	}

	@Override
	public double getMass() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getExplosiveRange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getWeaponDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public String getDetails() {
		// TODO Auto-generated method stub
		return details;
	}

	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object weapon) {
		Weapon weaponC = (Weapon) weapon;
		return this.name.equals(weaponC.getName());
	}

	@Override
	public void readImg(String path) {
		// TODO Auto-generated method stub
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
	public BufferedImage getDisplaySprite() {
		// TODO Auto-generated method stub
		return displaySprite;
	}

	@Override
	public BufferedImage getPanelSprite() {
		// TODO Auto-generated method stub
		return panelSprite;
	}

}
