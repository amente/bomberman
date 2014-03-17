package bomberman.game;

import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.floor.Bomb;
import bomberman.game.floor.BombFactory;
import bomberman.game.floor.BombScheduler;
import bomberman.game.floor.Floor;
import bomberman.game.floor.Player;
import bomberman.game.network.NetworkAddress;

public class GameResolver extends Thread{
	
	private Floor gameFloor;
	private BombFactory bombFactory;
	private BombScheduler bombScheduler;
	private GameServer gameServer;	
	private GameStateUpdater clientUpdater;
	private boolean gameIsRunning = false;
	
	
	private ArrayBlockingQueue<GameEvent> gameEventQueue;
	private int numEvents;
	
	public GameResolver(GameServer gameServer) {
		this(gameServer, false);
	}
		
	public GameResolver(GameServer gameServer, boolean isTest){
		super("GameResolver");
		this.gameServer = gameServer;		
		if (gameServer != null) {
			gameEventQueue = gameServer.getGameEventQueue();		
		}
		
		gameFloor = new Floor(this, isTest);	
		bombFactory  = new BombFactory();
		bombScheduler = new BombScheduler(bombFactory,this);		
		clientUpdater = new GameStateUpdater(this);			
	}	
			
	@Override
	public void run(){
		clientUpdater.start();	
		bombScheduler.start();
		while(gameServer.isRunning() && gameIsRunning){
			processEvents();
		}		
	}	
	
	/**
	 * Remove messages from the server queue and process them
	 */
	private void processEvents(){		
		GameEvent event = null;
		/*try {
			event = gameEventQueue.poll(10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		event = gameEventQueue.poll();
		if(event == null){return;}
		
		if (!senderIsAllowed(event)) {
			return;
		}		
					
		GameEvent.Type t = event.getType();
		
		switch(t){		
			
		case MOVE:
			processMoveEvent(event);
			break;					
		case BOMB:
			processBombEvent(event);
			break;	
		case GAMECHANGE:
			processGameChangeEvent(event);
			break;
			
		case EXPLOSION:
			processExplosionAction(event);	
			break;
		case KILL:
			processKillEvent(event);
		}
		numEvents++;
		System.out.println("Events Processed: "+numEvents);
	}

	private void processKillEvent(GameEvent action) {
		if(!(action.getType() == GameEvent.Type.KILL)){return;}
		
		Player p = (Player)(action.getParameter("PLAYER"));		
		gameFloor.killPlayer(p);
	}

	private void processExplosionAction(GameEvent action) {
		if(!(action.getType() == GameEvent.Type.EXPLOSION)){return;}
		
		Bomb bomb = (Bomb)(action.getParameter("BOMB"));
		gameFloor.explodeBomb(bomb);
		
	}

	private void processGameChangeEvent(GameEvent action) {
		if(!(action.getType() == GameEvent.Type.GAMECHANGE)){return;}
		
		String type = (String)(action.getParameter("CALL"));
		
		if (type.equals("END_GAME")) {
			gameServer.broadCastEndGame(gameFloor.getAddressOfAllPlayers());
			gameIsRunning = false;
			//Should we stop the gameSever? May be let players join a new game?
			gameServer.stopGracefully();			
		}
		
	}

	private void processBombEvent(GameEvent action) {
		if(!(action.getType() == GameEvent.Type.BOMB)){return;}
		
		Player player = null;
		if(gameFloor.hasPlayer(action.getSenderAddress())){
			player = gameFloor.getPlayer(action.getSenderAddress());
		}
		
		if (player == null) {
			return;
		}
		
		bombFactory.makeBombFor(player,gameFloor);			
	}
	
	private void processMoveEvent(GameEvent event) {
		if(event.getType() != GameEvent.Type.MOVE){return;}	
		Player player = null;
		if(gameFloor.hasPlayer(event.getSenderAddress())){
			player = gameFloor.getPlayer(event.getSenderAddress());
		}
		
		if (player == null) {
			return;
		}		
		String direction = (String)(event.getParameter("DIR"));

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

	private boolean senderHasJoinedGame(NetworkAddress senderAddress) {
		//Extract the sender player information from the packet and check if it has already joined the game
		if(gameFloor.getPlayer(senderAddress)==null){
			return false;
		}		
		return true;
	}

	private boolean senderIsAlive(NetworkAddress senderAddress){
		Player player = gameFloor.getPlayer(senderAddress);
		if(player!=null){
			return  player.isAlive();
		}
		return false;
	}
	
	public GameServer getGameServer() {		
		return gameServer;
	}	
	
	private boolean senderIsAllowed(GameEvent event){
				
		if(event.isFromPlayer()){
			NetworkAddress senderAddress = event.getSenderAddress();
			if (senderIsAlive(senderAddress) && senderHasJoinedGame(senderAddress)){
				return true;
			}else{
				return false;
			}
		}
		
		return true;
		
	}
	
	public BombFactory getBombFactory(){
		return bombFactory;
		
	}

	public boolean gameIsRunning() {
		
		return gameIsRunning;
	}

	public void setGameIsRunning(boolean b) {
		gameIsRunning = b;		
	}
	
	
}		
