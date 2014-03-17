package bomberman.game.floor;

import java.util.LinkedList;

/**
 * 
 * A Tile represents a grid location on a floor
 *   A tile will contain one or more objects, the objects are placed in order
 *   using a linked list
 *
 */
public class Tile{	
	
		private LinkedList<FloorObject> objects;
	
		public int x;
		public int y;
		Tile(int x, int y,FloorObject o){				
			this.x = x;
			this.y = y;				
			objects = new LinkedList<FloorObject>();
			if(o!=null){
				o.setLocationTo(x,y);
				objects.add(o);
			}
		}	
		
		/**
		 * Adds an object to the back of the linkedlist
		 * @param o
		 */
		public void addAnother(FloorObject o){
			objects.add(o);
		}
		
		/**
		 * Replace the first object on the tile
		 * @param o
		 */
		public void replaceObject(FloorObject o){
			if(objects.size()>0){
				objects.remove();
			}
			objects.addFirst(o);
		}
		
		/**
		 * Gets the first object on the tile
		 * @return
		 */
		public FloorObject getObject(){
			return objects.peekFirst();
		}
		
		/**
		 * Removes the first object on the tile
		 */
		public void removeObject(){
			if(objects.size()>0){
				objects.remove();
			}
		}
		
		
}