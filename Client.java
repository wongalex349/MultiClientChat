import java.io.*; 
import java.net.*; 
class Client 
{ 
	private static BufferedReader inUser = new BufferedReader(new InputStreamReader(System.in));
	private static BufferedReader inServer;
	private static DataOutputStream outServer;
	private static final int _NAME = 0x01;
	private static final int _RECV = 0x02;
	private static final int _SEND = 0x03; 
	private static final int _EXIT = 0x04;
	private static String name;

	public static void main(String argv[]) throws Exception 
    { 
		int flagInput;
		System.out.println("New client connecting to Server on port 5000...");
		Socket clientSocket = new Socket("localhost", 5000);
		inServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		outServer = new DataOutputStream(clientSocket.getOutputStream());

		name = getName();

		if(rcvFlag(_NAME))
		{
			System.out.println("sending name to server");
			sendName();
			System.out.println("Server received name");
		}

		while(true)
		{
			try
			{
				break;
			} catch(Exception e)	{	System.out.println("Client I/O error");	}
		}
		clientSocket.close();
	} 

	public static String getName() throws IOException
	{
		System.out.print("Enter username: ");
		return inUser.readLine();
	}

	public static void sendName() throws IOException
	{
		System.out.println("sendName() executing");
		if(sendFlag(_SEND))
			outServer.writeBytes(name+'\n');
	}

	public static boolean sendRcvFlag(int sendFlag, int rcvFlag) throws IOException
	{
		System.out.println("sending:"+sendFlag+"expecting:"+rcvFlag);
		outServer.writeByte(sendFlag);
		return (inServer.read() == rcvFlag);
	} 

	public static boolean rcvFlag(int rcvFlag) throws IOException
	{
		System.out.println("expecting:"+rcvFlag);
		return (inServer.read() == rcvFlag);
	}

	public static boolean sendFlag(int sendFlag) throws IOException
	{
		System.out.println("sending:"+sendFlag);
		outServer.writeByte(sendFlag);
		return true;
	}

	
	
}