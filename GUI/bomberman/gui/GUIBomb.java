package bomberman.gui;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;



public class GUIBomb extends GUIObject{
	
	
	private Animation explosionUp;
	private Animation explosionRight;
	private Animation explosionLeft;
	private Animation explosionDown;
	
	private Image unexploded;
	private Image image;
	private String id;		
	
	public int explodeTimout = 200;
	private boolean explode = false;
		
	
	public GUIBomb(String id,int xPos,int yPos) throws SlickException{			
		this.id = id;
		x = xPos ;
		y = yPos;	
		System.out.println("x: "+x+" y: "+y);						
	}
	
	
	public void redraw() throws SlickException{
		// TODO: Look for a better solution than checking loaded 
		if(!redraw  ){return;}		
		checkAndloadImage();		
		if(explode){
			if(explodeTimout==0){
				setRedraw(false);				
			}
			
			explosionUp.draw(64f*x,52f*(y-1));
			explosionDown.draw(64f*x,52f*(y+1));
			explosionLeft.draw(64f*(x-1),52f*y);
			explosionRight.draw(64f*(x+1),52f*y);
			
			explodeTimout--;				
		}else{
			image.draw(64f*x ,52f*y );
		}
	}
	
	
	private void loadImages() throws SlickException{
		unexploded = new Image("Resources/bomb.png");
	
		image = unexploded;
	}
	

	@Override
	public void setRedraw(boolean b) {
		redraw = b;			
	}
	
	
	public void setExplode(boolean b){		
		explode = true;
		System.out.println("Exploded Bomb");
	}


	@Override
	public Image getImage() {
		return image;		
	}


	@Override
	public void loadImage() {
		try {
			loadImages();
			
			Image [] explosionImageUp = {new Image("Resources/exp_1_up.png"), new Image("Resources/exp_2_up.png"),new Image("Resources/exp_3_up.png")};
			Image [] explosionImageDown = {new Image("Resources/exp_1_down.png"), new Image("Resources/exp_2_down.png"),new Image("Resources/exp_3_down.png")};
			Image [] explosionImageRight = {new Image("Resources/exp_1_right.png"), new Image("Resources/exp_2_right.png"),new Image("Resources/exp_3_right.png")};
			Image [] explosionImageLeft = {new Image("Resources/exp_1_left.png"), new Image("Resources/exp_2_left.png"),new Image("Resources/exp_3_left.png")};
			int [] duration = {300, 300,300};
			
			explosionUp = new Animation(explosionImageUp, duration, false);
			explosionDown = new Animation(explosionImageDown, duration, false);
			explosionLeft = new Animation(explosionImageRight, duration, false);
			explosionRight = new Animation(explosionImageLeft, duration, false); 
			
			
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
