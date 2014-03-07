package bomberman.game.floor;


public class FloorObject{
	
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
		this.x = x;
		this.y = y;
	}	
	
	public void moveLeft(){		
		floor.moveObjectTo(this,x+1,y);
	}	
	
	public void moveRight(){		
		floor.moveObjectTo(this,x-1,y);
	}	
	
	public void moveUp(){		
		floor.moveObjectTo(this,x,y+1);
	}	
	
	public void moveDown(){		
		floor.moveObjectTo(this,x,y-1);
	}				
	
}	
