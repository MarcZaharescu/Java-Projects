package gameEngine;

import gameEngine.flags.Flag;
import gameEngine.tools.Coordinates;
import gameEngine.weapons.Bazooka;
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
import gameEngine.weapons.SkipTurn;
import gameEngine.weapons.Teleport;
import gameEngine.weapons.Weapon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;

/**
 * Class that encapsulates character information and functionality
 * 
 * 
 */
public class Character {

	public static enum TYPE {
		NORMAL, ATTACKER, TANK, HEALER
	}

	private BufferedImage displaySprite;
	// size of the bounding square for the player (squareSize x squareSize)
	private final int squareSize = 30;
	private final int radius = squareSize / 2;
	private ArrayList<Weapon> weapons;
	private int currWeapon;
	// the coordinate pair of the center of the character
	private Coordinates currCoord;
	// old coordinate before move - initialized with default value
	private Coordinates oldCoord = new Coordinates(300, 300);
	// team number is either 0 or 1.
	private int teamNumber;
	private ArrayList<Flag> flags;
	// significant character attributes
	private int hp;
	private String playerName;
	private int playerID;
	private int moveLength = 150;
	private double damageAmp;
	private double damageRed;
	private int fullHP;
	private double healingAmp;
	private double healingRed;
	private TYPE type;
	// Buffered images for different roles/teams
	private BufferedImage bluesheepanimationright;
	private BufferedImage bluesheepwarrioranimationright;
	private BufferedImage bluesheephealeranimationright;
	private BufferedImage bluesheepdefenderanimationright;
	private BufferedImage orangesheepanimationleft;
	private BufferedImage orangesheepwarrioranimationleft;
	private BufferedImage orangesheephealeranimationleft;
	private BufferedImage orangesheepdefenderanimationleft;
	private BufferedImage[] orange_normal_animation_frames_left = new BufferedImage[6];
	private BufferedImage[] orange_warrior_animation_frames_left = new BufferedImage[6];
	private BufferedImage[] orange_healer_animation_frames_left = new BufferedImage[6];
	private BufferedImage[] orange_defender_animation_frames_left = new BufferedImage[6];
	private BufferedImage[] blue_normal_animation_frames_right = new BufferedImage[6];
	private BufferedImage[] blue_warrior_animation_frames_right = new BufferedImage[6];
	private BufferedImage[] blue_healer_animation_frames_right = new BufferedImage[6];
	private BufferedImage[] blue_defender_animation_frames_right = new BufferedImage[6];
	private boolean blocking;

	private HashSet<String> pickedCrates;

	/**
	 * Initializes a character
	 * 
	 * @param playerID
	 *            allocated in order of joining the game
	 * @param playerName
	 *            the name string of the player
	 * @param type
	 *            The type of character
	 */
	public Character(int playerID, String playerName, String type) {
		weapons = new ArrayList<Weapon>();

		pickedCrates = new HashSet<String>();

		// initialize flags - no flags at the start
		flags = new ArrayList<Flag>(4);
		flags.add(null);
		flags.add(null);
		flags.add(null);
		flags.add(null);

		// player attributes
		this.playerID = playerID;
		this.playerName = playerName;
		teamNumber = 0;
		this.type = stringToType(type);

		// All classes have a bazooka
		weapons.add(new Bazooka());

		// initialise the correct weapons/sprites based on type
		switch (this.type) {
		case NORMAL:
			initializeNormal();
			break;
		case ATTACKER:
			initializeAttacker();
			break;
		case TANK:
			initializeTank();
			break;
		case HEALER:
			initializeHealer();
			break;
		}
	}

