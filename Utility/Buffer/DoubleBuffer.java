/* AUTHOR: Andrew Belu 
	Handles multiple producers/consumers
	The consumer will not consume partial buffers without a call to flush()*/

package Buffer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class DoubleBuffer<T> implements IBuffer<T> {
	
	private Queue<T> buffer1;
	private Queue<T> buffer2;
	private Integer producerBufferNum = 0;
	private Integer sizeOfBuffer = 100;
	private Object producerLock = new Object();
	private Object consumerLock = new Object();
	
	public DoubleBuffer(int bufferSize) {
		sizeOfBuffer = bufferSize;
		buffer1 = new ArrayBlockingQueue<T>(bufferSize);
		buffer2 = new ArrayBlockingQueue<T>(bufferSize);
		producerBufferNum = 1;
	}
	
	public DoubleBuffer(
			Queue<T> firstQueue,
			Queue<T> secondQueue,
			int bufferSize
	){
		sizeOfBuffer = bufferSize;
		buffer1 = firstQueue;
		buffer2 = secondQueue;
		producerBufferNum = 1;
		
	}
	
	public Queue<T> getConsumerBuffer() {
		return chooseBuffer(false);
	}
	
	public Queue<T> getProducerBuffer() {
		return chooseBuffer(true);
	}
	
	private Queue<T> chooseBuffer(boolean getProducerBuffer) {
		synchronized(producerBufferNum){
			if (producerBufferNum == 1) {
				return getProducerBuffer ? buffer1 : buffer2;
			} else if (producerBufferNum == 2) {
				return getProducerBuffer ? buffer2 : buffer1;
			} else {
				return null;
			}
		}
	}
	
	public void switchBuffers() {
		synchronized(producerBufferNum) {
			producerBufferNum = producerBufferNum == 1 ? 2 : 1;
		}
	}
	
	public Integer getMaxSizeOfBuffer() {
		return sizeOfBuffer;
	}

	public void handleFullBuffer(Producer<T> producer) {
		Queue<T> producerBuffer = this.getProducerBuffer();
		
		if (producerBuffer.size() != getMaxSizeOfBuffer()) {
			return;
		}
		
		Queue<T> consumerBuffer = this.getConsumerBuffer();
		
		synchronized(consumerLock){
			while(!consumerBuffer.isEmpty()){
				try {
					consumerLock.wait();
				} catch (InterruptedException e) {}
			}
			
			switchBuffers();
			
			consumerLock.notify();
		}
		
		producer.setBuffer();
	}

	public void handleEmptyBuffer(Consumer<T> consumer) {
		Queue<T> buffer = this.getConsumerBuffer();
	
		while(buffer.isEmpty()){
			try {
				consumerLock.wait();
				buffer = this.getConsumerBuffer();
			} catch (InterruptedException e) {}
		}
		
		consumer.setBuffer();
	}

	public Object getProducerLock() {
		return producerLock;
	}

	public Object getConsumerLock() {
		return consumerLock;
	}
	
	public void flush() {
		Queue<T> consumerBuffer = this.getConsumerBuffer();
		
		synchronized(consumerLock) {
			while(!consumerBuffer.isEmpty()){
				try {
					consumerLock.wait();
				} catch (InterruptedException e) {}
			}
			
			switchBuffers();
			consumerLock.notifyAll();
		}
	}
}
