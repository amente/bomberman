package bomberman.game.floor;


public class Player extends FloorObject implements Movable {

	
	
	public Player(Floor floor, String name) {
		super(floor, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean movedToGridOccupiedBy(FloorObject o) {
		// TODO Auto-generated method stub
		return false;
	}

		
	
}
