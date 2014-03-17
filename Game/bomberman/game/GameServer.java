package bomberman.game;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;
import bomberman.game.network.PacketProcessor;


public class GameServer extends Thread {
	
	private NetworkManager networkManager;
	private boolean isStopped = false;
	private boolean gameStarted = false;
		
	private ArrayBlockingQueue<GameEvent> gameEventQueue; // Thread safe FIFO Queue
			
	public GameServer(int port) throws SocketException{
		super("GameServer");		
		networkManager = new NetworkManager(port);				
	}		
	
	public void listenForJoin(PacketProcessor p){
		System.out.println("Waiting for players to join ...");
		while(!gameStarted ){
			networkManager.receiveSynchronous(0,1024,false,p);			
		}
	}
		
	public void listenForGameCommands(){		
		DatagramPacket packet = networkManager.receiveAsynchronous(50,true);		
		if(packet!=null){
			GameEvent event = GameProtocol.getInstance().getEvent(packet);	
			if(event!=null){
				try {
					gameEventQueue.put(event);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}		
	}
		
	@Override
	public void run(){	
		if(gameEventQueue == null){
			return;
		}
		while(!isStopped){
			listenForGameCommands();
		}		
		stopGracefully();
	}
			
	public void stopGracefully(){	
		isStopped = true;
		networkManager.close();
	}
		
	public void setGameStarted(boolean gameStarted){
		this.gameStarted = gameStarted;
	}
			
	public void broadCastStartGame(Set<NetworkAddress> allPlayers) {
				
	}
	
	public void broadCastEndGame(Set<NetworkAddress> allPlayers) {
				
	}
	
	public void sendUpdateMessage(Set<NetworkAddress> allPlayers,String message){
		for(NetworkAddress addr:allPlayers){
			networkManager.sendAsynchronous(message, addr, false);
		}
	}
	
	public void setEventQueue(ArrayBlockingQueue<GameEvent> gameEventQueue){
		this.gameEventQueue = gameEventQueue;
	}
	
}
