package bomberman.game.floor;

public abstract class Movable extends FloorObject {
	
	
	
	public Movable(Floor floor, String name) {
		super(floor, name);
		// TODO Auto-generated constructor stub
	}

	public enum MovementType{
		UP,
		DOWN,
		RIGHT,
		LEFT;
		
		public static MovementType getMovement(String dir){
			if(dir.equalsIgnoreCase(UP.name())){
				return UP;
			}else if(dir.equalsIgnoreCase(DOWN.name())){
				return DOWN;
			}else if(dir.equalsIgnoreCase(LEFT.name())){
				return LEFT;
			}else{
				return RIGHT;
			}
		}
	}
	/**
	 * Subclasses implement this 
	 * @param o
	 * @return canAdvance
	 */	
	public abstract boolean movedOutOfGrid();
	public abstract boolean movedToOccupiedGrid(Tile tile, MovementType dir);
	
	public void moveLeft(){		
		getFloor().moveObjectTo(this,getX()-1,getY(),MovementType.LEFT);
	}	
	
	public void moveRight(){		
		getFloor().moveObjectTo(this,getX()+1,getY(),MovementType.RIGHT);
	}	
	
	public void moveUp(){		
		getFloor().moveObjectTo(this,getX(),getY()-1,MovementType.UP);
	}	
	
	public void moveDown(){		
		getFloor().moveObjectTo(this,getX(),getY()+1,MovementType.DOWN);
	}

}
