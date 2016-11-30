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
 * Wall object class
 * 
 *
 */
public class Wall implements MiscObject {

	private BufferedImage displaySprite;// 30x150
	private int width = 30;
	private int height = 150;
	private Coordinates currCoord; // width/2 + height/2
	private int hp = 1000;
	
	/**
	 * constructor of the crates
	 * @param coord landing coordinates
	 * @param rand a random number 
	 */
	public Wall(int x, int y) {
		currCoord = new Coordinates(x, y - height / 2);
		readImg("img" + File.separator + "objects" + File.separator  + "wall.png");
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
			displaySprite = new BufferedImage(width, height,
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
		return "WALL";
	}

	public String toString() {
		return "WALL";
	}

	@Override
	public ArrayList<Coordinates> getAllCoordinates() {
		ArrayList<Coordinates> allCoords = new ArrayList<Coordinates>();
		if(hp>0){
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				allCoords.add(new Coordinates(currCoord.getxCoord()
						- (width / 2) + i, currCoord.getyCoord() - (height / 2)
						+ j));
			}
		}
		}
		return allCoords;
	}
	
	/**
	 * return 0 to show this is not a normal crate
	 */
	@Override
	public int updateAfterCrate(Character activeCharacter , MainEngine engine) {
		return 0;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public String getLastUpdate() {
		return null;
	}

}
