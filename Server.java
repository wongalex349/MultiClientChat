import java.io.*; 
import java.net.*; 
import java.util.*;

class Server
{ 
	private static HashMap<String, ChatHandler> users = new HashMap<String,ChatHandler>();

	public static ChatHandler addUser(Socket user) 
	{
		ChatHandler ch = new ChatHandler(user);
		users.put(ch.getName(), ch);
		System.out.println(ch.getName());
		return ch;
	}

	public static void main(String argv[])
	{ 
		ServerSocket welcomeSocket; 
		
		try
		{
			welcomeSocket = new ServerSocket(5000);
			System.out.println("Server running on port 5000");
			
			while(true) 
			{ 
				Socket connect = welcomeSocket.accept();
				ChatHandler ch = addUser(connect);
				Thread thr = new Thread(ch);
				thr.start();
			} 
		} catch(IOException e) {System.out.println("Server socket creation error");}
	} 	
} 
 