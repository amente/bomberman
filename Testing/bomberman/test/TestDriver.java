package bomberman.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
		
		
		TestPlayer player1 = driver.new TestPlayer(p1File,serverPort,serverAddress);
		TestPlayer player2 = driver.new TestPlayer(p2File,serverPort,serverAddress);
		
		
		String player1ID = player1.sendJoin();
		if(player1ID != null){
			System.out.println(player1ID+ " Joining Game Success");
		}else{
			System.out.println("player1 joining game failed");
			return;
		}
		
			
		String player2ID = player2.sendJoin();
		if(player2ID != null){
			System.out.println(player2ID+ " Joining Game Success");
		}else{
			System.out.println("player2 joining game failed");
		}
		
		
		
		
		System.out.println("Sending Start Message");
		player1.sendStartGame();
		
		// Start sending move messages
		player1.start();
		// Let Player 1 get a head start so, he can be host
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		// Start sending move messages 
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
	
	public class TestPlayer extends Thread{
		
		BufferedReader reader;
		NetworkManager networkManager;		
		private NetworkAddress serverAddress;
						
		TestPlayer(File testFile,int serverPort,String serverAddress){
			try {
			    reader = new BufferedReader(new FileReader(testFile));
			    networkManager = new NetworkManager();
			    this.serverAddress = new NetworkAddress(serverAddress,serverPort);			    			  
			} catch (IOException e) {
			    e.printStackTrace();
			}			
		}		
		
		
		public String sendJoin(){	
			System.out.println("Sending Join Message...");
			return networkManager.sendSynchronous("Game Join",serverAddress,null,3,5000,false);			
		}
		
		public void sendStartGame(){			
			networkManager.sendAsynchronous("Game Start",serverAddress,false);			
		}
		
		@Override
		public void run(){			
			String move = null;
			try {
				while((move = reader.readLine()) != null){
					
					String[] lineArr = move.split(" ");			
					if(lineArr[0].equalsIgnoreCase("Move")){						
							System.out.println("Sending Move "+lineArr[1]);
							networkManager.sendAsynchronous(move.trim(),serverAddress,true);					
					}					
					//Move every 1000 milliseconds
					try {
						Thread.sleep(1000);
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
