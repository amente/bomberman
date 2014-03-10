package bomberman.game.floor;

import bomberman.game.Application;
import bomberman.utils.buffer.IBuffer;
import bomberman.utils.buffer.Producer;
import bomberman.utils.buffer.SingleBuffer;


public class BombFactory {

	private IBuffer<Bomb> bombQueue; 
	private Producer<Bomb> producer;
	
	public BombFactory(){		
		bombQueue = new SingleBuffer<Bomb>(Application.QUEUE_CAPACITY);
		producer = new Producer<Bomb>(bombQueue);
	}
	
	public Bomb makeBombFor(Player player,Floor floor){
		Bomb bomb = new Bomb(floor);
		bomb.setLocationTo(player.getX(),player.getY());	
		producer.produce(bomb);		
		return bomb;	
	}


	public IBuffer<Bomb> getBombQueue() {
		// TODO Auto-generated method stub
		return bombQueue;
	}	
}
