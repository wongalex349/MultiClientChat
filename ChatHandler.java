import java.io.*; 
import java.net.*; 
import java.util.*;

class ChatHandler implements Runnable
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
		
		while(sendRcvFlag(_NAME, _SEND) == false)	{	
			//do nothing 	
		}
		name = inUser.readLine();
		System.out.println("Username read from client");
	}

	public String getName()
	{
		return name;
	}
	//implement run method
	public void run() 
	{
		String input;
		try
		{	
			while(true)
			{
				System.out.println("the fridge is running");
				break;
			}
		} 	catch(Exception e) {}
	}

	public boolean sendRcvFlag(int sendFlag, int rcvFlag) throws IOException
	{
		System.out.println("sending:"+sendFlag+"expecting:"+rcvFlag);
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