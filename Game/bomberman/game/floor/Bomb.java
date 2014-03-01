package bomberman.game.floor;

public class Bomb extends FloorObject {

	
	private Player player;
	
	public Bomb(Floor floor) {
		super(floor, "BOMB");
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Might need to link a player to a bomb for few reasons
	 *  - A player might need to just stay where it placed a bomb
	 *  - May be needed to accommodate, new game rules (Group players .. etc..)
	 */
	public void  setPlayer(Player player){		
		this.player = player;
	}
	
	public Player getPlayer(){
		return player;
	}

	@Override
	public boolean movedToGridOccupiedBy(FloorObject o) {
		// TODO Auto-generated method stub
		return false;
	}

}
