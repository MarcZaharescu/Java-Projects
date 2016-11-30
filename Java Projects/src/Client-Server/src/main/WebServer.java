package main;

import java.io.IOException;
import java.net.*;

/**
 * 
 * @author mxz301 The main multi threaded server class that uses a web socket to
 *         initiate a server and in a while loop listens to any clients that
 *         want to connect
 */
public final class WebServer {

	public static Socket connectionsocket = null;
	public static ServerSocket initialsocket;

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		int portnumber = 8088;

		// establish the listen socket
		System.out.println("Starting the server");
		ServerSocket initialsocket = null;
		try {
			initialsocket = new ServerSocket(portnumber);
		} catch (IOException e) {
			System.out
					.println("Webserver: Server already in use with portnumber:"
							+ portnumber);
		}

		// process HTTP service requests in an infinite loop

		while (true) {

			// listen for a TCP connection request
			try {
				connectionsocket = initialsocket.accept();
			} catch (IOException e) {
				System.out.println("Webserver: Client connection socket error");
			}

			// create a new service request handler with the connection socket
			ServerRequestHandler request = new ServerRequestHandler(
					connectionsocket);

			// create a new thread with the request
			Thread thread = new Thread(request);

			// start the thread
			thread.start();

		}
	}

}
