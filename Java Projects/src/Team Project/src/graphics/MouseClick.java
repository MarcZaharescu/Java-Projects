package graphics;

import gameEngine.Character;
import gameEngine.tools.Coordinates;
import gameEngine.tools.GlobalMethods;
import gameEngine.weapons.SkipTurn;
import gameServer.AIClient;
import gameServer.Server;
import graphics.gameStates.HostGame;
import graphics.gameStates.MainMenu;
import graphics.gameStates.PlayGame;
import graphics.gameStates.SelectMap;
import graphics.helpScreens.ConnectHelp;
import graphics.helpScreens.PlayHelp;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Listener for mouse interaction throughout the game
 * 
 * @author B4 (all filled in relevant parts for screens we created)
 */
public class MouseClick implements MouseListener, MouseMotionListener {
	private RunGame client;

	double mouseAngle = 45; // initial
	double power = 70; // initial
	private Character charact;
	private GAMESTATE currState;

	/**
	 * Gamestate enums to distinguish between different states
	 */
	public static enum GAMESTATE {
		MAINMENU, HOSTMENU, JOINMENU, PLAYGAME, GAMEOVER, SELECTMAP
	}

	private final String[] ROLES = new String[] { "Normal", "Attacker",
			"Healer", "Tank" };

	public MouseClick(RunGame client) {
		this.client = client;
		currState = GAMESTATE.MAINMENU;

	}

	public void setGameState(GAMESTATE state) {
		currState = state;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (currState) {
		// 550,580 -> 580, 610
		case PLAYGAME:

			// ok, hovered over, get the coordinates
			int mouseX = e.getX();
			int mouseY = e.getY();

			// if we're on the panel
			if (mouseX > 510 && mouseX < 610 && mouseY > 580 && mouseY < 630) {
				PlayGame.weapInfo = true;
			}
			// otherwise, hide the square
			else {
				PlayGame.weapInfo = false;
			}
			
			if (mouseX > 410 && mouseX < 485 && mouseY > 575 && mouseY < 615) {
				PlayGame.skipInfo = true;
			} else {
				PlayGame.skipInfo = false;
			}

			if (mouseX > 50 && mouseX < 125 && mouseY > 575 && mouseY < 615) {
				PlayGame.leftInfo = true;
			} else {
				PlayGame.leftInfo = false;
			}

			if (mouseX > 170 && mouseX < 245 && mouseY > 575 && mouseY < 615) {
				PlayGame.rightInfo = true;
			} else {
				PlayGame.rightInfo = false;
			}

			if (mouseX > 290 && mouseX < 365 && mouseY > 575 && mouseY < 615) {
				PlayGame.fireInfo = true;
			} else {
				PlayGame.fireInfo = false;
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// X & Y coordinates of mouse
		int mouseX = e.getX();
		int mouseY = e.getY();
		if (mouseX > 875 && mouseX < 950 && mouseY > 575 && mouseY < 615 && currState != GAMESTATE.MAINMENU) {
			System.exit(1);
		}
		if (mouseX > 760 && mouseX < 835 && mouseY > 575 && mouseY < 615 && currState != GAMESTATE.MAINMENU) {
			new PlayHelp();
		}
		switch (currState) {
		case MAINMENU:
			menuClick(mouseX, mouseY);
			break;
		case JOINMENU:
			break;
		case HOSTMENU:
			hostClick(mouseX, mouseY);
			break;
		case PLAYGAME:
			playClick(mouseX, mouseY);
			break;
		case GAMEOVER:
			break;
		case SELECTMAP:
			selectClick(mouseX, mouseY);
			break;
		}

	}

	/**
	 * A button has been clicked in the select map screen
	 * 
	 * @param mouseX
	 * @param mouseY
	 */
	private void selectClick(int mouseX, int mouseY) {
		if (((SelectMap) (client.getCurrentState())).poly1.contains(mouseX,
				mouseY)) {
			((SelectMap) client.getCurrentState()).prevMap();
		} else if (((SelectMap) (client.getCurrentState())).poly2.contains(
				mouseX, mouseY)) {
			((SelectMap) client.getCurrentState()).nextMap();
		} else if (mouseX > 400 && mouseX < 600 && mouseY > 575 && mouseY < 615) {
			client.hostGame(((SelectMap) client.getCurrentState()).fileName());
		}

	}

	/**
	 * A button has been clicked in the main menu
	 * 
	 * @param mouseX
	 * @param mouseY
	 */
	private void menuClick(int mouseX, int mouseY) {
		// Join Game button clicked
		if (((MainMenu) client.getCurrentState()).joinR
				.contains(mouseX, mouseY)) {
			client.joinGame();
			return;
		}

		// Host Game button clicked
		if (((MainMenu) client.getCurrentState()).hostR
				.contains(mouseX, mouseY)) {
			client.selectScreen();
			return;
		}

		// Help Game button clicked
		if (((MainMenu) client.getCurrentState()).helpR
				.contains(mouseX, mouseY)) {
			new ConnectHelp();
			return;
		}
		// Quit button clicked
		if (((MainMenu) client.getCurrentState()).quitR
				.contains(mouseX, mouseY)) {
			System.exit(1);
			return;
		}

		
	}

	/**
	 * A button was clicked in the host screen
	 * 
	 * @param mouseX
	 * @param mouseY
	 */
	private void hostClick(int mouseX, int mouseY) {

		if (mouseX > 400 && mouseX < 600 && mouseY > 575 && mouseY < 615) {
			if(((HostGame) client.getCurrentState()).getActualPlayerCount() > 1){
				client.playGame(HostGame.getClient(),
						(client.getCurrentState()).getFilePath());
			}
		} else if (mouseX > 50 && mouseX < 250 && mouseY > 575 && mouseY < 615 && ((HostGame) client.getCurrentState()).getActualPlayerCount() < 8) {
			String role = GlobalMethods.comboBox(false, ROLES);
			if(role.equals("NO")){
				//Don't add the AI
			}else{
				new AIClient(Server.nextCPU(), 1234, role);
			}
		}

	}

	/**
	 * A button was clicked in the main game screen
	 * 
	 * @param mouseX
	 * @param mouseY
	 */
	private void playClick(int mouseX, int mouseY) {
		// Only allow the buttons if it's "my" turn
		if (((PlayGame) (client.getCurrentState())).getMyTurn()
				&& !((PlayGame) client.getCurrentState()).getMyMoveComplete()) {
			if (mouseX > 0 && mouseX < 1000 && mouseY > 0 && mouseY < 550) {
				charact = ((PlayGame) client.getCurrentState()).getEngine()
						.getActiveCharacter();
				Coordinates currCoord = charact.getCurrCoord();
				int xCoord = currCoord.getxCoord();
				int yCoord = currCoord.getyCoord();
				mouseAngle = Math.toDegrees((double) Math
						.atan((double) (yCoord - mouseY)
								/ (double) (mouseX - xCoord)));
				if ((mouseX < xCoord)) {
					mouseAngle = mouseAngle + 180;
				}

				if ((mouseX > xCoord) && (mouseY > yCoord)) {
					mouseAngle = mouseAngle + 360;
				}

				double distance = Math.sqrt((mouseX - xCoord)
						* (mouseX - xCoord) + (mouseY - yCoord)
						* (mouseY - yCoord));
				power = distance / 300 * 100;

				setFireParams();
			}

			if (mouseX > 50 && mouseX < 125 && mouseY > 575 && mouseY < 615) {
				// move left
				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);
				((PlayGame) client.getCurrentState())
						.getClient()
						.sendMove(
								((PlayGame) client.getCurrentState()).getMyID(),
								"LEFT");
				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);
			}
			if (mouseX > 165 && mouseX < 240 && mouseY > 575 && mouseY < 615) {
				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);
				// move right
				((PlayGame) client.getCurrentState()).getClient().sendMove(
						((PlayGame) client.getCurrentState()).getMyID(),
						"RIGHT");
				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);
			}

