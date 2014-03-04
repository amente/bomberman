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
		System.out.println("Moving object: " + name + " left.");
		floor.moveObjectTo(this,x++,y);
	}	
	
	public void moveRight(){			
		System.out.println("Moving object: " + name + " right.");
		floor.moveObjectTo(this,x--,y);
	}	
	
	public void moveUp(){			
		System.out.println("Moving object: " + name + " up.");
		floor.moveObjectTo(this,x,y++);
	}	
	
	public void moveDown(){			
		System.out.println("Moving object: " + name + " down.");
		floor.moveObjectTo(this,x,y--);
	}		
				
	
}	
