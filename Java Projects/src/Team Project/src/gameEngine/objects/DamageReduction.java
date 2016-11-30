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
 * Damage reduction crates
 *
 */
public class DamageReduction implements MiscObject {

	private BufferedImage displaySprite;//20*20
	private final int width = 20;
	private final int height = 20;
	private final int radius = width / 2;
	private Coordinates currCoord;
	
	//crate attributes
	private int hp;
	private double dmgRed;
	private String lastUpdate;
	
	/**
	 * constructor of the crates
	 * @param coord landing coordinates
	 * @param rand a random number 
	 */
	public DamageReduction(Coordinates coord , int rand) {
		this.currCoord = coord;
		dmgRed=(1+rand)*0.025;
		hp = 1;
		readImg("img" + File.separator + "objects" + File.separator + "mysterycrate.png");
	}
	

	/**
	 * do update after a player get the crates
	 */
	@Override
	public int updateAfterCrate(Character activeCharacter , MainEngine engine) {
		if(hp>0){
			double dmgRed = activeCharacter.getDamageAmp();
			double newRed = (1+dmgRed) * (1+this.dmgRed) -1 ;
			activeCharacter.setDamageRed(newRed);
			hp=0;
			this.lastUpdate =  activeCharacter.getPlayerName() + "'s damage reduced by " + String.valueOf(this.dmgRed*100).substring(0,4) + "%!";
			return 1;
		}
		return 2;

	}
	
	public String getLastUpdate() {
		return this.lastUpdate;
	}
	public double getDmgRed() {
		return this.dmgRed;
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
		return "DmgReductionCrate";
	}

	@Override
	public ArrayList<Coordinates> getAllCoordinates() {
		ArrayList<Coordinates> allCoords = new ArrayList<Coordinates>();
		if(hp>0){
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				allCoords.add(new Coordinates(currCoord.getxCoord() - radius
						+ i, currCoord.getyCoord() - radius + j));
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
	public String toString(){
		return "Damage Reduction Crates with " + (this.dmgRed *10) +"%";
	}

}
