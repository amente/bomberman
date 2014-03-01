package bomberman.game.floor;

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
	
		
	public boolean moveObjectTo(FloorObject o,int x,int y)
	{
		//Is the location occupied by another object
		if(grid[x][y]== null){
			grid[o.getX()][o.getY()]=null; // Remove from previous location			
			o.setLocationTo(x, y);
			grid[x][y] = o; // Move to new location
			return true;
		}else{
			//Moved to occupied space, what to do with it? Callback
			return o.movedToGridOccupiedBy(grid[x][y]);
		}		
	}	
		
	public void placeNewObjectAt(FloorObject o,int x,int y){
		
		// Is the location occupied by another object
		if (grid[x][y] == null) {
			grid[o.getX()][o.getY()] = null; // Remove from previous location
			o.setLocationTo(x, y);
			grid[x][y] = o; // Move to new location			
		} else {
			//Placed new object at occupied space
			//TODO; Implement this with a better approach;			
			placeNewObjectAt(o,x++,y++); // Try diagonal one up
		}	
		
		
	}
	
	
	public String getState(){
		StringBuilder state = new StringBuilder();
		for (FloorObject[] yGrid : grid){
			for(FloorObject o: yGrid){
				state.append(o==null?EMPTYNAME:o.getName());
			}
			state.append("\n");
		}
		return state.toString();
	}
	
	
	public void loadStateFromFile(String filePath){
		
		
		
	}
	
	public void writeStateToFile(String filePath){
		
		
		
		
		
	}
		
	
}
