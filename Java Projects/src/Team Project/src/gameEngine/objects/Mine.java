package gameEngine.objects;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.tools.Coordinates;
import gameEngine.weapons.FireMine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Mine object
 * 
 *
 */
public class Mine implements MiscObject {

	private BufferedImage displaySprite;// 20,20
	private int width = 20;
	private int height = 20;
	private Coordinates currCoord; // width/2 + height/2
	private int hp = 200;
	private int damage = new FireMine().getDamage();
	private String lastUpdate;

	public Mine(int x, int y) {
		currCoord = new Coordinates(x, y);
		readImg("img" + File.separator + "objects" + File.separator  + "mine.png");
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
		this.displaySprite= displaySprite;
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
		// TODO Auto-generated method stub
		return "MINE";
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

	@Override
	public int updateAfterCrate(Character activeCharacter , MainEngine engine) {
		if(hp>0){
			double angle;
			if(activeCharacter.getCurrCoord().getxCoord()>currCoord.getxCoord()){
				angle = 45;
			}else{
				angle = 135;
			}
			hp=0;			
			activeCharacter.setHp(activeCharacter.getHp()-damage/2);
			new FireMine().animateKnockBack(activeCharacter, engine, angle);
			activeCharacter.setHp(activeCharacter.getHp()-damage/2);

			lastUpdate = activeCharacter.getPlayerName() + " got hit by a mine";
			
		}
		return 0;
	}
	
	
	

	@Override
	public String getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
