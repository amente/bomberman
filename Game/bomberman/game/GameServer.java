package bomberman.game;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;


public class GameServer extends Thread {
	
	private NetworkManager networkManager;
	private boolean isStopped = false;	
	private boolean gameFinished = false;
	private boolean gameStarted = false;
		
	private ArrayBlockingQueue<GameEvent> gameEventQueue; // Thread safe FIFO Queue
			
	public GameServer(int port) throws SocketException{
		super("GameServer");
		
		networkManager = new NetworkManager(port);
		
		gameEventQueue = new ArrayBlockingQueue<GameEvent>(Application.QUEUE_CAPACITY,true);	
				
	}	
	
	public void listenForJoin(JoinResolver r){
		System.out.println("Waiting for players to join ...");
		while(!gameStarted ){
			networkManager.receiveSynchronous(0,1024,false,r);			
		}
	}
		
	public boolean listenForGameCommands(){		
		DatagramPacket packet = networkManager.receiveAsynchronous(50,true);		
		if(packet!=null){
			GameEvent event = GameProtocol.getInstance().getEvent(packet);			
			addEvent(event);
			return true;
		}
		return false;
	}
		
	public boolean isRunning(){
		return !isStopped;
	}

	@Override
	public void run(){		
		while(!gameFinished){
			listenForGameCommands();
		}
		
	}
			
	public void stopGracefully(){
		isStopped = true;		
		networkManager.close();
	}
	
	public ArrayBlockingQueue<GameEvent> getMessageBuffer(){
		return gameEventQueue;
	}
		
	public void setGameFinished(boolean gameFinished){
		this.gameFinished = gameFinished;
	}
	
	public void setGameStarted(boolean gameStarted){
		this.gameStarted = gameStarted;
	}
			
	public void broadCastStartGame(Set<NetworkAddress> allPlayers) {
				
	}
	
	public void broadCastEndGame(Set<NetworkAddress> allPlayers) {
				
	}

	public void addEvent(GameEvent event) {
		try {
			gameEventQueue.put(event);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public NetworkManager getNetworkManager(){
		return networkManager;
	}
}
