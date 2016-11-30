import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

/**
 * Creates a database and produces changes to it through console based interface
 * 
 * @author mxz301
 * 
 */

public class Exercise2 {
	static Scanner keyboard = new Scanner(System.in);
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
	static PreparedStatement InsertQuery = null;

	/**
	 * 
	 * 
	 * @throws SQLException
	 * @throws ParseException
	 * 
	 *             The main function : gets user data from a property file
	 *             Connects to the data base Drops, Creates and Populates the
	 *             tables Runs the interface
	 * 
	 */
	public static void main(String args[]) throws SQLException, ParseException {

		// read the user details from a property file

		String value[] = ReadUserDetails();

		// connect to the data base

		Connection dbConn = setUpConnection(value);

		// Create statements

		 CreateANDdrop(dbConn);
		 PopulateTable(dbConn);

		// run interface

		runInterface(dbConn);

	}

	/**
	 * 
	 * @param dbConn
	 *            Connection parameter
	 * @throws SQLException
	 * @throws ParseException
	 * 
	 *             Runs a console based interface which allows the user to
	 *             choose from 5 different options, performing the actions
	 *             required
	 */
	private static void runInterface(Connection dbConn) throws SQLException,
			ParseException {

		int option = 1;
		while (option > 0 && option <= 4) {
			System.out.println();
			System.out.println("You are accessing the date base");
			System.out.println("Choose one of the following options:");
			System.out.println("1) Register a new student:");
			System.out.println("2) Add a new Mark");
			System.out.println("3) Print the transcript form");
			System.out.println("4) Produce a report for the year:");
			System.out.println("5) Close application");
			System.out.println();
			System.out.print("You are choosing option number: ");

			option = getInteger(keyboard.nextLine());

			switch (option) {
			case 1: {
				registerStudent(dbConn);
				break;
			}
			case 2: {
				addMark(dbConn);
				break;
			}
			case 3: {
				produceTranscript(dbConn);
				break;
			}
			case 4: {
				produceReport(dbConn);
				break;
			}
			case 5: {
				Exit();
				break;
			}
			default: {
				System.out
						.println("Option entered is invalid : Input a different one !");
				option = getInteger(keyboard.nextLine());
				break;
			}

			}

		}
	}

	/**
	 * Prints end messages
	 */
	public static void Exit() {
		System.out.println("You have chosen the option 5) Exit");
		System.out.println("Application closed!");
		System.out.println();
	}

