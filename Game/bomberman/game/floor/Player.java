package bomberman.game.floor;

import java.net.SocketAddress;

import bomberman.game.floor.Floor.Tile;


public class Player extends FloorObject implements Movable {

	private boolean isHost;
	private SocketAddress addr;
	
	public Player(Floor floor, String name) {
		super(floor, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean movedToOccupiedGrid(Tile loc) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setIsHost(boolean isHost){
		this.isHost = isHost;
	}
	
	public boolean isHost(){
		return isHost;
	}

	public SocketAddress getAddress() {
		return addr;
	}

	public void setAddress(SocketAddress addr) {
		this.addr = addr;
	}
	
	
}
