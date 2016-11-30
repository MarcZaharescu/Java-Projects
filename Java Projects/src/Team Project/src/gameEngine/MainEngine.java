package gameEngine;

import gameEngine.objects.DamageBoost;
import gameEngine.objects.DamageReduction;
import gameEngine.objects.HpCrate;
import gameEngine.objects.MiscObject;
import gameEngine.objects.WeaponCrate;
import gameEngine.tools.Coordinates;
import gameEngine.weapons.Weapon;
import graphics.gameStates.GeneralGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

/**
 * Performs underlying calculations for the game Internal representation of the
 * PlayGame state
 * 
 *
 */
public class MainEngine {

	// The image which is displayed on the screen to the user
	private BufferedImage fancyMap;

	// The sky texture image used
	private static BufferedImage skyTexture;

	// The ground texture image used
	private static BufferedImage groundTexture;

	// The arrow image to indicate active player
	private static BufferedImage arrow;

	// The images used to represent the flags
	private static BufferedImage skull;
	private static BufferedImage healing;
	private static BufferedImage shield;
	private static BufferedImage flame;

	// Default image when character is loaded on right of map
	private BufferedImage bluesheepanimationright;
	private BufferedImage orangesheepanimationright;
	private BufferedImage bluesheepwarrioranimationright;
	private BufferedImage orangesheepwarrioranimationright;
	private BufferedImage bluesheephealeranimationright;
	private BufferedImage orangesheephealeranimationright;
	private BufferedImage bluesheepdefenderanimationright;
	private BufferedImage orangesheepdefenderanimationright;

	// Default image when character is loaded on left of map
	private BufferedImage orangesheepanimationleft;
	private BufferedImage bluesheepanimationleft;
	private BufferedImage bluesheepwarrioranimationleft;
	private BufferedImage orangesheepwarrioranimationleft;
	private BufferedImage bluesheephealeranimationleft;
	private BufferedImage orangesheephealeranimationleft;
	private BufferedImage bluesheepdefenderanimationleft;
	private BufferedImage orangesheepdefenderanimationleft;

	// animation frames for the left move
	public BufferedImage[] blue_normal_animation_frames_left = new BufferedImage[6];
	public BufferedImage[] orange_normal_animation_frames_left = new BufferedImage[6];
	public BufferedImage[] blue_warrior_animation_frames_left = new BufferedImage[6];
	public BufferedImage[] orange_warrior_animation_frames_left = new BufferedImage[6];
	public BufferedImage[] blue_healer_animation_frames_left = new BufferedImage[6];
	public BufferedImage[] orange_healer_animation_frames_left = new BufferedImage[6];
	public BufferedImage[] blue_defender_animation_frames_left = new BufferedImage[6];
	public BufferedImage[] orange_defender_animation_frames_left = new BufferedImage[6];

	// animation frames for the right move
	public BufferedImage[] blue_normal_animation_frames_right = new BufferedImage[6];
	public BufferedImage[] orange_normal_animation_frames_right = new BufferedImage[6];
	public BufferedImage[] blue_warrior_animation_frames_right = new BufferedImage[6];
	public BufferedImage[] orange_warrior_animation_frames_right = new BufferedImage[6];
	public BufferedImage[] blue_healer_animation_frames_right = new BufferedImage[6];
	public BufferedImage[] orange_healer_animation_frames_right = new BufferedImage[6];
	public BufferedImage[] blue_defender_animation_frames_right = new BufferedImage[6];
	public BufferedImage[] orange_defender_animation_frames_right = new BufferedImage[6];

	// The logical underlying representation
	private static Map bitMap;

	// The array of characters in the game
	private Character[] characters;

	// The array of objects on the screen
	private List<MiscObject> objects;

	// The square size and radius of the player sprite, and the length of the
	// move. Loaded statically so we don't have to get them every time
	private static int squareSize;
	private static int radius;
	private static int moveLength;


	private GeneralGame playGame;

