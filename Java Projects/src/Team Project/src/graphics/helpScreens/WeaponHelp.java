package graphics.helpScreens;

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
import gameEngine.weapons.Teleport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Help screen to explain different weapons in the game
 *
 */
public class WeaponHelp {
	public static int width = 700;
	public static int height = 500;
	private JFrame window;
	private Font titleFont = new Font("URW Gothic L", Font.PLAIN, 35);
	private Font bodyFont = new Font("URW Gothic L", Font.PLAIN, 15);

	public WeaponHelp() {
	
			window = new JFrame();

			window.setResizable(false);
			window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			window.setTitle("Help");
			window.getContentPane().add(new WeaponHelpPanel());
		
			
			window.setSize(width, height);

			window.setLocationRelativeTo(null);
			window.setVisible(true);
		}


	class WeaponHelpPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Image background;

		/*public WeaponHelpPanel(){
		this.adengineouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				if(mouseX > 410 && mouseX < 485 && mouseY > 260 && mouseY < 280){
					window.dispose();
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
		}*/

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
			g.drawString("Weapons", 250, 60);
			
			
			
			g.setColor(Color.BLACK);
			
			
			this.setLayout(null);
			JTextPane jtp = new JTextPane();
			String allDetails = new Bazooka().getDetails() + new Lazer().getDetails() + new LazerEX().getDetails() + new RocketLauncher().getDetails()
					+ new PoisonDart().getDetails() + new Teleport().getDetails()+new FireMine().getDetails()+
					new MediGun().getDetails() + new DirtCircle().getDetails() +
					new FireWall().getDetails() + new Enrage().getDetails() + 
					new Shield().getDetails() + new HealingBuff().getDetails() + new NuclearBlast().getDetails();
			jtp.setFont(bodyFont);
			jtp.setText(allDetails);
		
			jtp.setVisible(true);
			jtp.setSize(625,325);
			jtp.setEditable(false);
			JScrollPane sp = new JScrollPane(jtp);

			this.add(sp);
			sp.setLocation(40, 100);
			sp.setSize(625, 325);
			sp.setVisible(true);
			sp.getVerticalScrollBar().setValue(0);
			sp.getHorizontalScrollBar().setValue(0);

			
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(700, 500);
		}
	}
	
	

}
