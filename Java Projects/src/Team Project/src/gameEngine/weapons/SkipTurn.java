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
 * SkipTurn Weapon Class
 * 
 * 
 */
public class SkipTurn implements Weapon {

	private BufferedImage displaySprite;
	private BufferedImage panelSprite;
	private String name = "SkipTurn";
	private int damage = 300;
	private int mass = 1;
	private int explosiveRange = 100;
	private int accuracy = 100;
	private String description;
	private String details;

	

	/**
	 * Simply skips the character's turn.
	 * Return a new object to avoid null pointer exceptions.
	 */
	public ArrayList<Coordinates> fire(double power, double angle,
			Coordinates currCoord, Character active, Character[] characters,
			double ws, double wa, MainEngine engine) {
		return new ArrayList<Coordinates>();
	}

	/**
	 * No shot animation to do.
	 */
	public void animateShot(BufferedImage fancyMap,
			ArrayList<Coordinates> path, Character active,
			Character[] characters, MainEngine engine) {
	}

	/**
	 * No update to do.
	 */
	public void updateAfterShot(Coordinates center, Map bitMap,
			Character[] characters, int i, Character active, MainEngine engine) {
	}

	/**
	 * No explosion to animate.
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

}
