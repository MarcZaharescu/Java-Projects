import java.awt.EventQueue;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Properties;

/**
 * @author mxz301 Creates a new email frame where the user can send a email
 *
 */
public class NewEmailFrame {

	private JFrame frmNewEmail;
	private JTextField sendToField;
	private JTextField subjectField;
	private JTextArea textArea;
	private String emailSubject, emailSender, emailSendTo, emailBody;
	public String username;
	public String password;
	public Properties props;
	private FilePicker filePicker = null;
	private String attachments = "nofile";
	private JTextField ccField;
	private String cc = "nocc";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewEmailFrame window = new NewEmailFrame();
					window.frmNewEmail.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewEmailFrame() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		// creates a new frame
		frmNewEmail = new JFrame();
		frmNewEmail.setTitle("New Email");
		frmNewEmail.setBounds(100, 100, 740, 482);
		frmNewEmail.getContentPane().setLayout(null);

		// creates a new send to label
		JLabel lblSendto = new JLabel("send to:");
		lblSendto.setBounds(153, 28, 56, 20);
		frmNewEmail.getContentPane().add(lblSendto);
		// creates a new subject label
		JLabel lblSubject = new JLabel("subject");
		lblSubject.setBounds(153, 73, 46, 14);
		frmNewEmail.getContentPane().add(lblSubject);
		// creates a new cc label
		JLabel lblCc = new JLabel("cc:");
		lblCc.setBounds(176, 120, 30, 14);
		frmNewEmail.getContentPane().add(lblCc);
        // creates a new sendToField JtextField
		sendToField = new JTextField();
		sendToField.setBounds(219, 28, 389, 20);
		frmNewEmail.getContentPane().add(sendToField);
		sendToField.setColumns(10);
        //creates a new subject text field
		subjectField = new JTextField();
		subjectField.setBounds(218, 70, 390, 20);
		frmNewEmail.getContentPane().add(subjectField);
		subjectField.setColumns(10);
        // creates a  new  cc text field
		ccField = new JTextField();
		ccField.setBounds(219, 117, 389, 20);
		frmNewEmail.getContentPane().add(ccField);
		ccField.setColumns(10);
        //creates a new text area for the email content
		textArea = new JTextArea();
		textArea.setBounds(219, 168, 389, 184);
		frmNewEmail.getContentPane().add(textArea);
        // creates a new button send
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			//sets the email`s subject, sendTo , body, cc: fields
			public void actionPerformed(ActionEvent arg0) {
				setEmailSendTo(sendToField.getText());
				setEmailSubject(subjectField.getText());
				setEmailBody(textArea.getText());



			
					try {
						//sends the message from the user`s account
						sendMessage(GUI.getUserName(), GUI.getPassword(),
								GUI.getProps());
					} catch (MessagingException e) {
						
						e.printStackTrace();
					

				}
			}
		});
		
	
		btnSend.setBounds(519, 382, 89, 23);
		frmNewEmail.getContentPane().add(btnSend);
        
		
		//creates an attach button which  creates a new FilePciekr object when selected
		JButton btnAttach = new JButton("Attach");
		btnAttach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				filePicker = new FilePicker();
				attachments = filePicker.getAttachment();

			}
		});
		btnAttach.setBounds(400, 382, 89, 23);
		frmNewEmail.getContentPane().add(btnAttach);
		frmNewEmail.setVisible(true);

	}

	/** Sends the message using the required parameter
	 * @param username String username
	 * @param password String password
	 * @param props Property object
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendMessage(String username, String password, Properties props)
			throws AddressException, MessagingException {

		// initialises the username, password and cc 
		this.username = username;
		this.password = password;
		cc = ccField.getText();
		// checks if there is an attachment
		
		//sets the stpmhost
		String smtphost = "smtp.gmail.com";

		// Get system properties
		this.props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");
		
         // creates a session and a message
		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);
		//sets the message parameters accordingly
		message.setFrom(new InternetAddress(username));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(getEmailSendTo()));
		message.setSubject(getEmailSubject());
		message.setText(getEmailBody());

        // checks if the cc field is not null
		if (cc.length() > 1) {
			message.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(cc));
		}
		BodyPart messageBodyPart = new MimeBodyPart();
		Multipart multipart = new MimeMultipart();
		//if we have selected attachments
		if (!attachments.equals("nofile")) {

			// Create the message part and a multipart message
			multipart.addBodyPart(messageBodyPart);

	
            // gets the source of the file
			DataSource source = new FileDataSource(filePicker.getPath());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(attachments);
			multipart.addBodyPart(messageBodyPart);
			
			// sets the complete message parts
			message.setContent(multipart);

		}

		message.setSentDate(new Date());
		message.saveChanges();

		//Sends the message by javax.mail.Transport .
		Transport tr = session.getTransport("smtp"); // Get Transport object
														// from session
		tr.connect(smtphost, username, password); 
		tr.sendMessage(message, message.getAllRecipients()); 

		System.out.println("EMail has been Sent");

	}

	/**
	 * @return the subject of the email
	 */
	public String getEmailSubject() {
		return emailSubject;
	}

	/**Sets the Subject of the email to a new one
	 * @param emailSubject String email subject
	 */
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	/**
	 * @return the email sender
	 */
	public String getEmailSender() {
		return emailSender;
	}

	/**Sets the EmailSender to t a new one
	 * @param emailSender String email sender
	 */
	public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
	}

	/**
	 * @return a send to string
	 */
	public String getEmailSendTo() {
		return emailSendTo;
	}

	/**Sets the emailTo string to a new one
	 * @param emailSendTo a new senTo  string
	 */
	public void setEmailSendTo(String emailSendTo) {
		this.emailSendTo = emailSendTo;
	}

	/**
	 * @return the body of the email
	 */
	public String getEmailBody() {
		return emailBody;
	}

	/**sets the Body of the email to a new one
	 * @param emailBody a new email body
	 */
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

}
