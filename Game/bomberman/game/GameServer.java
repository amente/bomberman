package bomberman.game;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;


public class GameServer extends Thread {
	
	private NetworkManager networkManager;
	private boolean isStopped = false;	
	private boolean gameFinished = false;
	private boolean gameStarted = false;
		
	private ArrayBlockingQueue<GameAction> messageBuffer; // Thread safe FIFO Queue
	//private Producer<GameAction> producer;
	
		
	public GameServer(int port) throws SocketException{
		super("GameServer");
		
		networkManager = new NetworkManager(port);
		
		messageBuffer = new ArrayBlockingQueue<GameAction>(Application.QUEUE_CAPACITY);	
		//producer = new Producer<GameAction>(messageBuffer);
		
	}	
	
	public void listenForJoin(JoinResolver r){
		System.out.println("Waiting for players to join ...");
		while(!gameStarted ){
			networkManager.receiveSynchronous(0,1024,false,r);			
		}
	}
		
	public boolean listenForGameCommands(){		
		DatagramPacket packet = networkManager.receiveAsynchronous(0,true);		
		
		GameAction action = GameProtocol.getInstance().getAction(packet);			
		addAction(action);
		return true;
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
	
	public ArrayBlockingQueue<GameAction> getMessageBuffer(){
		return messageBuffer;
	}
		
	public void setGameFinished(boolean gameFinished){
		this.gameFinished = gameFinished;
	}
	
	public void setGameStarted(boolean gameStarted){
		this.gameStarted = gameStarted;
	}
			
	public void broadCastStartGame(NetworkAddress[] allPlayers) {
				
	}
	
	public void broadCastEndGame(NetworkAddress[] allPlayers) {
				
	}

	public void addAction(GameAction action) {
		try {
			messageBuffer.put(action);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public NetworkManager getNetworkManager(){
		return networkManager;
	}
}
