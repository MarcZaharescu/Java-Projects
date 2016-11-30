package email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

public class ModelDataBase {

	/**
	 * A model class for the data base that connects adds and searches through
	 * it
	 */

	private static String firstName;
	private static String familyName;
	private static String emailAdress;
	private static String family;
	private static String adress;
	private static String name;

	/**
	 * Sets up the connection
	 * 
	 * @param value
	 *            containing the user`s credentials
	 * @return a COnnection object
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection setUpConnection(String value[])
			throws SQLException, ClassNotFoundException {
		System.out
				.println("-------------------------------------------Connecting to DataBase-------------------------------------");
		System.out.println(value[1]);
		System.out.println(value[0]);

		// sets the properties and connects with the selected password and user
		// name
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", value[1]);
		connectionProps.put("password", value[0]);
		String database = "jdbc:postgresql://dbteach2.cs.bham.ac.uk/"
				+ value[1];

		// connects to the data base
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(database, connectionProps);
		System.out.println("Connected to the database");

		// returns the connection object
		return conn;
	}

	/**
	 * Creates and Drops the tables
	 * 
	 * @param dbConn
	 *            Connection object
	 * @throws SQLException
	 */
	public static void createTables(Connection dbConn) throws SQLException {
		PreparedStatement DropTables = dbConn
				.prepareStatement("DROP TABLE IF EXISTS EmailAdress ;");

		String EmailAdress = "CREATE TABLE EmailAdress ( firstname  varchar(20) NOT NULL, familyName varchar(20) NOT NULL, emailadress  varchar(20) NOT NULL)  ;";
		DropTables.execute();
		System.out.println("Tables Droped!");
		PreparedStatement CreateTables = dbConn.prepareStatement(EmailAdress);
		CreateTables.execute();
		System.out.println("Tables Created!");

	}

	/**
	 * Adds details to the database
	 * 
	 * @param first
	 *            first name field
	 * @param second
	 *            last name field
	 * @param email
	 *            email field
	 * @param dbConn
	 *            Connection object
	 * @throws SQLException
	 */
	public static void addDetails(String first, String second, String email,
			Connection dbConn) throws SQLException {

		// initialises the fields
		firstName = first;
		familyName = second;
		emailAdress = email;

		PreparedStatement insert = dbConn
				.prepareStatement("INSERT into EmailAdress VALUES(?,?,?)");

		// Input other values
		insert.setString(1, firstName);
		insert.setString(2, familyName);
		insert.setString(3, emailAdress);

		// execute the update
		insert.executeUpdate();

	}

	/**
	 * Searches a specific contact through the database
	 * 
	 * @param first
	 *            first name field
	 * @param second
	 *            last name field
	 * @param email
	 *            email field
	 * @param dbConn
	 *            Connection object
	 * @throws SQLException
	 */
	 
	public static void searchDetails(String first, String second,
			Connection dbConn) throws SQLException {
		
		// depending of the input value searches for either first ,last or both names through the data base
		System.out.println(first);
		System.out.println(second);
		if (first != "" && second != "") {
			PreparedStatement InsertQuery2 = dbConn
					.prepareStatement("SELECT firstname, familyName, emailadress  FROM EmailAdress  WHERE firstname = '"
							+ first + "'AND familyName ='" + second + "'");
			InsertQuery2.setFetchSize(50);
			ResultSet rs1 = InsertQuery2.executeQuery();

			// while there are still values in the selected data it gets the email address and prints it out
			while (rs1.next()) {
				name = rs1.getString("firstname");
				family = rs1.getString("familyName");
				adress = rs1.getString("emailadress");
				System.out.println("case12 emailadress:" + adress);

			}
		}

		if (first == "" && second != "") {
			PreparedStatement InsertQuery2 = dbConn
					.prepareStatement("SELECT firstname, familyName,  emailadress  FROM EmailAdress  WHERE  familyName ='"
							+ second + "'");
			InsertQuery2.setFetchSize(50);
			ResultSet rs1 = InsertQuery2.executeQuery();

			while (rs1.next()) {
				name = rs1.getString("firstname");
				family = rs1.getString("familyName");
				adress = rs1.getString("emailadress");
				System.out.println("case2 emailadress:" + adress);
			}

		}
		if (first != "" && second == "") {

			PreparedStatement InsertQuery2 = dbConn
					.prepareStatement("SELECT firstname, familyName,  emailadress  FROM EmailAdress  WHERE firstname = '"
							+ first + "'");
			InsertQuery2.setFetchSize(50);
			ResultSet rs1 = InsertQuery2.executeQuery();

			while (rs1.next()) {
				name = rs1.getString("firstname");
				family = rs1.getString("familyName");
				adress = rs1.getString("emailadress");
				System.out.println("case1 emailadress:" + adress);

			}

		}

	}

	/**
	 *  Reads the user details form a property file
	 * @return the users credentials
	 */
	public static String[] ReadUserDetails() {
		System.out
				.println("-------------------------------------------Readig the user details------------------------------------");
		String[] key = new String[2];
		String[] value = new String[2];
		int i = 0;
		int j = 0;

		try {
			File file = new File("/src/userDetails");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				key[i] = (String) enuKeys.nextElement();
				value[j] = properties.getProperty(key[i]);

				i++;
				j++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

}
