package graphics.helpScreens;

import graphics.gameStates.PlayGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Help screen for connecting and starting a game
 *
 */
public class ConnectHelp {
	public static int width = 700;
	public static int height = 500;
	private JFrame window;
	private Font titleFont = new Font("URW Gothic L", Font.PLAIN, 35);
	private Font bodyFont = new Font("URW Gothic L", Font.PLAIN, 15);

	public ConnectHelp() {
	
			window = new JFrame();

			window.setResizable(false);
			window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			window.setTitle("Help");

			window.getContentPane().add(new ConnectHelpPanel());
	
			window.setSize(width, height);

			window.setLocationRelativeTo(null);
			window.setVisible(true);
			PlayGame.helpActive = true;
		}


	class ConnectHelpPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Image background;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			try {
				background = ImageIO.read(new File("img" + File.separator + "misc" + File.separator
						+ "help-bkgrd.jpg"));
			} catch (IOException e) {
				System.out.println("Error reading background image");
			}
			g.drawImage(background, 0, 0, null);

			g.setFont(titleFont);
			g.setColor(Color.WHITE);
			g.drawString("How to connect", 200, 60);

			g.drawRect(40, 100, 625, 325);
			g.fillRect(40, 100, 625, 325);
			
			g.setFont(bodyFont);

			g.setColor(Color.BLACK);
			
			this.setLayout(null);
			JTextPane jtp = new JTextPane();
			String allDetails = "To join a game, select \"Join Game\" in the main menu. Enter your name and choose a role to play. "
								+ "Roles and the weapons available to them are listed below. "
								+ "Enter the IP of the host you wish to connect to and click OK. Then, simply wait for the host to start "
								+ "the game. \n\n"
								+ "To host a game, select \"Host Game\" in the main menu. Then, select a map and click \"Confirm Map\". "
								+ "Enter your name and choose a role to play. Roles and the weapons available to them are listed below."
								+ "Then, wait for players to join. "
								+ "As players join your game, their names will appear on your screen. You can also add some AI players by"
								+ "clicking the \"Add CPU\" button and choosing their role if you wish. \n\n"
								+ "When all the players you want to be in the game are on the screen, click \"Start game\". \n\n"
								+ "Roles and their weapons (all roles have bazooka plus the following) \n"
								+ "Normal: Lazer, RocketLauncher, PoisonDart, Teleport, FireMine, NuclearBlast, LazerEX \n"
								+ "Attacker: Lazer, RocketLauncher, Enrage \n"
								+ "Tank: RocketLauncher, DirtCircle, Shield \n"
								+ "Healer: MediGun, HealingBuff, FireWall \n";
			jtp.setFont(bodyFont);
			jtp.setText(allDetails);
		
			jtp.setVisible(true);
			jtp.setSize(625,325);
			jtp.setEditable(false);
			JScrollPane sp = new JScrollPane(jtp);

			sp.setLocation(40, 100);
			sp.setSize(625, 325);
			sp.setVisible(true);
			sp.getVerticalScrollBar().setValue(0);
			sp.getHorizontalScrollBar().setValue(0);
			this.add(sp);

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(700, 500);
		}
	}

}

