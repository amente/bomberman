package bomberman.utils.discardinglist;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class DiscardingList<E> extends LinkedList<E> {

	private int maxSize = 0;
	
    public DiscardingList(int size) {
        maxSize = size;
    }

    @Override
    public synchronized boolean add(E object){
    	if (size() == maxSize) {
    		this.removeFirst();
    	}
    	
    	this.add(object);
    	
    	return true;
    }
}
