package bomberman.game.floor;

import bomberman.game.GameServer;
import bomberman.utils.buffer.Consumer;
import bomberman.utils.buffer.IBuffer;

public class BombScheduler  extends Thread{
	
	private IBuffer<Bomb> bombQueue;
	private Consumer<Bomb> consumer;
	private GameServer gameServer;
	
	public BombScheduler(BombFactory bombFactory, GameServer gameServer){		
		this.bombQueue = bombFactory.getBombQueue();
		consumer = new Consumer<Bomb>(bombQueue);
		this.gameServer = gameServer; 
	}	
	
	@Override
	public void run(){
		while(gameServer.isRunning()){
			Bomb bomb  =  consumer.consume();
			bomb.setTimer(2000);
		}	
	}
}
