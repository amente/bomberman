/* AUTHOR: Andrew Belu 
	Handles multiple producers/consumers
	Should handle multiple types of buffers (double, single, triple, etc.)*/

package bomberman.utils.buffer;

import java.util.Queue;

public class Consumer<T> {
	private IBuffer<T> bufferController;
	private Queue<T> buffer;
	
	public Consumer(IBuffer<T> b){
		bufferController = b;
	}
	
	public T consume() {
		return consumeHelper();
	}
	
	public void setBuffer(){
		buffer = bufferController.getConsumerBuffer();
	}
	
	public T consumeHelper() {
		Object lock = bufferController.getConsumerLock();
		
		synchronized(lock){
			setBuffer();
			
			bufferController.handleEmptyBuffer(this);
			
			T ret = null;
			
			synchronized(buffer) {
				ret = buffer.remove();
			}
			
			lock.notify();

			return ret;
		}
	}
	
}
