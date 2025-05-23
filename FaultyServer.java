import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FaultyServer {
	public static DataOutputStream outputToClient;

	public static void main(String[] args) {		
		
		new Thread( () -> {
			try {
				// Create a server socket and connect to client
				ServerSocket serverSocket = new ServerSocket(8000);										
				Socket socket = serverSocket.accept();								
				outputToClient = new DataOutputStream(socket.getOutputStream());
				System.out.println("Faulty Server Connected.");
				
				// Send ok message to client at arbitrary time				
				while (true) {
					Timer timer = new Timer();
					int r = (int)(Math.random() * 10) + 1; 	// random delay					
					timer.schedule(new Send(), r * 1000);
					Thread.sleep(r * 1000 + 100); 		// wait for delay
					timer.cancel();
				}				
						
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
