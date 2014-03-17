package bomberman.game.floor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.game.GameEvent;
import bomberman.game.GameResolver;

public class BombScheduler  extends Thread{
	
	private ArrayBlockingQueue<Bomb> bombQueue;
	private GameResolver gameResolver;
	
	
	public BombScheduler(BombFactory bombFactory, GameResolver gameResolver){		
		bombQueue = bombFactory.getBombQueue();
		
		this.gameResolver = gameResolver; 
	}	
	
	public void attachTimer(Bomb bomb){
		Timer timer = new Timer();
		timer.schedule(new Detonator(bomb,timer,this), 2000);
	}
	
	@Override
	public void run(){
		while(gameResolver.getGameServer().isRunning()){
			Bomb bomb  =  bombQueue.poll();
			attachTimer(bomb);			
		}	
	}
	
	public void explosionTimeReached(Bomb bomb) {
		
		GameEvent action = new GameEvent();
		action.setType(GameEvent.Type.EXPLOSION);
		action.addParameter("BOMB", bomb);	
		action.setIsFromServer(true);
		gameResolver.getGameServer().addEvent(action);
	}
	
	private class Detonator extends TimerTask{

		private Bomb bomb;
		private BombScheduler scheduler;
		private Timer timer;
		public Detonator(Bomb bomb,Timer timer,BombScheduler scheduler){
			this.bomb = bomb;
			this.scheduler = scheduler;
			this.timer = timer;
		}
		@Override
		public void run() {
			scheduler.explosionTimeReached(bomb);
			timer.cancel();
		}
		
	}


	
	
}
