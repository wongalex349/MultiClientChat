import java.io.*; 
import java.net.*; 
import java.util.*;
import java.time.*;

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

	public static Set<String> getUsernames()
	{
		return users.keySet();
	}

	public static void printCurrentUsernames()
	{
		System.out.print("Current Users: ");
		for(String s:usernames)
			System.out.print(s+"\t");
		System.out.println();
	}

	public static void main(String argv[])
	{ 
		ChatHandler.testParser();
		/*ServerSocket welcomeSocket; 
		
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
				//printCurrentUsernames();
			} 
		} 
		catch(IOException e) {System.out.println("Server socket creation error");}*/
	} 	


	static class ChatHandler implements Runnable
	{
		private static final int _NAME = 0x01;
		private static final int _RECV = 0x02;
		private static final int _SEND = 0x03;
		private static final int _EXIT = 0x04;
		private Socket user;
		private String name;
		private BufferedReader inUser;
		private DataOutputStream outUser;

		public ChatHandler(Socket newUser)
		{
			user = newUser;
			try
			{
				inUser = new BufferedReader(new InputStreamReader(user.getInputStream()));
				outUser = new DataOutputStream(user.getOutputStream()); 
				getNameFromClient();
			}
			catch(IOException e)	{	System.out.println("ChatHandler creation error");	}
		}

		public void getNameFromClient() throws IOException
		{	
			while(sendRcvFlag(_NAME, _SEND) == false)	{	/*do nothing*/	}
			name = inUser.readLine();
			System.out.println("Username read from client");
		}

		public String getName()
		{
			return name;
		}
		public Socket getSocket()
		{
			return user;
		}
		public DataOutputStream getOutputStream()
		{
			return outUser;
		}

		//implement run method
		public void run() 
		{
			String input;
			try
			{	
				while(true)
				{
					
					break;
				}
			} 	catch(Exception e) {}
			finally
			{

			}
		}

		private static String parseMsgForOutput(String msg)
		{
			String[] parseTime = msg.split("@@parser@@");
			return parseTime[1];
		}

		private static LocalTime getTimestamp(String msg)
		{
			String[] parseTime = msg.split("@@parser@@");
			return LocalTime.parse(parseTime[0]);
		}

		public static void testParser()
		{
			String msg = LocalTime.now().toString() + "@@parser@@" + "yo what up";
			System.out.println(msg);
			String parsed = ChatHandler.parseMsgForOutput(msg);
			System.out.println(parsed);
			LocalTime timestamp = getTimestamp(msg);
			System.out.println(timestamp.toString());	
		}

		public void writeToAllUsers(String msg)
		{
			String parsedmsg = parseMsgForOutput(msg);
			Set<String> keys = getUsernames();
			DataOutputStream output;
			for(String name:keys)
			{
				output = users.get(name).getOutputStream();
				output.writeBytes(parsedmsg);
			}
		}

		public boolean sendRcvFlag(int sendFlag, int rcvFlag) throws IOException
		{
			System.out.println("sending:"+sendFlag+" expecting:"+rcvFlag);
			outUser.writeByte(sendFlag);
			return (inUser.read() == rcvFlag);
		} 
		public boolean rcvFlag(int rcvFlag) throws IOException
		{
			System.out.println("expecting:"+rcvFlag);
			return (inUser.read() == rcvFlag);
		}
		public boolean sendFlag(int sendFlag) throws IOException
		{
			System.out.println("sending:"+sendFlag);
			outUser.writeByte(sendFlag);
			return true;
		}
	}
} 
 