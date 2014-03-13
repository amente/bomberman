package bomberman.game;

import bomberman.game.network.NetworkAddress;
import bomberman.utils.buffer.Consumer;

public class GameStateUpdater extends Thread {

	private GameResolver gameResolver; 
	
	Consumer<GameStateUpdate> consumer;
	
	public GameStateUpdater(GameResolver resolver){
		super("ClientUpdater");
		this.gameResolver = resolver;	
		consumer = new Consumer<GameStateUpdate>(gameResolver.getGameFloor().getGameStateUpdateBuffer());
	}
	
	@Override
	public void run(){
		while(gameResolver.getGameServer().isRunning()){
			GameStateUpdate update = consumer.consume();
			if(update!=null){
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
