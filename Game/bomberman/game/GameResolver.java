package bomberman.game;

import bomberman.game.floor.Floor;
import bomberman.game.floor.Player;


public class GameResolver extends Thread{
	
	static Floor gameFloor;	
	GameServer gameServer;
	private Logger logger;
	
	public GameResolver(GameServer gameServer){
		
		gameFloor = new Floor("Resources/bomberman_floor_1.tmx");		
		this.gameServer = gameServer;
	}	
	
	public void setLogger(Logger l){
		logger = l;
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
		String message = gameServer.consumer.consume();
		
		if(message!=null){
			GameAction action = GameProtocol.getInstance().getAction(message);			
			
			if (logger != null){
				logger.addToLog(action.toString());
			}
			
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
			case GAME:
				processGameAction(action);
				break;
			}		
			
		}		
		
	}

	private void processGameAction(GameAction action) {
		String type = action.getParameter("CALL");
		
		if (type.equals("END_GAME")) {
			gameServer.stopGracefully();
			logger.stopLogging();
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
		String playerName = action.getParameter("PLAYER");
		Player player = new Player(gameFloor, playerName);
		
		gameFloor.addPlayer(player);
	}

	private void processMoveAction(GameAction action) {
		// TODO Auto-generated method stub
		// Get the name of the player
		// Locate the player on the board
		// Get the movement type from action parameters
		// Invoke the move on the floor
		String playerName = action.getParameter("PLAYER");
		
		Player player = gameFloor.getPlayerByName(playerName);
		if (player == null) {
			System.out.println("Player: " + playerName + " not found");
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
	
	
	
}		
