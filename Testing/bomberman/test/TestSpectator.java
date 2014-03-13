package bomberman.test;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import bomberman.game.GameStateUpdate;
import bomberman.game.floor.Movable.MovementType;
import bomberman.gui.Game;
import bomberman.gui.GUIObject;
import bomberman.test.TestDriver.TestPlayer;
import bomberman.utils.buffer.Consumer;

public class TestSpectator extends BasicGame{

	private TiledMap map;	
		
	Consumer<GameStateUpdate> consumer;
	private HashMap<String,GUIObject> objects;
	private Thread consumerThread;
	
    public TestSpectator(TestPlayer player)
    {
        super("Bomberman Test Spectator");     
        objects = new HashMap<String,GUIObject>(4); 
        consumer = new Consumer<GameStateUpdate>(player.getGameStateUpdates());
        
           
        consumerThread= new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					GameStateUpdate update = consumer.consume();
					if (update != null) {
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
										.getParameter("OBJECT_NAME") + update.hashCode();
								
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
								}
						} 

					}
			}
			
		}},"consumer thread");
		
		consumerThread.start();
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
		
		//Process Updates
	    		
	}
 
    
	public static void main(String[] arguments)
    {
        try
        {
        	Game main = new Game();
            AppGameContainer app = new AppGameContainer(main);  
            app.setShowFPS(false);
            app.setDisplayMode(960, 780, false);            
            app.start();
            
        }
        catch (SlickException e)
        {
            e.printStackTrace();
        }
        System.out.print("Main Finished");
    }
	
	public void loadMap(){
		try {
			map  = new TiledMap("Resources/bomberman_floor_1.tmx");
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	private class GUIPlayer implements GUIObject{
		
		private Animation sprite,up,down,left,right;
		private String name;
		private int x,y ;	
		private boolean loaded = false;
		
		public GUIPlayer(String name,int xPos,int yPos) throws SlickException{			
			this.name = name;
			x = xPos ;
			y = yPos;	
			System.out.println("x: "+x+" y: "+y);
		}
		
		
		public void setLocation(int xPos,int yPos,MovementType type){
			switch(type){
			case UP:
				sprite = up;
				break;
			case DOWN:
				sprite = down;
				break;
			case LEFT:
				sprite = left;
			default:
				sprite = right;			
			}
			
			this.x = xPos;
			this.y = yPos;			
			System.out.println("x: "+x+" y: "+y);
		}		
		
		public void loadMovements() throws SlickException{
			
			Image [] movementUp = {new Image("Resources/sprite/up_1.png"), new Image("Resources/sprite/up_2.png"),new Image("Resources/sprite/up_3.png"),new Image("Resources/sprite/up_4.png")};
			Image [] movementDown = {new Image("Resources/sprite/down_1.png"), new Image("Resources/sprite/down_2.png"),new Image("Resources/sprite/down_3.png"),new Image("Resources/sprite/down_4.png")};
			Image [] movementRight = {new Image("Resources/sprite/left_1.png"), new Image("Resources/sprite/left_2.png"),new Image("Resources/sprite/left_3.png"),new Image("Resources/sprite/left_4.png")};
			Image [] movementLeft = {new Image("Resources/sprite/right_1.png"),new Image("Resources/sprite/right_2.png"),new Image("Resources/sprite/right_3.png"),new Image("Resources/sprite/right_4.png")};
			int [] duration = {300, 300,300,300};
			
			up = new Animation(movementUp, duration, false);
			down = new Animation(movementDown, duration, false);
			left = new Animation(movementLeft, duration, false);
			right = new Animation(movementRight, duration, false); 
			
			sprite = right;
			
		}
		
		public void redraw() throws SlickException{
			// TODO: Look for a better solution than checking loaded 
			if(!loaded){
				loadMovements();
				loaded = true;
			}
			sprite.draw(64f*x ,52f*y );
		}
		
		
		
		
	}
	
	private class GUIBomb implements GUIObject{
		
		private Image image;
		private String id;
		private int x,y ;	
		private boolean loaded = false;
		
		public GUIBomb(String id,int xPos,int yPos) throws SlickException{			
			this.id = id;
			x = xPos ;
			y = yPos;	
			System.out.println("x: "+x+" y: "+y);
		}
		
		
		public void redraw() throws SlickException{
			// TODO: Look for a better solution than checking loaded 
			if(!loaded){
				loadImage();
				loaded = true;
			}
			image.draw(64f*x ,52f*y );
		}
		
		
		private void loadImage() throws SlickException{
			image = new Image("Resources/bomb.png");
		}
		
		
	}
	
	

}
