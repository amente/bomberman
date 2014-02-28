package bomberman.game;

/**
 * 
 * Floor represents the a 2D game grid * 
 *
 */
public class Floor {
	
	
	
	public static final int DEFAULTX = 0; // Default location for an object on the floor
	public static final int DEFAULTY = 0;  
	
	public static final String EMPTYNAME = "_"; // String representation of an empty floor grid
	
	public int xSize; // 
	public int ySize;		
	
	public FloorObject[][] grid;
		
	public Floor(int xSize, int ySize){		
		this.xSize = xSize;
		this.ySize = ySize;
		initialize();			
		
	}
	
	// Initializes the grid
	private void initialize(){
		grid = new FloorObject[xSize][ySize];		
	}
	
		
	public void moveObjectTo(FloorObject o,int x,int y)
	{
		//Is the location occupied by another object
		if(grid[x][y]== null){
			grid[o.getX()][o.getY()]=null; // Remove from previous location
			grid[x][y] = o; // Move to new location
		}else{
			// Invoke the call back to the object and advance if necessary 
			if(o.movedToGridOccupiedBy(grid[x][y])){
				grid[o.getX()][o.getY()]=null; 
				grid[x][y] = o;
			}
		}		
	}	
		
	
	public String getState(){
		StringBuilder state = new StringBuilder();
		for (FloorObject[] yGrid : grid){
			for(FloorObject o: yGrid){
				state.append(o==null?EMPTYNAME:o.name);
			}
			state.append("\n");
		}
		return state.toString();
	}
	
	
	public void loadStateFromFile(String filePath){
		
		
		
	}
	
	public void writeStateToFile(String filePath){
		
		
		
		
		
	}
		
	public abstract class FloorObject{
		
		private String name;
		private Floor floor;
		private int x;
		private int y;
		
		
		public FloorObject(Floor floor,String name){
			this.floor = floor;
			x = Floor.DEFAULTX;
			y = Floor.DEFAULTY;
			this.name = name;
		}
		
		
		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}
		
		public String getName(){
			return name;
		}
		
		public void setLocationTo(int x, int y){			
			floor.moveObjectTo(this,x,y);
		}	
		
		public void moveLeft(){			
			floor.moveObjectTo(this,x++,y);
		}	
		
		public void moveRight(){			
			floor.moveObjectTo(this,x--,y);
		}	
		
		public void moveUp(){			
			floor.moveObjectTo(this,x,y++);
		}	
		
		public void moveDown(){			
			floor.moveObjectTo(this,x,y--);
		}	
		
		/**
		 * Subclasses implement this to do the logic 
		 * @param o
		 * @return canAdvance
		 */
		public abstract boolean movedToGridOccupiedBy(FloorObject o);
					
		
	}	

}
