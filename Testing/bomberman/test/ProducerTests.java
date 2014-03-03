package bomberman.test;

import static org.junit.Assert.*;

import java.util.Queue;

import org.junit.Test;

import bomberman.utils.buffer.DoubleBuffer;
import bomberman.utils.buffer.IBuffer;
import bomberman.utils.buffer.Producer;


public class ProducerTests {
	
	private int sizeOfBuffer = 10;

	@Test
	public void AddToProducer() {
		IBuffer<Integer> buffer = new DoubleBuffer<Integer>(sizeOfBuffer);
		Producer<Integer> producer = new Producer<Integer>(buffer);
		
		producer.produce(1);
		
		assertEquals(1, buffer.getProducerBuffer().peek().intValue());
	}
	
	@Test
	public void AddMultipleThingsToProducer() {
		IBuffer<Integer> doubleBuffer = new DoubleBuffer<Integer>(sizeOfBuffer);
		Producer<Integer> producer = new Producer<Integer>(doubleBuffer);
		
		producer.produce(1);
		producer.produce(2);
		producer.produce(3);
		
		Queue<Integer> buffer = doubleBuffer.getProducerBuffer();
		
		assertEquals(1, buffer.poll().intValue());
		assertEquals(2, buffer.poll().intValue());
		assertEquals(3, buffer.poll().intValue());
	}

	@Test
	public void AddMoreThingsThanBufferSize(){	
		IBuffer<Integer> doubleBuffer = new DoubleBuffer<Integer>(sizeOfBuffer);
		Producer<Integer> producer = new Producer<Integer>(doubleBuffer);
		
		for (int i = 0; i < (sizeOfBuffer * 2) - 1; i++){
			producer.produce(i);
		}
		 
		Queue<Integer> producerBuffer = doubleBuffer.getProducerBuffer();
		Queue<Integer> consumerBuffer = doubleBuffer.getConsumerBuffer();
		
		// Taking from the first buffer that overflowed
		// causing the buffers to switch, so the first buffer is actually the consumer buffer
		for (int i = 0; i < sizeOfBuffer; i++){
			assertEquals(i, consumerBuffer.remove().intValue());
		}
		
		for (int i = 0; i < sizeOfBuffer - 1; i++){
			assertEquals(i + 10, producerBuffer.remove().intValue());
		}
	}
	
}
