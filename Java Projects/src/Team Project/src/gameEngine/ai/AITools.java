package gameEngine.ai;

import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.objects.MiscObject;
import gameEngine.tools.Coordinates;
import gameEngine.weapons.Bazooka;
import gameEngine.weapons.DirtCircle;
import gameEngine.weapons.Lazer;
import gameEngine.weapons.MediGun;
import gameEngine.weapons.PoisonDart;
import gameEngine.weapons.RocketLauncher;
import gameEngine.weapons.Weapon;
import graphics.gameStates.PlayGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that implements the AI functionality
 * 
 *
 */
public class AITools {

	// the random number generator used for calculating shooting errors
	private static Random randy = new Random();
	// the current error
	private static double angleError = 0;
	// the difficulty of the AI (affects its accuracy)
	private static DIFFICULTY difficulty = DIFFICULTY.MEDIUM;

	private static double windSpeed = PlayGame.getCurrWindSpeed();
	private static double windAngle = PlayGame.getCurrWindAngle();

	public static enum DIFFICULTY {
		EASY, MEDIUM, HARD
	}

	/**
	 * Select a move
	 * 
	 * @param engine
	 *            the current engine
	 * @param activeCharacter
	 *            the character to move
	 */
	public static String selectMove(MainEngine engine, Character activeCharacter) {
		try {
			switch (activeCharacter.getType()) {

			case NORMAL:
				return normalMove(engine, activeCharacter);
			case ATTACKER:
				return attackerMove(engine, activeCharacter);
			case HEALER:
				return healerMove(engine, activeCharacter);
			case TANK:
				return tankMove(engine, activeCharacter);
			}
		} catch (Exception e) {
			return "SHOOT\t" + activeCharacter.getPlayerID()
					+ "\tSkipTurn\t2.0\t100.0";
		}
		return "";
	}

	/**
	 * Select a move for the normal role
	 * 
	 * @param engine
	 *            the current engine
	 * @param activeCharacter
	 *            the character to move
	 * @return the move string
	 */
	private static String normalMove(MainEngine engine, Character activeCharacter) {
		//3 second delay to prolong game time
	//	try {
		//	Thread.sleep(3000);
		//} catch (InterruptedException e) {
	//		e.printStackTrace();
	//	}
		updateError();
		int choose = randy.nextInt(10);
		Character enemy = sortedHpEnemies(engine, activeCharacter).get(0);

		if (activeCharacter.isStuck(engine.getBitMap())) {
			return "SHOOT\t" + activeCharacter.getPlayerID()
					+ "\tBazooka\t2.0\t100.0";
		} else if (findHpCrate(engine, activeCharacter) != "") {
			// if low on hp and if a crate is available, go get an hp crate.
			return findHpCrate(engine, activeCharacter);
		} else if (choose == 0 || choose == 1) {
			// class specific move: poison enemies.
			return fireArcShot(engine, activeCharacter, enemy, new PoisonDart());
		} else if (choose == 2) {
			return fireRocketShot(engine, activeCharacter);
		} else if (choose == 3 && findRandomCrate(engine, activeCharacter) != "") {
			return findRandomCrate(engine, activeCharacter);
		}

		// if no special strategies available, attempt lazer, else do bazooka.
		return fireLazerShot(engine, activeCharacter);
	}

