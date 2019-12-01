import java.io.*; 
import java.net.*; 
import java.time.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
class Client 
{
	private static PrintWriter out;
	private static BufferedReader inServer;
	private static DataOutputStream outServer;
	private static final int _NAME = 0x01;
	private static final int _RECV = 0x02;
	private static final int _SEND = 0x03; 
	private static final int _EXIT = 0x04;
	private static String name;
	private static JFrame gui = new JFrame("Message Sender");
	private static JTextField text = new JTextField(100);
	private static JTextArea message = new JTextArea(50,100);
	private static BufferedReader inUser = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String argv[]) throws Exception 
    {
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
    	text.setEditable(true);
    	message.setEditable(false);
    	gui.getContentPane().add(text,BorderLayout.SOUTH);
    	gui.getContentPane().add(new JScrollPane(message), BorderLayout.CENTER);
    	gui.pack();
		//int flagInput;
		String userInput = null;
		boolean closeSocket = false;
		System.out.println("New client connecting to Server on port 5000...");
		Socket clientSocket = new Socket("localhost", 5000);
		inServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		outServer = new DataOutputStream(clientSocket.getOutputStream());

		name = getName();

		//if(rcvFlag(_NAME))
	//	{
			System.out.println("sending name to server");
			sendName();
			System.out.println("Server received name");
	//	}

		while(true)
		{
			try
			{
				getMessages();
				textBoxControls(); //only accepts first input and echoes?
				if(closeSocket)
					break;
			} catch(Exception e)	{	System.out.println("Client I/O error");	}
		}
		clientSocket.close();
	} 

//	public static String getName() throws IOException
//	{
//		String input;
//		do
//		{
//			System.out.print("Enter username: ");
//			input = inUser.readLine();
//			//input = text.getText();
//			text.setText("");
//			if(input==null)
//				System.out.print("Username cannot be blank!");
//		} while(input==null);
//		return input;
//
//	}
	public static String getName() {
		return JOptionPane.showInputDialog(
				gui,  "Choose a username: ","Screen name selection", JOptionPane.PLAIN_MESSAGE);
	}
	public static void sendName() throws IOException
	{
		System.out.println("sendName() executing");
		while(!rcvFlag(_NAME))	{	/*do nothing*/	}
		sendFlag(_SEND);
		message.append(name + " has joined!\n");
		outServer.writeBytes(name+'\n');
	}

	public static void textBoxControls() throws IOException
	{
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//out.println(text.getText());
				try {
					sendMsgToServer(text.getText());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				text.setText("");
			}
		});

//		String userInput = getUserInput();
		//sendMsgToServer(userInput);

	}

	public static String getUserInput() throws IOException
	{

		//return text.getText();
		System.out.print("\n> ");
		return inUser.readLine();
		//return text.getText();
	}

	public static void sendMsgToServer(String msg) throws IOException
	{
		String timestamp = LocalTime.now().toString();
		String fullmessage = timestamp + "@@parser@@" + msg + "\n";
		//sendFlag(_SEND);
		//outServer.writeBytes(fullmessage);
		message.append(fullmessage + "\n");
		//System.out.println(timestamp);
	}

	public static void getMessages() throws IOException
	{
		while(inServer.ready())
			System.out.println(inServer.readLine());
	}

	public static boolean sendRcvFlag(int sendFlag, int rcvFlag) throws IOException
	{
		System.out.println("sending:"+sendFlag+" expecting:"+rcvFlag);
		outServer.writeByte(sendFlag);
		return (inServer.read() == rcvFlag);
	} 

	public static boolean rcvFlag(int rcvFlag) throws IOException
	{
		if(inServer.ready())
		{
			System.out.println("expecting:"+rcvFlag);
			return (inServer.read() == rcvFlag);
		}
		else
			return false;
	}

	public static boolean sendFlag(int sendFlag) throws IOException
	{
		System.out.println("sending:"+sendFlag);
		outServer.writeByte(sendFlag);
		return true;
	}

	
	
}