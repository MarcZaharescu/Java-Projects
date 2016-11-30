package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

public class GUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private String URL = "localhost";
	private String PORT = "8088";
	private static String USER_AGENT = "Chrome";
	final static String CRLF = "\r\n";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
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
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("GO");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(101, 198, 145, 41);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean option1 =false;
				option1= rdbtnNewRadioButton.isSelected();
				boolean option2 =false;
				option2= rdbtnNewRadioButton_1.isSelected();

				URL = textField.getText();
				PORT = textField_1.getText();

				if (option1) {
					Option1();
				} else if (option2) {
					Option2(URL, Integer.valueOf(PORT));
				}

			}
		});

		JPanel panel = new JPanel();
		panel.setToolTipText("Enter Website");
		panel.setBounds(101, 87, 177, 30);
		frame.getContentPane().add(panel);

		textField_1 = new JTextField();
		panel.add(textField_1);
		textField_1.setColumns(10);

		JButton btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.setBounds(335, 11, 89, 41);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		final JRadioButton rdbtnNewRadioButton = new JRadioButton(
				"Get a file from the local directory");
		rdbtnNewRadioButton.setBounds(44, 20, 193, 23);
		frame.getContentPane().add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 if(rdbtnNewRadioButton_1.isSelected())
				{
				 	rdbtnNewRadioButton.setSelected(false);
				}
			}
		});

		final JRadioButton rdbtnNewRadioButton_1 = new JRadioButton(
				"Get a file from a website");
		rdbtnNewRadioButton_1.setBounds(44, 46, 193, 23);
		frame.getContentPane().add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 if(rdbtnNewRadioButton.isSelected())
				{
				 	rdbtnNewRadioButton_1.setSelected(false);
				}
			}
		});
		JLabel lblNewLabel = new JLabel("Enter URL");
		lblNewLabel.setBounds(10, 93, 89, 17);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Enter Port Number");
		lblNewLabel_1.setBounds(10, 137, 98, 14);
		frame.getContentPane().add(lblNewLabel_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(101, 128, 177, 30);
		frame.getContentPane().add(panel_1);

		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);
	}

	public void Option1() {
		// listen for a TCP connection request
		System.out.println("Establishing connection");
		System.out.println("Performing option 1");
		System.out.println();

		String file = "/index.html ";
		Socket clientSocket = null;
		try {
			clientSocket = new Socket("localhost", 8088);
		} catch (UnknownHostException e) {
			System.out
					.println("Client: cannot connect to localhost : Unkowkn Exception");

		} catch (IOException e) {
			System.out
					.println("Client: cannot connect to localhost : IO Exeption");

		}
		PrintWriter out = null;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Client: pritwriter error : IO Exeption");

		}

		String userInput = "GET " + file + "HTTP/1.1" + CRLF;
		out.println(userInput);
		out.println("Host: localhost");
		out.println("User Agent: User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
		out.println("Accept: */*");

		// flush the output stream
		out.flush();

		// read the file output
		String headerLine = null;
		BufferedReader is = null;
		try {
			is = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			headerLine = is.readLine();
			while ((headerLine = is.readLine()).length() != 0) {
				System.out.println(headerLine);
			}

		} catch (IOException e) {
			System.out.println("Client :IO read line error");

		}
	}

	public void Option2(String para_url, int para_port) {
		System.out.println("Establishing connection");
		System.out.println("Performing option 2");
		String url = para_url;

		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {

			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {

		}

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e) {

			e.printStackTrace();
		}
		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				response.append(CRLF);
			}
			in.close();
		} catch (IOException e) {

		}

		// print result
		System.out.println(response.toString());

	}
}
