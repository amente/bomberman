package bomberman.game;

import java.io.IOException;

public class Application {
	
	public static final int QUEUE_CAPACITY = 10;
	public static final int MAX_DTATAGRAM_SIZE = 1024; // In Bytes
	public static final String BROADCAST_ADDR = "203.0.113.0";
	
	
	static boolean gameRunning = false;
	

	
	public static void main(String args[]) throws IOException{
		
		if(args.length<1){
			System.out.println("Usage: java Application listenPort");
		}
		
		int listenPort = Integer.parseInt(args[0]);	
		int broadcastPort = Integer.parseInt(args[1]);		
				
		
		GameServer server = new GameServer(listenPort,broadcastPort);
		GameResolver gameResolver = new GameResolver(server);
		
		server.start();
		gameResolver.start();
		
	    	    
					
	}	
	
	
}
