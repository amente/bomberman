package bomberman.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
		if (args.length != 3) {
			System.out.println("usage: java TestDriver <serverAddress> <serverPort> <testFilePath>");
			System.exit(1);
		}		
		
        TestDriver driver = new TestDriver();
		
	    String serverAddress = args[0];
		int serverPort = Integer.parseInt(args[1]);
		String testFilePath = args[2];
		
		ArrayList<ArrayList<String>> playerCommands = new ArrayList<ArrayList<String>>();	
		
		int numPlayers= 0;
		
		// Read test file 		
		File testFile = new File(testFilePath);
		BufferedReader reader;		
		try {
			reader = new BufferedReader(new FileReader(testFile));			
			numPlayers = Integer.parseInt(reader.readLine());
			
			ArrayList<String> commands;
			for (int i=0;i<numPlayers;i++){
				reader.readLine() ; //Read START
				String nextLine = reader.readLine();
				commands = new ArrayList<String>();
				while(nextLine!=null && !nextLine.equalsIgnoreCase("END")){				
					commands.add(reader.readLine());
					nextLine = reader.readLine();
				}
				playerCommands.add(commands);
			}
			
			reader.close();
		} catch (NumberFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.exit(1);
		}
		
		ArrayList<TestPlayer> testPlayers = new ArrayList<TestPlayer>();
		
		// Send Join Messages to the server for each player		
		for(int i=0;i<numPlayers;i++){
			
			TestPlayer player = driver.new TestPlayer(playerCommands.get(i),serverPort,serverAddress);
			
			String playerID = player.sendJoin();
			if(playerID != null){
				System.out.println(playerID+ " Joining Game Success");
				testPlayers.add(player);
			}else{
				System.out.println("player joining game failed");
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Let each player join one by one
		}
				
		
		// Let the first player send start game	
		System.out.println("Sending Start Message");
		testPlayers.get(0).sendStartGame();
		
		
		//Start the spectator GUI		
		TestSpectator p1GUI = new TestSpectator(testPlayers.get(0));
		startGUIThread(p1GUI);
		
		
		// Let all players start sending their commands		
		for(TestPlayer player: testPlayers){
			player.start();							
		}
		
		// Join All Players
		
		for(TestPlayer player: testPlayers){
			try {
				player.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}							
		}	
		
		
	}
	
	public class TestPlayer extends Thread{
		
		NetworkManager networkManager;		
		private NetworkAddress serverAddress;
		private ArrayList<String> commands;
		String playerName ;
		Timer timer;
						
		private SingleBuffer<GameStateUpdate> gameStateUpdates;
		private Producer<GameStateUpdate> producer;
		
		TestPlayer(ArrayList<String> commands,int serverPort,String serverAddress){
			try {
			    this.commands = commands;
			    networkManager = new NetworkManager();
			    this.serverAddress = new NetworkAddress(serverAddress,serverPort);			    			  
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
			gameStateUpdates = new SingleBuffer<GameStateUpdate>(10);
			producer = new Producer<GameStateUpdate>(gameStateUpdates);
			timer = new Timer();			
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
			DatagramPacket packet = networkManager.receiveAsynchronous(50, true);
			if(packet!=null){
				System.out.println(playerName+" Recieved Updates:");
				String message = new String(packet.getData(),packet.getOffset(),packet.getLength());
				producer.produce(new GameStateUpdate(message));
				System.out.println(message);
			}
		}
		
		@Override
		public void run(){		
			
			// Listen for game state updates periodically
			timer.schedule(new TimerTask(){

				@Override
				public void run() {
					listenAndPrintUpdates();
				}
				
			}, 0, 50);
			
			// Send game commands
			for(String cmd: commands){
				
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
