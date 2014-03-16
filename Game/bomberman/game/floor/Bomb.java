package bomberman.game.floor;


public class Bomb extends FloorObject {

	
	private Player player;	
	private int explosionRange = 1;
	
	public Bomb(Floor floor) {
		super(floor, "BOMB");	
		setType("Bomb");
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

	public int getExplosionRange() {
		return explosionRange;
	}

	public void setExplosionRange(int explosionRange) {
		this.explosionRange = explosionRange;
	}

		
	
}
