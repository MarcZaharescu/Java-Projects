package test;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.objects.DamageBoost;
import gameEngine.objects.DamageReduction;
import gameEngine.objects.HpCrate;
import gameEngine.objects.WeaponCrate;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Class that contains tests for the main engine 
 * Both unit and integration tests
 *
 */
public class EngineTest {

	private Map mappy;
	private Character[] characters;
	private MainEngine dispy;
	private int numberofCharacters;
	private int squareSize;

	@BeforeClass
	public void setUP() {
		// in this test class all the modifications of the characters are both
		// checked through an
		// appropriate assertion method and written images of the current state
		// of the game in the temp folder

		// set the number of characters to 8
		numberofCharacters = 8;

		// initialise the characters
		characters = new Character[8];
		characters[0] = new Character(0, "Character" + String.valueOf(0),
				"NORMAL");
		characters[1] = new Character(1, "Character" + String.valueOf(1),
				"HEALER");
		characters[2] = new Character(2, "Character" + String.valueOf(2),
				"ATTACKER");
		characters[3] = new Character(3, "Character" + String.valueOf(3),
				"TANK");

		// get the square size of the chatacter
		squareSize = characters[0].getSquareSize();

		// initialise the bitmap
		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		// initialise the display map
		try {
			dispy = new MainEngine(characters, mappy, null);
			dispy.setCharacters(characters);

		} catch (IOException e) {

			e.printStackTrace();
		}

		// put the characters on the map and draw the image
		dispy.placeCharacters();
		// dispy.loadImage();

		try {
			ImageIO.write(dispy.getFancyMap(), "png", new File("temp" + seppy
					+ "map.png"));
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Test
	public void setWeapontest() {
		// tests the weapons for every character
		characters[0] = new Character(0, "Character" + String.valueOf(0),
				"NORMAL");
		characters[1] = new Character(1, "Character" + String.valueOf(1),
				"HEALER");
		characters[2] = new Character(2, "Character" + String.valueOf(2),
				"ATTACKER");
		characters[3] = new Character(3, "Character" + String.valueOf(3),
				"TANK");

		Weapon bazooka = new Bazooka();
		Weapon lazer = new Lazer();
		Weapon rocket = new RocketLauncher();
		Weapon poison = new PoisonDart();
		Weapon enrage = new Enrage();
		Weapon dirt = new DirtCircle();
		Weapon shield = new Shield();
		Weapon healing = new HealingBuff();
		Weapon medigun = new MediGun();
		Weapon wall = new FireWall();

		for (int i = 0; i < 4; i++) {
			switch (i) {

			case 0: {
				characters[i].setCurrWeapon(bazooka);
				Assert.assertEquals("Bazooka", characters[i].getCurrWeapon()
						.toString(), "Bazzoka set test for player " + i);

				characters[i].setCurrWeapon(rocket);
				Assert.assertEquals("RocketLauncher", characters[i]
						.getCurrWeapon().toString(),
						"Rocket set test for player " + i);

				characters[i].setCurrWeapon(lazer);
				Assert.assertEquals("Lazer", characters[i].getCurrWeapon()
						.toString(), "Lazer set test for player " + i);

				characters[i].setCurrWeapon(poison);
				Assert.assertEquals("PoisonDart", characters[i].getCurrWeapon()
						.toString(), "PosionDart test for player " + i);
				break;
			}

			case 1: {
				characters[i].setCurrWeapon(bazooka);
				Assert.assertEquals("Bazooka", characters[i].getCurrWeapon()
						.toString(), "Bazzoka set test for player " + i);

				characters[i].setCurrWeapon(wall);
				Assert.assertEquals("FireWall", characters[i].getCurrWeapon()
						.toString(), "FireWall set test for player " + i);

				characters[i].setCurrWeapon(healing);
				Assert.assertEquals("HealingBuff", characters[i]
						.getCurrWeapon().toString(),
						"HealingBuff set test for player " + i);

				characters[i].setCurrWeapon(medigun);
				Assert.assertEquals("MediGun", characters[i].getCurrWeapon()
						.toString(), "MediGun set test for player " + i);

				break;
			}

			case 2: {
				characters[i].setCurrWeapon(bazooka);
				Assert.assertEquals("Bazooka", characters[i].getCurrWeapon()
						.toString(), "Bazzoka set test for player " + i);

				characters[i].setCurrWeapon(rocket);
				Assert.assertEquals("RocketLauncher", characters[i]
						.getCurrWeapon().toString(),
						"Rocket set test for player " + i);

				characters[i].setCurrWeapon(lazer);
				Assert.assertEquals("Lazer", characters[i].getCurrWeapon()
						.toString(), "Lazer set test for player " + i);

				characters[i].setCurrWeapon(enrage);
				Assert.assertEquals("Enrage", characters[i].getCurrWeapon()
						.toString(), "Enrage test for player " + i);
				break;

			}

			case 3: {
				characters[i].setCurrWeapon(bazooka);
				Assert.assertEquals("Bazooka", characters[i].getCurrWeapon()
						.toString(), "Bazzoka set test for player " + i);

				characters[i].setCurrWeapon(dirt);
				Assert.assertEquals("DirtCircle", characters[i].getCurrWeapon()
						.toString(), "Dirt set test for player " + i);

				characters[i].setCurrWeapon(shield);
				Assert.assertEquals("Shield", characters[i].getCurrWeapon()
						.toString(), "Shield test for player " + i);
				break;
			}
			}

		}
	}

	@Test
	public void setCurrentCoordinates() {
		// tests the characters positions on different coordinates
		Coordinates coord = new Coordinates(100, 100);
		Coordinates coord1 = new Coordinates(100000, 1000);
		Coordinates coord2 = new Coordinates(-1000, -1000);
		Coordinates coord3 = new Coordinates(-500, 100);

		for (int i = 0; i < numberofCharacters; i++) {
			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"NORMAL");
			characters[i].setCurrCoord(coord);
			Assert.assertEquals("X COORD: 100 Y COORD: 100", characters[i]
					.getCurrCoord().toString(),
					"getCurrentCoordinates test for player " + i);

			characters[i].setCurrCoord(coord1);
			Assert.assertEquals("X COORD: 100000 Y COORD: 1000", characters[i]
					.getCurrCoord().toString(),
					"getCurrentCoordinates test for player " + i);

			characters[i].setCurrCoord(coord2);
			Assert.assertEquals("X COORD: -1000 Y COORD: -1000", characters[i]
					.getCurrCoord().toString(),
					"getCurrentCoordinates test for player " + i);

			characters[i].setCurrCoord(coord3);
			Assert.assertEquals("X COORD: -500 Y COORD: 100", characters[i]
					.getCurrCoord().toString(),
					"getCurrentCoordinates test for player " + i);

		}
	}

	@Test
	public void getHPtest() {
		// sets different hp to the characters and tests if the changes occur
		for (int i = 0; i < numberofCharacters; i++) {

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"NORMAL");
			Assert.assertEquals(1000, characters[i].getHp(),
					"GetHP test for player " + i);

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"TANK");
			Assert.assertEquals(1600, characters[i].getHp(),
					"GetHP test for player " + i);

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"ATTACKER");
			Assert.assertEquals(800, characters[i].getHp(),
					"GetHP test for player " + i);

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"HEALER");

			Assert.assertEquals(700, characters[i].getHp(),
					"GetHP test for player " + i);

		}
	}

	@Test
	public void getMoveLengthtest() {
		// tests the move length for every character class
		for (int i = 0; i < numberofCharacters; i++) {

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"NORMAL");
			Assert.assertEquals(100, characters[i].getMoveLength(),
					"getMoveLength test for player " + i);

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"TANK");
			Assert.assertEquals(80, characters[i].getMoveLength(),
					"getMoveLength test for player " + i);

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"ATTACKER");
			Assert.assertEquals(150, characters[i].getMoveLength(),
					"getMoveLength test for player " + i);

			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"HEALER");
			Assert.assertEquals(100, characters[i].getMoveLength(),
					"getMoveLength test for player " + i);

		}
	}

	@Test
	public void getPlayerIDtest() {
		// tests if the id are set properly
		for (int i = 0; i < numberofCharacters; i++) {

			Assert.assertEquals(-1, characters[i].getPlayerID(),
					"getPlayerID test for player " + i);

		}
	}

	@Test
	public void getTeamNumbertest() {
		// tests if the team number is set correctly
		for (int i = 0; i < numberofCharacters; i++) {

			Assert.assertEquals(0, characters[i].getTeamNumber(),
					"getTeamNumber test for player " + i);

		}
	}

	@Test
	public void getRadiustest() {
		// tests the radius of the character
		for (int i = 0; i < numberofCharacters; i++) {

			Assert.assertEquals(15, characters[i].getRadius(),
					"getRadius test for player " + i);

		}
	}

	@Test
	public void getSquareSizetest() {
		// tests the square size of the characters
		for (int i = 0; i < numberofCharacters; i++) {

			Assert.assertEquals(30, characters[i].getSquareSize(),
					"getSquareSize test for player " + i);

		}
	}

	@Test
	public void getOldCoordinatest() {
		// tests if the old coordinates of the characters remain consistent
		Coordinates oldcoord = new Coordinates(300, 300);
		Coordinates currcoord = new Coordinates(500, 500);

		for (int i = 0; i < numberofCharacters; i++) {

			characters[i].setCurrCoord(oldcoord);
			characters[i].setCurrCoord(currcoord);
			Assert.assertEquals(oldcoord, characters[i].getOldCoord(),
					"getOldCoord test for player " + i);

		}
	}

	@Test
	public void setPlayerNametest() {
		// tests the name setting of the characters
		for (int i = 0; i < numberofCharacters; i++) {
			characters[i] = new Character(-1, "Character" + String.valueOf(i),
					"NORMAL");
			characters[i].setPlayerName("Player " + i);
			Assert.assertEquals("Player " + i, characters[i].getPlayerName(),
					"setPlayerName test for player " + i);

		}
	}

	@Test
	public void moveLefttest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and test moving left thought the map
		// on its path it fires a wall blocking its moving to the left. The
		// character position is set after the wall
		// and applies the moveLEft function till the edge of the map, where its
		// position remains consistent

		Character[] characters = new Character[8];
		characters[0] = new Character(0, "Character" + String.valueOf(0),
				"HEALER");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyLeft = null;
		try {
			dispyLeft = new MainEngine(characters, mappy, null);
			dispyLeft.setCharacters(characters);

		} catch (IOException e) {

			e.printStackTrace();
		}
		dispyLeft.placeCharacters();

		int moveLength = characters[0].getMoveLength();
		int radius = characters[0].getRadius();

		Coordinates oldcoord = new Coordinates(700, mappy.getFirstY(700)
				- radius);
		characters[0].setCurrCoord(oldcoord);
		dispyLeft.loadImage();

		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft1.png"));

		int i = 0;

		int oldXCoord = characters[0].getCurrCoord().getxCoord();
		int yCoord = mappy.getFirstY(oldXCoord + moveLength) - radius;

		// engineg path with bazooka

		Weapon bazooka = new Bazooka();
		characters[0].setCurrWeapon(bazooka);
		ArrayList<Coordinates> path = characters[0].fire(50, 150, characters,
				0, 0, dispyLeft);
		dispyLeft.animateShot(bazooka, path);

		// add a wall during the path
		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft2.png"));

		Weapon wall = new FireWall();
		characters[0].setCurrWeapon(wall);
		ArrayList<Coordinates> path1 = characters[0].fire(15, 150, characters,
				0, 0, dispyLeft);
		dispyLeft.animateShot(wall, path1);

		dispyLeft.loadImage();
		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft3.png"));
		int block = 0, blockwidth = 0, minimObjectDistance = moveLength + 10;
		dispyLeft.loadImage();

		for (int j = 0; j < dispyLeft.getObjects().size(); j++) {
			if (-oldXCoord
					+ (dispyLeft.getObjects().get(i).getCurrCoord().getxCoord()) < moveLength)
				block = dispyLeft.getObjects().get(i).getCurrCoord()
						.getxCoord();
			blockwidth = dispyLeft.getObjects().get(i).getWidth();

			{
				if (-oldXCoord
						+ dispyLeft.getObjects().get(i).getCurrCoord()
								.getxCoord() < minimObjectDistance) {
					block = dispyLeft.getObjects().get(i).getCurrCoord()
							.getxCoord();
					blockwidth = dispyLeft.getObjects().get(i).getWidth();
				}
			}
		}

		Coordinates currcoord;
		if (block > 0)
			currcoord = new Coordinates(block + blockwidth - 1 - 3, dispyLeft
					.getBitMap().getFirstY(block + blockwidth) - radius);
		else
			currcoord = new Coordinates(oldXCoord - moveLength - 1, yCoord);

		dispyLeft.animateMoveLeft(i);

		Assert.assertEquals(currcoord, characters[0].getCurrCoord(),
				"moveLefttest for player " + i);

		dispyLeft.loadImage();

		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft4.png"));

		// move to the end of the screen on the left hand side

		Coordinates coord = new Coordinates(300, mappy.getFirstY(300) - radius);
		characters[0].setCurrCoord(coord);
		dispyLeft.loadImage();

		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft5.png"));

		dispyLeft.animateMoveLeft(0);
		dispyLeft.loadImage();

		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft6.png"));
		dispyLeft.animateMoveLeft(0);
		dispyLeft.loadImage();

		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft7.png"));
		dispyLeft.animateMoveLeft(0);
		dispyLeft.loadImage();

		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft8.png"));
		dispyLeft.animateMoveLeft(0);
		dispyLeft.loadImage();

		ImageIO.write(dispyLeft.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateMoveLeft9.png"));

		Coordinates leftmapedge = new Coordinates(16, dispyLeft.getBitMap()
				.getFirstY(16) - squareSize / 2);
		Assert.assertEquals(leftmapedge, characters[i].getCurrCoord(),
				"moveLefttest for player " + i);

	}

	@Test
	public void moveRighttest() throws IOException {
		// isolates 2 characters on the map, character 0 being the active one
		// and test moving right thought the map
		// on its path it fires a wall blocking its moving to the right. The
		// character position is set after the wall
		// and applies the moveRight function till the edge of the map, where
		// its position remains consistent

		characters = new Character[8];
		characters[0] = new Character(0, "Character" + String.valueOf(0),
				"HEALER");
		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyRight = null;
		try {
			dispyRight = new MainEngine(characters, mappy, null);
			dispyRight.setCharacters(characters);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyRight.placeCharacters();
		int moveLength = characters[0].getMoveLength();
		int radius = characters[0].getRadius();

		Coordinates oldcoord = new Coordinates(200, mappy.getFirstY(200)
				- radius);
		characters[0].setCurrCoord(oldcoord);
		dispyRight.loadImage();

		try {
			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight1.png"));
		} catch (IOException e) {
		}

		int i = 0;

		{
			int oldXCoord = characters[i].getCurrCoord().getxCoord();
			int yCoord = mappy.getFirstY(oldXCoord + moveLength) - radius;

			// engineg path with bazooka
			Weapon bazooka = new Bazooka();
			characters[0].setCurrWeapon(bazooka);
			ArrayList<Coordinates> path = characters[0].fire(20, 20,
					characters, 10, 10, dispyRight);
			dispyRight.animateShot(bazooka, path);

			// add a wall during the path
			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight2.png"));

			Weapon wall = new FireWall();
			characters[0].setCurrWeapon(bazooka);
			ArrayList<Coordinates> path1 = characters[0].fire(10, 45,
					characters, 10, 10, dispyRight);
			dispyRight.animateShot(wall, path1);

			dispyRight.loadImage();
			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight3.png"));
			int block = 0, blockwidth = 0, minimObjectDistance = moveLength + 10;
			dispyRight.loadImage();

			for (int j = 0; j < dispyRight.getObjects().size(); j++) {
				if (oldXCoord
						- (dispyRight.getObjects().get(i).getCurrCoord()
								.getxCoord()) < moveLength)

				{
					if (oldXCoord
							- dispyRight.getObjects().get(i).getCurrCoord()
									.getxCoord() < minimObjectDistance) {
						block = dispyRight.getObjects().get(i).getCurrCoord()
								.getxCoord();
						blockwidth = dispyRight.getObjects().get(i).getWidth();

					}
				}
			}
			Coordinates currcoord;
			if (block > 0)
				currcoord = new Coordinates(block - blockwidth + 1, dispyRight
						.getBitMap()
						.getFirstY(block - blockwidth - radius - 10) + 30);
			else
				currcoord = new Coordinates(oldXCoord + moveLength - 1, yCoord);

			dispyRight.animateMoveRight(0);
			Assert.assertEquals(currcoord, characters[0].getCurrCoord(),
					"moveRighttest for player " + 0);

			dispyRight.loadImage();

			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight4.png"));

			// move to the end of the screen

			Coordinates coord = new Coordinates(700, mappy.getFirstY(700)
					- radius);

			characters[0].setCurrCoord(coord);
			dispyRight.loadImage();
			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight5.png"));

			dispyRight.animateMoveRight(0);
			dispyRight.loadImage();
			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight6.png"));

			dispyRight.animateMoveRight(0);
			dispyRight.loadImage();
			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight7.png"));

			dispyRight.animateMoveRight(0);
			dispyRight.loadImage();

			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight8.png"));
			dispyRight.animateMoveRight(0);
			dispyRight.loadImage();

			ImageIO.write(dispyRight.getFancyMap(), "png", new File("temp"
					+ seppy + "AnimateMoveRight9.png"));

			Coordinates mapedge = new Coordinates(dispyRight.getBitMap()
					.getWidth() - 1 - squareSize / 2, dispyRight.getBitMap()
					.getFirstY(dispyRight.getBitMap().getWidth() - 1)
					- squareSize / 2);
			Assert.assertEquals(mapedge, characters[i].getCurrCoord(),
					"moveRighttest for player " + i);

		}
	}

	@Test
	public void LazerShootTest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and tests lazer shoot
		// until the character 1 dies

		Character[] charactersLazer = new Character[8];
		charactersLazer[0] = new Character(0, "Character" + String.valueOf(0),
				"NORMAL");

		charactersLazer[1] = new Character(-1, "Character" + String.valueOf(1),
				"TANK");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");
		MainEngine dispyLazer = null;
		try {
			dispyLazer = new MainEngine(charactersLazer, mappy, null);
			dispyLazer.setCharacters(charactersLazer);

		} catch (IOException e) {

			e.printStackTrace();
		}

		Weapon lazer = new Lazer();

		Coordinates coord0 = new Coordinates(300, dispyLazer.getBitMap()
				.getFirstY(300));
		charactersLazer[0].setCurrCoord(coord0);

		Coordinates coord1 = new Coordinates(700, dispyLazer.getBitMap()
				.getFirstY(700));
		charactersLazer[1].setCurrCoord(coord1);

		int engineg = charactersLazer[0].getCurrWeapon().getDamage() + 60;
		dispyLazer.placeCharacters();
		dispyLazer.loadImage();
		ImageIO.write(dispyLazer.getFancyMap(), "png", new File("temp" + seppy
				+ "LazerShoot1.png"));

		charactersLazer[0].setCurrWeapon(lazer);
		ArrayList<Coordinates> path = charactersLazer[0].fire(100, 0,
				charactersLazer, 0, 0, dispyLazer);
		dispyLazer.animateShot(lazer, path);
		ImageIO.write(dispyLazer.getFancyMap(), "png", new File("temp" + seppy
				+ "LazerShoot2.png"));

		dispyLazer.loadImage();
		Assert.assertEquals(1600 - engineg, charactersLazer[1].getHp(),
				"LazerShotest for player " + 1 + " " + 2);

		path = charactersLazer[0].fire(100, 0, charactersLazer, 0, 0,
				dispyLazer);
		dispyLazer.animateShot(lazer, path);

		ImageIO.write(dispyLazer.getFancyMap(), "png", new File("temp" + seppy
				+ "LazerShoot3.png"));

		Assert.assertEquals(1240 - engineg, charactersLazer[1].getHp(),
				"LazerShotest for player " + 1 + " " + 2);

		path = charactersLazer[0].fire(100, 0, charactersLazer, 0, 0,
				dispyLazer);
		dispyLazer.loadImage();
		dispyLazer.animateShot(lazer, path);
		ImageIO.write(dispyLazer.getFancyMap(), "png", new File("temp" + seppy
				+ "LazerShoot4.png"));

		Assert.assertEquals(880 - engineg, charactersLazer[1].getHp(),
				"LazerShotest for player " + 1 + " " + 2);
		path = charactersLazer[0].fire(100, 0, charactersLazer, 0, 0,
				dispyLazer);
		dispyLazer.loadImage();
		dispyLazer.animateShot(lazer, path);
		ImageIO.write(dispyLazer.getFancyMap(), "png", new File("temp" + seppy
				+ "LazerShoot5.png"));

		Assert.assertEquals(520 - engineg, charactersLazer[1].getHp(),
				"LazerShotest for player " + 1 + " " + 2);

		path = charactersLazer[0].fire(100, 0, charactersLazer, 0, 0,
				dispyLazer);
		dispyLazer.loadImage();
		dispyLazer.animateShot(lazer, path);

		ImageIO.write(dispyLazer.getFancyMap(), "png", new File("temp" + seppy
				+ "LazerShoot6.png"));

		Assert.assertEquals(0, charactersLazer[1].getHp(),
				"LazerShotest for player " + 1 + " " + 2);
		dispyLazer.updateAfterShot(path.get(path.size() - 1), lazer);
		dispyLazer.loadImage();
		path = charactersLazer[0].fire(100, 0, charactersLazer, 0, 0,
				dispyLazer);
		dispyLazer.animateShot(lazer, path);

		dispyLazer.loadImage();
		ImageIO.write(dispyLazer.getFancyMap(), "png", new File("temp" + seppy
				+ "LazerShoot7.png"));
		Assert.assertEquals(0, charactersLazer[1].getHp(),
				"LazerShotest for player " + 1 + " " + 2);

	}

	@Test
	public void LazerEXShootTest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and tests lazerEX shoot
		// until the character 1 dies

		Character[] charactersLazerEX = new Character[8];
		charactersLazerEX[0] = new Character(0,
				"Character" + String.valueOf(0), "NORMAL");

		charactersLazerEX[1] = new Character(-1, "Character"
				+ String.valueOf(1), "TANK");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");
		MainEngine dispyLazerEX = null;
		try {
			dispyLazerEX = new MainEngine(charactersLazerEX, mappy, null);
			dispyLazerEX.setCharacters(charactersLazerEX);

		} catch (IOException e) {

			e.printStackTrace();
		}

		Weapon lazerEX = new LazerEX();

		Coordinates coord0 = new Coordinates(300, dispyLazerEX.getBitMap()
				.getFirstY(300));
		charactersLazerEX[0].setCurrCoord(coord0);

		Coordinates coord1 = new Coordinates(700, dispyLazerEX.getBitMap()
				.getFirstY(700));
		charactersLazerEX[1].setCurrCoord(coord1);

		charactersLazerEX[0].getWeapons().add(lazerEX);
		dispyLazerEX.placeCharacters();
		dispyLazerEX.loadImage();
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot1.png"));

		charactersLazerEX[0].setCurrWeapon(lazerEX);
		ArrayList<Coordinates> path = charactersLazerEX[0].fire(100, 0,
				charactersLazerEX, 0, 0, dispyLazerEX);
		dispyLazerEX.animateShot(lazerEX, path);
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot2.png"));

		dispyLazerEX.loadImage();
		Assert.assertEquals(1600 - lazerEX.getDamage() + 80,
				charactersLazerEX[1].getHp(), "LazerEXShotest for player " + 1
						+ " " + 2);
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot3.png"));

		path = charactersLazerEX[0].fire(100, 0, charactersLazerEX, 0, 0,
				dispyLazerEX);
		dispyLazerEX.animateShot(lazerEX, path);
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot4.png"));

		Assert.assertEquals(880 - lazerEX.getDamage() + 80,
				charactersLazerEX[1].getHp(), "LazerEXShottest for player " + 1
						+ " " + 2);

		dispyLazerEX.loadImage();
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot5.png"));

		path = charactersLazerEX[0].fire(100, 0, charactersLazerEX, 0, 0,
				dispyLazerEX);
		dispyLazerEX.animateShot(lazerEX, path);
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot6.png"));

		Assert.assertEquals(0, charactersLazerEX[1].getHp(),
				"LazerEXShotest for player " + 1 + " " + 2);
		dispyLazerEX.loadImage();
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot7.png"));

		path = charactersLazerEX[0].fire(100, 0, charactersLazerEX, 0, 0,
				dispyLazerEX);
		dispyLazerEX.animateShot(lazerEX, path);
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot8.png"));

		dispyLazerEX.loadImage();
		ImageIO.write(dispyLazerEX.getFancyMap(), "png", new File("temp"
				+ seppy + "LazerEXShoot9.png"));

	}

	@Test
	public void BazookaAndWallShootTest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and tests bazooka and wall shoot
		// it places 2 walls on top of each other and then using the bazooka it
		// destroy the walls and damages the character 1 untill it dies

		Character[] charactersBazooka = new Character[8];
		charactersBazooka[0] = new Character(0,
				"Character" + String.valueOf(0), "HEALER");

		charactersBazooka[1] = new Character(1,
				"Character" + String.valueOf(1), "TANK");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyBazooka = null;
		try {
			dispyBazooka = new MainEngine(charactersBazooka, mappy, null);
			dispyBazooka.setCharacters(charactersBazooka);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyBazooka.placeCharacters();
		Weapon wall = new FireWall();
		Weapon bazooka = new Bazooka();

		Coordinates coord0 = new Coordinates(300, dispyBazooka.getBitMap()
				.getFirstY(300)-squareSize/2);
		charactersBazooka[0].setCurrCoord(coord0);

		Coordinates coord1 = new Coordinates(843, dispyBazooka.getBitMap()
				.getFirstY(843));
		charactersBazooka[1].setCurrCoord(coord1);

		dispyBazooka.loadImage();
		ImageIO.write(dispyBazooka.getFancyMap(), "png", new File("temp"
				+ seppy + "BazookaandWallShoot1.png"));

		charactersBazooka[0].setCurrWeapon(wall);

		dispyBazooka.loadImage();
		ArrayList<Coordinates> path = charactersBazooka[0].fire(30, 30,
				charactersBazooka, 0, 0, dispyBazooka);
		dispyBazooka.animateShot(wall, path);
		ImageIO.write(dispyBazooka.getFancyMap(), "png", new File("temp"
				+ seppy + "BazookaandWallShoot2.png"));

		dispyBazooka.loadImage();
		path = charactersBazooka[0].fire(105, 87, charactersBazooka, 0, 0,
				dispyBazooka);
		dispyBazooka.animateShot(wall, path);
		ImageIO.write(dispyBazooka.getFancyMap(), "png", new File("temp"
				+ seppy + "BazookaandWallShoot3.png"));

		// test bazzoka on enemy + wall

		charactersBazooka[0].setCurrWeapon(bazooka);
		int angle = 0;
		for (int i = 4; i < 21; i++) {
			if (i > 14)
				angle = 3;

			path = charactersBazooka[0].fire(75 - angle, 60, charactersBazooka,
					0, 0, dispyBazooka);
			dispyBazooka.loadImage();
			dispyBazooka.animateShot(bazooka, path);

			ImageIO.write(dispyBazooka.getFancyMap(), "png", new File("temp"
					+ seppy + "BazookaandWallShoot" + i + ".png"));

		}

		Assert.assertEquals(0, charactersBazooka[1].getHp(),
				"Bazooka for player " + 1 + " " + 2);
	}

	@Test
	public void DirtCircleTest() throws IOException {

		// initialising2 characters on a new map, fire 3 dirt circle shots
		// check if the bit map is modified, one of the shots hits the 1st
		// character and then it checks if it can still perform move left and
		// right

		Character[] charactersDirt = new Character[8];
		charactersDirt[0] = new Character(0, "Character" + String.valueOf(0),
				"TANK");

		charactersDirt[1] = new Character(1, "Character" + String.valueOf(1),
				"TANK");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyDirt = null;
		try {
			dispyDirt = new MainEngine(charactersDirt, mappy, null);
			dispyDirt.setCharacters(charactersDirt);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyDirt.placeCharacters();
		Weapon dirt = new DirtCircle();

		Coordinates coord0 = new Coordinates(300, dispyDirt.getBitMap()
				.getFirstY(300)-squareSize/2);
		charactersDirt[0].setCurrCoord(coord0);

		Coordinates coord1 = new Coordinates(843, dispyDirt.getBitMap()
				.getFirstY(843)-squareSize/2);
		charactersDirt[1].setCurrCoord(coord1);

		dispyDirt.loadImage();
		ImageIO.write(dispyDirt.getFancyMap(), "png", new File("temp" + seppy
				+ "DirtCircleShoot1.png"));

		charactersDirt[0].setCurrWeapon(dirt);

		ArrayList<Coordinates> path = charactersDirt[0].fire(30, 30,
				charactersDirt, 0, 0, dispyDirt);
		dispyDirt.loadImage();
		dispyDirt.animateShot(dirt, path);

		ImageIO.write(dispyDirt.getFancyMap(), "png", new File("temp" + seppy
				+ "DirtCircleShoot2.png"));

		dispyDirt.loadImage();
		Assert.assertEquals(333, mappy.getFirstY(350), "DirtTest for player "
				+ 1 + " " + 2);
		path = charactersDirt[0].fire(45, 30, charactersDirt, 0, 0, dispyDirt);

		dispyDirt.animateShot(dirt, path);

		ImageIO.write(dispyDirt.getFancyMap(), "png", new File("temp" + seppy
				+ "DirtCircleShoot3.png"));

		path = charactersDirt[0].fire(1, 1, charactersDirt, 0, 0, dispyDirt);
		dispyDirt.loadImage();
		dispyDirt.animateShot(dirt, path);

		ImageIO.write(dispyDirt.getFancyMap(), "png", new File("temp" + seppy
				+ "DirtCircleShoot4.png"));

		Coordinates currCoord = charactersDirt[0].getCurrCoord();

		dispyDirt.animateMoveLeft(0);
		Coordinates left = charactersDirt[0].getCurrCoord();
		Assert.assertEquals(currCoord, left, "DirtTest for player " + 1 + " "
				+ 2);
		ImageIO.write(dispyDirt.getFancyMap(), "png", new File("temp" + seppy
				+ "DirtCircleShoot5.png"));

		dispyDirt.animateMoveRight(0);
		Coordinates right = charactersDirt[0].getCurrCoord();
		Assert.assertEquals(currCoord, right, "DirtTest for player " + 1 + " "
				+ 2);
		ImageIO.write(dispyDirt.getFancyMap(), "png", new File("temp" + seppy
				+ "DirtCircleShoot6.png"));

	}

	@Test
	public void RocketandShieldTest() throws IOException {

		// initialising 2 characters on a new map, character 0 fires 3 rockets
		// towards character1 //
		// character 1 is set into different locations and when its shot its
		// position modifies unless its trapped into the ground

		Character[] charactersRocket = new Character[8];
		charactersRocket[0] = new Character(0, "Character" + String.valueOf(0),
				"TANK");

		charactersRocket[1] = new Character(1, "Character" + String.valueOf(1),
				"TANK");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyRocket = null;
		dispyRocket = new MainEngine(charactersRocket, mappy, null);
		dispyRocket.setCharacters(charactersRocket);

		dispyRocket.placeCharacters();
		Weapon rocket = new RocketLauncher();
		Weapon shield = new Shield();
		charactersRocket[0].getWeapons().add(rocket);
		charactersRocket[0].getWeapons().add(shield);

		Coordinates coord0 = new Coordinates(300, dispyRocket.getBitMap()
				.getFirstY(300) - squareSize / 2);
		charactersRocket[0].setCurrCoord(coord0);
		Coordinates coord1 = new Coordinates(700, dispyRocket.getBitMap()
				.getFirstY(700) - squareSize / 2);
		charactersRocket[1].setCurrCoord(coord1);

		dispyRocket.loadImage();
		ImageIO.write(dispyRocket.getFancyMap(), "png", new File("temp" + seppy
				+ "Rockethoot1.png"));

		charactersRocket[0].setCurrWeapon(rocket);
		ArrayList<Coordinates> path = charactersRocket[0].fire(100, 0,
				charactersRocket, 0, 0, dispyRocket);

		dispyRocket.loadImage();
		dispyRocket.animateShot(rocket, path);
		ImageIO.write(dispyRocket.getFancyMap(), "png", new File("temp" + seppy
				+ "Rockethoot2.png"));

		dispyRocket.loadImage();
		ImageIO.write(dispyRocket.getFancyMap(), "png", new File("temp" + seppy
				+ "Rockethoot3.png"));
		Assert.assertEquals(1600 - rocket.getDamage() + 78,
				charactersRocket[1].getHp(), "Rocket for player " + 1 + " " + 2);
		

		charactersRocket[0].setCurrWeapon(shield);
		path = charactersRocket[0].fire(0, 0, charactersRocket, 0, 0,
				dispyRocket);
		dispyRocket.animateShot(shield, path);
		charactersRocket[0].setCurrWeapon(rocket);
		dispyRocket.loadImage();
		ImageIO.write(dispyRocket.getFancyMap(), "png", new File("temp" + seppy
				+ "Rockethoot4.png"));

		path = charactersRocket[0].fire(100, 0, charactersRocket, 0, 0,
				dispyRocket);
		dispyRocket.loadImage();
		dispyRocket.animateShot(rocket, path);
		ImageIO.write(dispyRocket.getFancyMap(), "png", new File("temp" + seppy
				+ "Rockethoot5.png"));

		Assert.assertEquals(1600 - 2 * rocket.getDamage() + 2 * 78,
				charactersRocket[1].getHp(), "Rocket for player " + 1 + " " + 2);

		Coordinates coord2 = new Coordinates(700, 400);
		charactersRocket[1].setCurrCoord(coord2);
		dispyRocket.loadImage();

		ImageIO.write(dispyRocket.getFancyMap(), "png", new File("temp" + seppy
				+ "Rockethoot6.png"));

		path = charactersRocket[0].fire(100, 350, charactersRocket, 0, 0,
				dispyRocket);

		dispyRocket.loadImage();
		dispyRocket.animateShot(rocket, path);

		ImageIO.write(dispyRocket.getFancyMap(), "png", new File("temp" + seppy
				+ "Rockethoot7.png"));

		dispyRocket.loadImage();
		Assert.assertEquals(1600 - 3 * rocket.getDamage() + 3 * 78,
				charactersRocket[1].getHp(), "Rocket for player " + 1 + " " + 2);

	}

	@Test
	public void PoisonDartTest() throws IOException {

		Character[] charactersPoisonDart = new Character[8];
		charactersPoisonDart[0] = new Character(0, "Character"
				+ String.valueOf(0), "NORMAL");

		charactersPoisonDart[1] = new Character(1, "Character"
				+ String.valueOf(1), "NORMAL");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyPoisonDart = null;
		try {
			dispyPoisonDart = new MainEngine(charactersPoisonDart, mappy, null);
			dispyPoisonDart.setCharacters(charactersPoisonDart);
		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyPoisonDart.placeCharacters();

		Weapon poison = new PoisonDart();

		Coordinates coord0 = new Coordinates(300, dispyPoisonDart.getBitMap()
				.getFirstY(300) - squareSize / 2);
		charactersPoisonDart[0].setCurrCoord(coord0);
		Coordinates coord1 = new Coordinates(700, dispyPoisonDart.getBitMap()
				.getFirstY(700) - squareSize / 2);
		charactersPoisonDart[1].setCurrCoord(coord1);

		dispyPoisonDart.loadImage();
		ImageIO.write(dispyPoisonDart.getFancyMap(), "png", new File("temp"
				+ seppy + "PoisonDart1.png"));

		charactersPoisonDart[0].setCurrWeapon(poison);

		ArrayList<Coordinates> path = charactersPoisonDart[0].fire(100, 7,
				charactersPoisonDart, 0, 0, dispyPoisonDart);
		dispyPoisonDart.loadImage();
		dispyPoisonDart.animateShot(poison, path);
		ImageIO.write(dispyPoisonDart.getFancyMap(), "png", new File("temp"
				+ seppy + "PoisonDart2.png"));

		dispyPoisonDart.loadImage();
		boolean hasPoison = false;
		if (charactersPoisonDart[1].getFlags().get(0) != null)
			hasPoison = true;
		Assert.assertEquals(hasPoison, true);

		ImageIO.write(dispyPoisonDart.getFancyMap(), "png", new File("temp"
				+ seppy + "PoisonDart3.png"));

	}

	@Test
	public void HealingTest() throws IOException {

		// isolates 2 characters on the map, character 0 health being reduced
		// character 0 shoots itself using the medigun and healing buff
		// increasing its hp

		Character[] charactersHealing = new Character[8];
		charactersHealing[0] = new Character(0,
				"Character" + String.valueOf(0), "HEALER");

		charactersHealing[1] = new Character(1,
				"Character" + String.valueOf(1), "NORMAL");
		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyHealing = null;
		try {
			dispyHealing = new MainEngine(charactersHealing, mappy, null);
			dispyHealing.setCharacters(charactersHealing);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyHealing.placeCharacters();
		Weapon healingbuff = new HealingBuff();
		Weapon medigun = new MediGun();

		Coordinates coord0 = new Coordinates(300, dispyHealing.getBitMap()
				.getFirstY(300) - squareSize / 2);
		charactersHealing[0].setCurrCoord(coord0);
		Coordinates coord1 = new Coordinates(700, dispyHealing.getBitMap()
				.getFirstY(700) - squareSize / 2);
		charactersHealing[1].setCurrCoord(coord1);

		charactersHealing[0].setHp(40);
		dispyHealing.loadImage();
		ImageIO.write(dispyHealing.getFancyMap(), "png", new File("temp"
				+ seppy + "Healing1.png"));

		charactersHealing[0].setCurrWeapon(medigun);
		ArrayList<Coordinates> path = charactersHealing[0].fire(5, 90,
				charactersHealing, 0, 0, dispyHealing);

		dispyHealing.loadImage();
		dispyHealing.animateShot(medigun, path);
		ImageIO.write(dispyHealing.getFancyMap(), "png", new File("temp"
				+ seppy + "Healing2.png"));

		dispyHealing.loadImage();
		ImageIO.write(dispyHealing.getFancyMap(), "png", new File("temp"
				+ seppy + "Healing3.png"));
		Assert.assertEquals(40 + 319, charactersHealing[0].getHp());

		charactersHealing[0].setCurrWeapon(healingbuff);

		path = charactersHealing[0].fire(5, 90, charactersHealing, 0, 0,
				dispyHealing);
		dispyHealing.loadImage();
		dispyHealing.animateShot(healingbuff, path);
		ImageIO.write(dispyHealing.getFancyMap(), "png", new File("temp"
				+ seppy + "Healing4.png"));

		dispyHealing.loadImage();
		boolean hasHealing = false;
		if (charactersHealing[0].getFlags().get(1) != null)
			hasHealing = true;
		Assert.assertEquals(hasHealing, true);

		ImageIO.write(dispyHealing.getFancyMap(), "png", new File("temp"
				+ seppy + "Healing5.png"));
		charactersHealing[0].setCurrWeapon(medigun);

		path = charactersHealing[0].fire(5, 90, charactersHealing, 0, 0,
				dispyHealing);
		dispyHealing.loadImage();
		dispyHealing.animateShot(medigun, path);
		ImageIO.write(dispyHealing.getFancyMap(), "png", new File("temp"
				+ seppy + "Healing6.png"));

		dispyHealing.loadImage();
		Assert.assertEquals(359 + 319, charactersHealing[0].getHp());
		ImageIO.write(dispyHealing.getFancyMap(), "png", new File("temp"
				+ seppy + "Healing7.png"));

	}

	@Test
	public void EnrageTest() throws IOException {
		// tests the enrage flag for character 0

		Character[] charactersEnrage = new Character[8];
		charactersEnrage[0] = new Character(0, "Character" + String.valueOf(0),
				"ATTACKER");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyEnrage = null;
		try {
			dispyEnrage = new MainEngine(charactersEnrage, mappy, null);
			dispyEnrage.setCharacters(charactersEnrage);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyEnrage.placeCharacters();
		Weapon enrage = new Enrage();
		dispyEnrage.loadImage();
		ImageIO.write(dispyEnrage.getFancyMap(), "png", new File("temp" + seppy
				+ "Enrage1.png"));

		Coordinates coord0 = new Coordinates(300, dispyEnrage.getBitMap()
				.getFirstY(300) - squareSize / 2);
		charactersEnrage[0].setCurrCoord(coord0);
		charactersEnrage[0].setCurrWeapon(enrage);
		ArrayList<Coordinates> path = charactersEnrage[0].fire(5, 90,
				charactersEnrage, 0, 0, dispyEnrage);

		dispyEnrage.animateShot(enrage, path);
		dispyEnrage.loadImage();

		boolean hasEnrage = false;
		if (charactersEnrage[0].getFlags().get(2) != null)
			hasEnrage = true;

		Assert.assertEquals(hasEnrage, true);
		ImageIO.write(dispyEnrage.getFancyMap(), "png", new File("temp" + seppy
				+ "Enrage3.png"));

	}

	@Test
	public void NuclearBlastTest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and tests nuclear shoot
		// until the character 1 dies

		Character[] charactersNuclear = new Character[8];
		charactersNuclear[0] = new Character(0,
				"Character" + String.valueOf(0), "ATTACKER");

		charactersNuclear[1] = new Character(1,
				"Character" + String.valueOf(1), "TANK");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyNuclear = null;
		try {
			dispyNuclear = new MainEngine(charactersNuclear, mappy, null);
			dispyNuclear.setCharacters(charactersNuclear);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyNuclear.placeCharacters();
		Weapon nuclear = new NuclearBlast();

		Coordinates coord0 = new Coordinates(300, dispyNuclear.getBitMap()
				.getFirstY(300) - squareSize / 2);
		Coordinates coord1 = new Coordinates(600, dispyNuclear.getBitMap()
				.getFirstY(600) - squareSize / 2);
		charactersNuclear[0].setCurrCoord(coord0);
		charactersNuclear[1].setCurrCoord(coord1);

		charactersNuclear[0].setCurrWeapon(nuclear);

		dispyNuclear.loadImage();
		ImageIO.write(dispyNuclear.getFancyMap(), "png", new File("temp"
				+ seppy + "NuclearBlast1.png"));

		ArrayList<Coordinates> path = charactersNuclear[0].fire(90, 80,
				charactersNuclear, 0, 0, dispyNuclear);
		dispyNuclear.animateShot(nuclear, path);
		ImageIO.write(dispyNuclear.getFancyMap(), "png", new File("temp"
				+ seppy + "NuclearBlast2.png"));

		dispyNuclear.loadImage();
		ImageIO.write(dispyNuclear.getFancyMap(), "png", new File("temp"
				+ seppy + "NuclearBlast3.png"));

		path = charactersNuclear[0].fire(100, 80, charactersNuclear, 0, 0,
				dispyNuclear);
		dispyNuclear.animateShot(nuclear, path);
		ImageIO.write(dispyNuclear.getFancyMap(), "png", new File("temp"
				+ seppy + "NuclearBlast4.png"));

		dispyNuclear.loadImage();
		ImageIO.write(dispyNuclear.getFancyMap(), "png", new File("temp"
				+ seppy + "NuclearBlast5.png"));

		path = charactersNuclear[0].fire(100, 80, charactersNuclear, 0, 0,
				dispyNuclear);
		dispyNuclear.animateShot(nuclear, path);
		ImageIO.write(dispyNuclear.getFancyMap(), "png", new File("temp"
				+ seppy + "NuclearBlast6.png"));

		dispyNuclear.loadImage();
		ImageIO.write(dispyNuclear.getFancyMap(), "png", new File("temp"
				+ seppy + "NuclearBlast7.png"));

		Assert.assertEquals(0, charactersNuclear[1].getHp());

	}

	@Test
	public void FireMineTest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and tests mine shoot
		// initially character 0 fires a mine on the right of character 1 ,
		// character 1 performs a move right
		// character 1 encounters the mine on its path which decreases its hp
		// and throws it on the left
		// character 0 shoots another mine on its left; character 0 performs a
		// move to the left and it encounters
		// a mine on its path, reducing its hp and throwing it to its right

		Character[] charactersMine = new Character[8];
		charactersMine[0] = new Character(0, "Character" + String.valueOf(0),
				"ATTACKER");
		charactersMine[1] = new Character(1, "Character" + String.valueOf(1),
				"TANK");
		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyMine = null;
		try {
			dispyMine = new MainEngine(charactersMine, mappy, null);
			dispyMine.setCharacters(charactersMine);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyMine.placeCharacters();
		Weapon mine = new FireMine();

		Coordinates coord0 = new Coordinates(300, dispyMine.getBitMap()
				.getFirstY(300) - squareSize / 2);
		Coordinates coord1 = new Coordinates(700, dispyMine.getBitMap()
				.getFirstY(700) - squareSize / 2);
		charactersMine[0].setCurrCoord(coord0);
		charactersMine[1].setCurrCoord(coord1);
		charactersMine[0].setCurrWeapon(mine);

		dispyMine.loadImage();
		ImageIO.write(dispyMine.getFancyMap(), "png", new File("temp" + seppy
				+ "FireMine1.png"));
		ArrayList<Coordinates> path = charactersMine[0].fire(100, 78,
				charactersMine, 0, 0, dispyMine);

		dispyMine.animateShot(mine, path);
		ImageIO.write(dispyMine.getFancyMap(), "png", new File("temp" + seppy
				+ "FireMine2.png"));

		dispyMine.animateMoveRight(1);
		dispyMine.loadImage();
		ImageIO.write(dispyMine.getFancyMap(), "png", new File("temp" + seppy
				+ "FireMine3.png"));

		Assert.assertEquals(1600 - 500, charactersMine[1].getHp());
		Assert.assertEquals(coord1.getxCoord() - 268, charactersMine[1]
				.getCurrCoord().getxCoord());

		path = charactersMine[0].fire(10, 150, charactersMine, 0, 0, dispyMine);
		dispyMine.animateShot(mine, path);
		ImageIO.write(dispyMine.getFancyMap(), "png", new File("temp" + seppy
				+ "FireMine4.png"));

		dispyMine.loadImage();
		ImageIO.write(dispyMine.getFancyMap(), "png", new File("temp" + seppy
				+ "FireMine5.png"));

		dispyMine.animateMoveLeft(0);
		dispyMine.loadImage();
		ImageIO.write(dispyMine.getFancyMap(), "png", new File("temp" + seppy
				+ "FireMine6.png"));

		Assert.assertEquals(300, charactersMine[0].getHp());
		Assert.assertEquals(coord0.getxCoord() + 266, charactersMine[0]
				.getCurrCoord().getxCoord());
	}

	@Test
	public void TeleportTest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and tests teleport shoot
		// character 0 shoots using the teleport weapon; its coordinates change
		// according to where the missile hit
		// if it shoots outside the map the character dies

		Character[] charactersTeleport = new Character[8];
		charactersTeleport[0] = new Character(0, "Character"
				+ String.valueOf(0), "NORMAL");
		charactersTeleport[1] = new Character(1, "Character"
				+ String.valueOf(1), "HEALER");
		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyTeleport = null;
		try {
			dispyTeleport = new MainEngine(charactersTeleport, mappy, null);
			dispyTeleport.setCharacters(charactersTeleport);

		} catch (IOException e) {

			e.printStackTrace();
		}

		dispyTeleport.placeCharacters();
		Weapon teleport = new Teleport();

		Coordinates coord0 = new Coordinates(300, dispyTeleport.getBitMap()
				.getFirstY(300) - squareSize / 2);
		Coordinates coord1 = new Coordinates(700, dispyTeleport.getBitMap()
				.getFirstY(700) - squareSize / 2);
		charactersTeleport[0].setCurrCoord(coord0);
		charactersTeleport[1].setCurrCoord(coord1);
		charactersTeleport[0].setCurrWeapon(teleport);

		dispyTeleport.loadImage();
		ImageIO.write(dispyTeleport.getFancyMap(), "png", new File("temp"
				+ seppy + "Teleport1.png"));

		ArrayList<Coordinates> path = charactersTeleport[0].fire(100, 78,
				charactersTeleport, 0, 0, dispyTeleport);
		dispyTeleport.animateShot(teleport, path);
		ImageIO.write(dispyTeleport.getFancyMap(), "png", new File("temp"
				+ seppy + "Teleport2.png"));

		dispyTeleport.loadImage();
		ImageIO.write(dispyTeleport.getFancyMap(), "png", new File("temp"
				+ seppy + "Teleport3.png"));
		Assert.assertEquals(path.get(path.size() - 1).getxCoord() - 22,
				charactersTeleport[1].getCurrCoord().getxCoord());

		path = charactersTeleport[0].fire(100, 100, charactersTeleport, 0, 0,
				dispyTeleport);
		dispyTeleport.animateShot(teleport, path);
		ImageIO.write(dispyTeleport.getFancyMap(), "png", new File("temp"
				+ seppy + "Teleport4.png"));

		dispyTeleport.loadImage();
		ImageIO.write(dispyTeleport.getFancyMap(), "png", new File("temp"
				+ seppy + "Teleport5.png"));

		Assert.assertEquals(path.get(path.size() - 1).getxCoord(),
				charactersTeleport[0].getCurrCoord().getxCoord());

		dispyTeleport.loadImage();
		path = charactersTeleport[0].fire(100, 120, charactersTeleport, 0, 0,
				dispyTeleport);
		dispyTeleport.animateShot(teleport, path);
		ImageIO.write(dispyTeleport.getFancyMap(), "png", new File("temp"
				+ seppy + "Teleport6.png"));

		dispyTeleport.loadImage();
		ImageIO.write(dispyTeleport.getFancyMap(), "png", new File("temp"
				+ seppy + "Teleport7.png"));
		Assert.assertEquals(0, charactersTeleport[0].getHp());

	}

	@Test
	public void SkipTurnTest() throws IOException {
		// tests the skip turn weapon, which does not change anything to the
		// state of the game
		Weapon skip = new SkipTurn();
		characters[0].setCurrWeapon(skip);
		Coordinates curr = characters[0].getCurrCoord();
		int hp = characters[0].getHp();
		ArrayList<Coordinates> path = characters[0].fire(1, 1, characters, 10,
				10, dispy);
		dispy.animateShot(skip, path);

		Assert.assertEquals(curr, characters[0].getCurrCoord());
		Assert.assertEquals(hp, characters[0].getHp());
		Assert.assertEquals("[null, null, null, null]", characters[0]
				.getFlags().toString());

	}

	@Test
	public void AnimateCrateTest() throws IOException {

		// isolates 2 characters on the map, character 0 being the active one
		// and tests different crates type animations
		// after the crates landed on the ground the character 0 picks them up
		// by moving towards them
		// getting the crates it modifies the character`s hp, weapon, damage
		// amplifier or reduction depending on the crate type

		Character[] charactersCrate = new Character[8];
		charactersCrate[0] = new Character(0, "Character" + String.valueOf(0),
				"NORMAL");

		charactersCrate[1] = new Character(1, "Character" + String.valueOf(1),
				"HEALER");

		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		MainEngine dispyCrate = null;
		try {
			dispyCrate = new MainEngine(charactersCrate, mappy, null);
			dispyCrate.setCharacters(charactersCrate);

		} catch (IOException e) {

			e.printStackTrace();
		}

		int crateradius = new HpCrate(null, 0).getRadius();

		dispyCrate.placeCharacters();

		Coordinates coord0 = new Coordinates(300, dispyCrate.getBitMap()
				.getFirstY(300) - squareSize / 2);
		Coordinates coord1 = new Coordinates(700, dispyCrate.getBitMap()
				.getFirstY(700) - squareSize / 2);

		Coordinates coord3 = new Coordinates(500, dispyCrate.getBitMap()
				.getFirstY(500) - crateradius);
		Coordinates coord4 = new Coordinates(600, dispyCrate.getBitMap()
				.getFirstY(600) - crateradius);
		Coordinates coord5 = new Coordinates(900, dispyCrate.getBitMap()
				.getFirstY(900) - crateradius);
		Coordinates coord6 = new Coordinates(800, dispyCrate.getBitMap()
				.getFirstY(800) - crateradius);

		int currHp = 500;
		double enginegAmp = charactersCrate[0].getDamageAmp();
		double enginegRed = charactersCrate[0].getDamageRed();
		Weapon blast = new NuclearBlast();

		charactersCrate[0].setCurrCoord(coord0);
		charactersCrate[0].setHp(currHp);
		charactersCrate[1].setCurrCoord(coord1);

		dispyCrate.loadImage();

		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest1.png"));

		HpCrate hpcrate = new HpCrate(coord3, 2);
		dispyCrate.animateCrateDrop(coord3.getxCoord(), coord3.getyCoord(),
				hpcrate.getObjectType(), 2);
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest2.png"));

		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest3.png"));

		Assert.assertEquals(
				new Coordinates(coord3.getxCoord(), coord3.getyCoord()),
				hpcrate.getCurrCoord());

		WeaponCrate weaponcrate = new WeaponCrate(coord4, 14);
		dispyCrate.animateCrateDrop(coord4.getxCoord(), coord4.getyCoord(),
				weaponcrate.getObjectType(), 14);
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest4.png"));

		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest5.png"));

		Assert.assertEquals(
				new Coordinates(coord4.getxCoord(), coord4.getyCoord()),
				weaponcrate.getCurrCoord());

		DamageBoost boost = new DamageBoost(coord5, 5);
		dispyCrate.animateCrateDrop(coord5.getxCoord(), coord5.getyCoord(),
				boost.getObjectType(), 5);
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest6.png"));

		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest7.png"));

		Assert.assertEquals(
				new Coordinates(coord5.getxCoord(), coord5.getyCoord()),
				boost.getCurrCoord());

		DamageReduction reduction = new DamageReduction(coord6, 5);
		dispyCrate.animateCrateDrop(coord6.getxCoord(), coord6.getyCoord(),
				reduction.getObjectType(), 5);
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest8.png"));

		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest9.png"));

		Assert.assertEquals(
				new Coordinates(coord6.getxCoord(), coord6.getyCoord()),
				reduction.getCurrCoord());

		// the character 0 moves to the right in the scope of getting the crates

		dispyCrate.animateMoveRight(0);
		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest10.png"));

		dispyCrate.animateMoveRight(0);
		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest11.png"));

		dispyCrate.animateMoveRight(0);
		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest12.png"));

		dispyCrate.animateMoveRight(0);
		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest13.png"));

		dispyCrate.animateMoveRight(0);

		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest14.png"));

		boolean isblastAdded = false;
		if (charactersCrate[0].getWeapons().contains(blast))
			isblastAdded = true;

		Assert.assertEquals(currHp + hpcrate.getHpBoost(),
				charactersCrate[0].getHp());

		Assert.assertEquals(isblastAdded, true);

		boolean isDmgRedAdded = false;
		if (charactersCrate[0].getDamageRed() > enginegRed)
			isDmgRedAdded = true;

		Assert.assertEquals(isDmgRedAdded, true);

		dispyCrate.animateMoveRight(0);
		dispyCrate.loadImage();
		ImageIO.write(dispyCrate.getFancyMap(), "png", new File("temp" + seppy
				+ "AnimateCrateTest15.png"));

		boolean isDmgAmpAdded = false;
		if (charactersCrate[0].getDamageRed() > enginegAmp)
			isDmgAmpAdded = true;

		Assert.assertEquals(isDmgAmpAdded, true);

	}

}