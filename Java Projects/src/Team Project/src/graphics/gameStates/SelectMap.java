package graphics.gameStates;

import gameEngine.Character;
import gameEngine.Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * GameState to represent a host choosing a map for the game
 *
 */
public class SelectMap implements GameState {
	private BufferedImage skyTexture, groundTexture;
	private ArrayList<BufferedImage> availableMaps;
	private BufferedImage currentImage;
	private int currentMapPos;
	public Polygon poly1 = new Polygon(new int[]{325, 350, 350}, new int[]{595,575,615}, 3);
	public Polygon poly2 = new Polygon(new int[]{675, 650, 650}, new int[]{595,575,615}, 3);

	/**
	 * Update the current image
	 */
	public void update() {
		currentImage = availableMaps.get(currentMapPos);
	}

	/**
	 * Render the current image as well as the
	 * buttons to change map and confirm
	 */
	public void render(Graphics g) {
		g.drawImage(currentImage, 0, 0, null);
		
		// Start game rect
		g.setColor(Color.DARK_GRAY);
		g.fillRect(400, 575, 200, 40);
		g.fillPolygon(poly1);
		g.fillPolygon(poly2);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif", Font.PLAIN, 14));
		// Start game String
		g.drawString("Confirm Map", 460, 600);
	}

	/**
	 * Create a new instance of select map
	 * loads maps into the arraylist and allows the user to cycle through them
	 */
	public SelectMap() {
		try {
			skyTexture = ImageIO.read(new File("img" + File.separator + "misc" + File.separator + 
					"blue-sky-texture.jpg"));
			groundTexture = ImageIO.read(new File("img" + File.separator  + "misc" + File.separator + 
					"ground-texture.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		availableMaps = new ArrayList<BufferedImage>();
		availableMaps.add(loadImage(new Map("img" + File.separator + "bitmaps" + File.separator + "bitmap1.bmp")));
		availableMaps.add(loadImage(new Map("img" + File.separator + "bitmaps" + File.separator + "bitmap2.bmp")));
		availableMaps.add(loadImage(new Map("img" + File.separator + "bitmaps" + File.separator + "bitmap3.bmp")));
		availableMaps.add(loadImage(new Map("img" + File.separator + "bitmaps" + File.separator + "bitmap4.bmp")));
		availableMaps.add(loadImage(new Map("img" + File.separator + "bitmaps" + File.separator + "bitmap5.bmp")));
	}
	
	/**
	 * Create the filename for a chosen map
	 * @return
	 */
	public String fileName(){
		return "img"+File.separator+ "bitmaps" + File.separator + "bitmap"+(currentMapPos+1)+".bmp";
	}

	/**
	 * Go to the next map in the arraylist
	 * (Loops)
	 */
	public void nextMap() {
		if (currentMapPos + 1 < availableMaps.size()) {
			currentMapPos++;
		} else {
			currentMapPos = 0;
		}
	}

	/**
	 * Go to the previous map in the arraylist.
	 * (Loops)
	 */
	public void prevMap() {
		if (currentMapPos - 1 < 0) {
			currentMapPos = availableMaps.size() - 1;
		} else {
			currentMapPos--;
		}
	}

	/**
	 * Nothing to start
	 */
	public void start() {

	}

	/**
	 * No players at this stage
	 */
	public Character[] getPlayers() {
		return null;
	}

	/**
	 * No ID at this stage
	 */
	public int getMyID() {
		return 0;
	}

	/**
	 * No teams/winning teams at
	 * this stage
	 */
	public int winningTeam() {
		return 0;
	}

	@Override
	/**
	 * No connections to close
	 */
	public void close() {

	}

	/**
	 * Load an image
	 * @param currentMap
	 * @return
	 */
	private BufferedImage loadImage(Map currentMap) {
		BufferedImage drawnMap;
		int mapWidth = currentMap.getWidth();
		int mapHeight = currentMap.getHeight();
		drawnMap = new BufferedImage(1000, 550, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (currentMap.getCoord(i, j) == 1) {
					drawnMap.setRGB(i, j, groundTexture.getRGB(i, j));
				} else if (currentMap.getCoord(i, j) == 0) {
					drawnMap.setRGB(i, j, skyTexture.getRGB(i, j));
				}

			}
		}
		return drawnMap;
	}

	@Override
	/**
	 * No map chosen at this point
	 */
	public String getFilePath() {
		return null;
	}
}
