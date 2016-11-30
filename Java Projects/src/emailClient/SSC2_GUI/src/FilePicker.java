import java.awt.EventQueue;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class FilePicker {

	private JFrame frame;
	private JFileChooser filePicker;
	public JTextField attachmentTextField;
	private File file = null;
	public String fileName = "nofile";

	/**
	 * @author mxz301 Chooses a file for the attachment using the JFileChooser
	 * 
	 *
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FilePicker window = new FilePicker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FilePicker() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		// creates a new frame
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);

		// creates a new file chooser button
		filePicker = new JFileChooser("Browse...");
		filePicker.setBounds(0, 308, 278, 101);

		// adds the filePicker to the frame
		frame.getContentPane().add(filePicker);
		frame.setVisible(true);

		// checks if the option is approved and selects the file accordingly
		int returnVal = filePicker.showOpenDialog(filePicker);

		String log = "";
		String newline = "/n";
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = filePicker.getSelectedFile();

			log = ("Opening: " + file.getName() + "." + newline);
		} else {
			log = ("Open command cancelled by user." + newline);
		}

		System.out.println(log);

	}

	/**
	 * @return the file name
	 */
	public String getAttachment() {

		return file.getName();

	}

	/**
	 * @return path of the file
	 */
	public String getPath() {

		return file.getPath();

	}

}
