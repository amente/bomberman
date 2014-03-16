package bomberman.game.floor;


/**
 * 
 * FloorObject represents anything that can be placed on the game floor.
 *
 */
public class FloorObject{
	
	private String name;
	private String type;
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
		this.x = x;
		this.y = y;
	}	


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}				
	
	public void setName(String name){
		this.name = name;
	}
	
	public Floor getFloor(){
		return floor;
	}
}	
