package bomberman.game;

import java.net.DatagramPacket;

import bomberman.game.floor.Floor;
import bomberman.game.floor.Player;
import bomberman.utils.buffer.Consumer;
import bomberman.utils.buffer.IBuffer;

public class GameResolver extends Thread{
	
	private Floor gameFloor;
	GameServer gameServer;	
	private IBuffer<DatagramPacket> messageBuffer;
	private Consumer<DatagramPacket> consumer;
		
	public GameResolver(GameServer gameServer){
		
		gameFloor = new Floor();		
		this.gameServer = gameServer;
		messageBuffer = gameServer.getMessageBuffer();
		consumer = new Consumer<DatagramPacket>(messageBuffer);
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
		DatagramPacket packet  =  consumer.consume();
		
		
		if(packet!=null){
			GameAction action = GameProtocol.getInstance().getAction(packet,gameFloor,false);			
			if(action == null){return;}
						
			GameAction.Type t = action.getType();
			
			switch(t){		
				
			case MOVE:
				processMoveAction(action);
				break;					
			case BOMB:
				processBombAction(action);
				break;	
			case GAME:
				processGameAction(action);
				break;
			}		
			
		}		
		
	}

	private void processGameAction(GameAction action) {
		if(!(action.getType() == GameAction.Type.GAME)){return;}
		
		String type = action.getParameter("CALL");
		
		if (type.equals("END_GAME")) {
			gameServer.broadCastEndGame(gameFloor.getAddressOfAllPlayers());
			gameServer.stopGracefully();			
		}
		
	}

	private void processBombAction(GameAction action) {
		if(!(action.getType() == GameAction.Type.BOMB)){return;}
		// TODO Apply the action
		// Get the name of the player from action parameters
		// Get the location the player on the board
		// Get a bomb from the factory and put at the players location
	}
	
	private void processMoveAction(GameAction action) {
		if(!(action.getType() == GameAction.Type.MOVE)){return;}				
		Player player = action.getPlayer();
		if (player == null) {
			return;
		}		
		String direction = action.getParameter("DIR");

		if (direction.equals("UP")) {
			player.moveUp();
		} else if (direction.equals("DOWN")) {
			player.moveDown();
		} else if (direction.equals("LEFT")) {
			player.moveLeft();
		} else if (direction.equals("RIGHT")) {
			player.moveRight();
		}
		
	}

	public Floor getGameFloor() {		
		return gameFloor;
	}

	
	
	
}		
