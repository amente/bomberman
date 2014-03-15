package bomberman.gui;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Game extends BasicGame{

	private TiledMap map;	
	private Animation sprite,up,down,left,right;
	
	private Image bombImage;
	private float x = 64f, y = 52f;
	private int xPos = 1, yPos = 1;
	
    public Game()
    {
        super("Bomberman");      
    }
     

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		// TODO Auto-generated method stub
		map.render(0, 0);
		sprite.draw((int)x, (int)y);
		
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		loadMap();
		loadMovements();
		bombImage = new Image("Resources/bomb.png");
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// TODO Auto-generated method stub
				
		Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_UP))
        {
            sprite = up;
            
                sprite.update(delta);                
                y -= delta * 0.1f;
               // yPos = (int) Math.ceil((y-40f) / 52f)-1;
                //System.out.println("x: "+x+" y: "+y);
               // System.out.println("yPos: "+yPos); 
                int tmp = yPos;
                yPos = (int) Math.ceil((y-40f) / 52f);
                //System.out.println("x: "+x+" y: "+y);
                //System.out.println("xPos: "+xPos);
                if(yPos!=tmp){
                	System.out.println("Move Up");
                }
        }
        else if (input.isKeyDown(Input.KEY_DOWN))
        {
            sprite = down;
           
                sprite.update(delta);
                y += delta * 0.1f;              
                //System.out.println("x: "+x+" y: "+y);
                //System.out.println("yPos: "+yPos);
                int tmp = yPos;
                yPos = (int) Math.ceil((y-40f) / 52f);
                //System.out.println("x: "+x+" y: "+y);
                //System.out.println("xPos: "+xPos);
                if(yPos!=tmp){
                	System.out.println("Move Down");
                }
        }
        else if (input.isKeyDown(Input.KEY_LEFT))
        {
            sprite = left;
           
                sprite.update(delta);
                x -= delta * 0.1f;
                //System.out.println("x: "+x+" y: "+y);
                //System.out.println("xPos: "+xPos);
                int tmp = xPos;
                xPos = (int) Math.ceil((x-30f) / 64f) ;
                //System.out.println("x: "+x+" y: "+y);
                //System.out.println("xPos: "+xPos);
                if(xPos!=tmp){
                	System.out.println("Move Left");
                }
           
        }
        else if (input.isKeyDown(Input.KEY_RIGHT))
        {
            sprite = right;
           
                sprite.update(delta);
                x += delta * 0.1f;
                int tmp = xPos;
                xPos = (int) Math.ceil((x-30f) / 64f) ;
                //System.out.println("x: "+x+" y: "+y);
                //System.out.println("xPos: "+xPos);
                if(xPos!=tmp){
                	System.out.println("Move Right");
                }
        }
        else if(input.isKeyDown(Input.KEY_SPACE))
        {
        	
        	 bombImage.draw(x, delta);
        	
        }
		
		
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
    }
	
	public void loadMap(){
		try {
			map  = new TiledMap("Resources/bomberman_floor_1.tmx");
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		sprite = left;
		
	}
	
	
}
