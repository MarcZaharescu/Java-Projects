package gameEngine.weapons;

import gameEngine.Character;

import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.tools.Coordinates;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Interface for game weapons
 * 
 *
 */
public interface Weapon {

	

	/**
	 * Read the projectile image of the weapon
	 * 
	 * @param path
	 */
	public void readImg(String path);

	/**
	 * Read the file for the image on the panel
	 * 
	 * @param path
	 */
	public void readPanelImg(String path);

	/**
	 * Get the projectile image
	 * 
	 * @return
	 */
	public BufferedImage getDisplaySprite();

	/**
	 * Get the image on the panel
	 * 
	 * @return
	 */
	public BufferedImage getPanelSprite();


	public ArrayList<Coordinates> fire(double power, double angle,
			Coordinates currCoord, Character active, Character[] characters,
			double windSpeed, double windAngle, MainEngine engine);

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
			Character[] characters, MainEngine engine);

	/**
	 * Perform weapon-specific actions e.g., damage, heal players, teleport,
	 * etc.
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
			Character[] characters, int i, Character active, MainEngine engine);

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
			Character[] characters, MainEngine engine);

	/**
	 * Get the damage of the weapon
	 * 
	 * @return
	 */
	public int getDamage();

	/**
	 * Get the mass of the weapon
	 * 
	 * @return
	 */
	public double getMass();

	/**
	 * Get the heal boost
	 * 
	 * @return
	 */
	public int getHeal();

	/**
	 * Get the blast radius of the weapon
	 * 
	 * @return
	 */
	public int getExplosiveRange();

	/**
	 * Get the accuracy parameter
	 * 
	 * @return
	 */
	public int getAccuracy();

	/**
	 * Get the name of the weapon
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get the description that is shown on hover
	 * 
	 * @return
	 */
	public String getWeaponDescription();

	/**
	 * Get the details for the help screen
	 * 
	 * @return
	 */
	public String getDetails();

	@Override
	public boolean equals(Object weapon);

	@Override
	public String toString();

}
