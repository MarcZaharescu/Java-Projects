package test;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.ai.AITools;
import gameEngine.tools.Coordinates;
import gameEngine.tools.GlobalMethods;
import gameEngine.weapons.Weapon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Class that contains tests for the AI functionality

 *
 */
public class AITest {

	private Map mappy;
	private Character[] characters;
	private MainEngine dispy;
	private int squareSize;

	@BeforeClass
	public void setUP() {
		// The AI character types are being tested on a set number of instances.
		// In every game state the number of moves that the AI character[0]
		// has to perform in order to win the game are being recorded and in the
		// end an average number for each type of character is produced,
		// showing the AI performance depending to its role in the game

		// NOTE: The HEALER and TANK AI might take a a lot of moves to end a
		// game because they are specialised in supporting the other characters
		// in the team and their game play is rather defensive than offensive

		// initialise the characters
		characters = new Character[8];
		characters[0] = new Character(0, "AI" + String.valueOf(0), "NORMAL");
		characters[1] = new Character(1, "Character" + String.valueOf(1),
				"NORMAL");

		// get the square size of the character
		squareSize = characters[0].getSquareSize();

		// initialise the bitmap
		String seppy = File.separator;
		mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap2.bmp");

	}

	@Test
	public void testNormal() {
		System.out.println("Test Normal begins:");
		String seppy = File.separator;

		characters[0].setTeamNumber(0);
		characters[1].setTeamNumber(1);

		int numberoftests = 10;
		double average = 0;
		int numberofsteps = 0;

		for (int k = 0; k < numberoftests; k++) {
			characters[0] = new Character(0, "AI" + String.valueOf(0), "NORMAL");
			characters[1] = new Character(1, "Character" + String.valueOf(1),
					"NORMAL");
			numberofsteps = 0;
			// initialise the display map
			try {
				dispy = new MainEngine(characters, mappy, null);
				dispy.setCharacters(characters);

			} catch (IOException e) {

				e.printStackTrace();
			}
			mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap2.bmp");

			// put the characters on the map and draw the image
			dispy.placeCharacters();
			dispy.loadImage();

			Coordinates coord0 = new Coordinates(300, dispy.getBitMap()
					.getFirstY(300) - squareSize / 2);
			Coordinates coord1 = new Coordinates(700, dispy.getBitMap()
					.getFirstY(700) - squareSize / 2);
			characters[0].setCurrCoord(coord0);
			characters[1].setCurrCoord(coord1);

			dispy.loadImage();

			while (characters[1].getHp() > 0) {

				String actions[] = AITools.selectMove(dispy, characters[0])
						.split("\t");

				System.out.print(actions[0] + " ");
				ArrayList<Coordinates> path;
				switch (actions[0]) {

				case "SHOOT": {

					double angle = Double.parseDouble(actions[4]);
					double power = Double.parseDouble(actions[3]);
					Weapon weapon = GlobalMethods.getWeapon(actions[2]);
					characters[0].setCurrWeapon(weapon);

					path = characters[0].fire(power, angle, characters, 0, 0,
							dispy);
					dispy.animateShot(weapon, path);
					break;
				}

				case "MOVELEFT": {
					dispy.animateMoveLeft(0);
					break;
				}

				case "MOVERIGHT": {
					dispy.animateMoveRight(0);
					break;
				}

				}

				numberofsteps++;

				dispy.loadImage();

			}// end of hp>0 while loop

			Assert.assertEquals(0, characters[1].getHp(),
					" AI NORMAL TEST nr: " + k);
			
			System.out.println();
			System.out.println("For test number " + k
					+ " there was a number of : " + numberofsteps
					+ " steps involved!");
			average = average + numberofsteps;
		}// end of number of tests loop

		average = average / numberoftests;

		System.out.println("For NORMAL AI it takes a number of " + average
				+ "steps to kill a target");
	}