			if (mouseX > 280 && mouseX < 375 && mouseY > 575 && mouseY < 615) {
				// fire

				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);
				if (charact == null) {
					charact = ((PlayGame) client.getCurrentState()).getEngine()
							.getActiveCharacter();
					if (charact.getTeamNumber() == 1)
						mouseAngle = 135;
				}
				((PlayGame) client.getCurrentState()).getClient().sendShot(
						charact.getPlayerID(),
						charact.getCurrWeapon().getName(), this.power,
						this.mouseAngle);
				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);
			}
			// 410, 575, 75, 40)
			if (mouseX > 395 && mouseX < 470 && mouseY > 575 && mouseY < 615) {
				// skip turn
				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);
				if (charact == null) {
					((PlayGame) client.getCurrentState()).getEngine().getActiveCharacter().setCurrWeapon(new SkipTurn());
					charact = ((PlayGame) client.getCurrentState()).getEngine()
							.getActiveCharacter();
					if (charact.getTeamNumber() == 1)
						mouseAngle = 135;
				}
				((PlayGame) client.getCurrentState()).getClient().sendShot(
						charact.getPlayerID(), "SkipTurn", this.power,
						this.mouseAngle);

				((PlayGame) client.getCurrentState()).setMyMoveComplete(true);

			}

			if (mouseX > 510 && mouseX < 530 && mouseY > 575 && mouseY < 615) {
				// change weapon scroll left
				((PlayGame) client.getCurrentState()).getEngine()
						.getActiveCharacter().prevWeapon();

			}
			if (mouseX > 595 && mouseX < 615 && mouseY > 575 && mouseY < 615) {
				// change weapon scroll right
				((PlayGame) client.getCurrentState()).getEngine()
						.getActiveCharacter().nextWeapon();
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	public void setFireParams() {
		((PlayGame) this.client.getCurrentState()).setMouseAngle(mouseAngle);
		((PlayGame) this.client.getCurrentState()).setPower(power);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}
}