	/**
	 * Produce a report on a selected year and module for the students
	 * 
	 * @param dbConn
	 * @throws SQLException
	 */
	public static void produceReport(Connection dbConn) throws SQLException {
		System.out.println("You have chosen the option 3) Produce Report");

		System.out.println();
		System.out.println("Enter an Year");

		int year = getInteger(keyboard.nextLine());

		while (year < 0 || year > 5) {
			System.out
					.println("Year has to be between 1 - 5  : Input a different one !");
			year = getInteger(keyboard.nextLine());
		}

		System.out.println();
		System.out.println("Enter an ModuleID");
		int moduleID = getInteger(keyboard.nextLine());

		while (moduleID < 0 || moduleID > 10) {
			System.out
					.println("Module ID has to be between 1 - 10  : Input a different one !");
			moduleID = getInteger(keyboard.nextLine());
		}

		PreparedStatement moduleIDQuery = dbConn
				.prepareStatement("SELECT moduleName,lecturerID  FROM Module WHERE moduleID = "
						+ moduleID);
		moduleIDQuery.setFetchSize(50);
		ResultSet rsmoduleID = moduleIDQuery.executeQuery();
		String moduleName = "";
		int lecturerID = 0;
		while ((rsmoduleID).next()) {
			moduleName = rsmoduleID.getString("moduleName");
			lecturerID = rsmoduleID.getInt("lecturerID");
		}

		String StringlecturerID = Integer.toString(lecturerID);

		PreparedStatement lecturerIDQuery = dbConn
				.prepareStatement("SELECT* FROM Lecturer WHERE lecturerID = "
						+ StringlecturerID);
		lecturerIDQuery.setFetchSize(50);
		ResultSet rslecturerID = lecturerIDQuery.executeQuery();
		String lecturerForeName = "";
		String lecturerFamilyName = "";
		int lecturertitleID = 0;

		while ((rslecturerID).next()) {

			lecturerForeName = rslecturerID.getString("foreName");
			lecturerFamilyName = rslecturerID.getString("familyName");
			lecturertitleID = rslecturerID.getInt("titleID");
		}

		PreparedStatement marksQuery = dbConn
				.prepareStatement("SELECT COUNT(*) AS Number , SUM(mark) as TotalSum FROM Marks  WHERE (mark >0 and mark<101) AND moduleID ="
						+ moduleID +"AND year=" +year);

		ResultSet rsMarks = marksQuery.executeQuery();

		int ct = 0;
		int sum = 0;
		while ((rsMarks).next()) {
			ct = rsMarks.getInt("Number");
			sum = rsMarks.getInt("TotalSum");
		}

		PreparedStatement marks1Query = dbConn
				.prepareStatement("Select Count(*) AS Resits FROM Marks  Where mark<40 and moduleID="
						+ moduleID + "AND year=" + year);

		int resits = 0;
		ResultSet rsMarks1 = marks1Query.executeQuery();
		while ((rsMarks1).next()) {

			resits = rsMarks1.getInt("Resits");
		}

		PreparedStatement typeIDQuery = dbConn
				.prepareStatement("SELECT typeID AS typeIDsit FROM Type WHERE typeString = "
						+ " 'sit'");

		typeIDQuery.setFetchSize(50);

		ResultSet rstypeID = typeIDQuery.executeQuery();
		int typeID1 = 0;
		while ((rstypeID).next())
			typeID1 = rstypeID.getInt("typeIDsit");

		PreparedStatement marks2Query = dbConn
				.prepareStatement("Select Count(*) AS Sits FROM Marks  Where typeID="
						+ typeID1
						+ "AND moduleID="
						+ moduleID
						+ "AND year="
						+ year);

		int sit = 0;
		ResultSet rsMarks2 = marks2Query.executeQuery();
		while ((rsMarks2).next()) {

			sit = rsMarks2.getInt("Sits");
		}

		int failure = ct - sit;

		float average = sum / ct;
		float failures = (failure * 1000 / ct) / 10;

		System.out.println();
		System.out.println("ACADEMIC YEAR :" + year);
		System.out.println("Module ID :" + moduleID);
		System.out.println("Module Name:" + moduleName);
		System.out.println("Lecturer titleID:" + lecturertitleID);
		System.out.println("Lecturer Fore Name:" + lecturerForeName);
		System.out.println("Lecturer Family Name:" + lecturerFamilyName);
		System.out.println("Number of Students with Marks registered:" + ct);
		System.out.println("Average Mark:" + average);
		System.out.println("Number of resits:" + resits);
		System.out.println("Percentage of failures:" + failures + " %");

		// }

		System.out.println();

	}

