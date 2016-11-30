package gameEngine.weapons;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.objects.MiscObject;
import gameEngine.tools.Coordinates;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Lazer Weapon Class
 * 
 *
 */
public class Lazer implements Weapon {

	private BufferedImage display;
	private BufferedImage panelSprite;
	private String name = "Lazer";
	private int lazerDiameter = 10;
	private int damage = 400;
	private double mass = 0.1;
	private int accuracy = 100;
	private String description;
	private String details;

	public Lazer() {
		readPanelImg("img" + File.separator + "weapPanel" + File.separator+ "lazerpanel.png");
		details = "Lazer - Fires a straight line lazer to target, and deals 400 damage to the target it hits.\n"
				+ "Lazer will be blocked by anything so needs to be aimed accurately at an enemy.\n\n";
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

		ArrayList<Coordinates> cratesCoord = new ArrayList<Coordinates>();
		for (MiscObject obj : engine.getObjects()) {
			if (obj != null) {
				cratesCoord.addAll(obj.getAllCoordinates());
			}
		}

		while (!isCollide) {
			double yDistance = (initialYSpeed * t);
			double xDistance = (initialXSpeed * t);
			xCoord = (int) Math.round(initialXCoord + xDistance);
			yCoord = (int) Math.round(initialYCoord - yDistance);
			Coordinates result = new Coordinates(xCoord, yCoord);
			path.add(result);
			try {
				if (map.getCoord(xCoord, yCoord) == 1 // ground
						|| enemiesCoord
								.contains(new Coordinates(xCoord, yCoord))// hits
																			// enemies
						|| cratesCoord
								.contains(new Coordinates(xCoord, yCoord))) { // hits
																				// crates
					isCollide = true;
				} else {
					t = t + timeInterval;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				isCollide = true;
			}
		}
		return path;
	}

	private boolean isHit(Coordinates hit, Character character) {
		return character.getAllCoordinates().contains(hit);
	}

	/**
	 * Damage the target player in one go.
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
		// set the destroy crates to hp 0
		for (MiscObject obj : engine.getObjects()) {
			if (obj != null) {
				if (obj.getAllCoordinates().contains(center)) {
					obj.setHp(obj.getHp() - this.damage);
				}
			}
		}

		if (active.getFlags().get(3) != null) {
			active.getFlags().get(3).setMovesLeft(0);
			active.resetModifiers();
		}

	}

	public int getDamage() {
		return this.damage;
	}

	/**
	 * Animate the shot along the path
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
	public void animateShot(BufferedImage fancyMap,
			ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {

		// BufferedImage fancyMap = mappy.getFancyMap();
		ArrayList<Coordinates> allPixels = new ArrayList<Coordinates>();

		for (int i = 0; i < path.size(); i++) {

			Coordinates curr = path.get(i);
			int currX = curr.getxCoord();
			int currY = curr.getyCoord();
			// first we get all drawing coords along the firing path
			// because the lazer needs to have width
			for (int j = -lazerDiameter / 2; j < lazerDiameter / 2; j++) {
				for (int k = -5; k < 5; k++) {
					try {
						allPixels.add(new Coordinates(currX + k, currY + j));
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
			}
		}
		// set the color along the path some times red some times green
		for (int i = 0; i < 120; i++) {
			try {

				if (i % 2 == 0 && i > 30) {
					for (Coordinates pixel : allPixels) {
						fancyMap.setRGB(pixel.getxCoord(), pixel.getyCoord(),
								Color.RED.getRGB());
					}
				} else {
					for (Coordinates pixel : allPixels) {
						fancyMap.setRGB(pixel.getxCoord(), pixel.getyCoord(),
								Color.GREEN.getRGB());
					}

				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		// then do update the health
		updateAfterShot(path.get(path.size() - 1), engine.getBitMap(), characters,
				0, active, engine);

	}

	/**
	 * No explosion for the lazer.
	 */
	public void animateExplosion(ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {

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

	public String getWeaponDescription() {
		description = "Fires a straight line lazer that deals damage to first enemies it hits.";
		return description;
	}

	public int getHeal() {
		return 0;
	}

	public String getDetails() {
		return details;
	}

	public void readPanelImg(String path) {
		try {
			panelSprite = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getPanelSprite() {
		return this.panelSprite;
	}

	public void readImg(String path) {
		try {
			display = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getDisplaySprite() {
		return display;
	}

	public double getMass() {
		return this.mass;
	}

	public int getExplosiveRange() {
		return 0;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public String getName() {
		return name;
	}

}
