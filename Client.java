import java.io.*;
import java.net.*;
import java.util.Date;

public class Client {
	public static void main(String[] args) {		
				
		try {
			// Create a socket to connect to server
			Socket socket = new Socket("localhost", 8000);
			// Create input stream to receive data from server
			DataInputStream fromServer = new DataInputStream(socket.getInputStream());
			
			// Receive ok message from server and count
			String ok;
			int count = 0;
			while (true) {
				ok = fromServer.readUTF();
				System.out.println("Message \"" + ok + "\" received at " + new Date());
				count += 1;
				System.out.println("count = " + count);
			}			
		}
		catch (IOException ex) {System.out.println(ex);}			

	}

}
