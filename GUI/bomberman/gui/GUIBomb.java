package bomberman.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;



public class GUIBomb extends GUIObject{
	
	
	//private Animation explosionAnimation;
	private Image exploded;
	private Image unexploded;
	private Image image;
	private String id;		
	
	public int explodeTimout = 100;
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
		image.draw(64f*x ,52f*y );
		if(explode){
			if(explodeTimout==0){
				setRedraw(false);
			}
			explodeTimout--;				
		}
	}
	
	
	private void loadImages() throws SlickException{
		unexploded = new Image("Resources/bomb.png");
		exploded = new Image("Resources/sprite/up_1.png");
		image = unexploded;
	}
	

	@Override
	public void setRedraw(boolean b) {
		redraw = b;			
	}
	
	
	public void setExplode(boolean b){			
		image = exploded;
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
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
