// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */

	ChatIF clientUI;

	String loginId;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String loginId, String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		this.loginId = loginId;
		openConnection();
	}

	// Instance methods ************************************************
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {

		// client terminates
		// connection to server is terminated before exiting the program

		try {
			if (message.trim().startsWith("#")) {
				handleCommand(message); 
			}else {
				sendToServer(message);
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		} 

	}

	public void handleCommand(String message) {

		String command = (message.substring(1, message.length())).toLowerCase();

		// client terminates after connection to the server is terminated
		if (command.equals("quit")) {
			quit();
		} 
		// client disconnects from server
		// client does not quit
		else if (command.equals("logoff")) {

			try {
				closeConnection();
			} catch (IOException e) {
			}
		}
		// client connects to server
		// only if client is not already connected
		// display an error message if otherwise
		else if (command.equals("login")) {
 
			if (!isConnected()) {
				try {
					openConnection(); 
				} catch (IOException e) { 
				}
			} else {
				clientUI.display("You are already connected to the server.");
			}
		} 
		// displays the current host name
		else if (command.equals("gethost")) {

			clientUI.display(getHost());

		} 
		// displays the current port number
		else if (command.equals("getport")) {

			clientUI.display(String.valueOf(getPort()));
			
		} 

		command = message.substring(1, 8);
		String portOrHost = message.substring(9, message.length() - 1);
		
		// calls the setHost() method in the AbstractClient class
		// only if the client is logged off
		// display an error message if otherwise 
		if (command.equals("sethost")) {

			setHost(portOrHost);
		}
		// calls the setPort() method in the AbstractClient class
		// only if the client is logged off
		// display an error message if otherwise
		else if (command.equals("setport")) {

			setPort(Integer.parseInt(portOrHost));
		}

		else {
			// command does not exist (list of commands)
			clientUI.display(message.trim() 
					+ " does not exist. The available list of commands are: #quit, #logoff, #sethost<host>, "
					+ "#setport<port>, #login, #gethost, #getport");
		}
		
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/**
	 * Implemented hook method called each time an exception is thrown by the
	 * client's thread that is waiting for messages from the server. The method may
	 * be overridden by subclasses.
	 * 
	 * @param exception the exception raised.
	 */
	@Override
	protected void connectionException(Exception exception) {
		clientUI.display("Sorry, the server has shut down.");
		quit();
	}

	/**
	 * Implemented hook method called after the connection has been closed. The
	 * default implementation does nothing. The method may be overriden by
	 * subclasses to perform special processing such as cleaning up and terminating,
	 * or attempting to reconnect.
	 */
	@Override
	protected void connectionClosed() {
		clientUI.display("Connection closed.");
	}

}

//End of ChatClient class
