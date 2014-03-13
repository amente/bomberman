package bomberman.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.SlickException;

import bomberman.game.GameStateUpdate;
import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;
import bomberman.utils.buffer.Producer;
import bomberman.utils.buffer.SingleBuffer;

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
		
		
		TestSpectator p1GUI = new TestSpectator(player1);
				 
		
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
		
		startGUIThread(p1GUI);
		
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
		String playerName ;
		
						
		private SingleBuffer<GameStateUpdate> gameStateUpdates;
		private Producer<GameStateUpdate> producer;
		
		TestPlayer(File testFile,int serverPort,String serverAddress){
			try {
			    reader = new BufferedReader(new FileReader(testFile));
			    networkManager = new NetworkManager();
			    this.serverAddress = new NetworkAddress(serverAddress,serverPort);			    			  
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
			gameStateUpdates = new SingleBuffer<GameStateUpdate>(10);
			producer = new Producer<GameStateUpdate>(gameStateUpdates);
		}		
		
		
		public String sendJoin(){	
			System.out.println("Sending Join Message...");
			playerName = networkManager.sendSynchronous("Game Join",serverAddress,null,3,5000,false);		
			return playerName;
		}
		
		public void sendStartGame(){			
			networkManager.sendAsynchronous("Game Start",serverAddress,false);			
		}
		
		public void listenAndPrintUpdates(){			
			DatagramPacket packet = networkManager.receiveAsynchronous(200, true);
			if(packet!=null){
				System.out.println(playerName+" Recieved Updates:");
				String message = new String(packet.getData(),packet.getOffset(),packet.getLength());
				producer.produce(new GameStateUpdate(message));
				System.out.println(message);
			}
		}
		
		@Override
		public void run(){			
			String cmd = null;
			try {
				while((cmd = reader.readLine()) != null){
					
					String[] lineArr = cmd.split(" ");			
					if(lineArr[0].equalsIgnoreCase("Move")){						
							System.out.println("Sending Move "+lineArr[1]);
							networkManager.sendAsynchronous(cmd.trim(),serverAddress,true);					
					}else if(lineArr[0].equalsIgnoreCase("Bomb")){
						System.out.println("Sending Bomb");
						networkManager.sendAsynchronous(cmd.trim(),serverAddress,true);
					}
					
					//Send command every 200 milliseconds
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// After every sent command listen for updates					
					listenAndPrintUpdates();
					
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
		
		public SingleBuffer<GameStateUpdate> getGameStateUpdates(){
				return gameStateUpdates;
		}
		
		
		
	}
		
	public static void startGUIThread(final BasicGame game){
		
		Thread guiThread = new Thread(new Runnable(){

			@Override
			public void run() {
				 AppGameContainer app1;
					try {
						app1 = new AppGameContainer(game);
						app1.setShowFPS(false);
				        app1.setDisplayMode(960, 780, false);            
				        app1.start();
					} catch (SlickException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
				}  
				
			}
			
		});
		
		guiThread.start();
		
	}
	
	
	
}
