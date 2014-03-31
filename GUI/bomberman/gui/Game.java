package bomberman.gui;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import bomberman.game.GameClient;
import bomberman.game.GameStateReciever;
import bomberman.game.GameStateUpdate;
import bomberman.game.floor.Movable.MovementType;

public class Game extends BasicGame{

	ArrayBlockingQueue<GameStateUpdate> gameStateUpdateQueue;
	private HashMap<String,GUIObject> objects;
	GameStateReciever reciver;
	GameClient client;
	
	private float x = 64f, y = 52f;
	private int xPos = 1, yPos = 1;
	
	private int updatesCommited = 0;
	
    public Game(GameClient client)
    {
        super("Bomberman");     
        objects = new HashMap<String,GUIObject>(4); 
        gameStateUpdateQueue = new ArrayBlockingQueue<GameStateUpdate>(500,true); 
        this.client = client;
        reciver = new GameStateReciever(gameStateUpdateQueue,client.getNetworkManager());
    }   

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
	
		//Render the players
		for(GUIObject o: objects.values()){
			o.redraw();
		}		
		
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		client.sendJoin();
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		processGameInput(container.getInput(),delta);
		processGameStateUpdates();			    		
	}    
	
	public Thread start() {
		
		final Game game = this;
		Thread guiThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// Start the app container
				AppGameContainer app1;
				try {
					app1 = new AppGameContainer(game);
					app1.setShowFPS(false);
					app1.setDisplayMode(960, 780, false);
					app1.setAlwaysRender(true);
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
		client.close();				
	}
	
	
	private void processGameInput(Input input,int delta){		
	
        if (input.isKeyDown(Input.KEY_UP))
        {
        	y-=delta *0.1f;
        	int tmp = yPos;
            yPos = (int) Math.ceil((y-40f) / 52f);
            if(yPos!=tmp){
            	client.sendCommand("Move Up");
            }
        }
        else if (input.isKeyDown(Input.KEY_DOWN))
        {
        	y+=delta *0.1f;
        	int tmp = yPos;
            yPos = (int) Math.ceil((y-40f) / 52f);
            if(yPos!=tmp){
            	client.sendCommand("Move Down");
            }
        	
        }
        else if (input.isKeyDown(Input.KEY_LEFT))
        {
        	x-=delta *0.1f;
        	int tmp = xPos;
            xPos = (int) Math.ceil((x-30f) / 64f);
            if(xPos!=tmp){
            	client.sendCommand("Move Left");
            }        	           
        }
        else if (input.isKeyDown(Input.KEY_RIGHT))
        {
        	x+=delta *0.1f;
        	int tmp = xPos;
            xPos = (int) Math.ceil((x-30f) / 64f);
            if(xPos!=tmp){
            	client.sendCommand("Move Right");
            }        	
        }
        else if(input.isKeyDown(Input.KEY_SPACE))
        {        	
        	client.sendCommand("Bomb");
        }
	}			
	
	private void processGameStateUpdates(){
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
				GUIObject o = objects.get(name);
				if(o!=null){
					o.setRedraw(false);
				}
				
		}else if (update.getType().equals(GameStateUpdate.UpdateType.EXPLODEBOMB)) {				
			
			String name = update
					.getParameter("OBJECT_NAME");									
		   // System.out.println("Explode bomb "+name);
			((GUIBomb) objects.get(name)).setExplode(true);
			
		}else if(update.getType().equals(GameStateUpdate.UpdateType.FULL)){			
			
			String state = update.getParameter("STATE");
			int y = 0,x;
			String[] rows = state.split("\n");			
			for(String row: rows){								
				//System.out.println(row);
				String[] cols = row.split("\\|");
				x = 0;
				for(String col: cols){	
					System.out.print(col + "]");
					if(col.equalsIgnoreCase("Box")){
						GUIBox box = new GUIBox();
						//System.out.println("Found Box: "+ x+ ","+y);
						box.setX(x);
						box.setY(y);
						objects.put("box"+box.hashCode(),box);
					}else if(col.equalsIgnoreCase("PowerUp")){
						GUIPowerUp powerUp = new GUIPowerUp();
						//System.out.println("Found PowerUp: "+ x+ ","+y);
						powerUp.setX(x);
						powerUp.setY(y);
						objects.put("powerup"+powerUp.hashCode(),powerUp);
					}else if(col.equalsIgnoreCase("Wall")){
						GUIWall wall = new GUIWall();
						//System.out.println("Found Wall: "+ x+ ","+y);
						wall.setX(x);
						wall.setY(y);
						objects.put("wall"+wall.hashCode(),wall);
					}else if(col.equalsIgnoreCase("Door")){
						GUIDoor door = new GUIDoor();
						//System.out.println("Found Door: "+ x+ ","+y);
						door.setX(x);
						door.setY(y);
						objects.put("wall"+door.hashCode(),door);
					}else{
						GUITile tile = new GUITile();
						tile.setX(x);
						tile.setY(y);
						objects.put("tile"+tile.hashCode(), tile);
					}
					x++;
				}
				System.out.println();
			   y++;	
			}		
			
		}
	}
	}
	
	
}
