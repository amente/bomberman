package bomberman.game.floor;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;


public class BombFactory {

	public static int DEFAULT_LIMIT_PER_PLAYER = 1;
	
	private ArrayBlockingQueue<Bomb> bombQueue;
	private HashMap<String,Integer> allowedBombPerPlayer;
	
	public BombFactory(){		
		bombQueue = new ArrayBlockingQueue<Bomb>(10);	
		allowedBombPerPlayer = new HashMap<String,Integer>();
	}
	
	public Bomb makeBombFor(Player player,Floor floor){
		String playerName = player.getName();
		if(!allowedBombPerPlayer.containsKey(playerName)){
			allowedBombPerPlayer.put(playerName, DEFAULT_LIMIT_PER_PLAYER);
		}
		
		int currentLimit = allowedBombPerPlayer.get(playerName);
		
		if (currentLimit > 0) {
			Bomb bomb = new Bomb(floor);
			bomb.setName(bomb.getName() + bomb.hashCode());
			bomb.setExplosionRange(currentLimit);
			floor.addNewObject(bomb, player.getX(), player.getY());
			try {
				bombQueue.put(bomb);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Decrement the allowed bomb number
			allowedBombPerPlayer.put(playerName,
					currentLimit - 1);
			return bomb;
		}
			
		return null;
	}


	public ArrayBlockingQueue<Bomb> getBombQueue() {
		// TODO Auto-generated method stub
		return bombQueue;
	}	
	
	public void increaseLimit(Player player){
		String playerName = player.getName();
		if(!allowedBombPerPlayer.containsKey(playerName)){
			allowedBombPerPlayer.put(playerName,allowedBombPerPlayer.get(playerName) +1);
		}
	}
}
