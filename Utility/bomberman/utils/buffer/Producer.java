/* AUTHOR: Andrew Belu 
   Handles multiple producers/consumers
   Should handle multiple types of buffers (double, single, triple, etc.) */

package bomberman.utils.buffer;
import java.util.Queue;

public class Producer<T> {
	private IBuffer<T> bufferController;
	private Queue<T> buffer;
	
	public Producer(IBuffer<T> b){
		bufferController = b;
	}
	
	public void setBuffer() {
		buffer = bufferController.getProducerBuffer();
	}
	
	public void produce(T item) {
		Object lock = bufferController.getProducerLock();
		
		synchronized(lock) {
			setBuffer();
			
			bufferController.handleFullBuffer(this);
			
			buffer.add(item);
			lock.notify();
			
			bufferController.handleFullBuffer(this);
		}
	}
	
	
}
