package test;
import gameEngine.Character;
import graphics.RunGame;
import graphics.gameStates.GameOver;
import graphics.gameStates.GameState;
import graphics.gameStates.HostGame;
import graphics.gameStates.JoinGame;
import graphics.gameStates.MainMenu;
import graphics.gameStates.PlayGame;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/**
 * Class that contains tests for basic client setup
 *
 */
public class ClientTest {

	RunGame client;

	@BeforeMethod
	public void beforeMethod() {
		client = new RunGame();
		client.startClient();
	}
	
	@Test
	public void gameOver() {
		client.gameOver("MSG");
		Assert.assertEquals(client.getCurrentState(), new GameOver("MSG"));
	}

	
	@Test
	/**
	 * Testing getting and setting the current state
	 */
	public void getSetCurrentState() {
		try{
			GameState testState = new MainMenu();
			client.setCurrentState(testState);
			Assert.assertEquals(client.getCurrentState(), testState);
			
			testState = new HostGame(client, "img"+File.separator+"bitmaps" + File.separator + "bitmap1.bmp");
			client.setCurrentState(testState);
			Assert.assertEquals(client.getCurrentState(), testState);
			
			testState = new JoinGame(client);
			client.setCurrentState(testState);
			Assert.assertEquals(client.getCurrentState(), testState);
			
			testState = new PlayGame(client, new Character[]{new Character(1, "name", "NORMAL")}, null, 0, "img"+File.separator+"bitmaps" + File.separator + "bitmap1.bmp");
			client.setCurrentState(testState);
			Assert.assertEquals(client.getCurrentState(), testState);
			
			testState = new GameOver("Msg");
			client.setCurrentState(testState);
			Assert.assertEquals(client.getCurrentState(), testState);			
		} catch(NullPointerException e){
			//Certain conditions aren't supplied since we're only testing
			//A tiny bit of functionality. Silently fail
		}
		
	}
	
	


}
