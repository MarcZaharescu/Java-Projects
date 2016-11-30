package test;

import gameEngine.Character;
import gameEngine.weapons.Bazooka;
import gameEngine.weapons.DirtCircle;
import gameEngine.weapons.Enrage;
import gameEngine.weapons.FireWall;
import gameEngine.weapons.HealingBuff;
import gameEngine.weapons.Lazer;
import gameEngine.weapons.MediGun;
import gameEngine.weapons.PoisonDart;
import gameEngine.weapons.RocketLauncher;
import gameEngine.weapons.Shield;
import gameEngine.weapons.Weapon;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Class that contains tests for Character functionality
 *
 */
public class CharacterTest {

	private ArrayList<Character> testChars;
	private Character testChar;

	@BeforeMethod
	public void beforeMethod() {
		testChars = new ArrayList<Character>();
		testChars.add(new gameEngine.Character(0, "Name", "NORMAL"));
		testChars.add(new gameEngine.Character(1, "Name", "ATTACKER"));
		testChars.add(new gameEngine.Character(2, "Name", "HEALER"));
		testChars.add(new gameEngine.Character(3, "Name", "TANK"));
		testChar = new gameEngine.Character(0, "Name", "NORMAL");
	}

	@Test
	/**
	 * Test that classes are set up correctly based on strings
	 * (It is only these strings that will be used, based on a combo box)
	 */
	public void testClasses() {
		Assert.assertEquals(testChars.get(0).getType(), Character.TYPE.NORMAL);
		Assert.assertEquals(testChars.get(1).getType(), Character.TYPE.ATTACKER);
		Assert.assertEquals(testChars.get(2).getType(), Character.TYPE.HEALER);
		Assert.assertEquals(testChars.get(3).getType(), Character.TYPE.TANK);
	}

	@Test
	/**
	 * Test that the weapons are setup correctly for each class
	 */
	public void testClassWeps() {
		//Normal class expected weapons
		ArrayList<Weapon> normalWeps = new ArrayList<Weapon>();
		normalWeps.add(new Bazooka());
		normalWeps.add(new Lazer());
		normalWeps.add(new RocketLauncher());
		normalWeps.add(new PoisonDart());
		
		//Attacker class expected weapons
		ArrayList<Weapon> attackerWeps = new ArrayList<Weapon>();
		attackerWeps.add(new Bazooka());
		attackerWeps.add(new Lazer());
		attackerWeps.add(new RocketLauncher());
		attackerWeps.add(new Enrage());
		
		//Healer class expected weapons
		ArrayList<Weapon> healerWeps = new ArrayList<Weapon>();
		healerWeps.add(new Bazooka());
		healerWeps.add(new MediGun());
		healerWeps.add(new HealingBuff());
		healerWeps.add(new DirtCircle());
		
		//Tank class expected weapons
		ArrayList<Weapon> tankWeps = new ArrayList<Weapon>();
		tankWeps.add(new Bazooka());
		tankWeps.add(new DirtCircle());
		tankWeps.add(new FireWall());
		tankWeps.add(new Shield());
		
		Assert.assertEquals(normalWeps, testChars.get(0).getWeapons());
		Assert.assertEquals(attackerWeps, testChars.get(1).getWeapons());
		Assert.assertEquals(healerWeps, testChars.get(2).getWeapons());
		Assert.assertEquals(tankWeps, testChars.get(3).getWeapons());
	}
	
	public void testWepLoading(){
		ArrayList<Weapon> weps = new ArrayList<Weapon>();
	}
	
	
	
	
}