	/**
	 * Initialise for the normal role
	 */
	private void initializeNormal() {
		// Set HP
		fullHP = 1000;
		hp = fullHP;

		// Set weapons
		weapons.add(new Lazer());
		weapons.add(new RocketLauncher());
		weapons.add(new PoisonDart());
		weapons.add(new FireMine());

		// set damage modifiers
		currWeapon = 0;
		moveLength = 100;
		damageAmp = 0;
		damageRed = 0;
		healingAmp = 0;
		healingRed = 0;

		// Set sprites based on team
		if (playerID % 2 == 0) {
			try {
				this.bluesheepanimationright = ImageIO.read(new File("img"
						+ File.separator + "characterAnimations"
						+ File.separator + "bluesheepanimationright.png"));
				blue_normal_animation_frames_right[0] = bluesheepanimationright
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(blue_normal_animation_frames_right[0]);
		} else {
			try {
				this.orangesheepanimationleft = ImageIO.read(new File("img"
						+ File.separator + "characterAnimations"
						+ File.separator + "orangesheepanimationleft.png"));
				orange_normal_animation_frames_left[0] = orangesheepanimationleft
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(orange_normal_animation_frames_left[0]);
		}
	}

	/**
	 * Initialise for the attacker role
	 */
	private void initializeAttacker() {
		fullHP = 800;
		hp = fullHP;
		weapons.add(new Lazer());
		weapons.add(new RocketLauncher());
		weapons.add(new PoisonDart());
		weapons.add(new Enrage());
		weapons.add(new Teleport());

		// set damage modifiers
		currWeapon = 0;
		moveLength = 150;
		damageAmp = 0.3;
		damageRed = -0.1;
		healingAmp = 0.2;
		healingRed = 0;

		// Set sprites based on team
		if (playerID % 2 == 0) {
			try {
				this.bluesheepwarrioranimationright = ImageIO.read(new File(
						"img" + File.separator + "characterAnimations"
								+ File.separator
								+ "bluesheepwarrioranimationright.png"));
				blue_warrior_animation_frames_right[0] = bluesheepwarrioranimationright
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(blue_warrior_animation_frames_right[0]);
		} else {
			try {
				this.orangesheepwarrioranimationleft = ImageIO.read(new File(
						"img" + File.separator + "characterAnimations"
								+ File.separator
								+ "orangesheepwarrioranimationleft.png"));
				orange_warrior_animation_frames_left[0] = orangesheepwarrioranimationleft
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(orange_warrior_animation_frames_left[0]);
		}
	}

	/**
	 * Initialise for the tank role
	 */
	private void initializeTank() {
		fullHP = 1600;
		hp = fullHP;
		weapons.add(new FireMine());
		weapons.add(new DirtCircle());
		weapons.add(new Shield());
		weapons.add(new FireWall());
		// set damage modifiers
		currWeapon = 0;
		moveLength = 80;
		damageAmp = -0.3;
		damageRed = 0.1;
		healingAmp = 0;
		healingRed = 0.2;

		// Set sprites based on team
		if (playerID % 2 == 0) {
			try {
				this.bluesheepdefenderanimationright = ImageIO.read(new File(
						"img" + File.separator + "characterAnimations"
								+ File.separator
								+ "bluesheepdefenderanimationright.png"));
				blue_defender_animation_frames_right[0] = bluesheepdefenderanimationright
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(blue_defender_animation_frames_right[0]);
		} else {
			try {
				this.orangesheepdefenderanimationleft = ImageIO.read(new File(
						"img" + File.separator + "characterAnimations"
								+ File.separator
								+ "orangesheepdefenderanimationleft.png"));
				orange_defender_animation_frames_left[0] = orangesheepdefenderanimationleft
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(orange_defender_animation_frames_left[0]);
		}
	}

	/**
	 * Initialise for the healer role
	 */
	private void initializeHealer() {
		fullHP = 700;
		hp = fullHP;
		weapons.add(new MediGun());
		weapons.add(new HealingBuff());
		weapons.add(new FireWall());
		weapons.add(new Teleport());
		// set damage modifiers
		currWeapon = 0;
		moveLength = 100;
		damageAmp = -0.2;
		damageRed = 0.1;
		healingAmp = 0.35;
		healingRed = 0;

		// Set sprites based on team
		if (playerID % 2 == 0) {
			try {
				this.bluesheephealeranimationright = ImageIO.read(new File(
						"img" + File.separator + "characterAnimations"
								+ File.separator
								+ "bluesheephealeranimationright.png"));
				blue_healer_animation_frames_right[0] = bluesheephealeranimationright
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(blue_healer_animation_frames_right[0]);
		} else {
			try {
				this.orangesheephealeranimationleft = ImageIO.read(new File(
						"img" + File.separator + "characterAnimations"
								+ File.separator
								+ "orangesheephealeranimationleft.png"));
				orange_healer_animation_frames_left[0] = orangesheephealeranimationleft
						.getSubimage(0, 30 * (0), 30, 30);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setDisplaySprite(orange_healer_animation_frames_left[0]);
		}
	}

	/**
	 * Switches to next weapon
	 */
	public void nextWeapon() {
		currWeapon++;
		if (currWeapon >= weapons.size()) {
			currWeapon = 0;
		}
	}

	/**
	 * Switches to previous weapon
	 */
	public void prevWeapon() {
		currWeapon--;
		if (currWeapon < 0) {
			currWeapon = weapons.size() - 1;
		}
	}

	/**
	 * Get the list of weapons this player has
	 * 
	 * @return
	 */
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	/**
	 * Get the current weapon for this player
	 * 
	 * @return
	 */
	public Weapon getCurrWeapon() {
		return weapons.get(this.currWeapon);
	}

	/**
	 * Set the weapons for this character
	 * 
	 * @param weapons
	 */
	public void setWeapons(ArrayList<Weapon> weapons) {
		this.weapons = weapons;
	}

	/**
	 * Set the current weapon
	 * 
	 * @param currWeapon
	 */
	public void setCurrWeapon(Weapon currWeapon) {
		for (int i = 0; i < weapons.size(); i++) {
			if (currWeapon.equals(weapons.get(i))) {
				this.currWeapon = i;
				break;
			}
		}
	}

	/**
	 * Returns all the coordinates the player is on
	 * 
	 * @return
	 */
	public ArrayList<Coordinates> getAllCoordinates() {
		ArrayList<Coordinates> allCoords = new ArrayList<Coordinates>();
		for (int i = 0; i < squareSize; i++) {
			for (int j = 0; j < squareSize; j++) {
				allCoords.add(new Coordinates(currCoord.getxCoord() - radius
						+ i, currCoord.getyCoord() - radius + j));
			}
		}
		return allCoords;
	}

	/**
	 * Delegates the fire method call to the current weapon used
	 * 
	 * @param power
	 *            The power of the shot
	 * @param angle
	 *            The angle in relation to the active player
	 * @return
	 */
	public ArrayList<Coordinates> fire(double power, double angle,
			Character[] characters, double windSpeed, double windAngle,
			MainEngine engine) {
		return weapons.get(currWeapon).fire(power, angle, currCoord, this,
				characters, windSpeed, windAngle, engine);
	}

	/**
	 * Get the displaysprite for the character at the current point in time
	 * 
	 * @return
	 */
	public BufferedImage getDisplaySprite() {
		return displaySprite;
	}

	/**
	 * Get the square encompassing the sprite
	 * 
	 * @return
	 */
	public int getSquareSize() {
		return this.squareSize;
	}

	/**
	 * Get the radius around the current character
	 * 
	 * @return
	 */
	public int getRadius() {
		return this.radius;
	}

	/**
	 * Get the current coordinates of this character
	 * 
	 * @return
	 */
	public Coordinates getCurrCoord() {
		return currCoord;
	}

	/**
	 * Set the current coordinates of this character
	 * 
	 * @param currCoord
	 */
	public void setCurrCoord(Coordinates currCoord) {
		this.currCoord = currCoord;
	}

	/**
	 * Get the old coordinate
	 * 
	 * @return
	 */
	public Coordinates getOldCoord() {
		return oldCoord;
	}

	/**
	 * Set the previous coordinate for animating purposes
	 * 
	 * @param currCoord
	 */
	public void setOldCoord(Coordinates currCoord) {
		this.oldCoord = currCoord;
	}

	/**
	 * Get all of the pickedup crates for this character
	 * 
	 * @return A set of all crates
	 */
	public HashSet<String> getPickedCrates() {
		return this.pickedCrates;
	}

	/**
	 * Reset the picked up crates for this character
	 */
	public void resetCrates() {
		this.pickedCrates = new HashSet<String>();
	}

	/**
	 * Add a crate to this characters set of picked up crates
	 * 
	 * @param crate
	 */
	public void addPickedCrate(String crate) {
		boolean status = this.pickedCrates.add(crate);
	}

	/**
	 * Get the current HP of this character.
	 * 
	 * @return
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Set the HP for this character
	 * 
	 * @param hp
	 */
	public void setHp(int hp) {
		this.hp = hp;
		if (hp > this.fullHP) {
			this.hp = this.fullHP;
		}
		if (hp < 0) {
			this.hp = 0;
		}
	}

	/**
	 * Get the fullHP for this role
	 * 
	 * @return
	 */
	public int getFullHP() {
		return fullHP;
	}

	/**
	 * Set the maximum HP for this character
	 * 
	 * @param fullHP
	 */
	public void setFullHP(int fullHP) {
		this.fullHP = fullHP;
	}

	/**
	 * Get this players name
	 * 
	 * @return
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Set this players name
	 * 
	 * @param playerName
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Get the team number for this player
	 * 
	 * @return
	 */
	public int getTeamNumber() {
		return teamNumber;
	}

	/**
	 * Set the team number for this player
	 * 
	 * @param teamNumber
	 */
	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}

	/**
	 * The distance this role character can move
	 * 
	 * @return
	 */
	public int getMoveLength() {
		return moveLength;
	}

	/**
	 * Set the current displaySprite for this character
	 * 
	 * @param displaySprite
	 */
	public void setDisplaySprite(BufferedImage displaySprite) {
		this.displaySprite = displaySprite;
	}

	/**
	 * Get this characters player ID
	 * 
	 * @return
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Reset the damage modifiers for the current character
	 */
	public void resetModifiers() {
		switch (this.type) {
		case NORMAL:
			this.damageAmp = 0;
			break;
		case ATTACKER:
			this.damageAmp = 0.3;
			break;
		case TANK:
			this.damageAmp = -0.3;
			break;
		case HEALER:
			this.damageAmp = -0.2;
			break;
		}
	}

	/**
	 * Get the current damage amplifier for this character
	 * 
	 * @return
	 */
	public double getDamageAmp() {
		return damageAmp;
	}

	/**
	 * Set the current damage amplifier for this character
	 * 
	 * @param damegeAmp
	 */
	public void setDamageAmp(double damegeAmp) {
		this.damageAmp = damegeAmp;
	}

	/**
	 * Get the current damage reduction amplifier for this character
	 * 
	 * @return
	 */
	public double getDamageRed() {
		return damageRed;
	}

	/**
	 * Get the current damage reduction amplifier for this character
	 * 
	 * @param damageRed
	 */
	public void setDamageRed(double damageRed) {
		this.damageRed = damageRed;
	}

	/**
	 * Get the current healing amplifier for this character
	 * 
	 * @return
	 */
	public double getHealingAmp() {
		return healingAmp;
	}

	/**
	 * Set the current healing amplifier
	 * 
	 * @param healingAmp
	 */
	public void setHealingAmp(double healingAmp) {
		this.healingAmp = healingAmp;
	}

	/**
	 * Get the healing reduction this character has
	 * 
	 * @return
	 */
	public double getHealingRed() {
		return healingRed;
	}

	/**
	 * Set the healing reduction this character has
	 * 
	 * @param healingRed
	 */
	public void setHealingRed(double healingRed) {
		this.healingRed = healingRed;
	}

	/**
	 * Add a flag for the current player
	 * 
	 * @param flag
	 */
	public void addFlag(Flag flag) {
		switch (flag.getType()) {
		case "Poison":
			flags.set(0, flag);
			break;
		case "Healing":
			flags.set(1, flag);
			break;
		case "Enraged":
			flags.set(2, flag);
			break;
		case "Shielding":
			flags.set(3, flag);
		}
	}

	/**
	 * Handles all circumstances required to deal with flags
	 * 
	 * @param scale
	 *            how hard flag will affect player
	 */
	public void dealWithFlags(int scale) {
		for (Flag flag : flags) {
			if (flag != null) {
				if (flag.getMovesLeft() > 0) {
					flag.execute(scale);
				} else {
					flag = null;
				}
			}
		}

	}

	/**
	 * Get the flags currently active on this player
	 * 
	 * @return
	 */
	public ArrayList<Flag> getFlags() {
		return this.flags;
	}

	/**
	 * Set the distance per move for this character
	 * 
	 * @param moveLength
	 */
	public void setMoveLength(int moveLength) {
		this.moveLength = moveLength;
	}

	/**
	 * Return the type (Character class) of this character
	 * 
	 * @return
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * Check if this character is blocking moves
	 * 
	 * @return
	 */
	public boolean isBlocking() {
		return this.blocking;
	}

	/**
	 * Set if this character is blocking moves
	 * 
	 * @param blocking
	 */
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	/**
	 * return if a player is stuck in dirtCircle
	 */
	public boolean isStuck(Map bitmap) {
		int countNumberOfPixelStuck = 0;
		for (Coordinates coord : getAllCoordinates()) {
			try {
				if (bitmap.getCoord(coord.getxCoord(), coord.getyCoord()) == 1) {
					countNumberOfPixelStuck++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				countNumberOfPixelStuck++;
			}

		}
		return countNumberOfPixelStuck >= 600;
	}

	/**
	 * Convert a string to a type
	 * 
	 * @param type
	 * @return
	 */
	private TYPE stringToType(String type) {
		switch (type.toUpperCase()) {
		case "ATTACKER":
			return TYPE.ATTACKER;
		case "HEALER":
			return TYPE.HEALER;
		case "NORMAL":
			return TYPE.NORMAL;
		case "TANK":
			return TYPE.TANK;
		default:
			return null;
		}
	}

	/**
	 * To string method for Character
	 */
	public String toString() {
		return playerID + "\t" + playerName + "\t" + getType().toString()
				+ "\t" + teamNumber;
	}

	/**
	 * Equals method
	 * 
	 * @param character
	 * @return
	 */
	public boolean equals(Character character) {
		boolean result;
		try {
			result = playerID == character.getPlayerID();
		} catch (NullPointerException e) {
			result = false;
		}
		return result;
	}
}