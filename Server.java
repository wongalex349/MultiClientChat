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
				//printCurrentUsernames();
			} 
		} 
		catch(IOException e) {System.out.println("Server socket creation error");}
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
			//while(!inUser.ready())	{	sendFlag(_NAME);	}
			name = inUser.readLine();
			System.out.println("Username read from client: "+name);
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
			writeToAllUsers("New user <"+name+"> has joined!", false);
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

		private compareMessages(String msg1, String msg2)
		{
			LocalTime msgTime1 = getTimestamp(msg1);
			LocalTime msgTime2 = getTimestamp(msg2);
			return msgTime1.compareTo(msgTime2); 
		}

		private LocalTime getTimestamp(String msg)
		{
			String[] parseTime = msg.split("@@parser@@");
			return LocalTime.parse(parseTime[0]);
		}

		private String parseMsgForOutput(String msg)
		{
			String[] parseTime = msg.split("@@parser@@");	
			return parseTime[0].substring(0,8) + " >>> " + name + ":\t" + parseTime[1];
		}

		public void testParser()
		{
			String msg = LocalTime.now().toString() + "@@parser@@" + "yo what up";
			System.out.println(msg);
			String parsed = parseMsgForOutput(msg);
			System.out.println(parsed);
		}

		public void writeToAllUsers(String msg, boolean parse)
		{
			if(parse)
				msg = parseMsgForOutput(msg);
			Set<String> keys = getUsernames();
			DataOutputStream output;
			try
			{
				for(String name:keys)
				{
					System.out.println("attempting to echo");
					output = users.get(name).getOutputStream();
					//sendFlag(_SEND);
					//do
					//{
						output.writeBytes(msg+"\n");
					//} while(!rcvFlag(_RECV));

				}
			} catch(IOException e)	{	System.out.println("!! message could not be sent !!");	}
		}

		public boolean sendRcvFlag(int sendFlag, int rcvFlag) throws IOException
		{
			System.out.println("sending:"+sendFlag+" expecting:"+rcvFlag);
			outUser.writeByte(sendFlag);
			int temp = inUser.read();
			//System.out.println("flag: " + temp);
			return (temp == rcvFlag);
		} 
		public boolean rcvFlag(int rcvFlag) throws IOException
		{
			System.out.println("expecting:"+rcvFlag);
			if(inUser.ready())
				return (inUser.read() == rcvFlag);
			else
				return false;
		}
		public boolean sendFlag(int sendFlag) throws IOException
		{
			System.out.println("sending:"+sendFlag);
			outUser.writeByte(sendFlag);
			return true;
		}
	}
} 
 