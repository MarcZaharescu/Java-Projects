import java.awt.EventQueue;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JList;

/**
 * 
 * @author mxz301 The basic GUI interface for downloading images into a specific
 *         folder + multi- threads
 */

public class Interface {

	private JFrame frame;
	private JTextField textField;
	private String website;
	private JButton btnNewButton;
	private JSoupExampleImageGrabber image;
	private ArrayList<String> files = new ArrayList<String>();
	private DefaultTableModel model = new DefaultTableModel();
	private DefaultListModel listModel = new DefaultListModel();
	public static DefaultBoundedRangeModel progressModel = new DefaultBoundedRangeModel();// =new																					// DefaultBoundedRangeModel();
	public static JProgressBar progressBar_1;
	private JList list;
	public JScrollPane scrollPane_1;
	public String threadNumber;
	public int threadNr;
	public FilePicker filePicker;
	private JScrollPane scrollPane;
	private JTextField textField_1;
	private String imageType = "";
	protected int ok1;
	protected int ok2;
	protected int ok;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				Interface window = new Interface();
				window.frame.setVisible(true);

			}
		});
	}

	/**
	 * Create the application.
	 */
	public Interface() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		
		// creates the frame
		image = new JSoupExampleImageGrabber();
		frame = new JFrame();
		frame.setBounds(100, 100, 745, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
        // initialises the button go that downloads the images in a specific folder
		JButton btnGo = new JButton("Go");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//sets the guards for image type to null
				ok = ok1 = ok2 = 0;
				//gets the web site url
				website = textField.getText();
				System.out.println(website);

				//gets the a array list containing the image properties
				try {
					files = image.ImageGrabber(website, imageType);

				} catch (IOException e) {
					
					e.printStackTrace();
				}

				// initialises the table model and creates the scroll pane
				model = new DefaultTableModel();
				scrollPane = new JScrollPane();
				scrollPane.setBounds(38, 101, 379, 199);
				frame.getContentPane().add(scrollPane);
				//model will be initialised with the specific properties in the TableModel function
				model = TableModel(files);
				//creates the table using the model
				JTable table = new JTable(model);
				scrollPane.setViewportView(table);
				//resets the imageType
				imageType = "";

			}
		});

		btnGo.setBounds(320, 50, 89, 23);
		frame.getContentPane().add(btnGo);
		
		//creates new text field
		textField = new JTextField();
		textField.setBounds(126, 51, 165, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		//creates new jlabel
		JLabel lblWebsiteUrl = new JLabel("website url :");
		lblWebsiteUrl.setBounds(38, 54, 78, 14);
		frame.getContentPane().add(lblWebsiteUrl);

		//creates new scroll pane
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(424, 94, 114, 211);
		frame.getContentPane().add(scrollPane_1);

		//creates the Exit Button which closes the aplication
		btnNewButton = new JButton("Exit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(337, 316, 89, 33);
		frame.getContentPane().add(btnNewButton);

		//creates the GetLinks button
		JButton btnNewButton_1 = new JButton("Get links");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//the JList model is initialised with all the links
				listModel = ListModel(image.getLinks());
				list = new JList(listModel);
				scrollPane_1.setViewportView(list);

			}
		});
		btnNewButton_1.setBounds(420, 50, 118, 23);
		frame.getContentPane().add(btnNewButton_1);

		//initialises the get threads button
		JButton btnNewButton_2 = new JButton("get Threads");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//sets the progress bar models bounds and initial value
				progressModel.setMinimum(0);
				progressModel.setValue(0);
				progressModel.setMaximum(JSoupExampleImageGrabber.links.size());

				//creates a new instance of the file picker class
				filePicker = new FilePicker();
				//gets the inputed number of threads by user
				threadNumber = textField_1.getText();
				threadNr = Integer.parseInt(threadNumber);
				System.out.println(filePicker.getPath());
				//puts the images in a queue for multi-thread downloading
				image.QueImages(threadNr, filePicker.getPath());
				
				//creates a new progressbar of the progress model
				progressBar_1 = new JProgressBar(progressModel);
				progressBar_1.setBounds(50, 316, 262, 33);
				progressBar_1.setStringPainted(true);
				progressBar_1.repaint();
				progressBar_1.setVisible(true);
				frame.getContentPane().add(progressBar_1);
				frame.setVisible(true);

			}
		});
		btnNewButton_2.setBounds(423, 21, 115, 23);
		frame.getContentPane().add(btnNewButton_2);

		//creates a new text field
		textField_1 = new JTextField();
		textField_1.setBounds(320, 19, 89, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		//creates a new label
		JLabel lblNumberOfThreads = new JLabel("number of threads");
		lblNumberOfThreads.setBounds(203, 25, 107, 14);
		frame.getContentPane().add(lblNumberOfThreads);

		//creates the JPG button which chooses the JPG type of images
		JButton btnJpg = new JButton("JPG");
		btnJpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ok = 1;
				if (ok1 == 1 || ok2 == 1)
					imageType = imageType + "|jp?G";
				else
					imageType = imageType + "jp?G";
			}
		});
		btnJpg.setBounds(548, 21, 62, 23);
		frame.getContentPane().add(btnJpg);

		//creates the PNG button which chooses the PNG type of images
		JButton btnPng = new JButton("PNG");
		btnPng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ok1 = 1;
				if (ok == 1 || ok2 == 1)
					imageType = imageType + "|png";
				else
					imageType = imageType + "png";

			}
		});
		btnPng.setBounds(548, 50, 62, 23);
		frame.getContentPane().add(btnPng);

		//creates the GIF button which chooses the GIF type of images
		JButton GIF = new JButton("GIF");
		GIF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ok2 = 1;
				if (ok == 1 || ok1 == 1)
					imageType = imageType + "|gif";
				else
					imageType = imageType + "gif";
			}
		});
		GIF.setBounds(548, 77, 62, 23);
		frame.getContentPane().add(GIF);

	}
	
	
	/**Creates and Updates the details of the table
	 * 
	 * @param files2 arrayList of the  images properties
	 * @return the table model
	 */

	public DefaultTableModel TableModel(ArrayList<String> files2) {
		System.out
				.println("...........................Populate the Table..................................");

		ArrayList<String> images = files2;
		ArrayList<String> resolutions = image.getResolution();

		//adds the table columns
		model.addColumn("Image Name");
		model.addColumn("Type");
		model.addColumn("Resolution");

		//for every image updates the model row with the type, name and resolution
		for (int i = 0; i < images.size(); i++) {
			String name = images.get(i);

			String fileType = name.substring(
					images.get(i).lastIndexOf('.') + 1, name.length());
			String fileName = name.substring(name.lastIndexOf('/') + 1,
					name.length());
			model.addRow(new Object[] { fileName, fileType, resolutions.get(i) });

		}
		return model;
	}

	/**Updates the List model with the images inks
	 * 
	 * @param list a list containing the images links
	 * @return the list model
	 */

	public DefaultListModel ListModel(ArrayList<String> list) {

		ArrayList<String> links = list;

		// for every link updates the list model
		for (int i = 0; i < links.size(); i++) {
			listModel.add(i, links.get(i));
		}
		return listModel;

	}
/**
 * 
 * @param i index i
 * @return returns the images name according to index i
 */
	public String getImageName(int i) {

		return files.get(i);
	}
}
