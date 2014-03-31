package bomberman.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GUITile extends GUIObject{
	
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
		return null;
	}

	@Override
	public void loadImage() {
		// TODO Auto-generated method stub
		try {
			image = new Image("Resources/tile.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
