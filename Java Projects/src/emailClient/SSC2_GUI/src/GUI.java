import java.awt.Color;
import java.awt.EventQueue;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Properties;
import javaMailExample.IMAPClient;
import javax.swing.JList;
import javax.swing.JScrollPane;
import com.sun.mail.imap.IMAPFolder;
import javax.swing.border.Border;
/**
 * @author mxz301
 *
 */
/**
 * @author mxz301
 *
 */
public class GUI {

	private JFrame frame;
	private JTextArea contentList;
	private DefaultListModel listModel = new DefaultListModel();
	private static IMAPClient client = new IMAPClient();
	private JList<?> subjectList;
	private String contentModel;
	private JButton btnButton1;
	private JButton btnButton2;
	private JButton btnButton3;
	private JButton btnButtonLogin;
	private JButton btnButtonExit;
	private JScrollPane scrollPanel;
	private JLabel username_label;
	private static String username;
	private static Properties prop;
	private static IMAPFolder folder;
	private String[] subjectName;
	private String[] content;
	private Border brd = BorderFactory.createMatteBorder(1, 1, 1, 1,
			Color.BLACK);
	private JScrollPane scrollDown;
	private JScrollPane scrollPane_1;
	private static int keyPosition;
	private static boolean keyValue;
	private static String password;
	private NewEmailFrame emailFrame;
	private JTextField keyword1Field;
	private JTextField keyword2Field;
	private String keyword1=" ", keyword2=" ";
	private JButton btnNewButton;
	private JButton btnNewButton_1;

	/**
	 * Launch the application.
	 * 
	 * @throws MessagingException
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
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 */
	public GUI() {
		try {
			initialize();
		} catch (IOException | MessagingException e) {
			JOptionPane.showMessageDialog(frame, "Invalid login details",
					"Invalid", JOptionPane.ERROR_MESSAGE);

			System.out.println("Invalid details");
		}

	}

	/**
	 * Initialise the contents of the frame.
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void initialize() throws IOException, MessagingException {
		// creates a new frame
		frame = new JFrame();
		frame.setBounds(100, 100, 798, 539);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        //initialises user name, password, props and folder
		prop = Interface.getProp();
		//starts the session
		folder = IMAPClient.startSession(prop);
		password = IMAPClient.getPassword();
		username = IMAPClient.getUsername();

		frame.getContentPane().add(getButton1());
		frame.getContentPane().add(getButton2());
		frame.getContentPane().add(getButton3());
		frame.getContentPane().add(getButtonExit());
		addtJLabel();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(353, 140, 394, 252);
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(getContentList());
		frame.getContentPane().add(getScrollPane_1());
		frame.getContentPane().add(getKeyword1Field());
		frame.getContentPane().add(getKeyword2Field());

		//creates the inbox button and selects the folder inbox
		JButton btnInbox = new JButton("inbox");
		btnInbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					folder = (IMAPClient.getInboxFolder(prop));
				} catch (IOException | MessagingException e) {
					JOptionPane
							.showMessageDialog(frame, "No inbox found",
									"Null Pointer Exception",
									JOptionPane.ERROR_MESSAGE);

					System.out.println("No inbox found");
				}
			}
		});
		btnInbox.setBounds(49, 142, 76, 40);
		frame.getContentPane().add(btnInbox);
        
		//creates the recent button and selects the folder recent
		JButton btnRecent = new JButton("recent");
		btnRecent.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					folder = (IMAPClient.getRecentFolder(prop));
				} catch (IOException | MessagingException e) {
					JOptionPane.showMessageDialog(frame,
							"No recent folder found", "Null pointer Exception",
							JOptionPane.ERROR_MESSAGE);

					System.out.println("No recent folder found");

				}
			}
		});
		btnRecent.setBounds(49, 193, 76, 40);
		frame.getContentPane().add(btnRecent);

		//creates the spam button and selects the folder spam
		JButton btnSpam = new JButton("spam");
		btnSpam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					folder = (IMAPClient.getSpamFolder(prop));
				} catch (IOException | MessagingException e) {
					JOptionPane.showMessageDialog(frame,
							"No spam folder found", "Null pointer Exception",
							JOptionPane.ERROR_MESSAGE);

					System.out.println("No spam folder found");
				}

			}
		});
		btnSpam.setBounds(49, 245, 76, 40);
		frame.getContentPane().add(btnSpam);
		frame.getContentPane().add(getBtnNewButton());
		frame.getContentPane().add(getBtnNewButton_1());
		frame.setVisible(true);

	}

	
	/**
	 * @return the keyword1Field
	 */
	private JTextField getKeyword1Field() {
		if (keyword1Field == null) {

			keyword1Field = new JTextField();
			keyword1Field.setBounds(643, 68, 99, 20);

			keyword1Field.setColumns(10);

		}
		return keyword1Field;

	}

