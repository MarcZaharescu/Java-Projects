package gameEngine.tools;

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

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * A few methods used in different parts of code.
 * 
 * 
 */
public class GlobalMethods {

	/**
	 * Create a dialog box to request a string,
	 * Closes the program if cancel of x is clicked.
	 * Limits the entered string to 20 characters
	 * @param text
	 * @return
	 */
	public static String requestString(String text) {
		String response = "";		
		JTextField input = new JTextField(10);
		int action = JOptionPane.showConfirmDialog(null, input, text,
				JOptionPane.OK_CANCEL_OPTION);
		if (action == JOptionPane.CANCEL_OPTION || action == JOptionPane.CLOSED_OPTION) {
			System.exit(1);
		} else {
			response = new String(input.getText());
		}
		if(response.length() > 20){
			response = response.substring(0, 19);
		}
		return response;
	}
	
	
	
	/**
	 * Create a combobox to return a string
	 * @param types The list of options (Strings)
	 * @return
	 */
	public static String comboBox(String[] types) {
		return comboBox(true, types);
	}
	
	public static String comboBox(Boolean exit, String[] types) {
		JComboBox<String> options = new JComboBox<String>(types);
		int action = JOptionPane.showConfirmDialog(null, options, "Select your role: ",
				JOptionPane.OK_CANCEL_OPTION);
		if(action == JOptionPane.CANCEL_OPTION || action == JOptionPane.CLOSED_OPTION){
			if(!exit){
				return "NO";
			}else{
				System.exit(1);
			}
		}
		return types[options.getSelectedIndex()];
	}

	/**
	 * Convert a string weapon to its object counterpart
	 * @param name
	 * @return
	 */
	public static Weapon getWeapon(String name) {
		switch (name) {
		case "Bazooka":
			return new Bazooka();
		case "DirtCircle":
			return new DirtCircle();
		case "Lazer":
			return new Lazer();
		case "MediGun":
			return new MediGun();
		case "RocketLauncher":
			return new RocketLauncher();
		case "FireWall":
			return new FireWall();
		case "PoisonDart":
			return new PoisonDart();
		case "HealingBuff":
			return new HealingBuff();
		case "Enrage":
			return new Enrage();
		case "Shield":
			return new Shield();
		case "SkipTurn":
			return new SkipTurn();
		case "FireMine":
			return new FireMine();
		case "NuclearBlast":
			return new NuclearBlast();
		case "Teleport":
			return new Teleport();
		case "LazerEX":
			return new LazerEX();
		}
		return null;
	}
}
