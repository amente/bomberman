package bomberman.game;

import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.network.NetworkAddress;

public class GameStateUpdater extends Thread {

	private GameResolver gameResolver; 
	
	ArrayBlockingQueue<GameStateUpdate> consumer;
	
	public GameStateUpdater(GameResolver resolver){
		super("ClientUpdater");
		this.gameResolver = resolver;	
		consumer = gameResolver.getGameFloor().getGameStateUpdateBuffer();
	}
	
	@Override
	public void run(){
		while(gameResolver.getGameServer().isRunning()){
			GameStateUpdate update = consumer.poll();			
			if(update!=null){
				System.out.println("Sent Update "+ update.getType());
				for (NetworkAddress clientAddress : gameResolver.getGameFloor()
						.getAddressOfAllPlayers()) {
					gameResolver
							.getGameServer()
							.getNetworkManager()
							.sendAsynchronous(update.toString(), clientAddress,
									true);

				}
			}
		}
	}
	
}
