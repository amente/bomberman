package bomberman.game.floor;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import bomberman.game.GameStateUpdate;
import bomberman.game.floor.Movable.MovementType;
import bomberman.game.network.NetworkAddress;
import bomberman.utils.buffer.Producer;
import bomberman.utils.buffer.SingleBuffer;


/**
 * 
 * Floor represents the game floor
 *   A floor consists of a 2D grid of Tiles    
 *
 */
public class Floor {	
	
	//For testing only
	boolean next = false;
	
	public static final int DEFAULTX = 0; // Default location for an object on the floor
	public static final int DEFAULTY = 0; 
	
	
	public static final String EMPTYNAME = "_"; // String representation of an empty floor grid
	
	private HashMap<NetworkAddress,Player>  players;
	private Player hostPlayer;
	private ArrayList<Tile> emptyTiles = new ArrayList<Tile>() ;
	
    private SingleBuffer<GameStateUpdate> gameStateUpdates;
	private Producer<GameStateUpdate> producer;
	
	private Tile[][] tiles;
	private TiledMap map;
	private int xSize;
	private int ySize;
	
	Random rand = new Random();
	
	public Floor() {
		this(false);
	}
	
	public Floor(boolean isTest){	
		gameStateUpdates = new SingleBuffer<GameStateUpdate>(10);
		producer = new Producer<GameStateUpdate>(gameStateUpdates);
		if (!isTest) {
			initialize();
		} else {
			testInitialize();
		}
	}	
	
	private void initialize(){
		loadStateFromTmxFile("Resources/bomberman_floor_1.tmx");
		xSize = tiles[0].length;
		ySize = tiles.length;
		players = new HashMap<NetworkAddress,Player>();
	}
	
	private void testInitialize() {
		players = new HashMap<NetworkAddress,Player>();
		xSize = 20;
		ySize = 20;
		tiles = new Tile[ySize][xSize];
		
		for (int y = 0; y < ySize; y++) {
			for (int x = 0; x < xSize; x++) {
				if (x == 0 || y == 0 || x == xSize-1 || y == ySize-1) {
					tiles[y][x] = new Tile(x, y, new Brick(this));
				}
			}
		}
	}
	
	public boolean moveObjectTo(FloorObject o,int x,int y,MovementType dir)
	{
		// Check if movement is to location outside the floor
		if(x>xSize-1 || y>ySize-1 || x<0 || y<0){ return ((Movable)o).movedOutOfGrid();}
				
		//Only a movable object can move
		if(!(o instanceof Movable)){ return false;}
		
		//Is the location occupied by another object
		if(tiles[x][y].getObject()== null){
			tiles[o.getX()][o.getY()].removeObject(); // Remove from previous location	
			emptyTiles.add(tiles[o.getX()][o.getY()]);
			o.setLocationTo(x, y);
			//System.out.println("x: " + x + " y: " + y);
			tiles[x][y].replaceObject(o); // Move to new location
			System.out.println(o.getName()+" moved to "+x+","+y);
			producer.produce(makeUpdateForMove(o,dir));
			return true;
		}else{
			//Moved to occupied space, what to do with it? Callback
			return ((Movable)o).movedToOccupiedGrid(tiles[x][y]);
		}		
	}	
		
	public void addNewObject(FloorObject o,int x,int y){		
		if(tiles[x][y].getObject() == null){
			tiles[x][y].replaceObject(o);	
			o.setLocationTo(x, y);
			producer.produce(makeUpdateForAddObject(o));
		}else{
			tiles[x][y].addAnother(o);	
			o.setLocationTo(x, y);
			producer.produce(makeUpdateForAddObject(o));
		}
	}
	
	/**
	 * Get a string representation of the floor
	 * @return
	 */
	public String getState(){
		StringBuilder state = new StringBuilder();
		for (Tile[] yGrid : tiles){
			for(Tile l: yGrid){
				state.append(l.getObject()==null?EMPTYNAME:l.getObject().getName());
			}
			state.append("\n");
		}
		return state.toString();
	}
	
	/**
	 * Adds a player to the floor, it picks up a random empty location
	 * @param player
	 */
	public String addPlayer(NetworkAddress playerAddress) {
		// Pop the next empty tile
		//Tile t = emptyTiles.remove(rand.nextInt(emptyTiles.size()));
		//return addPlayer(playerAddress,1,1);
		
		//For testing only
		if(!next){
			next = true;
			return addPlayer(playerAddress,1,1);			
		}else{
			return addPlayer(playerAddress,5,1);
		}		
	}
	
	/**
	 * Locates a player on the floor by its address
	 * @param addr
	 * @return
	 */
	public Player getPlayer(NetworkAddress addr){
		return players.get(addr);
	}
	
	public Player getPlayer(String name) {
		for(Player p : players.values()) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		
		return null;
	}
	
	/**
	 * Adds a player to the game floor at a specified location
	 * @param player
	 * @param x
	 * @param y
	 */
	public String addPlayer(NetworkAddress playerAddress,int x, int y) {
		
		if (players.containsKey(playerAddress)){
			System.out.println(players.get(playerAddress).getName()+ " already in game");
			return null;
		}
		Player player = new Player(this,createUniquePlayerID());
		player.setAddress(playerAddress);
		
		if(players.size() == 0){
			hostPlayer = player;
		}
		
		players.put(player.getAddress(), player);
				
		System.out.println("Placing " + player.getName()+ " on Floor at "+x+","+y);		
		addNewObject(player,x,y);		
		return player.getName();
	}
	
