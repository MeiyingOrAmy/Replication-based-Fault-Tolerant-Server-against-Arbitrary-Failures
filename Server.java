import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	public static DataOutputStream outputToClient;

	public static void main(String[] args) {
		// Reference time to schedule send
		long refTime = System.currentTimeMillis();		
		
		new Thread( () -> {
			try {
				// Create a server socket and connect to client
				ServerSocket serverSocket = new ServerSocket(8000);										
				Socket socket = serverSocket.accept();								
				outputToClient = new DataOutputStream(socket.getOutputStream());
				System.out.println("Server Connected.");
				
				// Send ok message to client every 30 seconds
				Timer timer = new Timer();
				timer.schedule(new Send(),new Date(refTime + 100),30000);						
			}
			catch (Exception ex) {
				System.out.println(ex);
			}
			
		}).start();

	} 	// end of main
	
	static class Send extends TimerTask {
		@Override
		public void run() {
			try {				
				outputToClient.writeUTF("OK");	
			} 
			catch (Exception ex) {System.out.println("Send: " + ex);}								
		}			
	}	// end of Send

}
