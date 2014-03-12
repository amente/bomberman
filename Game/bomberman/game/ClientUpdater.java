package bomberman.game;

import bomberman.game.network.NetworkAddress;
import bomberman.utils.buffer.Consumer;

public class ClientUpdater extends Thread {

	private GameResolver gameResolver; 
	
	Consumer<ClientUpdate> consumer;
	
	public ClientUpdater(GameResolver resolver){
		super("ClientUpdater");
		this.gameResolver = resolver;	
		consumer = new Consumer<ClientUpdate>(gameResolver.getGameFloor().getGameStateUpdateBuffer());
	}
	
	@Override
	public void run(){
		ClientUpdate update = consumer.consume();		
		for(NetworkAddress clientAddress: gameResolver.getGameFloor().getAddressOfAllPlayers()){
			gameResolver.getGameServer().getNetworkManager().sendAsynchronous(update.toString(),clientAddress,true);
			
		}
	}
	
}
