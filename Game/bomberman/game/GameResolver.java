package bomberman.game;

import bomberman.game.floor.Bomb;
import bomberman.game.floor.BombFactory;
import bomberman.game.floor.BombScheduler;
import bomberman.game.floor.Floor;
import bomberman.game.floor.Player;
import bomberman.game.network.NetworkAddress;
import bomberman.utils.buffer.Consumer;

public class GameResolver extends Thread{
	
	private Floor gameFloor;
	private BombFactory bombFactory;
	private BombScheduler bombScheduler;
	private GameServer gameServer;	
	private GameStateUpdater clientUpdater;
	
	
	private Consumer<GameAction> consumer;
		
	public GameResolver(GameServer gameServer){
		super("GameResolver");
		this.gameServer = gameServer;		
		
		if (gameServer != null) {
			consumer = new Consumer<GameAction>(gameServer.getMessageBuffer());		
		}
		
		gameFloor = new Floor();	
		bombFactory  = new BombFactory();
		bombScheduler = new BombScheduler(bombFactory,this);
		
		
		clientUpdater = new GameStateUpdater(this);			
	}	
			
	@Override
	public void run(){
		clientUpdater.start();	
		bombScheduler.start();
		while(gameServer.isRunning()){
			processMessages();
		}		
	}	
	
	private void processMessages() {
		GameAction action = consumer.consume();	
		if(action == null){return;}
		
		processAction(action);
	}
	
	/**
	 * Remove messages from the server queue and process them
	 */
	public void processAction(GameAction action){	
		if (!senderHasJoinedGame(action.getSenderAddress(), gameFloor) && !action.isFromServer()) {
			return;
		}
		
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
			
		case EXPLOSION:
			processExplosionAction(action);			
		}	
		
	}

	private void processExplosionAction(GameAction action) {
		if(!(action.getType() == GameAction.Type.EXPLOSION)){return;}
		
		Bomb bomb = (Bomb)(action.getParameter("BOMB"));		
		gameFloor.explodeBomb(bomb);
		
	}

	private void processGameAction(GameAction action) {
		if(!(action.getType() == GameAction.Type.GAME)){return;}
		
		String type = (String)(action.getParameter("CALL"));
		
		if (type.equals("END_GAME")) {
			gameServer.broadCastEndGame(gameFloor.getAddressOfAllPlayers());
			gameServer.stopGracefully();			
		}
		
	}

	private void processBombAction(GameAction action) {
		if(!(action.getType() == GameAction.Type.BOMB)){return;}
		
		Player player = null;
		if(gameFloor.hasPlayer(action.getSenderAddress())){
			player = gameFloor.getPlayer(action.getSenderAddress());
		}
		
		if (player == null) {
			return;
		}
		
		bombFactory.makeBombFor(player,gameFloor);			
	}
	
	private void processMoveAction(GameAction action) {
		if(action.getType() != GameAction.Type.MOVE){return;}	
		Player player = null;
		if(gameFloor.hasPlayer(action.getSenderAddress())){
			player = gameFloor.getPlayer(action.getSenderAddress());
		}
		
		if (player == null) {
			return;
		}		
		String direction = (String)(action.getParameter("DIR"));

		if (direction.equalsIgnoreCase("UP")) {
			player.moveUp();
		} else if (direction.equalsIgnoreCase("DOWN")) {
			player.moveDown();
		} else if (direction.equalsIgnoreCase("LEFT")) {
			player.moveLeft();
		} else if (direction.equalsIgnoreCase("RIGHT")) {
			player.moveRight();
		}
		
	}

	public Floor getGameFloor() {		
		return gameFloor;
	}

	private boolean senderHasJoinedGame(NetworkAddress senderAddress, Floor floor) {
		//Extract the sender player information from the packet and check if it has already joined the game
		if(floor.getPlayer(senderAddress)==null){
			return false;
		}		
		return true;
	}

	public GameServer getGameServer() {		
		return gameServer;
	}	
	
	
}		
