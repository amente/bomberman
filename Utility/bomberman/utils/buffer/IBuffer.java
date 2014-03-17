/* AUTHOR: Andrew Belu 
   Abstract buffer interface */

package bomberman.utils.buffer;

import java.util.Queue;

public interface IBuffer<T> {
	// Gets the buffer that the producer uses
	// Note that for single buffers, this should 
	// return the same buffer as the getConsumerBuffer() call
	public Queue<T> getProducerBuffer();
	
	// Gets the buffer that the consumer uses
	// Note that for single buffers, this should 
	// return the same buffer as the getConsumerBuffer() call
	public Queue<T> getConsumerBuffer();
	
	public Integer getMaxSizeOfBuffer();
	
	// Handles the case when a producer has encountered a full buffer
	public void handleFullBuffer(Producer<T> p);
	
	// Handles the case when a consumer has encountered an empty buffer
	public void handleEmptyBuffer(Consumer<T> c);
	
	public Object getProducerLock();
	public Object getConsumerLock();

	// Flushes the buffer
	// If a producer does not fill a buffer, this will let the consumers
	// be able to consume the buffer
	public void flush();
}
