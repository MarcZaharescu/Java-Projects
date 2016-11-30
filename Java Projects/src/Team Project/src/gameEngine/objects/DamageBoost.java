package gameEngine.objects;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.tools.Coordinates;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Damage boost crates
 * 
 *
 */
public class DamageBoost implements MiscObject {

	private BufferedImage displaySprite;// 20*20
	private final int width = 20;
	private final int height = 20;
	private final int radius = width / 2;
	private Coordinates currCoord;
	// crate attributes
	private int hp;
	private double dmgBoost;
	private String lastUpdate;

	/**
	 * constructor of the crates
	 * 
	 * @param coord landing coordinates
	 * @param rand a random number
	 */
	public DamageBoost(Coordinates coord, int rand) {
		this.currCoord = coord;
		dmgBoost = (1 + rand) * 0.025;
		hp = 1;
		readImg("img" + File.separator + "objects" + File.separator +  "mysterycrate.png");
	}

	/**
	 * do update after a player get the crates
	 */
	@Override
	public int updateAfterCrate(Character activeCharacter , MainEngine engine) {
		if (hp > 0) {
			double dmgAmp = activeCharacter.getDamageAmp();
			double newAmp = (1 + dmgAmp) * (1 + this.dmgBoost) - 1;
			activeCharacter.setDamageAmp(newAmp);
			hp = 0;
			this.lastUpdate = activeCharacter.getPlayerName()
					+ "'s damage increased by "
					+ String.valueOf(this.dmgBoost*100).substring(0, 4)
					+ "%!";
			return 1;

		}
		return 2;
	}

	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public double getDmgBoost() {
		return this.dmgBoost;
	}

	@Override
	public Coordinates getCurrCoord() {
		return currCoord;
	}

	@Override
	public void setCurrCoord(Coordinates currCoord) {
		this.currCoord = currCoord;

	}

	@Override
	public void readImg(String path) {
		try {
			displaySprite = new BufferedImage(width, width,
					BufferedImage.TYPE_INT_ARGB);
			displaySprite = ImageIO.read(new File(path));
		} catch (IOException e) {

		}
	}

	@Override
	public BufferedImage getDisplaySprite() {
		return displaySprite;
	}

	@Override
	public void setDisplaySprite(BufferedImage displaySprite) {

	}

	@Override
	public int getHp() {
		return hp;
	}

	@Override
	public void setHp(int hp) {
		this.hp = hp;
	}

	@Override
	public String getObjectType() {
		return "DmgBoostCrate";
	}

	/**
	 * return all coordinates around the crates for collision detection
	 */
	@Override
	public ArrayList<Coordinates> getAllCoordinates() {
		ArrayList<Coordinates> allCoords = new ArrayList<Coordinates>();
		if (hp > 0) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < width; j++) {
					allCoords.add(new Coordinates(currCoord.getxCoord()
							- radius + i, currCoord.getyCoord() - radius + j));
				}
			}
		}
		return allCoords;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "Damage Boost Crates with " + (this.dmgBoost * 10) + "%";
	}

}
