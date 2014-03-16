package bomberman.game;

import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.network.NetworkAddress;

public class GameStateUpdater extends Thread {

	private GameResolver gameResolver; 
	
	ArrayBlockingQueue<GameStateUpdate> gameStateUpdateQueue;
	
	private int numSent = 0;
	public GameStateUpdater(GameResolver resolver){
		super("ClientUpdater");
		this.gameResolver = resolver;	
		gameStateUpdateQueue = gameResolver.getGameFloor().getGameStateUpdateQueue();
	}
	
	@Override
	public void run(){
		while(gameResolver.getGameServer().isRunning()){
			GameStateUpdate update = gameStateUpdateQueue.poll();			
			if(update!=null){
				//System.out.println("Sent Update "+ update.getType());
				String updateMessage = update.toString();
				System.out.println(updateMessage);
				for (NetworkAddress clientAddress : gameResolver.getGameFloor()
						.getAddressOfAllPlayers()) {
					gameResolver
							.getGameServer()
							.getNetworkManager()
							.sendAsynchronous(updateMessage, clientAddress,
									true);

				}
				numSent++;
			 System.out.println("Updates Sent: "+numSent);
			}
		}
	}
	
}
