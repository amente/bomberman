package bomberman.game.floor;

public interface Movable {
	
	/**
	 * Subclasses implement this to do the logic 
	 * @param o
	 * @return canAdvance
	 */
	public boolean movedToGridOccupiedBy(FloorObject o);

}