	/**
	 * Produces a transcript on a selected studentID. The studentID has to exist
	 * in the database.
	 * 
	 * @param dbConn
	 * @throws SQLException
	 */
	public static void produceTranscript(Connection dbConn) throws SQLException {

		System.out.println("You have chosen the option 4) Produce Transcript");

		System.out.println();
		System.out.println("Enter an studentID");

		int studentID = getInteger(keyboard.nextLine());

		while (studentID < 0 || studentID > 100) {
			System.out
					.println("Student ID has to be between 1 - 100  : Input a different one !");
			studentID = getInteger(keyboard.nextLine());
		}

		PreparedStatement InsertQuery2 = dbConn
				.prepareStatement("SELECT studentID, moduleID, year,typeID ,mark ,notes  FROM Marks WHERE studentID = "
						+ studentID);
		InsertQuery2.setFetchSize(50);
		ResultSet rs1 = InsertQuery2.executeQuery();

		while (rs1.next()) {
			int moduleID = rs1.getInt("moduleID");
			int year = rs1.getInt("year");
			int typeID = rs1.getInt("typeID");

			PreparedStatement moduleIDQuery = dbConn
					.prepareStatement("SELECT moduleName  FROM Module WHERE moduleID = "
							+ moduleID);
			moduleIDQuery.setFetchSize(50);
			ResultSet rsmoduleID = moduleIDQuery.executeQuery();
			String moduleName = "";
			while ((rsmoduleID).next())
				moduleName = rsmoduleID.getString("moduleName");

			PreparedStatement typeIDQuery = dbConn
					.prepareStatement("SELECT typeString  FROM Type WHERE typeID = "
							+ typeID);
			typeIDQuery.setFetchSize(50);
			ResultSet rstypeID = typeIDQuery.executeQuery();
			String typeString = "";
			while ((rstypeID).next())
				typeString = rstypeID.getString("typeString");

			int mark = rs1.getInt("mark");
			System.out.println();
			System.out.println("ACADEMIC YEAR :" + year);
			System.out.println();
			System.out.println("Module Name:" + moduleName);
			System.out.println("typeString:" + typeString);
			System.out.println("mark:" + mark);

		}

		rs1.close();

		System.out.println();
	}

	/**
	 * Adds a new mark for a student. The combination of the studentID, year and
	 * module has to be unique. The studentID has to be within the database
	 * 
	 * @param dbConn
	 * @throws SQLException
	 */
	private static void addMark(Connection dbConn) throws SQLException {
		int studentID = 0;
		System.out.println("You have selected : Add a Mark");

		System.out.println("Enter an studentID");

		studentID = getInteger(keyboard.nextLine());

		String studentID1 = String.valueOf(studentID);

		while (studentID < 0 || !checkStudentID(studentID1, dbConn)) {
			System.out
					.println("Sorry there doesn`t exists a student  with id number"
							+ studentID1 + "! Input a different one");
			studentID = getInteger(keyboard.nextLine());
			studentID1 = String.valueOf(studentID);

		}

		System.out.println("Enter a Module ID");

		int moduleID = getInteger(keyboard.nextLine());

		while (moduleID < 0 || moduleID > 10) {
			System.out
					.println("Module ID has to be between 1 - 10  : Input a different one !");
			moduleID = getInteger(keyboard.nextLine());
		}

		System.out.println("Enter an year of study");

		int year = getInteger(keyboard.nextLine());

		while (year < 0 || year > 5) {
			System.out
					.println("Year has to be between 1 - 5  : Input a different one !");
			year = getInteger(keyboard.nextLine());

		}

		System.out.println("Enter a Type ID");

		int typeID = getInteger(keyboard.nextLine());

		while (typeID < 0 || typeID > 5) {
			System.out
					.println("Type ID has to be between 1 - 5 : Input a different one !");
			typeID = getInteger(keyboard.nextLine());
		}

		System.out.println("Enter a Mark");

		int mark = getInteger(keyboard.nextLine());

		while (mark < 0 || mark > 100) {
			System.out
					.println("Mark has to be between 1 - 100 : Input a different one !");
			mark = getInteger(keyboard.nextLine());
		}

		System.out.println("Enter a Note");
		String notes = keyboard.nextLine();

		while (notes.length() < 0 || notes.length() > 100) {
			System.out
					.println("Notes has to be between 1 - 100 : Input a different one !");
			notes = keyboard.nextLine();
		}

		String returnValue = "the new Mark has been sucesfully added !";

		// Insert
		PreparedStatement insert = dbConn
				.prepareStatement("INSERT into Marks VALUES(?,?,?,?,?,?)");

		// Input other values
		insert.setInt(1, studentID);
		insert.setInt(2, moduleID);
		insert.setInt(3, year);
		insert.setInt(4, typeID);
		insert.setInt(5, mark);
		insert.setString(6, notes);

		// execute the update
		try {
			insert.executeUpdate();
		} catch (SQLException e) {
			returnValue = "Sorry! The combination between studentsID , year , module ID has to be Unique";

		}

		// close the result set, statement

		System.out.println(returnValue);
	}

