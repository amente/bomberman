package bomberman.gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GUIDoor extends GUIObject {

	private Image image;
	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public void loadImage() {
		// TODO Auto-generated method stub
		try {
			image = new Image("Resources/door.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

}
