package bomberman.game.floor;

import bomberman.game.floor.Floor.Tile;
import bomberman.game.network.NetworkAddress;


public class Player extends FloorObject implements Movable {

	private boolean isHost;
	private NetworkAddress addr;
	private Floor gameFloor;
	
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
			gameFloor.MoveAnotherObjectTo((Player)o, o.getX(), o.getY(), dir);
			gameFloor.addkillPlayerAction((Player)o);
			gameFloor.addkillPlayerAction(this);	
			System.out.println(getName()+ " moved to space occupied by "+ o.getName());			
		}else if(o.getType().equalsIgnoreCase("PowerUp")){
			gameFloor.moveObjectTo(o, o.getX(), o.getY(), dir);
			gameFloor.givePowerUp(this);
			System.out.println(getName()+ " moved to space occupied by "+ o.getName());
			System.out.println(getName()+ "has got a power up!");
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
	
}
