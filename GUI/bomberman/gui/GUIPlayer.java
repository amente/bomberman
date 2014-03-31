package bomberman.gui;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import bomberman.game.floor.Movable.MovementType;

public class GUIPlayer extends GUIObject{
	
	private Animation sprite,up,down,left,right;
	private String name;
	
	
	public GUIPlayer(String name,int xPos,int yPos) throws SlickException{			
		this.name = name;
		x = xPos ;
		y = yPos;	
		//System.out.println("x: "+x+" y: "+y);
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
			break;
		default:
			sprite = right;			
		}
		
		this.x = xPos;
		this.y = yPos;			
		//System.out.println("x: "+x+" y: "+y);
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
		if(!redraw ){return;}
		
		if(!loaded){
			loadMovements();
			loaded = true;
		}
		sprite.draw(64f*x ,52f*y );
	}


	@Override
	public void setRedraw(boolean b) {
		// TODO Auto-generated method stub
		redraw = b;
	}


	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void loadImage() {
		// TODO Auto-generated method stub
		
	}		
}