	/**
	 * Checks if a input date is an actual date format
	 * 
	 * @param inDate
	 *            A String version of date
	 * @return
	 * @throws ParseException
	 */

	public static boolean isDate(String inDate) throws ParseException {

		if (inDate == null)
			return false;

		// set the format to use as a constructor argument
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

		if (inDate.trim().length() != dateFormat.toPattern().length())
			return false;

		dateFormat.setLenient(false);

		try {
			// parse the inDate parameter
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}

		int date1 = Integer.parseInt(inDate.substring(0, 4));

		if (date1 > 2000 || date1 < 1900) {
			System.out
					.println("The year of birth has to be lower 2000 and greater 1900");
			return false;
		}
		return true;
	}

	/**
	 * Checks if a studentID exists in the data base
	 * 
	 * @param studentID
	 *            a String studentID
	 * @param dbConn
	 * @return true or false , depending on the finding value
	 * @throws SQLException
	 */
	public static boolean checkStudentID(String studentID, Connection dbConn)
			throws SQLException {

		PreparedStatement studentIDQuery = null;

		studentIDQuery = dbConn
				.prepareStatement("SELECT studentID  FROM Student WHERE studentID = "
						+ studentID);

		ResultSet rsstudentID;

		rsstudentID = studentIDQuery.executeQuery();

		String studentID1 = "";
		while ((rsstudentID).next())

			studentID1 = rsstudentID.getString("studentID");

		if (studentID.equals(studentID1))
			return true;

		return false;
	}

	/**
	 * 
	 * @param inDate
	 *            a String version of a date type
	 * @return a date type
	 * @throws ParseException
	 */

	public static java.util.Date getDate(String inDate) throws ParseException {
		while (!isDate(inDate)) {
			System.out.println(inDate + " - is not a date");

			inDate = keyboard.nextLine();
		}

		return df.parse(inDate);

	}

	/**
	 * Checks if the String s has any digits
	 * 
	 * @param s
	 *            A String s
	 * @return
	 */

	public static boolean isInteger(String s) {

		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (!Character.isDigit(c) && c == '-')
				return false;
		}

