// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
    {
        System.out.println("Message received: " + msg + " from " + client);
        if (msg.toString().startsWith("#login")) {
            if (client.getInfo("loginId") != null) {
                try {
                    // send an error message back
                    client.sendToClient("You've already sent an loginId before!");
                } catch (IOException ignored) {
                }
            } else {
                String loginId = msg.toString().split(" ")[1];
                client.setInfo("loginId", loginId);
            }
        } else {
            if (client.getInfo("loginId") == null) {
                try {
                    // send an error message back
                    client.sendToClient("You didn't set loginId!");
                    client.close();
                } catch (IOException ignored) {
                }
            }
            this.sendToAllClients(client.getInfo("loginId") + " " + msg);
        }
    }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //c)
  /**
	This method overrides the ClientConnected method in AbstractServer. 
	*/
  protected void clientConnected(ConnectionToClient client){
	System.out.println("A client has connected");
	System.out.println("A new client is attempting to connect to the server.");
	System.out.println("Message received #login " + client.getInfo("Integer") + " from null" );
	System.out.println(client.getInfo("Integer") + " has logged on");
  }

  /**
	This method overrides the ClientDisconnected method in AbstractServer.
	*/
  synchronized protected void clientDisconnected(ConnectionToClient client){
	System.out.println(client.getInfo("Integer") + " client has disconnected");
  }

  /**
	This method overrides the clientException method in AbstractServer.
	*/
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) {
		System.out.println("A client has disconnected");
    }
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
	  System.out.println("A new client is attempting to connect to the server");
	  
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
//End of EchoServer class
}
