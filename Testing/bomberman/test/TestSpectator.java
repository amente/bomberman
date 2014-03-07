package bomberman.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;
import bomberman.gui.Game;

public class TestSpectator extends BasicGame{

	private TiledMap map;	
	private Animation sprite,up,down,left,right;
	
	private NetworkManager networkManager;		
	private NetworkAddress serverAddress; 
	private boolean isRegistered = false;

	
	private float x = 64f, y = 52f;
	
    public TestSpectator(int serverPort,String serverAddress)
    {
        super("Bomberman Test Spectator");     
        try {
			networkManager = new NetworkManager();
			this.serverAddress = new NetworkAddress(serverAddress,serverPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	   		
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
		registerSpectator();
	}

	private void registerSpectator() {
		// TODO Auto-generated method stub
		String message = networkManager.sendSynchronous("Game Spectator",serverAddress,null,3,5000,false);	
		if(message.equalsIgnoreCase("OK")){
			isRegistered = true;
		}
	}



	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// TODO Auto-generated method stub
		if(isRegistered){		
			DatagramPacket packet  = networkManager.receiveAsynchronous(0, false);
			System.out.println(new String(packet.getData(),packet.getOffset(),packet.getLength()));			
			
		}
		
		
		
		/*
		Input input = container.getInput();
        if (input.isKeyDown(Input.KEY_UP))
        {
            sprite = up;
            
                sprite.update(delta);                
                y -= delta * 0.1f;
            
        }
        else if (input.isKeyDown(Input.KEY_DOWN))
        {
            sprite = down;
           
                sprite.update(delta);
                y += delta * 0.1f;
            
        }
        else if (input.isKeyDown(Input.KEY_LEFT))
        {
            sprite = left;
           
                sprite.update(delta);
                x -= delta * 0.1f;
                
           
        }
        else if (input.isKeyDown(Input.KEY_RIGHT))
        {
            sprite = right;
           
                sprite.update(delta);
                x += delta * 0.1f;
            
        }*/
		
		
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
