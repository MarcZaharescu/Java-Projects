package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.io.IOException;

/**
 * Client class that performs 2 actions according to the user input: 1 - gets a
 * file from the file system 2 - gets the html content of a website
 * 
 */

public class Client {
	private static String USER_AGENT = "Chrome";

	final static String CRLF = "\r\n";

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		Scanner keyboard = new Scanner(System.in);

		System.out.println("Client started");
		System.out.println();

		System.out.println("Waiting for the user input");
		System.out.println("Choose 1 of the following options ");
		System.out.println("1 - get file from local directory");
		System.out.println("2 - get website content");

		System.out.println("Enter your option");
		int userOption = keyboard.nextInt();

		while (userOption == 1 || userOption == 2) {
			switch (userOption) {
			case 1: {

				String file = "/";
				System.out.println("Enter a file from your local system");
				keyboard.nextLine();
				file = file + keyboard.nextLine() + " ";
				// listen for a TCP connection request
				System.out.println("Establishing connection");
				System.out.println("Performing option 1");
				System.out.println();

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
					System.out
							.println("Client: pritwriter error : IO Exeption");

				}

				String userInput = "GET " + file + "HTTP/1.1" + CRLF;
				out.println(userInput);
				out.println("Host: localhost");
				out.println("User Agent: User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
				out.println("Accept: */*");

				// flush the output stream
				out.flush();

				// read the file output
				System.out.println("File content is:");
				System.out.println();
				String headerLine = null;
				BufferedReader is = null;
				try {
					is = new BufferedReader(new InputStreamReader(
							clientSocket.getInputStream()));
					headerLine = is.readLine();
					while ((headerLine = is.readLine()) != null) {
						System.out.println(headerLine);
					}

				} catch (IOException e) {
					System.out.println("Client :IO read line error");

				}
				System.out.println();
				break;

			}
			case 2: {
				System.out
						.println("Enter a website ( 'http://' is not needed )");
				String website = keyboard.nextLine();
				website = keyboard.nextLine();
				System.out.println("Establishing connection");
				System.out.println("Performing option 2");
				String url = "http://" + website;

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

					e.printStackTrace();
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
					in = new BufferedReader(new InputStreamReader(
							con.getInputStream()));
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

				break;

			}

			default: {
				System.out.println("Incorrect option try again");
				userOption = keyboard.nextInt();

				break;
			}

			}
			System.out.println("Choose another option");
			userOption = keyboard.nextInt();

		}
	}
}
