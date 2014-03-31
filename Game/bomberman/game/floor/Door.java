package bomberman.game.floor;

public class Door extends FloorObject {

	private boolean isVisible = true;
	private boolean isOpen = true;
	
	
	public Door(Floor floor) {
		super(floor, "DOOR");
		// TODO Auto-generated constructor stub
		setType("Door");
	}


	public boolean isVisible() {
		return isVisible;
	}


	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}


	public boolean isOpen() {
		return isOpen;
	}


	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	

}
