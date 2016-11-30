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
 * HP crates
 * 
 *
 */
public class HpCrate implements MiscObject {

	private BufferedImage displaySprite;// 20*20
	private final int width = 20;
	private final int height = 20;
	private final int radius = width / 2;

	private Coordinates currCoord;

	// crate attributes
	private int hp;
	private int hpBoost;
	private String lastUpdate;

	/**
	 * constructor of the crates
	 * 
	 * @param coord
	 *            landing coordinates
	 * @param rand
	 *            a random number
	 */
	public HpCrate(Coordinates coord, int rand) {
		this.currCoord = coord;
		hpBoost = (1 + rand) * 20;
		hp = 1;
		readImg("img" + File.separator + "objects" + File.separator  + "hpcrate.png");
	}

	/**
	 * do update after player gets crates hp increase
	 */
	@Override
	public int updateAfterCrate(Character activeCharacter, MainEngine engine) {
		if (hp > 0) {
			activeCharacter.setHp(activeCharacter.getHp() + this.hpBoost);
			hp = 0;
			this.lastUpdate = activeCharacter.getPlayerName()
					+ "'s HP increased by " + String.valueOf(this.hpBoost)
					+ " !";
			return 1;
		}
		return 2;

	}

	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public void readImg(String path) {
		try {
			displaySprite = new BufferedImage(width, width,
					BufferedImage.TYPE_INT_ARGB);
			displaySprite = ImageIO.read(new File(path));
		} catch (IOException e) {

		}
	}

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

	public int getSquareSize() {
		return this.width;
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

	public int getHpBoost() {
		return this.hpBoost;

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

	public String getObjectType() {
		return "HpCrate";
	}

	@Override
	public String toString() {
		return "HP CRATE WITH BOOST " + String.valueOf(this.hpBoost);
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
