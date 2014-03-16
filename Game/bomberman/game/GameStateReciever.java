package bomberman.game;

import java.net.DatagramPacket;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.network.NetworkManager;

public class GameStateReciever extends Thread {
	
	private NetworkManager manager;
	
	private ArrayBlockingQueue<GameStateUpdate> gameStateUpdates;
	private int recieved = 0;
	
	public GameStateReciever(NetworkManager manager){
		super("GameStateReciever");			
		gameStateUpdates = new ArrayBlockingQueue<GameStateUpdate>(500,true);
		this.manager = manager;
	}
	
	@Override
	public void run(){
		while(true){
			DatagramPacket packet = manager.receiveAsynchronous(10, true);
			if(packet!=null){
				//System.out.println(playerName+" Recieved Updates:");
				String message = new String(packet.getData(),packet.getOffset(),packet.getLength());
				System.out.println(message);
				try {
					gameStateUpdates.put((new GameStateUpdate(message)));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(message);
				recieved++;
				System.out.println("Updates Recieved: "+ recieved);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

	public ArrayBlockingQueue<GameStateUpdate> getGameStateUpdates() {
		// TODO Auto-generated method stub
		return gameStateUpdates;
	}
	
}

