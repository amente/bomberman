package bomberman.game;

import java.net.DatagramPacket;

import bomberman.game.floor.Floor;


public class GameResolver extends Thread{
	
	static Floor gameFloor;	
	GameServer gameServer;
	
	
	public GameResolver(GameServer gameServer,int xSize,int ySize){
		
		gameFloor = new Floor(xSize,ySize);			
		this.gameServer = gameServer;
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
