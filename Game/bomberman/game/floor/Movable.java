package bomberman.game.floor;

import bomberman.game.floor.Floor.Tile;

public interface Movable {
	
	/**
	 * Subclasses implement this to do the logic 
	 * @param o
	 * @return canAdvance
	 */
	public boolean movedToOccupiedGrid(Tile loc);

}
