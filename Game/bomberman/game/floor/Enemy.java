package bomberman.game.floor;


public class Enemy extends Movable{

	public Enemy(Floor floor) {
		super(floor, "ENEMY");
		// TODO Auto-generated constructor stub
		setType("Enemy");
	}

	@Override
	public boolean movedToOccupiedGrid(Tile loc,MovementType dir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean movedOutOfGrid() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
