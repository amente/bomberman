package bomberman.game.floor;

public class Door extends FloorObject {

	public Door(Floor floor) {
		super(floor, "DOOR");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean movedToGridOccupiedBy(FloorObject o) {
		// TODO Auto-generated method stub
		return false;
	}

}
