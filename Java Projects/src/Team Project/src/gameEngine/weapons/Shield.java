package gameEngine.weapons;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.flags.Shielding;
import gameEngine.tools.Coordinates;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Shield Weapon Class
 * 
 * 
 */
public class Shield implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private String name = "Shield";
	private int damage = 300;
	private int mass = 1;
	private int explosiveRange = 100;
	private int accuracy = 100;
	private String description;
	private String details;

	public Shield() {
		readPanelImg("img" + File.separator + "weapPanel" + File.separator + "shieldpanel.png");
		description = "Provides player with a buffer that blocks the next attack.";
		details = "Shield - When you click fire with this weapon, you will block the next shot.\n\n";
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

	/**
	 * Add the Shielding flag to the player who fired the shot.
	 */
	public ArrayList<Coordinates> fire(double power, double angle,
			Coordinates currCoord, Character active, Character[] characters,
			double ws, double wa, MainEngine engine) {
		// to do calculations and set initial details of the shot

		active.addFlag(new Shielding(active));
		active.setBlocking(true);
		return new ArrayList<Coordinates>();
	}

	/**
	 * No shot animation to do.
	 */
	public void animateShot(BufferedImage fancyMap,
			ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {
		animateExplosion(path, active, characters, engine);
	}

	/**
	 * No more updates to do.
	 */
	public void updateAfterShot(Coordinates center, Map bitMap,
			Character[] characters, int i, Character active, MainEngine engine) {
	}

	/**
	 * This weapon does not explode on firing.
	 * 
	 */
	public void animateExplosion(ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {

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

	public String getDetails() {
		return details;
	}
}
