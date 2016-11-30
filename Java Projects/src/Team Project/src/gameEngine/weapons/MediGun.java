package gameEngine.weapons;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.tools.Coordinates;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * MediGun Weapon Class
 * 
 *
 */
public class MediGun implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private String name = "MediGun";
	private int damage = 0;
	private int accuracy = 100;
	private int heal = 350;
	private String description;
	private String details;

	public MediGun() {
		readImg("img" + File.separator + "weapSprites" + File.separator + "heal.png");
		readPanelImg("img" + File.separator + "weapPanel" + File.separator + "medigunpanel.png");
		details = "MediGun - Heals the target ally with 350 hp on your mouse location. Heals less against yourself.\n"
				+ "This weapon is targeted so just click on the ally you want to heal.\n\n";
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
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		double distance = power * 300 / 100;
		int xCoord = currCoord.getxCoord();
		int yCoord = currCoord.getyCoord();
		double radian = Math.toRadians(angle);
		int xDistance = (int) Math.round(Math.cos(radian) * distance);
		int yDistance = (int) Math.round(Math.sin(radian) * distance);
		int targetXCoord = xCoord + xDistance;
		int targetyCoord = yCoord - yDistance;

		// y = mx+c <---- y = mx - c because y is 0 at top
		// y = (height) - (mx +c)
		// y = h - mx - c
		int height = engine.getBitMap().getHeight() - 1;
		double m = (double) (yDistance) / (double) (xDistance);
		double c = height - xCoord * m - yCoord;

		int y;
		if (power == 0
				|| angle == -90
				|| active.getAllCoordinates().contains(
						new Coordinates(targetXCoord, targetyCoord))) { // click
																		// itself
			for (int x = 160; x >= 0; x -= 8) {
				path.add(new Coordinates(xCoord, yCoord - x));
			}
		} else if (targetXCoord >= xCoord) {
			for (int x = xCoord; x <= targetXCoord; x += 5) {
				y = (int) (height - Math.round(m * x + c));
				path.add(new Coordinates(x, y));

			}
		} else if (targetXCoord < xCoord) {
			for (int x = xCoord; x > targetXCoord; x -= 5) {
				y = (int) (height - Math.round(m * x + c));
				path.add(new Coordinates(x, y));

			}
		}

		// check if the target coord is enemies or else do nothing
		return path;
	}

	private boolean isHit(Coordinates hit, Character character) {
		return character.getAllCoordinates().contains(hit);
	}

	/**
	 * Heal the allied players
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
				if (isHit(center, character)) {
					if (teamNumber == character.getTeamNumber()
							&& character.getHp() > 0
							&& character.getFullHP() > character.getHp()) {
						int heal = (int) (Math.round(this.heal
								* (1 + active.getHealingAmp())
								* (1 + character.getHealingAmp())
								* (1 - character.getHealingRed())));
						if (isHit(center, active)) {
							heal = heal / 2; // heal itself less amount
						}
						character.setHp(character.getHp() + heal);
					}
				}
			}
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
		for (int t = 0; t < 3; t++) {
			for (int i = 0; i < path.size(); i += 2) {
				drawWeaponSprite(fancyMap, path.get(i));
				try {
					Thread.sleep(35);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (path.size() > 35) {
					i += 1;
				}
			}
		}
		updateAfterShot(path.get(path.size() - 1), engine.getBitMap(), characters,
				0, active, engine);
	}

	/**
	 * Draw the sprite at specific coordinates
	 * 
	 * @param fancyMap
	 *            the image to show on screen
	 * @param coord
	 *            current coordinate
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

	public int getHeal() {
		return heal;
	}

	@Override
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
		description = "Heals the ally at target location. Heals less to urself. This waepon deals no damage.";
		return description;
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

	public BufferedImage getPanelSprite() {
		return this.panelSprite;
	}

	public BufferedImage getDisplaySprite() {
		return displaySprite;
	}

	public int getDamage() {
		return damage;
	}

	public double getMass() {
		return 0;
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
