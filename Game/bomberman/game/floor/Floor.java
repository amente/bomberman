package bomberman.game.floor;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import bomberman.game.GameEvent;
import bomberman.game.GameResolver;
import bomberman.game.GameStateUpdate;
import bomberman.game.floor.Movable.MovementType;
import bomberman.game.network.NetworkAddress;


/**
 * 
 * Floor represents the game floor
 *   A floor consists of a 2D grid of Tiles    
 *
 */
public class Floor {	
	
	//For testing only
	int currNumPlayers = 0;
	private int addedUpdates = 0;
	
	public static final int DEFAULTX = 0; // Default location for an object on the floor
	public static final int DEFAULTY = 0; 
	
	
	public static final String EMPTYNAME = "_"; // String representation of an empty floor grid
	
	private HashMap<NetworkAddress,Player>  players;
	private ArrayList<Enemy> enemies;
	private Player hostPlayer;
	private ArrayList<Tile> emptyTiles = new ArrayList<Tile>() ;
	private GameResolver gameResolver;
	  
	private Tile[][] tiles;
	private TiledMap map;
	private int xSize;
	private int ySize;
	
	public int getXSize() { return xSize; }
	public int getYSize() { return ySize; }
	
	public Floor(GameResolver gameResolver){	
		this(gameResolver, false);
	}
	
	public Floor(GameResolver gameResolver, boolean isTest){		
		this.gameResolver = gameResolver;
		if (!isTest) {
			initialize();
		} else {
			testInitialize();
		}
	}	
	
	private void initialize(){
		enemies = new ArrayList<Enemy>();
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
		
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				if (x == 0 || y == 0 || x == xSize-1 || y == ySize-1) {
					tiles[x][y] = new Tile(x, y, new Wall(this));;
				} else {
					tiles[x][y] = new Tile(x,y,null);
					emptyTiles.add(tiles[x][y]);
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
			addUpdate(GameStateUpdate.makeUpdateForMove(o,dir));			
			return true;
		}else{
			//Moved to occupied space, what to do with it? Callbacks
			return ((Movable)o).movedToOccupiedGrid(tiles[x][y],dir);
		}		
	}	
	
	public void MoveAnotherObjectTo(FloorObject o, int x, int y,
			MovementType dir) {
		    
			tiles[o.getX()][o.getY()].removeObject(); // Remove from previous location	
			emptyTiles.add(tiles[o.getX()][o.getY()]);
			
			o.setLocationTo(x, y);
			tiles[x][y].addAnother(o);	
			//System.out.println(o.getName()+" moved to "+x+","+y);
			addUpdate(GameStateUpdate.makeUpdateForMove(o,dir));
				
	}
		