	/**
	 * Select a move for the attacker role
	 * 
	 * @param engine
	 *            the current engine
	 * @param activeCharacter
	 *            the character to move
	 * @return the move string
	 */
	private static String attackerMove(MainEngine engine, Character activeCharacter) {
		// do a delay, so game does not go by too quickly, to mimic the amount
		// of time it would take a human player to shoot.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// power angle
		updateError();
		int choose = randy.nextInt(10);
		if (activeCharacter.isStuck(engine.getBitMap())) {
			return "SHOOT\t" + activeCharacter.getPlayerID()
					+ "\tBazooka\t2.0\t100.0";
		} else if (findHpCrate(engine, activeCharacter) != "") {
			// if low on hp and if a crate is available, go get an hp crate.
			return findHpCrate(engine, activeCharacter);
		} else if (choose == 0 || choose == 1) {
			// class specific move: increase damage of enxt shot.
			return "SHOOT\t" + activeCharacter.getPlayerID()
					+ "\tEnrage\t100.0\t100.0";
		} else if (choose == 2) {
			return fireRocketShot(engine, activeCharacter);
		} else if (choose == 3 && findRandomCrate(engine, activeCharacter) != "") {
			return findRandomCrate(engine, activeCharacter);
		}
		// if no special strategies available, attempt lazer, else do bazooka.
		return fireLazerShot(engine, activeCharacter);
	}

	/**
	 * Select a move for the tank role
	 * 
	 * @param engine
	 *            the current engine
	 * @param activeCharacter
	 *            the character to move
	 * @return the move string
	 */
	private static String tankMove(MainEngine engine, Character activeCharacter) {
		// do a delay, so game does not go by too quickly, to mimic the amount
		// of time it would take a human player to shoot.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// move left
		updateError();
		Character enemy = sortedHpEnemies(engine, activeCharacter).get(0);

		int choose = randy.nextInt(10);
		if (activeCharacter.isStuck(engine.getBitMap())) {
			return "SHOOT\t" + activeCharacter.getPlayerID()
					+ "\tBazooka\t2.0\t100.0";
		} else if (findHpCrate(engine, activeCharacter) != "") {
			// if low on hp and if a crate is available, go get an hp crate.
			return findHpCrate(engine, activeCharacter);
		} else if (choose == 0 || choose == 1) {
			// do class specific move: block the next shot.
			return "SHOOT\t" + activeCharacter.getPlayerID()
					+ "\tShield\t100.0\t100.0";
		} else if (choose == 2) {
			return fireRocketShot(engine, activeCharacter);
		} else if (choose == 3 && findRandomCrate(engine, activeCharacter) != "") {
			return findRandomCrate(engine, activeCharacter);
		} else if (choose == 4) {
			return fireArcShot(engine, activeCharacter, enemy, new DirtCircle());
		}

		// if no special strategies available, do a simple bazooka shot
		return fireArcShot(engine, activeCharacter, enemy, new Bazooka());
	}

	/**
	 * Select a move for the healer role
	 * 
	 * @param engine
	 *            the current engine
	 * @param activeCharacter
	 *            the character to move
	 * @return the move string
	 */
	private static String healerMove(MainEngine engine, Character activeCharacter) {
		// do a delay, so game does not go by too quickly, to mimic the amount
		// of time it would take a human player to shoot.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (activeCharacter.isStuck(engine.getBitMap())) {
			return "SHOOT\t" + activeCharacter.getPlayerID()
					+ "\tBazooka\t2.0\t100.0";
		}
		// move right
		ArrayList<Character> allies = sortedHpAllies(engine, activeCharacter);

		Character charact = activeCharacter;
		charact.setCurrWeapon(new MediGun());
		Coordinates currCoord = charact.getCurrCoord();
		int xCoord = currCoord.getxCoord();
		int yCoord = currCoord.getyCoord();

		int mouseX = allies.get(0).getCurrCoord().getxCoord();
		int mouseY = allies.get(0).getCurrCoord().getyCoord();
		double mouseAngle = Math.toDegrees((double) Math
				.atan((double) (yCoord - mouseY) / (double) (mouseX - xCoord)));
		if ((mouseX < xCoord)) {
			mouseAngle = mouseAngle + 180;
		}

		if ((mouseX > xCoord) && (mouseY > yCoord)) {
			mouseAngle = mouseAngle + 360;
		}
		double distance = Math.sqrt((mouseX - xCoord) * (mouseX - xCoord)
				+ (mouseY - yCoord) * (mouseY - yCoord));
		double power = distance / 300 * 100;

