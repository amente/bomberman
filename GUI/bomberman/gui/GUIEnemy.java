package bomberman.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GUIEnemy extends GUIObject {

	private Image image;
	@Override
	public void redraw() throws SlickException {
		// TODO Auto-generated method stub
		if(!redraw  ){return;}	
		checkAndloadImage();
		image.draw(64f*x ,52f*y );
	}

	@Override
	public void setRedraw(boolean b) {
		// TODO Auto-generated method stub
		
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