	public void addNewObject(FloorObject o,int x,int y){		
		if(tiles[x][y].getObject() == null){
			tiles[x][y].replaceObject(o);	
			o.setLocationTo(x, y);
			addUpdate(GameStateUpdate.makeUpdateForAddObject(o));
		}else{
			tiles[x][y].addAnother(o);	
			o.setLocationTo(x, y); 
			addUpdate(GameStateUpdate.makeUpdateForAddObject(o));
		}
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
		if(currNumPlayers == 0){
			currNumPlayers++;
			return addPlayer(playerAddress,1,1);			
		} else if (currNumPlayers == 1) {
			currNumPlayers++;
			return addPlayer(playerAddress,5,1);
		} else if (currNumPlayers == 2) {
			currNumPlayers++;
			return addPlayer(playerAddress, 1, ySize - 2);
		} else {
			currNumPlayers++;
			return addPlayer(playerAddress, xSize - 2, ySize - 2);
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
	
	
	public Player getHostPlayer() {
		return hostPlayer;
	}

	public void setHostPlayer(Player hostPlayer) {
		this.hostPlayer = hostPlayer;
	}

	public boolean hasPlayer(NetworkAddress senderAddress) {
		return players.containsKey(senderAddress);
	}

	public void addkillPlayerEvent(Player p) {
		// TODO Auto-generated method stub
		GameEvent event = new GameEvent();
		event.setType(GameEvent.Type.KILL);
		event.addParameter("PLAYER",p);	
		event.setIsFromPlayer(false);
		gameResolver.addEvent(event);
	}

	public void givePowerUp(Player player) {	
		gameResolver.getBombFactory().increaseLimit(player);		
	}	

	public void killPlayer(Player p) {
		tiles[p.getX()][p.getY()].removeObject();	
		System.out.println(p.getName()+ "died!");	
		 p.setIsAlive(false);
		  addUpdate(GameStateUpdate.makeUpdateForRemoveObject(p));		
	}
	
	public String createUniquePlayerID(){		
		return "Player"+(players.size()+1);		
	}	

	public Set<NetworkAddress> getAddressOfAllPlayers() {
		// TODO Auto-generated method stub
		return players.keySet();
	}

	
	
	private void addUpdate(GameStateUpdate makeUpdateForMove) {		
		gameResolver.addUpdate(makeUpdateForMove);		
		addedUpdates++;
		System.out.println("Added Updates: "+addedUpdates);
	}
	
	public void explodeBomb(Bomb bomb){		
		if(bomb==null){return;}
		System.out.println("Bomb Exploded"+" x:"+bomb.getX()+" y:"+bomb.getY());
		
		addUpdate(GameStateUpdate.makeUpdateForExplodeBomb(bomb));	
		
		int range = bomb.getExplosionRange();
		
		// Kill all at right
		for(int i=0;i<=range;i++){
			if(bomb.getX()+i< tiles[0].length){
				FloorObject o = tiles[bomb.getX()+i][bomb.getY()].getObject();
				if(o!=null){
					tiles[o.getX()][o.getY()].removeObject();
					if (o instanceof Player) { ((Player)o).setIsAlive(false); }
					addUpdate(GameStateUpdate.makeUpdateForRemoveObject(o));
				}
			}			
		}
		
		//Kill all at left
		for(int i=0;i<=range;i++){
			if(bomb.getX()-i > 0){
				FloorObject o = tiles[bomb.getX()-i][bomb.getY()].getObject();
				if(o!=null){
					tiles[o.getX()][o.getY()].removeObject();
					if (o instanceof Player) { ((Player)o).setIsAlive(false); }
					addUpdate(GameStateUpdate.makeUpdateForRemoveObject(o));
				}
			}			
		}
		
		//Kill all up
		for(int i=0;i<=range;i++){
			if(bomb.getY()-i > 0){
				FloorObject o = tiles[bomb.getX()][bomb.getY()-i].getObject();
				if(o!=null){
					tiles[o.getX()][o.getY()].removeObject();
					if (o instanceof Player) { ((Player)o).setIsAlive(false); }
					addUpdate(GameStateUpdate.makeUpdateForRemoveObject(o));
				}
			}			
		}
		
		// Kill all down
		for (int i = 0; i <= range; i++) {
			if (bomb.getY() + i > 0) {
				FloorObject o = tiles[bomb.getX()][bomb.getY() + i].getObject();
				if (o != null) {
					tiles[o.getX()][o.getY()].removeObject();
					if (o instanceof Player) { ((Player)o).setIsAlive(false); }
					addUpdate(GameStateUpdate.makeUpdateForRemoveObject(o));
				}
			}
		}		
			
	}
	public int getEnemyCount(){
		return enemies.size();
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

 				if(value.equalsIgnoreCase("box")){					
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Box(this)); 					
 				}else if(value.equalsIgnoreCase("door")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Door(this));					
 				} else if(value.equalsIgnoreCase("wall")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Wall(this));					
 				}else if(value.equalsIgnoreCase("enemy")){
 					Enemy enemy = new Enemy(this);
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,enemy);
 					enemies.add(enemy);
 				}else if(value.equalsIgnoreCase("powerup")){
 					PowerUp powerup = new PowerUp(this);
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,powerup); 					
 				}else if(value.equalsIgnoreCase("empty")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,null);
 					emptyTiles.add(tiles[xAxis][yAxis]);					
 				}else{
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,null);
 					emptyTiles.add(tiles[xAxis][yAxis]);
 				}
                 
             }  
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
				state.append("|");
			}
			state.append("\n");
		}
		return state.toString();
	}
	

	public void writeStateToFile(String filePath){
			
		//ToDo: Call getState() and Write the out put to file? 
		
	}		

	
	public TiledMap getMap(){
		return map;
	}
	
}
