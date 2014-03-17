package bomberman.game.floor;

import java.util.HashMap;
import java.util.Random;

import bomberman.game.GameEvent;
import bomberman.game.GameResolver;

public class EnemyCommander extends Thread{
	
	
	private static int ENEMY_MOV_SPEED = 500; // In ms 
	
	private HashMap<String,Enemy> enemies;
	private GameResolver gameResolver ;
	Random random = new Random();
	
	public EnemyCommander(Enemy[] enemiesList,GameResolver gameResolver){
		this.gameResolver = gameResolver;
		enemies = new HashMap<String,Enemy>();
		for(int i=0; i<enemiesList.length; i++){
			enemies.put("enemy"+i, enemiesList[i]);
		}		
	}
	
		
	@Override
	public void run(){
		
		while(gameResolver.gameIsRunning()){
		
		int nextMove = random.nextInt(4);		
		
		// Simple random enemy movement
		for(Enemy e: enemies.values()){
			
			GameEvent event = new GameEvent();
			event.setType(GameEvent.Type.MOVE);
			
			event.addParameter("OBJECT", e);	
			String dir ="";
			
			switch(nextMove){
				
			case 0:
				dir = "DOWN";
				break;				
			case 1:
				dir = "UP";
				break;
			case 2:
				dir = "LEFT";
				break;
			case 3:
				dir = "RIGHT";				
				break;
			}			
			
			nextMove = random.nextInt(4);		
			
			try {
				Thread.sleep(ENEMY_MOV_SPEED);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			event.addParameter("DIR",dir);
			event.setIsFromPlayer(false);
			gameResolver.getGameServer().addEvent(event);			
			
		}		
		
	}
	}

}
