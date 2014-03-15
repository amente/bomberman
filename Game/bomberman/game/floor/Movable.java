package bomberman.game.floor;

import bomberman.game.floor.Floor.Tile;

public interface Movable {
	
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
	 * Subclasses implement this to do the logic 
	 * @param o
	 * @return canAdvance
	 */	
	public boolean movedOutOfGrid();
	public boolean movedToOccupiedGrid(Tile tile, MovementType dir);

}
