package bomberman.game.floor;

public class Block extends FloorObject {

	public Block(Floor floor) {
		super(floor, "BLOCK");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean movedToGridOccupiedBy(FloorObject o) {
		// TODO Auto-generated method stub
		return false;
	}

}
