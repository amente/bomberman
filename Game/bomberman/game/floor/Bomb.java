package bomberman.game.floor;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends FloorObject {

	
	private Player player;
	private Timer timer;
	private Detonator detonator;
	private Floor floor;
	
	public Bomb(Floor floor) {
		super(floor, "BOMB");
		this.floor = floor;	
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

	public void setTimer(long time){
		timer = new Timer();
		detonator = new Detonator(this);
		timer.schedule(detonator, time);
	}

	public void explode(){		
		floor.explodeBomb(this);		
	}
	
	private class Detonator extends TimerTask{

		private Bomb bomb;
		public Detonator(Bomb bomb){
			this.bomb = bomb;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			bomb.explode();
		}
		
	}
	
}
