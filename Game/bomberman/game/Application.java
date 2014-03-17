package bomberman.game;

import java.io.IOException;
import java.net.SocketException;

public class Application {
	
	public static final int QUEUE_CAPACITY = 500;
	public static final int MAX_DTATAGRAM_SIZE = 1024; // In Bytes
	
	static boolean gameRunning = false;
	

	
	public static void main(String args[]) throws IOException{
		
		if(args.length<1){
			System.out.println("Usage: java Application listenPort");
		}
		
		int listenPort = Integer.parseInt(args[0]);							
		
		GameServer gameServer = null;
		try{
			gameServer = new GameServer(listenPort);	
		}catch(SocketException e){
			System.out.println("Unable to start server!");
			System.exit(1);
		}
		
		GameResolver gameResolver = new GameResolver(gameServer);
		JoinResolver joinResolver = new JoinResolver(gameResolver,gameServer);
		
		
		System.out.println("Creating Game: Success");
		gameServer.listenForJoin(joinResolver);
		
		
		System.out.println("Game Started!");
		gameServer.start();
		gameResolver.start();	    	    
					
	}	
	
	
}
