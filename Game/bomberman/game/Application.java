package bomberman.game;

import java.io.IOException;

import bomberman.gui.Game;

public class Application {
	
	public static final int QUEUE_CAPACITY = 500;
	public static final int MAX_DTATAGRAM_SIZE = 1024; // In Bytes
	
	public static final int FLOOR_X = 15;
	public static final int FLOOR_Y = 15;	
	public static final int MAX_NUM_PLAYERS = 4;
	public static final int GAME_STATE_UPDATE_RATE = 200;
	public static final int ENEMY_MOVEMENT_SPEED = 500;
	
	
	static boolean gameRunning = false;
	
		
	public static GameResolver startServer(int listenPort) {		
		GameResolver gameResolver = new GameResolver(listenPort);		
		gameResolver.start();
		return gameResolver;			
	}


	public static GameClient startClient(String serverAddress,int serverPort) {		
		GameClient client = new GameClient(serverPort,serverAddress);
		Game game = new Game(client);
		game.start();
		return client;
	}

	
	
	public static void main(String args[]) throws IOException{
		
		if(args.length<1){
			System.out.println("Usage: java Application listenPort");
		}
		
		int listenPort = Integer.parseInt(args[0]);							
		startServer(listenPort);
		    	    
					
	}	
	
	
}
