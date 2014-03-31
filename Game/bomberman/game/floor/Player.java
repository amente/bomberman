package bomberman.game.floor;

import bomberman.game.network.NetworkAddress;


public class Player extends Movable {

	private boolean isHost;
	private NetworkAddress addr;
	private Floor gameFloor;
	private boolean isAlive = true;
	
	public Player(Floor floor, String name) {
		super(floor, name);
		// TODO Auto-generated constructor stub
		setType("Player");
		this.gameFloor = floor;
	}

	@Override
	public boolean movedToOccupiedGrid(Tile loc,MovementType dir) {
		FloorObject o = loc.getObject();		
		if(o.getType().equalsIgnoreCase("Player")){			
			gameFloor.MoveAnotherObjectTo(this, o.getX(), o.getY(), dir);	
			//Sometimes this logic may glitch for fast movements so added this guard
			if(o!=this){
				gameFloor.addkillPlayerEvent((Player)o);
				gameFloor.addkillPlayerEvent(this);
				System.out.println(getName()+ " moved to space occupied by "+ o.getName());	
			}
		}else if(o.getType().equalsIgnoreCase("PowerUp")){
			gameFloor.MoveAnotherObjectTo(this, o.getX(), o.getY(), dir);
			gameFloor.givePowerUp(this,(PowerUp)o);
			System.out.println(getName()+ " moved to space occupied by "+ o.getName());
			System.out.println(getName()+ "has got a power up!");
		}else if(o.getType().equalsIgnoreCase("Door")){
			Door d = (Door)o;
			if(d.isOpen()){
				gameFloor.MoveAnotherObjectTo(this, d.getX(), d.getY(), dir);
				System.out.println(getName()+ " has reached the door!");
				System.out.println(getName()+ " wins!");
				System.out.println(" Game Over!");
			}else if(!d.isVisible()){				
				d.setVisible(true);
				//For test only
				d.setOpen(true);
				gameFloor.moveObjectTo(this, d.getX(), d.getY(), dir);				
			}
		}else if(o.getType().equalsIgnoreCase("Enemy")){
			gameFloor.MoveAnotherObjectTo(this, o.getX(), o.getY(), dir);			
			gameFloor.addkillPlayerEvent(this);
		}else{		
			System.out.println(getName()+ " can not move to space occupied by "+ o.getName());		
		}		
		return false;
	}

	public void setIsHost(boolean isHost){
		this.isHost = isHost;
	}
	
	public boolean isHost(){
		return isHost;
	}

	public NetworkAddress getAddress() {
		return addr;
	}

	public void setAddress(NetworkAddress addr) {
		this.addr = addr;
	}
	
	public String toString(){
		return getName();
	}

	@Override
	public boolean movedOutOfGrid() {
		System.out.println(getName()+ " moved out of grid "+ getX()+ "," +getY());
		return false;
	}
	
	public void setIsAlive(boolean b){
		isAlive = b;
	}
	
	public boolean isAlive(){
		return isAlive;
	}
}
