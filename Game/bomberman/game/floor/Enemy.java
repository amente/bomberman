package bomberman.game.floor;

import bomberman.game.floor.Floor.Tile;

public class Enemy extends FloorObject implements Movable{

	public Enemy(Floor floor) {
		super(floor, "ENEMY");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean movedToOccupiedGrid(Tile loc) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
