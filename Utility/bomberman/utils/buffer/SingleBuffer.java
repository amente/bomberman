/* AUTHOR: Andrew Belu 
	Handles multiple producers/consumers
	The consumer will not consume partial buffers without a call to flush()*/

package bomberman.utils.buffer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class SingleBuffer<T> implements IBuffer<T> {
	
	private Queue<T> buffer;
	private Integer sizeOfBuffer = 100;
	private Object bufferLock = new Object();
	
	public SingleBuffer(
			Queue<T> queue
	){
		buffer = queue;
		sizeOfBuffer = buffer.size();
	}
	
	public SingleBuffer(
			int bufferSize
	){
		sizeOfBuffer = bufferSize;
		buffer = new ArrayBlockingQueue<T>(bufferSize);
		
	}
	
	public Queue<T> getConsumerBuffer() {
		return buffer;
	}
	
	public Queue<T> getProducerBuffer() {
		return buffer;
	}
	
	public Integer getMaxSizeOfBuffer() {
		return sizeOfBuffer;
	}

	public void handleFullBuffer(Producer<T> p) {
		synchronized(bufferLock) {
			while (buffer.size() == getMaxSizeOfBuffer()){
				try {
					bufferLock.wait();
				}
				catch (InterruptedException e) {}
			}
			
			bufferLock.notify();
		}
	}

	public void handleEmptyBuffer(Consumer<T> c) {
		synchronized(bufferLock){
			while (buffer.isEmpty()){
				try {
					bufferLock.wait();
				}
				catch (InterruptedException e) {}
			}
			
			bufferLock.notify();
		}
	}

	public Object getProducerLock() {
		return bufferLock;
	}

	public Object getConsumerLock() {
		return bufferLock;
	}
	
	public void flush() {
		synchronized(bufferLock) {
			bufferLock.notifyAll();
		}
	}
}
