package test;

import static org.testng.AssertJUnit.assertEquals;
import gameEngine.Character;
import gameEngine.MainEngine;
import gameEngine.Map;
import gameEngine.tools.Coordinates;
import gameEngine.weapons.Bazooka;
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
import gameEngine.weapons.Teleport;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for testing weapon damages
 *
 */
public class WeaponTest {

	private Map bitMap;
	private Character[] characters;
	private MainEngine engine;
	private int squareSize;
	
	@BeforeMethod
	public void beforeMethod() {
		// initialise the characters
		characters = new Character[8];
		characters[0] = new Character(0, "Character" + String.valueOf(0),
				"NORMAL");
		characters[1] = new Character(1, "Character" + String.valueOf(1),
				"NORMAL");
		characters[2] = new Character(2, "Character" + String.valueOf(2),
				"ATTACKER");
		characters[3] = new Character(3, "Character" + String.valueOf(3),
				"ATTACKER");
		characters[4] = new Character(4, "Character" + String.valueOf(4),
				"HEALER");
		characters[5] = new Character(5, "Character" + String.valueOf(5),
				"HEALER");
		characters[6] = new Character(6, "Character" + String.valueOf(6),
				"TANK");
		characters[7] = new Character(7, "Character" + String.valueOf(7),
				"TANK");

		// get the square size of the chatacter
		squareSize = characters[0].getSquareSize();

		// initialise the bitmap
		String seppy = File.separator;
		bitMap = new Map("img" + seppy + "bitmaps" + seppy + "bitmap1.bmp");

		// initialise the display map
		try {
			engine = new MainEngine(characters, bitMap, null);
			engine.setCharacters(characters);

		} catch (IOException e) {

			e.printStackTrace();
		}

		// put the characters on the map and draw the image
		engine.placeCharacters();
		// dispy.loadImage();
	}
	
	//reset the characters positions and health
	private void reset(){
		int i =100;
		for(Character charact : characters){
			charact.setCurrCoord(new Coordinates(100+i, bitMap.getFirstY(100+i)));
			i+=100;
			charact.setHp(charact.getFullHP());
		}
	}
	
	@Test
	//test bazooka
	public void testBazooka(){
		reset();
		characters[0].setCurrWeapon(new Bazooka());
		for(int i =1 ; i<8 ; i+=2){
			int newHP = characters[i].getHp()
					- (int) Math.round(1 * new Bazooka().getDamage()
							* (1 - characters[i].getDamageRed())
							* (1 + characters[0].getDamageAmp()));
			//call update after shot
		characters[0].getCurrWeapon()
		.updateAfterShot(characters[i].getCurrCoord(), bitMap, characters, new Bazooka().getExplosiveRange(), characters[0], engine);
		assertEquals(newHP,characters[i].getHp());
		}
		
		//test for attacker
		reset();
		characters[2].setCurrWeapon(new Bazooka());
		for(int i =1 ; i<8 ; i+=2){
			int newHP = characters[i].getHp()
					- (int) Math.round(1 * new Bazooka().getDamage()
							* (1 - characters[i].getDamageRed())
							* (1 + characters[2].getDamageAmp()));
		characters[2].getCurrWeapon()
		.updateAfterShot(characters[i].getCurrCoord(), bitMap, characters, new Bazooka().getExplosiveRange(), characters[2], engine);
		assertEquals(newHP,characters[i].getHp());
		}
	}
	
	//test lazer weapon
	@Test
	public void testLazer(){
		reset();
		characters[2].setCurrWeapon(new Lazer());
		for(int i =1 ; i<8 ; i+=2){
			int newHP = characters[i].getHp()
					- (int) Math.round(1 * new Lazer().getDamage()
							* (1 - characters[i].getDamageRed())
							* (1 + characters[2].getDamageAmp()));
		characters[2].getCurrWeapon()
		.updateAfterShot(characters[i].getCurrCoord(), bitMap, characters, new Lazer().getExplosiveRange(), characters[2], engine);
		assertEquals(newHP,characters[i].getHp());
		}
	}
	
