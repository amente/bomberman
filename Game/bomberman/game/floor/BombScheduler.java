package bomberman.game.floor;

import java.util.Timer;
import java.util.TimerTask;

import bomberman.game.GameAction;
import bomberman.game.GameResolver;
import bomberman.utils.buffer.Consumer;

public class BombScheduler  extends Thread{
	
	private Consumer<Bomb> consumer;
	private GameResolver gameResolver;
	
	
	public BombScheduler(BombFactory bombFactory, GameResolver gameResolver){		
		consumer = new Consumer<Bomb>( bombFactory.getBombQueue());
		
		this.gameResolver = gameResolver; 
	}	
	
	public void attachTimer(Bomb bomb){
		Timer timer = new Timer();
		timer.schedule(new Detonator(bomb,timer,this), 2000);
	}
	
	@Override
	public void run(){
		while(gameResolver.getGameServer().isRunning()){
			Bomb bomb  =  consumer.consume();
			attachTimer(bomb);			
		}	
	}
	
	public void explosionTimeReached(Bomb bomb) {
		
		GameAction action = new GameAction();
		action.setType(GameAction.Type.EXPLOSION);
		action.addParameter("BOMB", bomb);	
		action.setIsFromServer();
		gameResolver.getGameServer().addAction(action);
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