		if (allies.get(0).getHp() > allies.get(0).getFullHP() - 100) { 
			if (randy.nextInt(10) > 0) { // health
				return fireArcShot(engine, activeCharacter,
						sortedHpEnemies(engine, activeCharacter).get(0),
						new Bazooka());
			} else {
				if (activeCharacter.getTeamNumber() == 0) {
					return "SHOOT\t" + activeCharacter.getPlayerID()
							+ "\tFireWall\t30.0\t" + 0;
				} else {
					return "SHOOT\t" + activeCharacter.getPlayerID()
							+ "\tFireWall\t30.0\t" + 180;
				}
			}
		}

		return "SHOOT\t" + activeCharacter.getPlayerID() + "\tMediGun\t"
				+ power + "\t" + mouseAngle;
	}

	/**
	 * Get the enemies
	 * 
	 * @param engine
	 * @param activeCharacter
	 * @return a list of enemies, sorted in ascending order of their HP
	 */
	private static ArrayList<Character> sortedHpEnemies(MainEngine engine,
			Character activeCharacter) {
		Character[] characters = engine.getCharacters();
		ArrayList<Character> sortedEnemy = new ArrayList<Character>();
		int myTeam = activeCharacter.getTeamNumber();
		// Character temp = new Character(0, "", "TANK");
		if (myTeam == 1) {
			for (int i = 0; i <= 6; i += 2) {
				if (characters[i] != null && characters[i].getHp() > 0)
					sortedEnemy.add(characters[i]);
			}
		} else if (myTeam == 0) {
			for (int i = 1; i <= 7; i += 2) {
				if (characters[i] != null && characters[i].getHp() > 0)
					sortedEnemy.add(characters[i]);
			}
		}

		for (int i = 0; i < sortedEnemy.size(); i++) {
			for (int j = 0; j < sortedEnemy.size() - 1; j++) {
				if (sortedEnemy.get(j) != null
						&& sortedEnemy.get(j + 1) != null) {
					if (sortedEnemy.get(j).getHp() > sortedEnemy.get(j + 1)
							.getHp()) {
						Character temp = sortedEnemy.get(j);
						sortedEnemy.set(j, sortedEnemy.get(j + 1));
						sortedEnemy.set(j + 1, temp);

					}
				}
			}
		}
		return sortedEnemy;
		// return lowestHpChar;
	}

	/**
	 * Get the allies
	 * 
	 * @param engine
	 * @param activeCharacter
	 * @return a list of alies, sorted in ascending order of their HP
	 */
	private static ArrayList<Character> sortedHpAllies(MainEngine engine,
			Character activeCharacter) {
		Character[] characters = engine.getCharacters();
		ArrayList<Character> sortedEnemy = new ArrayList<Character>();
		int myTeam = activeCharacter.getTeamNumber();
		// Character temp = new Character(0, "", "TANK");
		if (myTeam == 0) {
			for (int i = 0; i <= 6; i += 2) {
				if (characters[i] != null && characters[i].getHp() > 0)
					sortedEnemy.add(characters[i]);
			}
		} else if (myTeam == 1) {
			for (int i = 1; i <= 7; i += 2) {
				if (characters[i] != null && characters[i].getHp() > 0)
					sortedEnemy.add(characters[i]);
			}
		}

		for (int i = 0; i < sortedEnemy.size(); i++) {
			for (int j = 0; j < sortedEnemy.size() - 1; j++) {
				if (sortedEnemy.get(j) != null
						&& sortedEnemy.get(j + 1) != null) {
					if (sortedEnemy.get(j).getHp() > sortedEnemy.get(j + 1)
							.getHp()) {
						Character temp = sortedEnemy.get(j);
						sortedEnemy.set(j, sortedEnemy.get(j + 1));
						sortedEnemy.set(j + 1, temp);

					}
				}
			}
		}
		return sortedEnemy;
		// return lowestHpChar;
	}

	/**
	 * Returns true if point (x,y) is inside the circle with center (centerX,
	 * centerY) of radius = radius
	 * 
	 * @param x
	 * @param y
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @return whether point is on the circle
	 */
	public static boolean isOnCircle(int x, int y, int centerX, int centerY,
			int radius) {
		return Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2) < Math
				.pow(radius, 2);
	}

	/**
	 * Returns true if a character has a coordinate pair which is hit
	 * 
	 * @param hit
	 * @param character
	 * @return
	 */
	private static boolean isHit(Coordinates hit, Character character) {
		return character.getAllCoordinates().contains(hit);
	}

	/**
	 * Updates the error parameter, ready for the next shot.
	 */
	private static void updateError() {
		int x;
		switch (difficulty) {
		case EASY:
			x = randy.nextInt(15);
			angleError = (double) x - (double) 7.0;
			break;
		case MEDIUM:
			x = randy.nextInt(8);
			angleError = (double) x - (double) 4.0;
			break;
		case HARD:
			x = randy.nextInt(4);
			angleError = (double) x - (double) 2.0;
			break;
		}
	}

	/**
	 * Fires a shot with the rocketLauncher weapon, if possible
	 * 
	 * @param engine
	 * @param activeCharacter
	 * @return shot string
	 */
	private static String fireRocketShot(MainEngine engine,
			Character activeCharacter) {

		ArrayList<Character> enemies = sortedHpEnemies(engine, activeCharacter);
		activeCharacter.setCurrWeapon(new RocketLauncher());
		if (activeCharacter.getTeamNumber() == 0) {
			for (int i = 0; i <= 90; i++) {
				ArrayList<Coordinates> path = activeCharacter.fire(100, i,
						engine.getCharacters(), 0, 0, engine);
				for (Character enemy : enemies) {
					if (isHit(path.get(path.size() - 1), enemy)) {

						return "SHOOT\t" + activeCharacter.getPlayerID()
								+ "\tRocketLauncher\t100.0\t" + (i);
					}
				}
			}
			for (int i = 270; i <= 360; i++) {
				ArrayList<Coordinates> path = activeCharacter.fire(100, i,
						engine.getCharacters(), 0, 0, engine);
				for (Character enemy : enemies) {
					if (isHit(path.get(path.size() - 1), enemy)) {

						return "SHOOT\t" + activeCharacter.getPlayerID()
								+ "\tRocketLauncher\t100.0\t" + (i);
					}
				}
			}
		} else if (activeCharacter.getTeamNumber() == 1) {
			for (int i = 90; i <= 270; i++) {
				ArrayList<Coordinates> path = activeCharacter.fire(100, i,
						engine.getCharacters(), 0, 0, engine);
				for (Character enemy : enemies) {
					if (isHit(path.get(path.size() - 1), enemy)) {
						return "SHOOT\t" + activeCharacter.getPlayerID()
								+ "\tRocketLauncher\t100.0\t" + (i);
					}
				}
			}
		}
		return "WALK" + "\t" + activeCharacter.getPlayerID() + "\t"
		+ "LEFT"; //Contingency for failed search...
	}

	/**
	 * Fires a shot with the Lazer weapon, if possible
	 * 
	 * @param engine
	 * @param activeCharacter
	 * @return shot string
	 */
	private static String fireLazerShot(MainEngine engine, Character activeCharacter) {

		ArrayList<Character> enemies = sortedHpEnemies(engine, activeCharacter);
		activeCharacter.setCurrWeapon(new Lazer());
		if (activeCharacter.getTeamNumber() == 0) {
			for (int i = 0; i <= 90; i++) {
				ArrayList<Coordinates> path = activeCharacter.fire(100, i,
						engine.getCharacters(), 0, 0, engine);
				for (Character enemy : enemies) {
					if (isHit(path.get(path.size() - 1), enemy)) {

						return "SHOOT\t" + activeCharacter.getPlayerID()
								+ "\tLazer\t100.0\t" + (i + angleError / 5);
					}
				}
			}
			for (int i = 270; i <= 360; i++) {
				ArrayList<Coordinates> path = activeCharacter.fire(100, i,
						engine.getCharacters(), 0, 0, engine);
				for (Character enemy : enemies) {
					if (isHit(path.get(path.size() - 1), enemy)) {

						return "SHOOT\t" + activeCharacter.getPlayerID()
								+ "\tLazer\t100.0\t" + (i + angleError / 5);
					}
				}
			}
		} else if (activeCharacter.getTeamNumber() == 1) {
			for (int i = 90; i <= 270; i++) {
				ArrayList<Coordinates> path = activeCharacter.fire(100, i,
						engine.getCharacters(), 0, 0, engine);
				for (Character enemy : enemies) {
					if (isHit(path.get(path.size() - 1), enemy)) {
						return "SHOOT\t" + activeCharacter.getPlayerID()
								+ "\tLazer\t100.0\t" + (i + angleError / 5);
					}
				}
			}
		}

		return fireArcShot(engine, activeCharacter, enemies.get(0), new Bazooka());
	}

	/**
	 * Fires a shot with a weapon that goes in a straight arc
	 * 
	 * @param engine
	 * @param activeCharacter
	 * @param targetCharacter
	 *            the target of the shot
	 * @param weapon
	 *            the arc weapon
	 * @return shot string
	 */
	private static String fireArcShot(MainEngine engine, Character activeCharacter,
			Character targetCharacter, Weapon weapon) {
		windSpeed = AIGame.getCurrWindSpeed();
		windAngle = AIGame.getCurrWindAngle();
		activeCharacter.setCurrWeapon(weapon);
		Character lowestHpChar = targetCharacter;
		Coordinates activeCoord = activeCharacter.getCurrCoord();
		Coordinates lowestCoord = lowestHpChar.getCurrCoord();

		int myX = activeCharacter.getCurrCoord().getxCoord();
		int hisX = lowestHpChar.getCurrCoord().getxCoord();

		if (myX < hisX) {
			for (int angle = 10; angle <= 80; angle++) {
				for (int i = 0; i <= 100; i++) {
					ArrayList<Coordinates> path = activeCharacter
							.getCurrWeapon().fire(i, angle, activeCoord,
									activeCharacter, engine.getCharacters(),
									windSpeed, windAngle, engine);
					Coordinates landingCoord = path.get(path.size() - 1);

					// search 20 pixel radius around the landingCoord
					int xCoord = landingCoord.getxCoord();
					int yCoord = landingCoord.getyCoord();

					if (isOnCircle(lowestCoord.getxCoord(),
							lowestCoord.getyCoord(), xCoord, yCoord, 20)) {
						return "SHOOT\t" + activeCharacter.getPlayerID() + "\t"
								+ activeCharacter.getCurrWeapon().getName()
								+ "\t" + i + "\t" + (angle + angleError);

					}

				}
			}
			for (int angle = 280; angle <= 350; angle++) {
				for (int i = 0; i <= 100; i++) {
					ArrayList<Coordinates> path = activeCharacter
							.getCurrWeapon().fire(i, angle, activeCoord,
									activeCharacter, engine.getCharacters(),
									windSpeed, windAngle, engine);
					Coordinates landingCoord = path.get(path.size() - 1);

					// search 20 pixel radius around the landingCoord
					int xCoord = landingCoord.getxCoord();
					int yCoord = landingCoord.getyCoord();

					if (isOnCircle(lowestCoord.getxCoord(),
							lowestCoord.getyCoord(), xCoord, yCoord, 20)) {
						return "SHOOT\t" + activeCharacter.getPlayerID() + "\t"
								+ activeCharacter.getCurrWeapon().getName()
								+ "\t" + i + "\t" + (angle + angleError);

					}

				}
			}
		}

		else {
			for (int angle = 100; angle <= 260; angle++) {
				for (int i = 0; i <= 100; i++) {
					ArrayList<Coordinates> path = activeCharacter
							.getCurrWeapon().fire(i, angle, activeCoord,
									activeCharacter, engine.getCharacters(),
									windSpeed, windAngle, engine);
					Coordinates landingCoord = path.get(path.size() - 1);

					// search 20 pixel radius around the landingCoord
					int xCoord = landingCoord.getxCoord();
					int yCoord = landingCoord.getyCoord();

					if (isOnCircle(lowestCoord.getxCoord(),
							lowestCoord.getyCoord(), xCoord, yCoord, 20)) {
						return "SHOOT\t" + activeCharacter.getPlayerID() + "\t"
								+ activeCharacter.getCurrWeapon().getName()
								+ "\t" + i + "\t" + (angle + angleError);

					}
				}
			}
		}


		return findRandomCrate(engine, activeCharacter);
	}

	/**
	 * Finds a nearby crate, regardless of contents
	 * 
	 * @param engine
	 * @param activeCharacter
	 * @return move string
	 */

	private static String findRandomCrate(MainEngine engine,
			Character activeCharacter) {
		// no need to get a crate

		int currX = activeCharacter.getCurrCoord().getxCoord();
		int moveLength = activeCharacter.getMoveLength();
		CopyOnWriteArrayList<MiscObject> objects = (CopyOnWriteArrayList<MiscObject>) engine
				.getObjects();

		for (int i = currX - moveLength; i <= currX; i++) {
			for (MiscObject obj : objects) {
				if (obj.getCurrCoord().getxCoord() == i) {
					return "WALK" + "\t" + activeCharacter.getPlayerID() + "\t"
							+ "LEFT";
				}
			}
		}

		for (int i = currX; i <= currX + moveLength; i++) {
			for (MiscObject obj : objects) {
				if (obj.getCurrCoord().getxCoord() == i) {
					return "WALK" + "\t" + activeCharacter.getPlayerID() + "\t"
							+ "RIGHT";
				}
			}
		}

		if (randy.nextInt(2) == 0) {
			return "WALK" + "\t" + activeCharacter.getPlayerID() + "\t"
					+ "RIGHT";
		}
		return "WALK" + "\t" + activeCharacter.getPlayerID() + "\t" + "LEFT";
	}

	/**
	 * Finds a nearby HP crate, in the case where character has low HP.
	 * 
	 * @param engine
	 * @param activeCharacter
	 * @return move string
	 */
	private static String findHpCrate(MainEngine engine, Character activeCharacter) {

		int hp = activeCharacter.getHp();
		int fullHp = activeCharacter.getFullHP();

		// no need to get a crate

		if (hp > fullHp / 2) {
			return "";
		}

		int currX = activeCharacter.getCurrCoord().getxCoord();
		int moveLength = activeCharacter.getMoveLength();
		CopyOnWriteArrayList<MiscObject> objects = (CopyOnWriteArrayList<MiscObject>) engine
				.getObjects();

		for (int i = currX - moveLength; i <= currX; i++) {
			for (MiscObject obj : objects) {
				if (obj.getObjectType() == "HpCrate"
						&& obj.getCurrCoord().getxCoord() == i) {
					return "WALK" + "\t" + activeCharacter.getPlayerID() + "\t"
							+ "LEFT";
				}
			}
		}

		for (int i = currX; i <= currX + moveLength; i++) {
			for (MiscObject obj : objects) {
				if (obj.getObjectType() == "HpCrate"
						&& obj.getCurrCoord().getxCoord() == i) {
					return "WALK" + "\t" + activeCharacter.getPlayerID() + "\t"
							+ "RIGHT";
				}
			}
		}
		return "";
	}
}