		return true;
	}

	/**
	 * 
	 * @param in
	 *            a String in
	 * @return an integer
	 */
	public static int getInteger(String in) {

		while (!isInteger(in)) {
			System.out.println(in + " - is not an Integer try again!");

			in = keyboard.nextLine();
		}

		return Integer.parseInt(in);

	}

	/**
	 * Registers a new student entry in the data base
	 * 
	 * @param dbConn
	 * @throws SQLException
	 * @throws ParseException
	 */

	public static void registerStudent(Connection dbConn) throws SQLException,
			ParseException {
		int studentID = 0;
		System.out.println("You Have Selected :  Reister a new Student");
		String returnValue = "Student has been sucesfully registered !";

		System.out.println("Enter an studentID");

		String studentID1 = "";

		studentID = getInteger(keyboard.nextLine());
		studentID1 = String.valueOf(studentID);

		while (checkStudentID(studentID1, dbConn)) {
			System.out
					.println("Sorry there already exists a student  with id number"
							+ studentID1 + "! Input a different one");
			studentID = getInteger(keyboard.nextLine());
			studentID1 = String.valueOf(studentID);

		}

		System.out.println();

		System.out.println("Enter an titleID");
		int titleID = getInteger(keyboard.nextLine());

		while (titleID < 0 || titleID > 5) {
			System.out
					.println("Title ID has to be between 0 - 5 : Input a different one !");
			titleID = getInteger(keyboard.nextLine());
		}

		System.out.println("Enter a Fore Name");
		String foreName = keyboard.nextLine();

		System.out.println("Enter a Family Name");
		String familyName = keyboard.nextLine();

		System.out.println("Enter a date of Birth");

		java.util.Date dateOfBirth = getDate(keyboard.nextLine());
		java.sql.Date sqlDate = new java.sql.Date(dateOfBirth.getTime());

		// Insert into Student
		PreparedStatement insert = dbConn
				.prepareStatement("INSERT into Student VALUES(?,?,?,?,?)");

		// Input other values
		insert.setInt(1, studentID);
		insert.setInt(2, titleID);
		insert.setString(3, foreName);
		insert.setString(4, familyName);
		insert.setDate(5, sqlDate);

		// execute the update
		try {
			insert.executeUpdate();
		} catch (SQLException e) {
			returnValue = "Sorry! There already  is a student on the selected studentID";
		}

		
		
		System.out.println(returnValue);
		System.out.println();
		
		System.out.println("Add values in the StudentContact table");
		
		String returnValue1 = "StudentContact table has been succesfully updated!";
		System.out.println("Enter an  Email Adress");
		String emailAdress  = keyboard.nextLine();
		
		while(emailAdress.length()<0 || emailAdress.length()>200)
		{ System.out.println("email Adress invalid: input a different one");
			emailAdress  = keyboard.nextLine();
		}
		
		System.out.println("Enter a  Postal Adress");
		String postalAdress  = keyboard.nextLine();
		
		while(postalAdress.length()<0 || postalAdress.length()>200)
		{ System.out.println("Postal Adress invalid: input a different one");
			postalAdress  = keyboard.nextLine();
		}
		
		// Insert into studentContact
				PreparedStatement insert2 = dbConn
						.prepareStatement("INSERT into StudentContact VALUES(?,?,?)");

				// Input other values
				insert2.setInt(1, studentID);
				insert2.setString(2, emailAdress);
				insert2.setString(3, postalAdress);
				

				// execute the update
			
					insert2.executeUpdate();
				

		

		System.out.println(returnValue1);
		
     System.out.println();
		
		System.out.println("Add values in the nextofKinContact table");

		String returnValue2 = "NextOFKin table has been succesfully updated!";
		System.out.println("Enter an  Email Adress");
		String nextemailAdress  = keyboard.nextLine();
		
		while(nextemailAdress.length()<0 || nextemailAdress.length()>200)
		{ System.out.println("email Adress invalid: input a different one");
			nextemailAdress  = keyboard.nextLine();
		}
		
		System.out.println("Enter a  Postal Adress");
		String nextpostalAdress  = keyboard.nextLine();
		
		while(nextpostalAdress.length()<0 || nextpostalAdress.length()>200)
		{ System.out.println("Postal Adress invalid: input a different one");
			nextpostalAdress  = keyboard.nextLine();
		}
		
		// Insert into studentContact
				PreparedStatement insert3 = dbConn
						.prepareStatement("INSERT into nextOfKinContact VALUES(?,?,?)");

				// Input other values
				insert3.setInt(1, studentID);
				insert3.setString(2, nextemailAdress);
				insert3.setString(3, nextpostalAdress);
				

				// execute the update
			
					insert3.executeUpdate();
			

		

		System.out.println(returnValue2);
		
	}

	/**
	 * Connects the user to the data base
	 * 
	 * @param value
	 *            contains the user`s details
	 * @return a Connection
	 */

	public static Connection setUpConnection(String value[]) {

		System.setProperty("jdbc.drivers", "org.postgresql.Driver");
		String dbName = "jdbc:postgresql://dbteach2/" + value[1];
		Connection dbConn = null;

		try {
			dbConn = DriverManager.getConnection(dbName, value[1], value[0]);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		try {
			Class.forName("org.postgresql.Driver");
		}

		catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}

		System.out.println("PostgreSQL driver registered.");

		if (dbConn != null) {
			System.out.println("Database accessed!");
		}

		else {
			System.out.println("Failed to make connection");
		}
		return dbConn;
	}

	/**
	 * Reds the user`s details from a property file
	 * 
	 * @return a array of string containing the details
	 */
	public static String[] ReadUserDetails() {
		String[] key = new String[2];
		String[] value = new String[2];
		int i = 0;
		int j = 0;

		try {
			File file = new File("src/userDetails");
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

	/**
	 * Populate the data base with realistic values
	 * 
	 * @param dbConn
	 * @param random
	 *            an instance of the RandomName class
	 * @throws SQLException
	 */
	private static void PopulateTable(Connection dbConn) throws SQLException {

		System.out.print("Populating Tables ");
		// populate Titles

		String titleString[] = { "Mr", "Miss", "Mrs", "Dr", "Master" };
		String title = "";
		for (int i = 0; i < 5; i++) {
			String titleID = Integer.toString(i + 1);

			title = title + "INSERT INTO Titles " + "(titleID,titleString)"
					+ "VALUES ( " + titleID + ",'" + titleString[i] + "');";

		}

		PreparedStatement InsertQuery1 = dbConn.prepareStatement(title);
		InsertQuery1.execute();
		System.out.print(".");
		// populate Lecturer
		String lecturer = "";
		for (int i = 0; i < 5; i++) {
			String titleID = Integer.toString(i + 1);
			String lecturerID = Integer.toString(i + 1);
			lecturer = lecturer + "INSERT INTO Lecturer "
					+ "(lecturerID,titleID,foreName, familyName)" + "VALUES ( "
					+ titleID + "," + lecturerID + ",'"
					+ RandomName.getForename() + "','"
					+ RandomName.getSurname() + "');";

		}
		PreparedStatement InsertQuery2 = dbConn.prepareStatement(lecturer);
		InsertQuery2.execute();
		System.out.print(".");
		// populate Student

		String StudentName[] = new String[101];

		String student = "";
		for (int i = 0; i < 100; i++) {
			String studentID = Integer.toString(i + 1);

			String date = DateofBirth();
			Random rand = new Random();
			String titleID = Integer.toString(1 + rand.nextInt(5));
			String foreName = RandomName.getForename();
			String surName = RandomName.getSurname();
			StudentName[i + 1] = foreName.charAt(0) + "." + surName;

			student = student + "INSERT INTO Student "
					+ "(studentID,titleID,forename,familyName,dateOfBirth)"
					+ "VALUES (+" + studentID + "," + titleID + ",'" + foreName
					+ "','" + surName + "','" + date + "');";

		}

		PreparedStatement InsertQuery3 = dbConn.prepareStatement(student);
		InsertQuery3.execute();
		System.out.print(".");
		// populate Modules
		//System.out.println("MODULE");

		String modules = "";
		for (int i = 0; i < 10; i++) {
			String moduleID = Integer.toString(i + 1);
			Random rand = new Random();
			String moduleName[] = { "SoftWare", "AI", "Soft-Eng", "Logic",
					"InfoWeb", "FOCS", "SSC", "Profetional", "RP", "Haskell" };
			String moduleDescription[] = { "java programming",
					"artificial intelligence ",
					"software engineering , UML diagrams ",
					"language and logic",
					"Information on the web,  website programming",
					"foundation of computer science",
					"software system components", "Profetional Computing",
					"Robot Programming", "Functional Programming" };
			// int randomModule = (new Random()).nextInt(moduleName.length);
			String lecturerID = Integer.toString(1 + rand.nextInt(5));
			modules = modules
					+ "INSERT INTO Module "
					+ "(moduleID , moduleName , moduleDescription , lecturerID)"
					+ "VALUES ( " + moduleID + ",'" + moduleName[i] + "','"
					+ moduleDescription[i] + "'," + lecturerID + ");";

		}

		PreparedStatement InsertQuery4 = dbConn.prepareStatement(modules);
		InsertQuery4.execute();
		System.out.print(".");
		// populate Types

		String types = "";
		for (int i = 0; i < 3; i++) {
			String typeID = Integer.toString(i + 1);

			String typeString[] = { "sit", "resit", "repeat" };
			types = types + "INSERT INTO Type " + "(typeID , typeString )"
					+ "VALUES ( " + typeID + ",'" + typeString[i] + "');";

		}

		PreparedStatement InsertQuery5 = dbConn.prepareStatement(types);
		InsertQuery5.execute();
		System.out.print(".");
		// populate Marks

		String marks = "";
		for (int i = 0; i < 100; i++) {
			String studentID = Integer.toString(i + 1);

			Random rand = new Random();
			String moduleID = Integer.toString(1 + rand.nextInt(10));
			int year = 1 + rand.nextInt(5);
			String year1 = Integer.toString(year);
			String typeID = Integer.toString(1 + rand.nextInt(3));
			String mark = Integer.toString(1 + rand.nextInt(100));
			String notes[] = { "good", "bad", "could be better" };

			marks = marks + "INSERT INTO Marks "
					+ "(studentID, moduleID,year,typeID,mark,notes)"
					+ "VALUES (+" + studentID + "," + moduleID + "," + year1
					+ "," + typeID + "," + mark + ",'"
					+ notes[(new Random()).nextInt(notes.length)] + "');";

		}

		PreparedStatement InsertQuery6 = dbConn.prepareStatement(marks);
		InsertQuery6.execute();
		System.out.print(".");
		// populate student contacts

		String studentContacts = "";
		for (int i = 0; i < 100; i++) {

			Random rand = new Random();
			int studentID1 = 1 + i;
			String studentID = Integer.toString(studentID1);
			String ext[] = { "@bham.ac.uk", "@yahoo.com", "gmail.com" };
			String emailAdress = "'" + StudentName[studentID1]
					+ ext[rand.nextInt(3)] + "'";
			String address1[] = { "Birmingham", "Nottingham", "Oxford",
					"Derby", "London", "outside UK" };
			int adress2 = 1 + rand.nextInt(300);
			String address3[] = { "Luton Road", "A Road", "B Road ", "C Road",
					" Edgbaston Park", "Selly Hill", "Pricharts Road",
					" Coronation Road " };
			String postalAddr = "'"
					+ address1[(new Random()).nextInt(address1.length)] + " , "
					+ Integer.toString(adress2) + " "
					+ address3[(new Random()).nextInt(address3.length)] + "'";
			studentContacts = studentContacts + "INSERT INTO StudentContact "
					+ "(studentID, eMailAddress, postalAddress)" + "VALUES (+"
					+ studentID + "," + emailAdress + ", " + postalAddr + ");";

		}
		PreparedStatement InsertQuery7 = dbConn
				.prepareStatement(studentContacts);
		InsertQuery7.execute();
		System.out.print(".");
		// populate NextOfKinContact

		String nextOfKinContact = "";
		for (int i = 0; i < 100; i++) {

			Random rand = new Random();
			int studentID1 = i + 1;
			String studentID = Integer.toString(studentID1);

			String ext[] = { "@bham.ac.uk", "@yahoo.com", "gmail.com" };
			String emailAdress = "'" + RandomName.getForename() + "."
					+ RandomName.getSurname() + ext[rand.nextInt(3)] + "'";

			String address1[] = { "Birmingham", "Nottingham", "Oxford",
					"Derby", "London", "outside UK" };
			int adress2 = 1 + rand.nextInt(300);
			String address3[] = { "Luton Road", "A Road", "B Road ", "C Road",
					" Edgbaston Park", "Selly Hill", "Pricharts Road",
					" Coronation Road " };
			String postalAddr = "'"
					+ address1[(new Random()).nextInt(address1.length)] + " , "
					+ Integer.toString(adress2) + " "
					+ address3[(new Random()).nextInt(address3.length)] + "'";
			nextOfKinContact = nextOfKinContact
					+ "INSERT INTO NextOfKinContact "
					+ "(studentID , eMailAddress , postalAddress)"
					+ "VALUES (+" + studentID + "," + emailAdress + ", "
					+ postalAddr + ");";

		}

		PreparedStatement InsertQuery8 = dbConn
				.prepareStatement(nextOfKinContact);
		InsertQuery8.execute();
		System.out.print(".done");
		System.out.println();

	}

	/**
	 * Produces a random string as a date of birth
	 * 
	 * @return a String version the type date
	 */

	public static String DateofBirth() {
		Random rand = new Random();
		String month1;
		String day1;
		int year = 1985 + rand.nextInt(10);
		int month = 1 + rand.nextInt(12);
		int day = 1 + rand.nextInt(27);

		if (month < 10)
			month1 = "0" + Integer.toString(month);
		else
			month1 = Integer.toString(month);

		if (day < 10)
			day1 = "0" + Integer.toString(day);
		else
			day1 = Integer.toString(day);

		String date = Integer.toString(year) + "-" + month1 + "-" + day1;

		return date;
	}

	/**
	 * Drops and creates the tables
	 * 
	 * @param dbConn
	 * @throws SQLException
	 */
	public static void CreateANDdrop(Connection dbConn) throws SQLException {

		PreparedStatement DropTables = dbConn
				.prepareStatement("DROP TABLE IF EXISTS Student CASCADE;"
						+ "DROP TABLE IF EXISTS Lecturer CASCADE;"
						+ "DROP TABLE IF EXISTS Module CASCADE;"
						+ "DROP TABLE IF EXISTS Marks CASCADE;"
						+ "DROP TABLE IF EXISTS StudentContact CASCADE;"
						+ "DROP TABLE IF EXISTS NextOfKinContact CASCADE;"
						+ "DROP TABLE IF EXISTS Titles CASCADE;"
						+ "DROP TABLE IF EXISTS Type CASCADE;");

		String studentTable = "CREATE TABLE Student (studentID int , titleID  int  , forename  varchar(20) NOT NULL, familyName varchar(20) NOT NULL, dateOfBirth date Check( dateOfBirth <CURRENT_DATE) , PRIMARY KEY (studentID), FOREIGN KEY (titleId) REFERENCES Titles(titleID))  ;";

		String lecturerTable = "CREATE TABLE Lecturer (lecturerID int  , titleID int , foreName varchar(20) NOT NULL, familyName varchar(20) NOT NULL, PRIMARY KEY (lecturerID), FOREIGN KEY (titleID) REFERENCES Titles(titleID) );";

		String moduleTable = "CREATE TABLE Module (moduleID int , moduleName varchar(20) , moduleDescription varchar(200), lecturerID int  , PRIMARY KEY (moduleID),  FOREIGN KEY (lecturerID) REFERENCES Lecturer(lecturerID));";

		String marksTable = "CREATE TABLE Marks (studentID int , moduleID int , year int Check (year>=1 AND year <=5), typeID int  , mark int , notes  varchar(200),FOREIGN KEY (studentID) REFERENCES Student(studentID), FOREIGN KEY (moduleID) REFERENCES Module(moduleID), PRIMARY KEY (studentID,moduleID,year), CONSTRAINT mark CHECK (mark>0 AND mark <=100) );";

		String studentContactTable = "CREATE TABLE StudentContact (studentID int , eMailAddress char(40) NOT NULL , postalAddress char(100) NOT NULL, FOREIGN KEY (studentID) REFERENCES Student(studentID) );";

		String nextOfKinTable = "CREATE TABLE NextOfKinContact(studentID int , eMailAddress varchar(201) , postalAddress varchar(201) NOT NULL, FOREIGN KEY (studentID) REFERENCES Student(studentID) );";

		String tableTitlesTable = "CREATE TABLE Titles (titleID int  , titleString varchar(20) NOT NULL,  PRIMARY KEY (titleID));";

		String typeTable = "CREATE TABLE Type (typeID int , typeString  varchar(20) Check (typeString ='sit' OR typeString ='resit' OR typeString ='repeat'), PRIMARY KEY (typeID)); ";

		DropTables.execute();
		System.out.println("Tables Droped!");
		PreparedStatement CreateTables = dbConn
				.prepareStatement(tableTitlesTable + studentTable
						+ lecturerTable + moduleTable + marksTable
						+ studentContactTable + nextOfKinTable + typeTable);
		CreateTables.execute();
		System.out.println("Tables Created!");

	}

}