	/**
	 * Initializes MainEngine object
	 * 
	 * @param characters
	 *            array of characters in the game
	 * @param objects
	 *            other objects in game, such as crates
	 * @param bitMap
	 *            underlying bitmap representing the game
	 * @throws IOException
	 */
	public MainEngine(Character[] characters, Map bitMap, GeneralGame playGame)
			throws IOException {

		this.characters = characters;
		this.bitMap = bitMap;
		this.playGame = playGame;

		MainEngine.squareSize = characters[0].getSquareSize();
		MainEngine.radius = characters[0].getRadius();
		MainEngine.moveLength = characters[0].getMoveLength();
		objects = new CopyOnWriteArrayList<MiscObject>();

		// load the texture files
		MainEngine.skyTexture = ImageIO.read(new File("img" + File.separator
				+ "misc" + File.separator + "blue-sky-texture.jpg"));
		MainEngine.groundTexture = ImageIO.read(new File("img" + File.separator
				+ "misc" + File.separator + "ground-texture.jpg"));
		this.fancyMap = ImageIO.read(new File("img" + File.separator + "misc"
				+ File.separator + "ground-texture.jpg"));

		// load flag files
		MainEngine.arrow = ImageIO.read(new File("img" + File.separator
				+ "misc" + File.separator + "arrow.png"));
		MainEngine.skull = ImageIO.read(new File("img" + File.separator
				+ "flags" + File.separator + "skull2.png"));
		MainEngine.healing = ImageIO.read(new File("img" + File.separator
				+ "flags" + File.separator + "healing.png"));
		MainEngine.flame = ImageIO.read(new File("img" + File.separator
				+ "flags" + File.separator + "flame.png"));
		MainEngine.shield = ImageIO.read(new File("img" + File.separator
				+ "flags" + File.separator + "shield.png"));

		// load animation files
		this.bluesheepanimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheepanimationright.png"));

		this.orangesheepanimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheepanimationright.png"));

		this.orangesheepwarrioranimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheepwarrioranimationright.png"));
		this.bluesheepwarrioranimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheepwarrioranimationright.png"));

		this.orangesheepdefenderanimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheepdefenderanimationright.png"));
		this.bluesheepdefenderanimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheepdefenderanimationright.png"));

		this.orangesheephealeranimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheephealeranimationright.png"));
		this.bluesheephealeranimationright = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheephealeranimationright.png"));

		this.orangesheepwarrioranimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheepwarrioranimationleft.png"));
		this.bluesheepwarrioranimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheepwarrioranimationleft.png"));

		this.orangesheepdefenderanimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheepdefenderanimationleft.png"));
		this.bluesheepdefenderanimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheepdefenderanimationleft.png"));

		this.orangesheephealeranimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheephealeranimationleft.png"));
		this.bluesheephealeranimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheephealeranimationleft.png"));

		this.bluesheepanimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "bluesheepanimationleft.png"));
		this.orangesheepanimationleft = ImageIO.read(new File("img"
				+ File.separator + "characterAnimations" + File.separator
				+ "orangesheepanimationleft.png"));

		for (int i = 0; i < 6; i++) {
			blue_normal_animation_frames_left[i] = bluesheepanimationleft
					.getSubimage(0, 30 * (i), 30, 30);
			orange_normal_animation_frames_left[i] = orangesheepanimationleft
					.getSubimage(0, 30 * (i), 30, 30);
			blue_warrior_animation_frames_left[i] = bluesheepwarrioranimationleft
					.getSubimage(0, 30 * (i), 30, 30);
			orange_warrior_animation_frames_left[i] = orangesheepwarrioranimationleft
					.getSubimage(0, 30 * (i), 30, 30);
			blue_healer_animation_frames_left[i] = bluesheephealeranimationleft
					.getSubimage(0, 30 * (i), 30, 30);
			orange_healer_animation_frames_left[i] = orangesheephealeranimationleft
					.getSubimage(0, 30 * (i), 30, 30);
			blue_defender_animation_frames_left[i] = bluesheepdefenderanimationleft
					.getSubimage(0, 30 * (i), 30, 30);
			orange_defender_animation_frames_left[i] = orangesheepdefenderanimationleft
					.getSubimage(0, 30 * (i), 30, 30);

			blue_normal_animation_frames_right[i] = bluesheepanimationright
					.getSubimage(0, 30 * (i), 30, 30);
			orange_normal_animation_frames_right[i] = orangesheepanimationright
					.getSubimage(0, 30 * (i), 30, 30);
			blue_warrior_animation_frames_right[i] = bluesheepwarrioranimationright
					.getSubimage(0, 30 * (i), 30, 30);
			orange_warrior_animation_frames_right[i] = orangesheepwarrioranimationright
					.getSubimage(0, 30 * (i), 30, 30);
			blue_healer_animation_frames_right[i] = bluesheephealeranimationright
					.getSubimage(0, 30 * (i), 30, 30);
			orange_healer_animation_frames_right[i] = orangesheephealeranimationright
					.getSubimage(0, 30 * (i), 30, 30);
			blue_defender_animation_frames_right[i] = bluesheepdefenderanimationright
					.getSubimage(0, 30 * (i), 30, 30);
			orange_defender_animation_frames_right[i] = orangesheepdefenderanimationright
					.getSubimage(0, 30 * (i), 30, 30);
		}

	}

	/**
	 * Generate random Coordinates pairs and assign them to players
	 * 
	 * @return ArrayList<Coordinates> of where the characters are placed
	 */
	public ArrayList<Coordinates> placeCharacters() {
		Random randy = new Random();
		ArrayList<Coordinates> positions = new ArrayList<Coordinates>(8);
		// characters in team 1
		for (int i = 0; i < 8; i += 2) {
			if (characters[i] != null) {
				int randX = 100 + randy.nextInt(300);
				int randY = bitMap.getFirstY(randX);
				this.characters[i].setTeamNumber(0);
				this.characters[i].setCurrCoord(new Coordinates(randX, randY
						- radius));
				positions.add(new Coordinates(randX, randY - radius));
			}
		}
		for (int i = 1; i <= 7; i += 2) {
			if (characters[i] != null) {
				int randX = 600 + randy.nextInt(300);
				int randY = bitMap.getFirstY(randX);

				this.characters[i].setTeamNumber(1);
				this.characters[i].setCurrCoord(new Coordinates(randX, randY
						- radius));
				positions.add(new Coordinates(randX, randY - radius));
			}
		}
		return positions;
	}

	/**
	 * load the buffered display image from the bitmap, drawing the textures
	 */
	public void loadImage() {
		int mapWidth = bitMap.getWidth();
		int mapHeight = bitMap.getHeight();
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (bitMap.getCoord(i, j) == 1) {
					fancyMap.setRGB(i, j, groundTexture.getRGB(i, j));

				} else {
					fancyMap.setRGB(i, j, skyTexture.getRGB(i, j));
				}

			}
		}
		loadObjects();
		loadCharacters();
		// load the individual characters
	}

	/**
	 * Load the characters to the map
	 */

	private void loadCharacters() {
		for (Character character : characters) {
			if (character != null && character.getHp() > 0) {
				// BufferedImage displaySprite = character.getDisplaySprite();
				// Coordinates location = character.getCurrCoord();
				loadCharacter(character);
			}
		}
		// show HP and names
		// showPlayerDetail();
	}

	/**
	 * Load a character on the map
	 * 
	 * @param character
	 */
	private void loadCharacter(Character character) {

		int currX = character.getCurrCoord().getxCoord();
		int currY = character.getCurrCoord().getyCoord();
		int teamNumber = character.getTeamNumber();
		int radius = squareSize / 2;

		BufferedImage displaySprite = character.getDisplaySprite();

		if (currY + radius + 1 >= getHeight()) {
			// character dropped of the map - he's dead.
			character.setHp(0);
		}
		if (character.getHp() > 0) {

			// load coordinates on bitmap
			if (teamNumber == 0) {
				bitMap.setCoord(currX, currY, 2);
			} else if (teamNumber == 1) {
				bitMap.setCoord(currX, currY, 3);
			}

			// load character sprite
			for (int i = 0; i < squareSize; i++) {
				for (int j = 0; j < squareSize; j++) {
					int rgb = displaySprite.getRGB(i, j);
					// ignore transparency
					int alpha = (rgb >> 24) & 0xff;
					if (alpha != 0) {
						try {
							fancyMap.setRGB(currX - radius + i, currY - radius
									+ j, rgb);
						} catch (ArrayIndexOutOfBoundsException e) {
						}

					}
				}
			}
			showPlayerDetail(character);
		}

		else {
			bitMap.setCoord(currX, currY, 0);
		}
	}

	/**
	 * Load the player HP bars and names on the screen
	 */
	public void showPlayerDetail(Character character) {
		if (character != null && character.getHp() > 0) {
			double characterHP = character.getHp();
			double fullHpLength = ((double) character.getFullHP() / 1000 * squareSize);
			double hpLength = (characterHP / 1000 * squareSize);
			int xCoord = character.getCurrCoord().getxCoord();
			int yCoord = character.getCurrCoord().getyCoord();
			double percentageHealth = hpLength / fullHpLength;
			for (int i = 0; i < hpLength; i++) {
				for (int j = 0; j < 3; j++) {
					// different colors for lower and lower HPs
					int rgb = Color.GREEN.getRGB();
					if (percentageHealth < 0.7 && percentageHealth >= 0.3) {
						rgb = Color.YELLOW.getRGB();
					} else if (percentageHealth < 0.3) {
						rgb = Color.RED.getRGB();
					}
					try {
						fancyMap.setRGB(
								(int) (xCoord - Math.round(fullHpLength / 2) + i),
								yCoord - radius - 5 + j, rgb);
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
			}
			// borders for hp bar
			for (int x = -1; x <= fullHpLength; x++) {
				for (int y = -1; y < 4; y++) {
					try {
						if (x == fullHpLength || (x + 1) % 6 == 0) {
							fancyMap.setRGB(
									(int) (xCoord
											- Math.round(fullHpLength / 2) + x),
									yCoord - radius - 5 + y,
									Color.BLACK.getRGB());
						} else if (y == -1 || y == 3) {
							fancyMap.setRGB(
									(int) (xCoord
											- Math.round(fullHpLength / 2) + x),
									yCoord - radius - 5 + y,
									Color.BLACK.getRGB());
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
			}
			// draw the player names using drawString method
			String characterName = character.getPlayerName();
			Font font = new Font("Arial", Font.PLAIN, 15);
			Graphics2D g2d = fancyMap.createGraphics();
			FontMetrics metrics = g2d.getFontMetrics(font);
			int length = metrics.stringWidth(characterName);
			g2d.setColor(Color.BLACK);
			g2d.setFont(font);
			g2d.drawString(characterName, xCoord - (length / 2), yCoord
					- radius - 20);

			// type string
			String type = "<" + character.getType().toString() + ">";
			Font typeFont = new Font("Jokerman", Font.PLAIN, 9);
			FontMetrics typeMetrics = g2d.getFontMetrics(typeFont);
			int typeLength = typeMetrics.stringWidth(type);
			g2d.setColor(Color.BLACK);
			g2d.setFont(typeFont);
			g2d.drawString(type, xCoord - (typeLength / 2), yCoord - radius - 9);

			// draw arrow to indicate the active player

			if (playGame != null) {
				if (playGame.getActiveCharacter().equals(character)) {
					for (int x = 0; x < arrow.getWidth(); x++) {
						for (int y = 0; y < arrow.getHeight(); y++) {
							int rgb = arrow.getRGB(x, y);
							if (rgb != -1) {
								try {
									fancyMap.setRGB(xCoord - radius + x, yCoord
											- 50 - arrow.getHeight() + y, rgb);
								} catch (ArrayIndexOutOfBoundsException e) {
								}
							}
						}
					}
				}
			}

			loadCharacterFlags(character);
		}
	}

	/**
	 * Load all the flags for a specific character
	 * 
	 * @param character
	 */
	public void loadCharacterFlags(Character character) {
		int xCoord = character.getCurrCoord().getxCoord();
		int yCoord = character.getCurrCoord().getyCoord();

		// poison flag
		if (character.getFlags().get(0) != null
				&& character.getFlags().get(0).getMovesLeft() > 0) {
			for (int x = 0; x < skull.getWidth(); x++) {
				for (int y = 0; y < skull.getHeight(); y++) {
					int rgb = skull.getRGB(x, y);

					try {
						if (rgb != 0) {
							fancyMap.setRGB(xCoord - radius - 15 + x, yCoord
									- skull.getHeight() + y, rgb);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

				}
			}
		}

		// healing flag
		if (character.getFlags().get(1) != null
				&& character.getFlags().get(1).getMovesLeft() > 0) {
			for (int x = 0; x < healing.getWidth(); x++) {
				for (int y = 0; y < healing.getHeight(); y++) {
					int rgb = healing.getRGB(x, y);
					try {
						if (rgb != 0) {
							fancyMap.setRGB(xCoord - radius + 30 + x, yCoord
									- healing.getHeight() + y, rgb);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

				}
			}
		}

		// enraged flag
		if (character.getFlags().get(2) != null
				&& character.getFlags().get(2).getMovesLeft() > 0) {
			for (int x = 0; x < flame.getWidth(); x++) {
				for (int y = 0; y < flame.getHeight(); y++) {
					int rgb = flame.getRGB(x, y);

					try {
						if (rgb != 0) {
							fancyMap.setRGB(xCoord - radius + 30 + x, yCoord
									- flame.getHeight() + y + 16, rgb);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

				}
			}
		}

		// shielding flag
		if (character.getFlags().get(3) != null
				&& character.getFlags().get(3).getMovesLeft() > 0) {
			for (int x = 0; x < shield.getWidth(); x++) {
				for (int y = 0; y < shield.getHeight(); y++) {
					int rgb = shield.getRGB(x, y);

					try {
						if (rgb != 0) {
							fancyMap.setRGB(xCoord - radius + 30 + x, yCoord
									- shield.getHeight() + y, rgb);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

				}
			}
		}
	}

	/**
	 * Animate a crate drop f
	 * 
	 * @param xCoord
	 *            X coord of the crate
	 * @param yCoord
	 *            Y coord of the crate
	 * @param crateType
	 *            The type of crate involved
	 * @param rand
	 *            Generate the item
	 */
	public void animateCrateDrop(int xCoord, int yCoord, String crateType,
			int rand) {
		// depending on type of crate:
		MiscObject crate = null;
		switch (crateType) {
		case "HpCrate":
			crate = new HpCrate(new Coordinates(xCoord, yCoord), rand);
			break;
		case "WeaponCrate":
			crate = new WeaponCrate(new Coordinates(xCoord, yCoord), rand);
			break;
		case "DmgBoostCrate":
			crate = new DamageBoost(new Coordinates(xCoord, yCoord), rand);
			break;
		case "DmgReductionCrate":
			crate = new DamageReduction(new Coordinates(xCoord, yCoord), rand);
			break;
		default:
		}
		objects.add(crate);
		int radius = (crate.getDisplaySprite().getWidth() / 2);
		for (int y = 0 + radius; y <= yCoord; y++) {
			// set coord on bitmap
			crate.setCurrCoord(new Coordinates(xCoord, y));
			// draw it
			drawObject(crate);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Load all objects onto the map
	 */
	public void loadObjects() {
		try {
			for (MiscObject obj : objects) {
				if (obj.getHp() > 0)
					drawObject(obj);
			}
		} catch (ConcurrentModificationException e) {
			for (MiscObject obj : objects) {
				if (obj.getHp() > 0)
					drawObject(obj);
			}
		}

	}

	/**
	 * Draw a single object to the map
	 * 
	 * @param obj
	 */
	private void drawObject(MiscObject obj) {
		int width = obj.getDisplaySprite().getWidth();
		int height = obj.getDisplaySprite().getHeight();
		BufferedImage displaySprite = obj.getDisplaySprite();
		int currX = obj.getCurrCoord().getxCoord();
		int currY = obj.getCurrCoord().getyCoord();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = displaySprite.getRGB(i, j);
				// ignore transparency
				int alpha = (rgb >> 24) & 0xff;
				if (alpha != 0) {
					try {
						fancyMap.setRGB(currX - (width / 2) + i, currY
								- (height / 2) + j, rgb);

					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
			}
		}
	}

	/**
	 * Update the map after a shot has been fired
	 * 
	 * @param coord
	 * @param weapon
	 */
	public void updateAfterShot(Coordinates coord, Weapon weapon) {
		weapon.updateAfterShot(coord, bitMap, characters, 0,
				getActiveCharacter(), this);
	}

	/**
	 * Animates the shot, updating the image
	 * 
	 * @param weapon
	 * @param path
	 */
	public void animateShot(Weapon weapon, ArrayList<Coordinates> path) {
		weapon.animateShot(fancyMap, path, getActiveCharacter(), characters,
				this);

	}

	/**
	 * Get the bitmap associated with this map
	 * 
	 * @return
	 */
	public Map getBitMap() {
		return bitMap;
	}

	/**
	 * Set the characters on the current map
	 * 
	 * @param characters
	 */
	public void setCharacters(Character[] characters) {
		this.characters = characters;
	}

	/**
	 * Return the charactes on this engine
	 * 
	 * @return
	 */
	public Character[] getCharacters() {
		return characters;
	}

	/**
	 * Get the fancymap
	 * 
	 * @return
	 */
	public BufferedImage getFancyMap() {
		return fancyMap;
	}

	/**
	 * Animates the move to the left of the character of specified ID
	 * 
	 * @param ID
	 */
	public void animateMoveLeft(int ID) {

		// load the character and his location
		Character activeCharacter = characters[ID];
		Coordinates oldCoord = activeCharacter.getCurrCoord();
		Coordinates movingCoord = activeCharacter.getCurrCoord();
		// load the image initially
		loadImage();
		if (activeCharacter.isStuck(bitMap)) {
			return;
		}

		objects.iterator();
		int t = 0;
		movingloop: for (int i = 1; i <= moveLength; i += 2) {

			if (t == 6)
				t = 0;
			movingCoord = new Coordinates(oldCoord.getxCoord() - i,
					bitMap.getFirstY(oldCoord.getxCoord() - i) - radius);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.NORMAL)
				activeCharacter
						.setDisplaySprite(blue_normal_animation_frames_left[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.NORMAL)
				activeCharacter
						.setDisplaySprite(orange_normal_animation_frames_left[t]);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.ATTACKER)
				activeCharacter
						.setDisplaySprite(blue_warrior_animation_frames_left[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.ATTACKER)
				activeCharacter
						.setDisplaySprite(orange_warrior_animation_frames_left[t]);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.TANK)
				activeCharacter
						.setDisplaySprite(blue_defender_animation_frames_left[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.TANK)
				activeCharacter
						.setDisplaySprite(orange_defender_animation_frames_left[t]);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.HEALER)
				activeCharacter
						.setDisplaySprite(blue_healer_animation_frames_left[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.HEALER)
				activeCharacter
						.setDisplaySprite(orange_healer_animation_frames_left[t]);

			t++;
			if (movingCoord.getxCoord() <= radius) {
				break;
			}

			activeCharacter.setCurrCoord(movingCoord);
			activeCharacter.setOldCoord(movingCoord);
			ArrayList<MiscObject> toRemove = new ArrayList<MiscObject>();

			for (MiscObject obj : objects) {
				int movingX = movingCoord.getxCoord();
				int movingY = movingCoord.getyCoord();
				ArrayList<Coordinates> allCoords = obj.getAllCoordinates();

				coordloop: for (int k = movingX - radius; k < movingX + radius; k++) {
					for (int j = movingY - radius; j < movingY + radius; j++) {
						Coordinates onPath = new Coordinates(k, j);
						if (allCoords.contains(onPath)) {
							int status = obj.updateAfterCrate(activeCharacter,
									this);
							if (status == 0) {

								break movingloop; // we have encountered a wall
													// or a
													// tree. STOP MOVING.
							} else if (status != 2) {
								activeCharacter.addPickedCrate(obj
										.getLastUpdate());
								obj.setHp(0);
								toRemove.add(obj);
								break coordloop;
							}

						}
					}
				}

			}

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

	/**
	 * Animates the move to the right of character of specified ID
	 * 
	 * @param ID
	 */
	public void animateMoveRight(int ID) {

		Character activeCharacter = characters[ID];
		Coordinates oldCoord = activeCharacter.getCurrCoord();
		Coordinates movingCoord = activeCharacter.getCurrCoord();
		bitMap.getWidth();
		bitMap.getHeight();
		loadImage();
		objects.iterator();
		if (activeCharacter.isStuck(bitMap)) {
			return;
		}

		int t = 0;
		movingloop: for (int i = 1; i <= moveLength; i += 2) {

			if (t == 6)
				t = 0;
			movingCoord = new Coordinates(oldCoord.getxCoord() + i,
					bitMap.getFirstY(oldCoord.getxCoord() + i) - radius);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.NORMAL)
				activeCharacter
						.setDisplaySprite(blue_normal_animation_frames_right[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.NORMAL)
				activeCharacter
						.setDisplaySprite(orange_normal_animation_frames_right[t]);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.ATTACKER)
				activeCharacter
						.setDisplaySprite(blue_warrior_animation_frames_right[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.ATTACKER)
				activeCharacter
						.setDisplaySprite(orange_warrior_animation_frames_right[t]);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.TANK)
				activeCharacter
						.setDisplaySprite(blue_defender_animation_frames_right[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.TANK)
				activeCharacter
						.setDisplaySprite(orange_defender_animation_frames_right[t]);

			if (activeCharacter.getTeamNumber() == 0
					&& activeCharacter.getType() == Character.TYPE.HEALER)
				activeCharacter
						.setDisplaySprite(blue_healer_animation_frames_right[t]);

			if (activeCharacter.getTeamNumber() == 1
					&& activeCharacter.getType() == Character.TYPE.HEALER)
				activeCharacter
						.setDisplaySprite(orange_healer_animation_frames_right[t]);

			t++;
			if (movingCoord.getxCoord() >= bitMap.getWidth() - radius) {
				break;

			}

			activeCharacter.setCurrCoord(movingCoord);
			activeCharacter.setOldCoord(movingCoord);

			// check for miscobject on the movingCoord

			ArrayList<MiscObject> toRemove = new ArrayList<MiscObject>();

			for (MiscObject obj : objects) {
				int movingX = movingCoord.getxCoord();
				int movingY = movingCoord.getyCoord();

				ArrayList<Coordinates> allCoords = obj.getAllCoordinates();

				coordloop: for (int k = movingX - radius; k < movingX + radius; k++) {
					for (int j = movingY - radius; j < movingY + radius; j++) {
						Coordinates onPath = new Coordinates(k, j);
						if (allCoords.contains(onPath)) {
							int status = obj.updateAfterCrate(activeCharacter,
									this);
							if (status == 0) {
								break movingloop; // we have encountered a wall
													// or a
													// tree. STOP MOVING.
							} else if (status != 2) {

								activeCharacter.addPickedCrate(obj
										.getLastUpdate());
								obj.setHp(0);
								toRemove.add(obj);
								break coordloop;
							}
						}
					}
				}

			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Get the active character
	 * @return the current active characters
	 */
	public Character getActiveCharacter() {
		Character result = characters[0];
		for (Character character : characters) {
			if (character != null && playGame != null) {
				if (playGame.getActiveCharacter().equals(character)) {
					result = character;
				}
			}
		}
		return result;
	}

	/*
	 * General getters/setters
	 */
	public BufferedImage getSkyTexture() {
		return MainEngine.skyTexture;
	}

	public BufferedImage getGroundTexture() {
		return MainEngine.groundTexture;
	}

	public static int getRadius() {
		return radius;
	}

	public List<MiscObject> getObjects() {
		return objects;
	}

	public int getHeight() {
		return bitMap.getHeight();
	}

	public int getWidth() {
		return bitMap.getWidth();
	}

}
