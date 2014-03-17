package bomberman.test;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import bomberman.game.GameStateReciever;
import bomberman.game.GameStateUpdate;
import bomberman.game.floor.Movable.MovementType;
import bomberman.gui.GUIBomb;
import bomberman.gui.GUIObject;
import bomberman.gui.GUIPlayer;

/**
 * 
 * A GUI spectator displays game updates
 *
 */
public class TestSpectator extends BasicGame{

	private TiledMap map;	
		
	ArrayBlockingQueue<GameStateUpdate> gameStateUpdateQueue;
	private HashMap<String,GUIObject> objects;
	GameStateReciever reciver;
	TestPlayer player;
	
	private int updatesCommited = 0;
	
    public TestSpectator(TestPlayer player)
    {
        super("Bomberman Test Spectator");     
        objects = new HashMap<String,GUIObject>(4); 
        gameStateUpdateQueue = new ArrayBlockingQueue<GameStateUpdate>(500,true);  
        this.player = player;
        reciver = new GameStateReciever(gameStateUpdateQueue,player.getNetworkManager());
    }   

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		// TODO Auto-generated method stub
		// Render the map
		map.render(0, 0);	
		//Render the players
		for(GUIObject o: objects.values()){
			o.redraw();
		}		
		
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		loadMap();
		
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
		GameStateUpdate update = null;
		try {
			update = gameStateUpdateQueue.poll(10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (update != null) {
			updatesCommited++;
			System.out.println("Commited Updates:"+updatesCommited);
			
			if (update.getType().equals(GameStateUpdate.UpdateType.NEW)) {
				String objectType = update.getParameter("OBJECT_TYPE");
				if (objectType.equalsIgnoreCase("PLAYER")) {

					String name = update
							.getParameter("OBJECT_NAME");
					int x = Integer.parseInt(update
							.getParameter("X_LOC"));
					int y = Integer.parseInt(update
							.getParameter("Y_LOC"));

					GUIPlayer player = null;
					try {
						player = new GUIPlayer(name, x, y);
					} catch (SlickException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (player != null) {
						objects.put(name, player);
						System.out.println("Added "+ name+ "to GUI");
					}
				}else if(objectType.equalsIgnoreCase("BOMB")){
					
					String id = update
							.getParameter("OBJECT_NAME");
					
					int x = Integer.parseInt(update
							.getParameter("X_LOC"));
					int y = Integer.parseInt(update
							.getParameter("Y_LOC"));
					
					GUIBomb bomb = null;
					try {
						bomb = new GUIBomb(id, x, y);
					} catch (SlickException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (bomb != null) {
						objects.put(id, bomb);
						System.out.println("Added "+ id+ " to GUI");
					}
					
				}							
				
			}else if (update.getType().equals(GameStateUpdate.UpdateType.MOVE)) {
				
					//System.out.println("Polled: "+polled);
					String name = update
							.getParameter("OBJECT_NAME");
					String dir = update.getParameter("DIR");
					
					int x = Integer.parseInt(update
							.getParameter("X_LOC"));
					int y = Integer.parseInt(update
							.getParameter("Y_LOC"));
					
					GUIPlayer object = (GUIPlayer)(objects.get(name));
					if(object!=null){
						object.setLocation(x, y,MovementType.getMovement(dir));
						//System.out.println("Moved object to x: "+x+" y:"+y);
					}
			}else if (update.getType().equals(GameStateUpdate.UpdateType.DEL)) {				
			
				String name = update
						.getParameter("OBJECT_NAME");									
			    System.out.println("Remove "+name);
				objects.get(name).setRedraw(false);
				
		}else if (update.getType().equals(GameStateUpdate.UpdateType.EXPLODEBOMB)) {				
			
			String name = update
					.getParameter("OBJECT_NAME");									
		   // System.out.println("Explode bomb "+name);
			((GUIBomb) objects.get(name)).setExplode(true);
			
		}  


		}		
	    		
	}    
	
	public void loadMap(){
		try {
			map  = new TiledMap("Resources/bomberman_floor_1.tmx");
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Thread start() {
		
		final TestSpectator game = this;
		Thread guiThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// Start the app container
				AppGameContainer app1;
				try {
					app1 = new AppGameContainer(game);
					app1.setShowFPS(false);
					app1.setDisplayMode(960, 780, false);
					app1.start();
				} catch (SlickException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
								
				while(!game.closeRequested()){
					//Wait
				}
				System.out.print("Finished!");
				game.finish();
			}
			
			
		},"GUI Wrapper");
		
		
		guiThread.start();
		// Start the receiver
		reciver.start();
		
		return guiThread;

	}

	protected void finish() {
		reciver.interrupt();
		player.interrupt();
				
	}
	 

}
