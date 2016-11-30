package graphics.helpScreens;

import gameEngine.weapons.Bazooka;
import gameEngine.weapons.DirtCircle;
import gameEngine.weapons.Enrage;
import gameEngine.weapons.FireMine;
import gameEngine.weapons.FireWall;
import gameEngine.weapons.HealingBuff;
import gameEngine.weapons.Lazer;
import gameEngine.weapons.MediGun;
import gameEngine.weapons.PoisonDart;
import gameEngine.weapons.RocketLauncher;
import gameEngine.weapons.Shield;
import gameEngine.weapons.Teleport;
import graphics.gameStates.PlayGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Help screen for main game play
 *
 */
public class PlayHelp {
	public static int width = 700;
	public static int height = 500;
	private JFrame window;
	
	private Font titleFont = new Font("URW Gothic L", Font.PLAIN, 35);
	private Font bodyFont = new Font("URW Gothic L", Font.PLAIN, 15);

	public PlayHelp() {
		if (!PlayGame.helpActive) {
			window = new JFrame();

			window.setResizable(false);
			window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			// window.addWindowListener(new WindowAdapter());
			window.setTitle("Help");

			window.getContentPane().add(new PlayHelpPanel());
			window.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					PlayGame.helpActive = false;
				}
			});
			window.setSize(width, height);

			window.setLocationRelativeTo(null);
			window.setVisible(true);
			PlayGame.helpActive = true;
		}
	}



	class PlayHelpPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Image background;

		public PlayHelpPanel(){
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
			
				if(mouseX > 575 && mouseX < 660 && mouseY > 30 && mouseY < 70){
					window.dispose();
					PlayGame.helpActive = false;
					new WeaponHelp();
				}	
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			
		});
		}

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
			g.drawString("How to play", 250, 60);

			g.drawRect(40, 100, 625, 325);
			g.fillRect(40, 100, 625, 325);
			
			g.setColor(Color.DARK_GRAY);
			g.drawRect(575, 30, 85, 40);
			g.fillRect(575, 30, 85, 40);
		
			g.setColor(Color.WHITE);
			g.setFont(bodyFont);
			g.drawString("Weapons", 585, 55);
			
			g.setColor(Color.BLACK);
			
			this.setLayout(null);
			JTextPane jtp = new JTextPane();
			String allDetails = "When it is your turn to play, a yellow arrow will appear above your character and your name will appear in the top left. \n"
								+ "In a turn, you can either move or fire. To move, select left or right. To fire, select the weapon you wish to play and aim by clicking on the map. "
								+ "The further away from your character you click, the more power your shot will have - indicated by the power bar next to weapon selection. \n"
								+ "Once you've lined up your shot, click FIRE to send it.\n \n"
								+ "To change weapon, use the < and > arrows to scroll through your options. "
								+ "At random intervals throughout the game, crates (HP boosts, extra weapons) will fall through the sky. Walk to them to pick them up. "
								+ "When your HP becomes less than 0 or you fall through the bottom of the map, you will die. The game ends and a team wins when all members of the other team "
								+ "have died.";
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
