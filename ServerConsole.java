import java.io.*;
import client.*;
import common.*;
import ocsf.server.*;
//E6
public class ServerConsole implements ChatIF{
	private int port;
	private final static int DEFAULT_PORT = 5555;
	private AbstractServer server;

	public ServerConsole(int port){
		this.port = port;
		server = new EchoServer(port);
		try{
			server.listen();
		}
		catch(IOException ex){
			System.out.println("Error, can't set up connection " + "Terminating server" );

		}
	}

	public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
	{
		int loginid;
		System.out.println("A new client is attempting to connect to the server");
		System.out.println("Message received: " + msg + " from " + client.getInfo("Int"));
		String message= (String)msg;
		//E7c)
		if(!server.isListening()){	
			String[] newMsg = message.split(" ");
			loginid = Integer.parseInt(newMsg[1]);
			client.setInfo("Integer",loginid);
		}
		server.sendToAllClients(client.getInfo("Integer") + message);
	}
	public void display(String message){
		System.out.println("SERVER MSG> " + message);
	}

	public void accept(){
		try
    	{
      		BufferedReader fromConsole = 
        	new BufferedReader(new InputStreamReader(System.in));
      		String message;
      		//c)
      		while (true) 
      		{
       		 	message = fromConsole.readLine();
       		 	server.sendToAllClients(">SERVER MSG " + message);
				if(message.equals("#stop")){
					server.sendToAllClients("WARNING - Server has stopped listening for connections.");
				}
				if(message.equals("#quit")){
					server.stopListening();	
					server.close();
					System.out.println("Quitting server...");	
				}
				else if(message.equals("#stop")){
					server.stopListening();
				}
				else if(message.equals("#close")){
					server.stopListening();
					System.out.println(" has disconnected");
					server.sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
					server.sendToAllClients("Abnormal termination of connection.");
					server.stopListening();	
				}
				else if(message.equals("#setport " + port )){
						String[] newPort = message.split(" ");
						server.setPort(Integer.parseInt(newPort[1]));
						System.out.println("port set to " + newPort[1]);
					
				}
				else if(message.equals("#start")){
					if(!server.isListening()){
						try{
							server.listen();
						}
						catch(IOException exception){
							System.out.println("Cannot connect to client");
						}
					}
				}
				else if (message.equals("#getPort"))
				{	
					System.out.println("The current port is " + server.getPort());
				}
     		}
    	}
    	catch (Exception ex) 
    	{
      		System.out.println
        	("Unexpected error while reading from console!");
    	}
		
	}
	//b)
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
	
	    ServerConsole sv = new ServerConsole(port);

		sv.accept();
	}
}
 

