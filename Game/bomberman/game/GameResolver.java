package bomberman.game;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.floor.Bomb;
import bomberman.game.floor.BombFactory;
import bomberman.game.floor.BombScheduler;
import bomberman.game.floor.Floor;
import bomberman.game.floor.Movable;
import bomberman.game.floor.Player;
import bomberman.game.network.NetworkAddress;
import bomberman.game.network.PacketProcessor;

public class GameResolver extends Thread implements PacketProcessor{
	
	private Floor gameFloor;
	private BombFactory bombFactory;
	private BombScheduler bombScheduler;
	private GameServer gameServer;	
	private GameStateUpdater clientUpdater;
	private boolean gameIsRunning = false;
	
	
	private ArrayBlockingQueue<GameEvent> gameEventQueue;
	private ArrayBlockingQueue<GameStateUpdate> gameStateUpdateQueue;
	
	private int numEvents;
	
	public GameResolver(int port) {
		super("GameResolver");
		try {
			gameServer = new GameServer(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		gameEventQueue = new ArrayBlockingQueue<GameEvent>(Application.QUEUE_CAPACITY,true);	
		gameStateUpdateQueue = new ArrayBlockingQueue<GameStateUpdate>(500,true);
		gameFloor = new Floor(this);	
		bombFactory  = new BombFactory();
		bombScheduler = new BombScheduler(bombFactory,this);		
		clientUpdater = new GameStateUpdater(this);			
	}
		
	public GameResolver() {
		super("GameResolver");		
				
		gameEventQueue = new ArrayBlockingQueue<GameEvent>(Application.QUEUE_CAPACITY,true);	
		gameStateUpdateQueue = new ArrayBlockingQueue<GameStateUpdate>(500,true);
		gameFloor = new Floor(this);	
		bombFactory  = new BombFactory();
		bombScheduler = new BombScheduler(bombFactory,this);		
		clientUpdater = new GameStateUpdater(this);			
	}
	
				
	@Override
	public void run(){
		
		// Block and process joins
		if(gameServer!=null){
		gameServer.listenForJoin(this);
		
		gameServer.setEventQueue(gameEventQueue);
		gameServer.start();	
		}
		clientUpdater.start();	
		bombScheduler.start();
		
		while(gameIsRunning){
			processEvents();
		}
		
		if(gameServer!=null){
			gameServer.stopGracefully();
		}
		
		clientUpdater.stopGracefully();
	}	
	
	private void processEvents() {
		GameEvent event = null;
		/*try {
			event = gameEventQueue.poll(10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		event = gameEventQueue.poll();
		if(event == null){return;}
		System.out.println(event);
		processEvent(event);
	}
	
	/**
	 * Remove messages from the server queue and process them
	 */
	public void processEvent( GameEvent event ){	
		System.out.println(event);
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
		
		Movable sender = null;
		if (event.isFromPlayer()) {
			if (gameFloor.hasPlayer(event.getSenderAddress())) {
				sender = gameFloor.getPlayer(event.getSenderAddress());
			}
		} else {
			sender = (Movable) event.getParameter("OBJECT");
		}

		if (sender == null) {
			return;
		}

		String direction = (String) (event.getParameter("DIR"));
		
		if (direction.equalsIgnoreCase("UP")) {
			sender.moveUp();
		} else if (direction.equalsIgnoreCase("DOWN")) {
			sender.moveDown();
		} else if (direction.equalsIgnoreCase("LEFT")) {
			sender.moveLeft();
		} else if (direction.equalsIgnoreCase("RIGHT")) {
			sender.moveRight();
		}
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

	public void sendUpdateMessage(String updateMessage) {
		if(gameServer==null){return;}
		gameServer.sendUpdateMessage(gameFloor.getAddressOfAllPlayers(), updateMessage);		
	}
	
	public void addEvent(GameEvent event) {
		try {
			gameEventQueue.put(event);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}		
	
	@Override
	public String processPacket(DatagramPacket packet) {		
		if(packet!=null){			
			GameEvent action = GameProtocol.getInstance().getEvent(packet);			
			if(action == null){return null;}
			
						
			if(action.getType() == GameEvent.Type.GAMECHANGE){
				
				String param = (String)(action.getParameter("CALL"));
				
				if(param.equalsIgnoreCase("JOIN")){
					
					String playerName = gameFloor.addPlayer(new NetworkAddress(packet.getSocketAddress()));
					if(playerName!=null){
						System.out.println(playerName+ " Joined");	
						return playerName;
					}  
					
				}else if(param.equalsIgnoreCase("START")){					
					if(gameFloor.getHostPlayer().getAddress().equals(action.getSenderAddress())){
						setGameIsRunning(true);		
						gameServer.setGameStarted(true);
					}					
				}				
			}
						
		}		
		return null;
	}


	public ArrayBlockingQueue<GameStateUpdate> getGameStateUpdateQueue() {
		// TODO Auto-generated method stub
		return gameStateUpdateQueue;
	}


	public void addUpdate(GameStateUpdate update) {
		try {
			gameStateUpdateQueue.put(update);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public GameStateUpdate getUpdate() {
		return gameStateUpdateQueue.poll();
	}


	public GameStateUpdate getFloorState() {
		return GameStateUpdate.makeFullUpdateFor(gameFloor);
	}
	
	public int getFloorX(){
		return gameFloor.getXSize();
	}
	public int getFloorY(){
		return gameFloor.getYSize();
	}
	
	public void addPlayerToFloor(NetworkAddress playerAddress,int x, int y){
		gameFloor.addPlayer(playerAddress, x, y);
	}


	public Player getPlayer(String string) {
		return gameFloor.getPlayer(string);
	}
	
	
}		