	@Test
	public void testAttacker() {
		System.out.println("Test Attacker begins:");
		String seppy = File.separator;

		characters[0].setTeamNumber(0);
		characters[1].setTeamNumber(1);

		int numberoftests = 10;
		double average = 0;
		int numberofsteps = 0;

		for (int k = 0; k < numberoftests; k++) {
			characters[0] = new Character(0, "AI" + String.valueOf(0),
					"ATTACKER");
			characters[1] = new Character(1, "Character" + String.valueOf(1),
					"NORMAL");
			numberofsteps = 0;
			// initialise the display map
			try {
				dispy = new MainEngine(characters, mappy, null);
				dispy.setCharacters(characters);

			} catch (IOException e) {

				e.printStackTrace();
			}
			mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap2.bmp");

			// put the characters on the map and draw the image
			dispy.placeCharacters();
			dispy.loadImage();

			Coordinates coord0 = new Coordinates(300, dispy.getBitMap()
					.getFirstY(300) - squareSize / 2);
			Coordinates coord1 = new Coordinates(700, dispy.getBitMap()
					.getFirstY(700) - squareSize / 2);
			characters[0].setCurrCoord(coord0);
			characters[1].setCurrCoord(coord1);

			dispy.loadImage();

			while (characters[1].getHp() > 0) {

				String actions[] = AITools.selectMove(dispy, characters[0])
						.split("\t");

				ArrayList<Coordinates> path;

				System.out.print(actions[0] + " ");
				switch (actions[0]) {

				case "SHOOT": {

					double angle = Double.parseDouble(actions[4]);
					double power = Double.parseDouble(actions[3]);
					Weapon weapon = GlobalMethods.getWeapon(actions[2]);
					characters[0].setCurrWeapon(weapon);

					path = characters[0].fire(power, angle, characters, 0, 0,
							dispy);
					dispy.animateShot(weapon, path);
					break;
				}

				case "MOVELEFT": {
					dispy.animateMoveLeft(0);
					break;
				}

				case "MOVERIGHT": {
					dispy.animateMoveRight(0);
					break;
				}

				}

				numberofsteps++;

				dispy.loadImage();

			}// end of hp>0 while loop

			Assert.assertEquals(0, characters[1].getHp(), " AI ATTACKER TEST nr: "
					+ k);
			System.out.println();
			System.out.println("For test number " + k
					+ " there was a number of : " + numberofsteps
					+ " steps involved!");
			average = average + numberofsteps;
		}// end of number of tests loop

		average = average / numberoftests;

		System.out.println("For Attacker AI it takes a number of " + average
				+ "steps to kill a target");
	}

