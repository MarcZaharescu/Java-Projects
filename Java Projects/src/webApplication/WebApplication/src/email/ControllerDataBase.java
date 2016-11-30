package email;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ControllerDataBase that connects to the
 * database, adds a user and searches for it
 */
@WebServlet("/ControllerDataBase")
public class ControllerDataBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String value[] = new String[2];
	private String addFirstField;
	private String addLastField;
	private String addEmailField;
	private String searchFirstField;
	private String searchLastField;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ControllerDataBase() {

		super();

	}

	/**
	 * Connects to the data base and performs the required action according to
	 * button search or add
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		value[0] = "Calculator1";
		value[1] = "mxz301";

		// if the add button is pressed gets the input field and calls the
		// database model that connects to the data base and performs the
		// actions
		if (request.getParameter("button2") != null) {

			addFirstField = request.getParameter("addFirstField");
			addLastField = request.getParameter("addLastField");
			addEmailField = request.getParameter("addEmailField");

			// sets up the connection creates the tables and adds the details to the selected table
			try {
				Connection dbConn = ModelDataBase.setUpConnection(value);
				ModelDataBase.createTables(dbConn);
				ModelDataBase.addDetails(addFirstField, addLastField,
						addEmailField, dbConn);
				PrintWriter out = response.getWriter();
				//prints that the contact has been added and  goes to the database page
				out.println("Contact Added! ");
				RequestDispatcher rd = request
						.getRequestDispatcher("/DataBase.jsp");
				rd.include(request, response);

			} catch (SQLException e) {
				
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
		}
		//if the button search has been pressed uses the data base model class to perform the searches through the database
		if (request.getParameter("button1") != null) {

			//gets the search fields form the Database page
			searchFirstField = request.getParameter("searchFirstField");
			searchLastField = request.getParameter("searchLastField");
		
			Connection dbConn;
			try {
				//sets up the connection and searches through the database
				dbConn = ModelDataBase.setUpConnection(value);
				ModelDataBase.searchDetails(searchFirstField, searchLastField,
						dbConn);
				PrintWriter out = response.getWriter();
				// if the contact has been found it prints it out and goes to the database page
				out.println("Contact Found! ");
				RequestDispatcher rd = request
						.getRequestDispatcher("/DataBase.jsp");
				rd.include(request, response);
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}

		}

	}

}