	public void explodeBomb(Bomb bomb){		
		System.out.println("Bomb Exploded"+" x:"+bomb.getX()+" y:"+bomb.getY());
		producer.produce(makeUpdateForExplodeBomb(bomb));		
	}
	
	private GameStateUpdate makeUpdateForAddObject(FloorObject o){
		GameStateUpdate update = new GameStateUpdate(GameStateUpdate.UpdateType.NEW);
		update.addParameter("OBJECT_TYPE", o.getType());
		update.addParameter("OBJECT_NAME", o.getName());
		update.addParameter("X_LOC", ""+o.getX());
		update.addParameter("Y_LOC", ""+o.getY());
		return update;		
	}
	
	
	private GameStateUpdate makeUpdateForMove(FloorObject o,MovementType dir){
		GameStateUpdate update = new GameStateUpdate(GameStateUpdate.UpdateType.MOVE);
		update.addParameter("DIR",dir.name());
		update.addParameter("OBJECT_NAME", o.getName());
		update.addParameter("X_LOC", ""+o.getX());
		update.addParameter("Y_LOC", ""+o.getY());
		return update;		
	}
	
		
	private GameStateUpdate makeUpdateForExplodeBomb(Bomb b){
		GameStateUpdate update = new GameStateUpdate(GameStateUpdate.UpdateType.BOMB);		
		update.addParameter("STATUS", "EXPLODE");
		update.addParameter("X_LOC", ""+b.getX());
		update.addParameter("Y_LOC", ""+b.getY());
		return update;		
	}
	
	public SingleBuffer<GameStateUpdate> getGameStateUpdateBuffer(){
		return gameStateUpdates;
	}
	
	
	/**
	 * Loads the initial state of the floor from a map(.tmx) file
	 * @param filePath
	 */
	public void loadStateFromTmxFile(String filePath){
		
		/*
		 * A workaround using reflection to invoking private method from TiledMap class
		 */
		try {
			Method pvtMethod  = TiledMap.class.getDeclaredMethod("setHeadless",Boolean.TYPE);
			pvtMethod.setAccessible(true);
			pvtMethod.invoke(null, true);
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			map  = new TiledMap(filePath);
			
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
		tiles = new Tile[map.getWidth()][map.getHeight()];
		
		for (int xAxis=0;xAxis<map.getWidth(); xAxis++)
        {
			 
			
             for (int yAxis=0;yAxis<map.getHeight(); yAxis++)
             {
                 int tileID = map.getTileId(xAxis, yAxis, 0);
                 String value = map.getTileProperty(tileID, "type", "none");                 

 				if(value.equalsIgnoreCase("brick")){					
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Brick(this)); 					
 				}else if(value.equalsIgnoreCase("door")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Door(this));					
 				} else if(value.equalsIgnoreCase("wall")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Wall(this));					
 				}else if(value.equalsIgnoreCase("enemy")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Enemy(this));					
 				}else if(value.equalsIgnoreCase("empty")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,null);
 					emptyTiles.add(tiles[xAxis][yAxis]);					
 				}
                 
             }
        }		
		
	}	
	
	public void writeStateToFile(String filePath){
			
		//ToDo: Call getState() and Write the out put to file? 
		
	}
		
	public String createUniquePlayerID(){		
		return "Player"+(players.size()+1);		
	}
	
	/**
	 * 
	 * A Tile represents a grid location on a floor
	 *   A tile will contain one or more objects, the objects are placed in order
	 *   using a linked list
	 *
	 */
	public class Tile{	
		
			private LinkedList<FloorObject> objects;
		
			public int x;
			public int y;
			Tile(int x, int y,FloorObject o){				
				this.x = x;
				this.y = y;
				objects = new LinkedList<FloorObject>();
				if(o!=null){
					objects.add(o);
				}
			}	
			
			/**
			 * Adds an object to the back of the linkedlist
			 * @param o
			 */
			public void addAnother(FloorObject o){
				objects.add(o);
			}
			
			/**
			 * Replace the first object on the tile
			 * @param o
			 */
			public void replaceObject(FloorObject o){
				if(objects.size()>0){
					objects.remove();
				}
				objects.addFirst(o);
			}
			
			/**
			 * Gets the first object on the tile
			 * @return
			 */
			public FloorObject getObject(){
				return objects.peekFirst();
			}
			
			/**
			 * Removes the first object on the tile
			 */
			public void removeObject(){
				objects.remove();
			}
			
			
	}
	
	public TiledMap getMap(){
		return map;
	}

	public NetworkAddress[] getAddressOfAllPlayers() {
		// TODO Auto-generated method stub
		return players.keySet().toArray(new NetworkAddress[0]);
	}

	public Player getHostPlayer() {
		return hostPlayer;
	}

	public void setHostPlayer(Player hostPlayer) {
		this.hostPlayer = hostPlayer;
	}

	public boolean hasPlayer(NetworkAddress senderAddress) {
		return players.containsKey(senderAddress);
	}
	
}
