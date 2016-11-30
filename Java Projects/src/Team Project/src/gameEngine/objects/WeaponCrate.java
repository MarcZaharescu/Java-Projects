package gameEngine.objects;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.tools.Coordinates;
import gameEngine.weapons.DirtCircle;
import gameEngine.weapons.Enrage;
import gameEngine.weapons.FireMine;
import gameEngine.weapons.FireWall;
import gameEngine.weapons.HealingBuff;
import gameEngine.weapons.Lazer;
import gameEngine.weapons.LazerEX;
import gameEngine.weapons.MediGun;
import gameEngine.weapons.NuclearBlast;
import gameEngine.weapons.PoisonDart;
import gameEngine.weapons.RocketLauncher;
import gameEngine.weapons.Shield;
import gameEngine.weapons.Teleport;
import gameEngine.weapons.Weapon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Weapon crate class
 * 
 *
 */
public class WeaponCrate implements MiscObject {

	private BufferedImage displaySprite;

	private final int width = 20;
	private final int height = 20;
	private final int radius = width / 2;

	private Coordinates currCoord;

	private int hp;
	private Weapon weapon;
	private String lastUpdate;

	/**
	 * constructor of the crates
	 * 
	 * @param coord
	 *            landing coordinates
	 * @param rand
	 *            a random number
	 */
	public WeaponCrate(Coordinates coordinates, int rand) {
		currCoord = coordinates;
		hp = 1;
		readImg("img" + File.separator + "objects" + File.separator
				+ "weapcrate.png");
		switch (rand) {
		case 0:
			this.weapon = new DirtCircle();
			break;
		case 1:
		case 2:
			this.weapon = new RocketLauncher();
			break;
		case 3:
			this.weapon = new FireWall();
			break;
		case 4:
			this.weapon = new LazerEX();
			break;
		case 5:
			this.weapon = new HealingBuff();
			break;
		case 6:
		case 7:
			this.weapon = new FireMine();
			break;
		case 8:
		case 9:
			this.weapon = new PoisonDart();
			break;
		case 10:
		case 11:
			this.weapon = new Shield();
			break;
		case 12:
		case 13:
			this.weapon = new Teleport();
			break;
		case 14:
			this.weapon = new NuclearBlast();
			break;
		case 15:
		case 16:
			this.weapon = new MediGun();
			break;
		case 17:
			this.weapon = new Lazer();
			break;
		case 18:
		case 19:
			this.weapon = new Enrage();
			break;
		}
	}

	/**
	 * update the player get crates player get new weapon
	 */
	@Override
	public int updateAfterCrate(Character activeCharacter, MainEngine engine) {
		if (hp > 0) {
			if (!activeCharacter.getWeapons().contains(this.weapon)) {
				activeCharacter.getWeapons().add(this.weapon);
			}
			hp = 0;
			this.lastUpdate = activeCharacter.getPlayerName()
					+ " picked up a " + this.weapon.getName()
					+ " weapon!";
			return 1;
		}
		return 2;

	}

	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public void readImg(String path) {
		try {
			displaySprite = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			displaySprite = ImageIO.read(new File(path));
		} catch (IOException e) {

		}
	}

	public ArrayList<Coordinates> getAllCoordinates() {
		ArrayList<Coordinates> allCoords = new ArrayList<Coordinates>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				allCoords.add(new Coordinates(currCoord.getxCoord() - radius
						+ i, currCoord.getyCoord() - radius + j));
			}
		}
		return allCoords;
	}

	public int getRadius() {
		return this.radius;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public BufferedImage getDisplaySprite() {
		return displaySprite;
	}

	public void setDisplaySprite(BufferedImage displaySprite) {
		this.displaySprite = displaySprite;
	}

	public Coordinates getCurrCoord() {
		return currCoord;
	}

	public void setCurrCoord(Coordinates currCoord) {
		this.currCoord = currCoord;
	}

	@Override
	public String getObjectType() {
		// TODO Auto-generated method stub
		return "WeaponCrate";
	}

	@Override
	public String toString() {
		return "WEAPON CRATE WITH WEAPON "
				+ String.valueOf(this.weapon.toString());
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

}
