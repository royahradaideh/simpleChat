// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.IOException;
import java.util.Scanner;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	private ServerConsole serverConsole;

	private Scanner scanner;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */

	private int port;

	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	public void handleMessageFromServer(String message) {

		// client terminates
		// connection to server is terminated before exiting the program

		if (message.trim().startsWith("#")) {

			handleCommand(message.trim());

		} else {
			// DISPLAY
			// serverConsole.display(message);
			System.out.println("SERVER MSG> " + message);
			sendToAllClients("SERVER MSG> " + message);
		}

	}

	public void handleCommand(String message) {

		String command = (message.substring(1, message.length())).toLowerCase();

		// server quits gracefully
		if (command.equals("quit")) {
			try {
				close();
			} catch (IOException e) {
			}
		}
		// server stops listening to new clients
		else if (command.equals("stop")) {
			stopListening();
		} 
		// server stops listening and disconnects all existing clients
		else if (command.equals("close")) {
			try {
				close();
			} catch (IOException e) { 
			}
		}
		// causes server to start listening to clients
		// server must be stopped
		else if (command.equals("start")) {
			try {
				listen();
			} catch (IOException e) {
			}
		} 
		// displays the current port number
		else if (command.equals("getport")) {
			getPort(); 
		}

		command = message.substring(0, 6);
		int port = Integer.parseInt(message.substring(9, message.length() - 1));

		// calls the setPort() method in the AbstractServer class
		// server must be closed
		if (command.equals("setport")) {
			setPort(port);
		}
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("Message received: " + msg + " from " + client);
		this.sendToAllClients(msg);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
//    
//	   Scanner scanner = new Scanner(System.in);
//	    System.out.println("What's your name?");
//	    String name = scanner.nextLine();
//	    serverConsole.display(name);
//	     
//   
//  
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
		scanner.close();
	}

	/**
	 * Implemented hook method called each time a new client connection is accepted.
	 * The default implementation does nothing.
	 * 
	 * @param client the connection connected to the client.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("A client: " + client + " has connected.");

	}

	/**
	 * Implemented hook method called each time a client disconnects. The default
	 * implementation does nothing. The method may be overridden by subclasses but
	 * should remains synchronized.
	 *
	 * @param client the connection with the client.
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("A client: " + client + " has disconnected.");

	}

}
//End of EchoServer class
