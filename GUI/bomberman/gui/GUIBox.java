package bomberman.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GUIBox extends GUIObject{

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
		redraw = b;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public void loadImage() {
		try {
			image = new Image("Resources/box.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
