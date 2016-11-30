package email;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Controller that logs in and sends an email to a
 * specified user
 */

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String username;
	public String password;
	private Properties prop;

	/**
	 * Log in to the MAIL API and sends and Email
	 */

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// gets the username and password from the Login file
		username = request.getParameter("element_1");
		System.out.println("Login attempted : ");
		System.out.println(username);
		password = request.getParameter("element_2");
		System.out.println(password);

		// username="zaharescumarc10@gmail.com";
		// password="zaharescu10";

		// sets up the session
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		session.setAttribute("password", password);

		// creates cookies for user name and password
		Cookie cookieUsername = new Cookie("username", username);
		Cookie cookiePassword = new Cookie("password", password);

		response.addCookie(cookieUsername);
		response.addCookie(cookiePassword);
		PrintWriter out = response.getWriter();

		// creates a new model for the email client
		Model imapClient = new Model();
		// sets up the connection
		prop = imapClient.setConnection(username, password);
		// if the session is started then redirects the user to the Email page
		if (imapClient.startSession(prop)) {
			response.sendRedirect("Email.jsp");
			System.out.println("redirecting to login success page");

		} else {
		// if the details are invalid goes to the log in page again printing
		// out Invalid Credentials
			out.println("User Name or Password Error! ");
			RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
			rd.include(request, response);
		}

	}

}