	/**
	 * @return keyword2Field
	 */
	private JTextField getKeyword2Field() {
		if (keyword2Field == null) {

			keyword2Field = new JTextField();
			keyword2Field.setBounds(643, 94, 99, 20);

			keyword2Field.setColumns(10);

		}
		return keyword2Field;

	}

	/**
	 *  addsJLabel to the frame
	 */
	private void addtJLabel() {

		JLabel lblKeyword = new JLabel("keyword1");
		lblKeyword.setBounds(584, 71, 62, 14);
		frame.getContentPane().add(lblKeyword);

		JLabel lblKeyword_1 = new JLabel("keyword2");
		lblKeyword_1.setBounds(584, 97, 56, 14);
		frame.getContentPane().add(lblKeyword_1);

	}

	/**
	 * @return the created Button1 - AddSubject
	 */
	private JButton getButton1() {
		if (btnButton1 == null) {
			btnButton1 = new JButton("AddSubject");
			btnButton1.setBounds(207, 71, 112, 40);
			btnButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					try {
                    
					//gets the name of the subjects from the folder
					subjectName = IMAPClient.getEmailSubjects(folder);
					} catch (MessagingException e2) {
		

					}
					// removes all the elements for mthe lsit model
					listModel.removeAllElements();
					//creates a new render object to color the Jlist
					final MyListRender render = new MyListRender();
					subjectList.setCellRenderer(render);
				
					//adds all the model components to the Jlist
					for (int i = 0; i < subjectName.length; i++) {
						try {
							listModel.addElement(subjectName[i].toString());
						}

						catch (NullPointerException e1) {
							System.out.println("No element added!");

						}
					}
					subjectList.setModel(listModel);
					
                    //adds a mouse event that checks what element is selected from the JList and updates the content area with the selected subject
					subjectList.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							contentList.removeAll();
							JList subjectList = (JList) evt.getSource();
							if (evt.getClickCount() == 1) {
								int index = subjectList.locationToIndex(evt
										.getPoint());
								try {
									String content1 = IMAPClient
											.getEmailContent(folder, index);
									contentModel = (content1.toString());
									contentList.setText(contentModel);
									setKey(IMAPClient
											.returnFlags(folder, index));
									setKeyPosition(index);

								
								} catch (MessagingException | IOException e) {
									System.out.println("Message | IOException");
								}

							}
						}
					});

				
				}
			});

		}

		return btnButton1;

	}

	/**
	 * @return  the created  new Email button
	 */
	//
	private JButton getButton2() {
		if (btnButton2 == null) {
			btnButton2 = new JButton("New Email");
			btnButton2.setBounds(329, 71, 105, 40);
			// creates an instance of the email frame
			btnButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					emailFrame = new NewEmailFrame();

				}
			});
		}
		return btnButton2;
	}

	/**
	 * @return the Filter Button
	 */
	private JButton getButton3() {
		if (btnButton3 == null) {
			btnButton3 = new JButton("Filter");
			btnButton3.setBounds(444, 71, 116, 40);
			btnButton3.addActionListener(new ActionListener() {
				
				// gets the 2 inputed key words and checks if the message contains any of them
				public void actionPerformed(ActionEvent e) {
					keyword1 = keyword1Field.getText().toString();
					keyword2 = keyword2Field.getText().toString();


					Message messages[] = null;

					try {
						messages = IMAPClient.returnMesages(folder);
					} catch (MessagingException e1) {
						System.out.println("No messages found in the folder");
					}
					int count = 0;
					boolean ok = false;
					Message message1[] = new Message[1];
					Folder spamFolder = null;
					try {
						spamFolder = IMAPClient.getSpamFolder(prop);
					} catch (IOException | MessagingException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					
					//creates a new flag called spam
					Flags spam = new Flags();

				//checks if the keywords are not blank
				if(keyword1!="" && keyword2!=" ")
				{
					// for every message gets the content and then splits the content into words
					for (Message message : messages) {
						ok = false;
						String content1 = null;
						try {
							content1 = IMAPClient
									.getEmailContent(folder, count);
					
						} catch (MessagingException | IOException e3) {
						
							e3.printStackTrace();
						}
						
						String[] content = null;
						content = content1.toString()
								.split(" ");
				
					
						// for every word in the message checks if it contains any of the 2 keywords if so the ok is set to true
						for (int i = 0; i < content.length; i++) {
							//System.out.println(content[i]);
							if (content[i].contains(keyword1) || content[i].contains(keyword2))
									 {
								ok = true;
								break;
							}
						}

						
						if (ok) {
									System.out.println("Message number "+count + " has been  moved into the spam folder");
							
							message1[0] = messages[count];
							try {
								//sets the new flag for the selected message
								message1[0].setFlags(spam, true);
								//sends the message to the spam folder
								folder.copyMessages(message1, spamFolder);
							} catch (MessagingException e1) {
								e1.printStackTrace();
							}

						}

						count++;

					}
				}
				}
			});

		}
		return btnButton3;
	}

	/**
	 * @return the Exit buttton
	 */
	private JButton getButtonExit() {
		if (btnButtonExit == null) {
			btnButtonExit = new JButton("Exit");
			btnButtonExit.setBounds(559, 450, 87, 40);
			btnButtonExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					try {
						// closes the folder and the store and exits the application
						IMAPClient.closeFolderAndStore(folder);
					} catch (MessagingException e1) {
						System.out.println("The Folder could not be closed");
					}
					System.exit(0);
				}
			});

		}
		return btnButtonExit;
	}

	

	/**Deletes a selected message by setting the flag to DELETED
	 * @param message 
	 */
	public void deleteMessage(Message message) {
		try {
			message.setFlag(Flags.Flag.DELETED, true);
			System.out.println("deleted mail");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**Unreads a selected message by setting the flag to SEEN, false
	 * @param message a Selected Message
	 */
	public void unseenMessage(Message message) {
		try {
			message.setFlag(Flags.Flag.SEEN, false);
			System.out.println("deleted mail");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**Reads a selected message by setting the flag to SEEN, false
	 * @param message a selected Message
	 */
	public void seenMessage(Message message) {
		try {
			message.setFlag(Flags.Flag.SEEN, true);
			System.out.println("deleted mail");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @return a created JList of a list model
	 */
	private JList<?> getSubjectList() {
		if (subjectList == null) {
			subjectList = new JList(listModel);
			subjectList.setBorder(brd);

		}
		return subjectList;

	}

	/** 
	 * @return a created JText area
	 */
	private JTextArea getContentList() {
		if (contentList == null) {
			contentList = new JTextArea();
			contentList.setBorder(brd);

		}
		return contentList;

	}

	/**
	 * @return creates JScrollPane area
	 */
	private JScrollPane getScrollPane_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(153, 141, 161, 251);
			scrollPane_1.setViewportView(getSubjectList());
		}
		return scrollPane_1;
	}

	/** Sets the Keys to true or false
	 * @param keyValue a boolean value
	 */
	public void setKey(boolean keyValue) {

		this.keyValue = keyValue;
	}

	/**
	 * @param i index
	 * @return true if the message has the selected flag
	 * @throws MessagingException
	 */
	public static boolean geKey(int i) throws MessagingException {
		// TODO Auto-generated method stub
		return IMAPClient.returnFlags(folder, i);
	}

	/** Sets the key position to a new index
	 * @param keyPosition index
	 */
	public void setKeyPosition(int keyPosition) {

		this.keyPosition = keyPosition;
	}

	/**
	 * @return the key position
	 */
	public static int geKeyPosition() {

		return keyPosition;
	}

	/**
	 * @return the username
	 */
	public static String getUserName() {


		return username;
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	/**
	 * @return the Properties object
	 */
	public static Properties getProps() {
		// TODO Auto-generated method stub
		return prop;
	}

	/**
	 * @return the created unRead Button
	 */
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Unread");
			
			// marks the selected messages as unread by changing the flag to SEEN,false; it uses a mouse event
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					subjectList.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {

							JList subjectList = (JList) evt.getSource();
							if (evt.getClickCount() == 1) {
								int index = subjectList.locationToIndex(evt
										.getPoint());
								try {
									String content1 = IMAPClient
											.getEmailContent(folder, index);
									Message Message[] = folder.getMessages();
									unseenMessage(Message[index]);

								

								} catch (MessagingException | IOException e) {
									
									e.printStackTrace();
								}

							}
						}
					});

				}
			});
			btnNewButton.setBounds(21, 71, 46, 23);
		}
		return btnNewButton;
	}

	/**
	 * @return the created read Button
	 */
	private JButton getBtnNewButton_1() {
		if (btnNewButton_1 == null) {
			btnNewButton_1 = new JButton("Read");
			btnNewButton_1.setBounds(87, 71, 46, 23);
			btnNewButton_1.addActionListener(new ActionListener() {
				// marks the selected messages as read by changing the flag to SEEN,true; it uses a mouse events
				public void actionPerformed(ActionEvent arg0) {

					subjectList.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {

							JList subjectList = (JList) evt.getSource();
							if (evt.getClickCount() == 1) {
								int index = subjectList.locationToIndex(evt
										.getPoint());
								try {
									String content1 = IMAPClient
											.getEmailContent(folder, index);
									Message Message[] = folder.getMessages();

									seenMessage(Message[index]);

								

								} catch (MessagingException | IOException e) {
									
									e.printStackTrace();
								}

							}
						}
					});

				}
			});

		}
		return btnNewButton_1;
	}
}
