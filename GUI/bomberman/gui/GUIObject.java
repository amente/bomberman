package bomberman.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public abstract class GUIObject {
	protected int x,y;
	protected boolean loaded = false;
	protected boolean redraw = true;
	
	public abstract Image getImage();
	public abstract void loadImage();
	public abstract void redraw() throws SlickException;

	public abstract void setRedraw(boolean b);

	public void checkAndloadImage(){
		if(!redraw  ){return;}		
		if(!loaded){
			loadImage();
			loaded = true;
		}
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}	
}
