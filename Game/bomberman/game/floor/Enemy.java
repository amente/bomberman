package bomberman.game.floor;

public class Enemy extends FloorObject implements Movable{

	public Enemy(Floor floor) {
		super(floor, "ENEMY");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean movedToGridOccupiedBy(FloorObject o) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
