import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FaultTolerantServer {	
	volatile static ArrayList<Integer> voteBox = new ArrayList<Integer>(3);
	static DataOutputStream outputToClient;	
	static long refTime;

	public static void main(String[] args) {		
		
		refTime = System.currentTimeMillis();
		
		try {
			// Create a server socket and connect to client
			ServerSocket serverSocket = new ServerSocket(8000);											
			Socket socket = serverSocket.accept();							
			outputToClient = new DataOutputStream(socket.getOutputStream());
			System.out.println("Server Connected.");
			
			// Initialize vote box
			for (int i=0; i<3; i++) {voteBox.add(0);}			
			
			// Create 3 sub-server threads (among which one is faulty)
			// Randomly select faulty sub-server ID
			int faultyID = (int)(Math.random()*3);
			for (int i=0; i<3; i++) {
				if (i == faultyID) {
					new Thread(new SubServer(i, true)).start();
				}
				else {
					new Thread(new SubServer(i)).start();
				}
			}			
		}
		catch (Exception ex) {
			System.out.println(ex);
		}

	} 	// end of main
	
	static class SubServer implements Runnable {
		private int id;		// sub-server id
		private boolean faulty;
		
		// Constructors
		public SubServer(int i) {id = i; faulty = false;}
		public SubServer(int i, boolean b) {id = i; faulty = b;}
		
		@Override
		public void run() {		// Decide time to send		
			// Normal server: schedule periodically
			if (!faulty) {
				Timer timer = new Timer();
				timer.schedule(new TimeToSend(),new Date(refTime + 100),30000);
			}
			// Faulty server: schedule randomly
			else {
				while (true) {
					Timer timer = new Timer();
					int r = (int)(Math.random() * 10) + 1; 	// random delay					
					timer.schedule(new TimeToSend(), r * 1000);
					try {Thread.sleep(r * 1000 + 100);} 	// wait for delay
					catch (Exception ex) {System.out.println("SubServer: " + ex);}
					timer.cancel();
				}
			}				
		} 	// end of run
		
		// Inner class TimeToSend
		class TimeToSend extends TimerTask {			
			@Override
			public void run() {
				// Vote and call send()
				try {
				voteBox.set(id, 1);	
				Thread.sleep(20); 	// wait for votes from other threads				
				send();
				}
				catch (Exception ex) {System.out.println("TimeToSend: " + ex);}
			}			
		} 	// end of TimeToSend
		
	} 	// end of SubServer
	
	public synchronized static void send() {
		// Count number of votes in the vote box
		int sumOfVotes = 0;
		for (int i=0; i<3; i++) {sumOfVotes += voteBox.get(i);}
		
		// If there are at least 2 votes, send OK
		if (sumOfVotes >= 2) { 	
			try {outputToClient.writeUTF("OK");}		
			catch (Exception ex) {System.out.println("send: " + ex);}
		}
		
		// Reset vote box
		for (int i=0; i<3; i++) {voteBox.set(i, 0);}
		
	}	// end of send

}