	//test Enrage
	@Test
	public void testEnrage(){
		reset();
		characters[0].getWeapons().add(new Enrage());
		characters[0].setCurrWeapon(new Enrage());
		characters[0].getCurrWeapon().fire(0, 0, characters[0].getCurrCoord(), characters[0], characters, 0, 0, engine);
		assertEquals(true, characters[0].getFlags().get(2)!=null);
	}
	
	//test Mine 
	@Test
	public void testFireMine(){
		reset();
		characters[0].setCurrWeapon(new FireMine());
		characters[0].getCurrWeapon()
		.updateAfterShot(characters[1].getCurrCoord(), bitMap, characters, new FireMine().getExplosiveRange(), characters[1], engine);
		engine.animateMoveLeft(1);		
		assertEquals(characters[1].getFullHP()-500, characters[1].getHp());
	}
	
	//test fireWall
	@Test
	public void testFireWall(){
		reset();
		characters[7].setCurrWeapon(new FireWall());
		characters[7].getCurrWeapon()
		.updateAfterShot(characters[0].getCurrCoord(), bitMap, characters, new FireWall().getExplosiveRange(), characters[0], engine);
		int wallX = engine.getObjects().get(0).getCurrCoord().getxCoord();
		int wallY = engine.getObjects().get(0).getCurrCoord().getyCoord();
		assertEquals(wallX, characters[0].getCurrCoord().getxCoord());
		assertEquals(wallY, characters[0].getCurrCoord().getyCoord()-150/2);		
	}
	
	//test healingBuff flag
	@Test
	public void testHealingBuff(){
		reset();
		characters[5].setCurrWeapon(new HealingBuff());
		characters[5].getCurrWeapon()
		.updateAfterShot(characters[5].getCurrCoord(), bitMap, characters, new HealingBuff().getExplosiveRange(), characters[5], engine);
		assertEquals(true , characters[5].getFlags().get(1)!=null);
	}
	
	//test lazerEX
	@Test
	public void testLazerEX(){
		reset();
		characters[2].getWeapons().add(new LazerEX());
		characters[2].setCurrWeapon(new LazerEX());
		for(int i =1 ; i<8 ; i+=2){
			int newHP = characters[i].getHp()
					- (int) Math.round(1 * new LazerEX().getDamage()
							* (1 - characters[i].getDamageRed())
							* (1 + characters[2].getDamageAmp()));
		characters[2].getCurrWeapon()
		.updateAfterShot(characters[i].getCurrCoord(), bitMap, characters, new LazerEX().getExplosiveRange(), characters[2], engine);
		if(newHP<0){
			newHP =0;
		}
		assertEquals(newHP , characters[i].getHp());
		}
	}
	
