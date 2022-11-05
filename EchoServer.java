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
		
		try {
			
			if (message.startsWith("#")) {
				handleCommand(message);
			}else {
				System.out.println("SERVER MSG> " + message);
				sendToAllClients("SERVER MSG> " + message);
			}
			
		} catch (IOException e) {
			System.out.println("Could not send message to server.  Terminating client.");
		} 
	}

	public void handleCommand(String message) throws IOException{

		String[] command = message.toLowerCase().trim().split(" ");
		
		switch (command[0]) {
		case "#quit": 
			stopListening();
			close();
			break; 
		 
		case "#stop":
			stopListening();
			break;
			
		case "#close":
			close();
			break;
			
		case "#start":
			if(!isListening()) {
				listen();  
			}else {
				System.out.println("The server is already listening to connections.");
			}
			break; 
			
		case "#getport":
			getPort();
			break;
	
		case "#setport":
			if(command.length == 2) {
				setPort(Integer.parseInt(command[1]));
			}else {
				System.out.println("Please provide a port name (#sethost PORTNAME).");
			}
			break;
		default:
			System.out.println("Invalid Command");
		}
	}
 
	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) { 
		
		String message = (String) msg;
		String[] messageL =  message.split(" ");
		String loginId = null;
		 
		
		if(messageL.length == 4) {
			if((messageL[1]+messageL[2]).equals("haslogged")) {
				loginId = messageL[0];
				client.setInfo("loginId", loginId);
				System.out.println("Message received: #login " + loginId + " from " + client.getInfo("loginId"));
				System.out.println(client.getInfo("loginId") + " has logged on.");
				this.sendToAllClients(msg);
			}
		}
		
		else if (msg.equals("Client Already logged in.")) {
			try { 
				client.close();
			} catch (IOException e) {
			}
		}
		
		else {
			System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
			this.sendToAllClients(client.getInfo("loginId") + "> " +  message);	 
		}
		
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort()); 
	}

	/**
	 * This method overrides the one in the superclass. Called when the"<loginID> has logged on. server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	} 

	/**
	 * Implemented hook method called each time a new client connection is accepted.
	 * The default implementation does nothing.
	 * 
	 * @param client the connection connected to the client.
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		//System.out.println("A client: " + client + " has connected.");
		System.out.println("A new client has connected to the server.");
		//handleMessageFromClient(loginId,null);

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
		System.out.println(client.getInfo("loginId") + " has disconnected.");

	}

}
//End of EchoServer class