	@Test
	public void testTank() throws IOException {
		System.out.println("Test Tank begins:");
		String seppy = File.separator;

		characters[0].setTeamNumber(0);
		characters[1].setTeamNumber(1);

		int numberoftests = 10;
		double average = 0;
		int numberofsteps = 0;

		for (int k = 0; k < numberoftests; k++) {
			characters[0] = new Character(0, "AI" + String.valueOf(0), "TANK");
			characters[1] = new Character(1, "Character" + String.valueOf(1),
					"NORMAL");
			numberofsteps = 0;
			// initialise the display map
			try {
				dispy = new MainEngine(characters, mappy, null);
				dispy.setCharacters(characters);

			} catch (IOException e) {

				e.printStackTrace();
			}
			mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap2.bmp");

			// put the characters on the map and draw the image
			dispy.placeCharacters();
			dispy.loadImage();

			Coordinates coord0 = new Coordinates(300, dispy.getBitMap()
					.getFirstY(300) - squareSize / 2);
			Coordinates coord1 = new Coordinates(700, dispy.getBitMap()
					.getFirstY(700) - squareSize / 2);
			characters[0].setCurrCoord(coord0);
			characters[1].setCurrCoord(coord1);

			dispy.loadImage();

			while (characters[1].getHp() > 0) {

				String actions[] = AITools.selectMove(dispy, characters[0])
						.split("\t");

				ArrayList<Coordinates> path;

				System.out.println(actions[0] + " ");
				switch (actions[0]) {

				case "SHOOT": {

					double angle = Double.parseDouble(actions[4]);
					double power = Double.parseDouble(actions[3]);
					Weapon weapon = GlobalMethods.getWeapon(actions[2]);
					characters[0].setCurrWeapon(weapon);

					path = characters[0].fire(power, angle, characters, 0, 0,
							dispy);
					dispy.animateShot(weapon, path);
					break;
				}

				case "MOVELEFT": {
					dispy.animateMoveLeft(0);
					break;
				}

				case "MOVERIGHT": {
					dispy.animateMoveRight(0);
					break;
				}

				}

				
				numberofsteps++;

				dispy.loadImage();

			}// end of hp>0 while loop

			Assert.assertEquals(0, characters[1].getHp(), " AI TANK TEST nr: "
					+ k);
			
			System.out.println();
			System.out.println("For test number " + k
					+ " there was a number of : " + numberofsteps
					+ " steps involved!");
			average = average + numberofsteps;
		}// end of number of tests loop

		average = average / numberoftests;

		System.out.println("For TANK AI it takes a number of " + average
				+ "steps to kill a target");
	}
/*
 * This test can take a very extended period of time to complete
 * The healer is not designed to kill other players
 */
//	@Test
//	public void testHealer() {
//		System.out.println("Test Healer begins");
//
//		String seppy = File.separator;
//
//		characters[0].setTeamNumber(0);
//		characters[1].setTeamNumber(1);
//
//		int numberoftests = 10;
//		double average = 0;
//		int numberofsteps = 0;
//
//		for (int k = 0; k < numberoftests; k++) {
//			characters[0] = new Character(0, "AI" + String.valueOf(0), "HEALER");
//			characters[1] = new Character(1, "Character" + String.valueOf(1),
//					"NORMAL");
//			numberofsteps = 0;
//			// initialise the display map
//			try {
//				dispy = new MainEngine(characters, mappy, null);
//				dispy.setCharacters(characters);
//
//			} catch (IOException e) {
//
//				e.printStackTrace();
//			}
//			mappy = new Map("img" + seppy + "bitmaps" + seppy + "bitmap2.bmp");
//
//			// put the characters on the map and draw the image
//			dispy.placeCharacters();
//			dispy.loadImage();
//
//			Coordinates coord0 = new Coordinates(300, dispy.getBitMap()
//					.getFirstY(300) - squareSize / 2);
//			Coordinates coord1 = new Coordinates(700, dispy.getBitMap()
//					.getFirstY(700) - squareSize / 2);
//			characters[0].setCurrCoord(coord0);
//			characters[1].setCurrCoord(coord1);
//
//			dispy.loadImage();
//
//			while (characters[1].getHp() > 0) {
//
//				String actions[] = AITools.selectMove(dispy, characters[0])
//						.split("\t");
//
//				ArrayList<Coordinates> path;
//
//				System.out.print(actions[0] + " ");
//				switch (actions[0]) {
//
//				case "SHOOT": {
//
//					double angle = Double.parseDouble(actions[4]);
//					double power = Double.parseDouble(actions[3]);
//					Weapon weapon = GlobalMethods.getWeapon(actions[2]);
//					characters[0].setCurrWeapon(weapon);
//
//					path = characters[0].fire(power, angle, characters, 0, 0,
//							dispy);
//					dispy.animateShot(weapon, path);
//					break;
//				}
//
//				case "MOVELEFT": {
//					dispy.animateMoveLeft(0);
//					break;
//				}
//
//				case "MOVERIGHT": {
//					dispy.animateMoveRight(0);
//					break;
//				}
//
//				}
//
//				numberofsteps++;
//
//				dispy.loadImage();
//
//			}// end of hp>0 while loop
//
//			Assert.assertEquals(0, characters[1].getHp(),
//					" AI HEALER TEST nr: " + k);
//			System.out.println();
//			System.out.println("For test number " + k
//					+ " there was a number of : " + numberofsteps
//					+ " steps involved!");
//			average = average + numberofsteps;
//		}// end of number of tests loop
//
//		average = average / numberoftests;
//
//		System.out.println("For HEALER AI it takes a number of " + average
//				+ "steps to kill a target");
//	}

}