	//test mediGun heals
	@Test
	public void testMediGun(){
		reset();
		characters[0].setHp(100);
		characters[4].setCurrWeapon(new MediGun());
		characters[4].getCurrWeapon()
		
		.updateAfterShot(characters[0].getCurrCoord(), bitMap, characters, new MediGun().getExplosiveRange(), characters[4], engine);
		int heal = (int) (Math.round(new MediGun().getHeal()
				* (1 + characters[4].getHealingAmp())
				* (1 + characters[0].getHealingAmp())
				* (1 - characters[0].getHealingRed())));
		assertEquals(100+heal , characters[0].getHp());
	}
	
	
	@Test
	//test nuclear
	public void testNuclearBlast(){
		reset();
		characters[0].getWeapons().add(new NuclearBlast());
		characters[0].setCurrWeapon(new NuclearBlast());
		for(int i =1 ; i<8 ; i+=2){
			int newHP = characters[i].getHp()
					- (int) Math.round(1 * new NuclearBlast().getDamage()
							* (1 - characters[i].getDamageRed())
							* (1 + characters[0].getDamageAmp()));
		characters[0].getCurrWeapon()
		.updateAfterShot(characters[i].getCurrCoord(), bitMap, characters, new NuclearBlast().getExplosiveRange(), characters[0], engine);
			if(newHP<0){
					newHP=0;
			}
			assertEquals(newHP,characters[i].getHp());
		}
		//test for attacker
		reset();
		characters[2].getWeapons().add(new NuclearBlast());
		characters[2].setCurrWeapon(new NuclearBlast());
		for(int i =1 ; i<8 ; i+=2){
			int newHP = characters[i].getHp()
					- (int) Math.round(1 * new NuclearBlast().getDamage()
							* (1 - characters[i].getDamageRed())
							* (1 + characters[2].getDamageAmp()));
		characters[2].getCurrWeapon()
		.updateAfterShot(characters[i].getCurrCoord(), bitMap, characters, new NuclearBlast().getExplosiveRange(), characters[2], engine);
		if(newHP<0){
			newHP=0;
		}
		assertEquals(newHP,characters[i].getHp());
		}	
	}
	
	//test poison dart 
	@Test
	public void testPoisonDart(){
		reset();
		characters[0].setCurrWeapon(new PoisonDart());
		characters[0].getCurrWeapon()
		.updateAfterShot(characters[1].getCurrCoord(), bitMap, characters, new PoisonDart().getExplosiveRange(), characters[0], engine);
		assertEquals(true, characters[1].getFlags().get(0)!=null );
	}
	
	//test rocket launcher weapon
	@Test
	public void testRocket(){
		reset();
		characters[2].setCurrWeapon(new RocketLauncher());
		for(int i =1 ; i<8 ; i+=2){
			int newHP = characters[i].getHp()
					- (int) Math.round(1 * new RocketLauncher().getDamage()
							* (1 - characters[i].getDamageRed())
							* (1 + characters[2].getDamageAmp()));
		characters[2].getCurrWeapon()
		.updateAfterShot(characters[i].getCurrCoord(), bitMap, characters, new RocketLauncher().getExplosiveRange(), characters[2], engine);
		assertEquals(newHP,characters[i].getHp());
		}
	}
	//test if shield blocked damage
	@Test
	public void testShield(){
		reset();
		characters[6].setCurrWeapon(new Shield());
		characters[6].getCurrWeapon()
		.fire(0, 0, characters[6].getCurrCoord(), characters[6], characters, 0, 0, engine);
		assertEquals(true, characters[6].getFlags().get(3)!=null);
		characters[1].getWeapons().add(new LazerEX());
		characters[1].setCurrWeapon(new LazerEX());
		characters[1].getCurrWeapon()
		.updateAfterShot(characters[6].getCurrCoord(), bitMap, characters, new LazerEX().getExplosiveRange(), characters[1], engine);
		assertEquals(characters[6].getFullHP(), characters[6].getHp());
		int newHP = characters[6].getHp()
				- (int) Math.round(1 * new LazerEX().getDamage()
						* (1 - characters[6].getDamageRed())
						* (1 + characters[1].getDamageAmp()));
		characters[1].getCurrWeapon()
		.updateAfterShot(characters[6].getCurrCoord(), bitMap, characters, new LazerEX().getExplosiveRange(), characters[1], engine);
		
		assertEquals(newHP, characters[6].getHp());	
	}
	
	//test teleport
	@Test
	public void teleport(){
		reset();
		for(int i =0 ; i<8 ; i++){
			characters[i].getWeapons().add(new Teleport());
			characters[i].setCurrWeapon(new Teleport());
			Coordinates newCoord = new Coordinates(100, bitMap.getFirstY(100) - characters[i].getRadius() - 1);
			characters[i].getCurrWeapon().updateAfterShot(newCoord, bitMap, characters, 0, characters[i], engine);
			assertEquals(newCoord, characters[i].getCurrCoord());

		}
	}
}

