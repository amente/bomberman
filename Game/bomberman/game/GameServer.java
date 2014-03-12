package bomberman.game;

import java.net.DatagramPacket;
import java.net.SocketException;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;
import bomberman.utils.buffer.IBuffer;
import bomberman.utils.buffer.Producer;
import bomberman.utils.buffer.SingleBuffer;


public class GameServer extends Thread {
	
	private NetworkManager networkManager;
	private boolean isStopped = false;	
	private boolean gameFinished = false;
	private boolean gameStarted = false;
		
	private IBuffer<GameAction> messageBuffer; // Thread safe FIFO Queue
	private Producer<GameAction> producer;
	
		
	public GameServer(int port){
		super("GameServer");
		try {
			networkManager = new NetworkManager(port);
		} catch (SocketException e) {
			System.out.println("Socket bind failed for game server!");
		}
		messageBuffer = new SingleBuffer<GameAction>(Application.QUEUE_CAPACITY);	
		producer = new Producer<GameAction>(messageBuffer);
		
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
		
		producer.produce(action);			
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
	
	public IBuffer<GameAction> getMessageBuffer(){
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
		producer.produce(action);
	}
	
	public NetworkManager getNetworkManager(){
		return networkManager;
	}
}
