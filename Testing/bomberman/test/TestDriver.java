package bomberman.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;

public class TestDriver {
	public static void main(String args[]) {
		if (args.length != 2) {
			System.out.println("usage: java TestDriver <serverAddress> <serverPort>");
			System.exit(1);
		}
		
        TestDriver driver = new TestDriver();
		
	    String serverAddress = args[0];
		int serverPort = Integer.parseInt(args[1]);
				
		File p1File = new File("Testing/Resources/player1.txt");
		File p2File = new File("Testing/Resources/player2.txt");
		
		
		Player player1 = driver.new Player(p1File,serverPort,serverAddress);
		Player player2 = driver.new Player(p2File,serverPort,serverAddress);
		
		player1.start();
		player2.start();
		
		try {
			player1.join();
			player2.join();
		} catch (InterruptedException e) {
			player1.interrupt();
			player2.interrupt();
			e.printStackTrace();
		}
		
		
	}
	
	public class Player extends Thread{
		
		BufferedReader reader;
		NetworkManager networkManager;
		private int serverPort;
		private String serverAddress;
				
		Player(File testFile,int serverPort,String serverAddress){
			try {
			    reader = new BufferedReader(new FileReader(testFile));
			    networkManager = new NetworkManager();
			    this.serverAddress = serverAddress;
			    this.serverPort = serverPort;			  
			} catch (IOException e) {
			    e.printStackTrace();
			}			
		}		
		
		@Override
		public void run(){			
			String message = null;
			try {
				while((message = reader.readLine()) != null){
					
					String[] lineArr = message.split(" ");			
					if (lineArr[0].equals("Game")){
						try {
							System.out.println("Sending Join Message...");
							String playerID = networkManager.sendSynchronous(message.trim(),new NetworkAddress(serverAddress,serverPort),null,3,300,false);
							System.out.println("Joining Game: "+ (playerID!=null?("Success "+playerID):"Fail"));
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
					} 					
					
					//Send the messages every 100 milliseconds
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(this.isInterrupted()){
						reader.close();
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
