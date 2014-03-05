package bomberman.game.floor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


/**
 * 
 * Floor represents the game floor
 *   A floor consists of a 2D grid of Tiles    
 *
 */
public class Floor {	
	
	
	public static final int DEFAULTX = 0; // Default location for an object on the floor
	public static final int DEFAULTY = 0;  
	
	public static final String EMPTYNAME = "_"; // String representation of an empty floor grid
	
	private HashMap<String,Player>  players;
	private ArrayList<Tile> emptyTiles = new ArrayList<Tile>() ;
	
	private Tile[][] tiles;
	private TiledMap map;
	private int xSize;
	private int ySize;
	
	Random rand = new Random();
	
	public Floor(){	
		initialize();
	}	
	
	private void initialize(){
		loadStateFromTmxFile("Resources/bomberman_floor_1.tmx");
		xSize = tiles[0].length;
		ySize = tiles.length;
	}
	
	public boolean moveObjectTo(FloorObject o,int x,int y)
	{
		// Check if movement is to location outside the floor
		if(x>xSize-1 || y>ySize-1){ return false;}
		
		//Only a movable object can move
		if(!(o instanceof Movable)){ return false;}
		
		//Is the location occupied by another object
		if(tiles[x][y].getObject()== null){
			tiles[o.getX()][o.getY()].removeObject(); // Remove from previous location	
			emptyTiles.add(tiles[o.getX()][o.getY()]);
			o.setLocationTo(x, y);
			System.out.println("x: " + x + " y: " + y);
			tiles[x][y].replaceObject(o); // Move to new location
			return true;
		}else{
			//Moved to occupied space, what to do with it? Callback
			return ((Movable)o).movedToOccupiedGrid(tiles[x][y]);
		}		
	}	
		
	public void placeNewObjectAt(FloorObject o,int x,int y){		
		if(tiles[x][y].getObject() == null){
			tiles[x][y].replaceObject(o);			
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
	public void addPlayer(Player player) {
		// Pop the next empy tile
		Tile t = emptyTiles.remove(rand.nextInt(emptyTiles.size()-1));
		addPlayer(player,t.x,t.y);
	}
	
	/**
	 * Locates a player on the floor by its name
	 * @param name
	 * @return
	 */
	public Player getPlayer(String name){
		return players.get(name);
	}
	
	/**
	 * Adds a player to the game floor at a specified location
	 * @param player
	 * @param x
	 * @param y
	 */
	private void addPlayer(Player player, int x, int y) {
		if (players.containsKey(player.getName())){
			System.out.println("Player with name already in game");
			return;
		}
		
		players.put(player.getName(),player);
		
		System.out.println("Adding player: " + player.getName());
		
		placeNewObjectAt(player,x,y);
		
	}
	
	/**
	 * Loads the initial state of the floor from a map(.tmx) file
	 * @param filePath
	 */
	public void loadStateFromTmxFile(String filePath){
		
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

 				if(value.equalsIgnoreCase("block")){					
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Brick(this)); 					
 				}else if(value.equalsIgnoreCase("door")){
 					tiles[xAxis][yAxis] = new Tile(xAxis,yAxis,new Door(this));					
 				} else if(value.equalsIgnoreCase("wall")){
 					emptyTiles.add(new Tile(xAxis,yAxis,new Wall(this)));					
 				}else if(value.equalsIgnoreCase("enemy")){
 					emptyTiles.add(new Tile(xAxis,yAxis,new Enemy(this)));					
 				}else if(value.equalsIgnoreCase("empty")){
 					emptyTiles.add(new Tile(xAxis,yAxis,null));					
 				}
                 
             }
        }		
		
	}	
	
	public void writeStateToFile(String filePath){
			
		//ToDo: Call getState() and Write the out put to file? 
		
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
				objects.add(o);
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
				objects.remove();
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
	
}
