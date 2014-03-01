package bomberman.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import bomberman.game.floor.Floor;


public class GameResolver extends Thread{
	
	Floor gameFloor;	
	GameServer gameServer;
	
	private MulticastSocket broadcastSocket;
	private InetAddress broadcastGroup; 
	
	public GameResolver(GameServer gameServer,int broadcastPort,int xSize,int ySize){
		try {
			broadcastSocket = new MulticastSocket(broadcastPort);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		try {
			broadcastGroup = InetAddress.getByName(Application.BROADCAST_ADDR);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		gameFloor = new Floor(xSize,ySize);			
		this.gameServer = gameServer;
	}	
	
	/**
	 * Broad cast the game status
	 * @return
	 */
	public void broadCast(){		
		// TODO: Broadcast the game state to client players
		
	}
	
	@Override
	public void run(){
		
		while(gameServer.isRunning()){
			processMessages();				
		}
		
	}
	
	/**
	 * Remove messages from the server queue and process them
	 */
	private void processMessages(){
		
		//This is critical call, but the ArrayBlockingQueue is thread safe
		DatagramPacket packet = gameServer.messageQueue.poll();
		byte[] message = packet!=null?packet.getData():null;	
		
		if(message!=null){
			GameAction action = GameProtocol.getInstance().getAction(message);			
			
			GameAction.Type t = action.getType();
			
			switch(t){		
				
			case MOVE:
				processMoveAction(action);
				break;
			case JOIN_LEAVE:
				processJoinAction(action);	
				break;			
			case BOMB:
				processBombAction(action);
				break;		
			
			}		
			
		}		
		
	}

	private void processBombAction(GameAction action) {
		// TODO Apply the action
		// Get the name of the player from action parameters
		// Get the location the player on the board
		// Get a bomb from the factory and put at the players location
		
	}

	private void processJoinAction(GameAction action) {
		// TODO Auto-generated method stub
		// Get the name of the player from action paramters
		// Get the type of request JOIN or LEAVE from paramters
		// Create a Player and place it on the floor
		
	}

	private void processMoveAction(GameAction action) {
		// TODO Auto-generated method stub
		// Get the name of the player
		// Locate the player on the board
		// Get the movement type from action parameters
		// Invoke the move on the floor
	}
	
	
	
}		
