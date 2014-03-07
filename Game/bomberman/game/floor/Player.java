package bomberman.game.floor;

import bomberman.game.floor.Floor.Tile;
import bomberman.game.network.NetworkAddress;


public class Player extends FloorObject implements Movable {

	private boolean isHost;
	private NetworkAddress addr;
	
	public Player(Floor floor, String name) {
		super(floor, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean movedToOccupiedGrid(Tile loc) {
		
		System.out.println(getName()+ " moved to space occupied by "+ loc.getObject().getName());
		
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
