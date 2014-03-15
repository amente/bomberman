package bomberman.gui;

import org.newdawn.slick.SlickException;


public interface GUIObject {
	public void redraw() throws SlickException;

	public void setRedraw(boolean b);	
}
