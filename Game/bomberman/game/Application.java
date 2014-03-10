package bomberman.game;

import java.io.IOException;

public class Application {
	
	public static final int QUEUE_CAPACITY = 10;
	public static final int MAX_DTATAGRAM_SIZE = 1024; // In Bytes
	public static final String BROADCAST_ADDR = "203.0.113.0";
	public static final String MESSAGE_DELIMITER = "/delim/";
	
	
	static boolean gameRunning = false;
	

	
	public static void main(String args[]) throws IOException{
		
		if(args.length<1){
			System.out.println("Usage: java Application listenPort");
		}
		
		int listenPort = Integer.parseInt(args[0]);							
		
		GameServer gameServer = new GameServer(listenPort);		
		GameResolver gameResolver = new GameResolver(gameServer);
		JoinResolver joinResolver = new JoinResolver(gameResolver,gameServer);
		
		

		System.out.println("Creating Game: Success");
		gameServer.listenForJoin(joinResolver);
		
		
		System.out.println("Game Started!");
		gameServer.start();
		gameResolver.start();	    	    
					
	}	
	
	
}
