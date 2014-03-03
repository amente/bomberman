package bomberman.test;

import static org.junit.Assert.*;

import java.util.Queue;

import org.junit.Test;

import Buffer.Consumer;
import Buffer.DoubleBuffer;
import Buffer.IBuffer;

public class ConsumerTests {

	@Test
	public void ConsumeItem() {
		IBuffer<Integer> doubleBuffer = new DoubleBuffer<Integer>(10);
		Consumer<Integer> consumer = new Consumer<Integer>(doubleBuffer);
		
		doubleBuffer.getConsumerBuffer().add(1);
		assertEquals(1, consumer.consume().intValue());
	}
	
	@Test
	public void ConsumeMultipleItem() {
		IBuffer<Integer> doubleBuffer = new DoubleBuffer<Integer>(10);
		Consumer<Integer> consumer = new Consumer<Integer>(doubleBuffer);
		
		Queue<Integer> consumerBuffer = doubleBuffer.getConsumerBuffer();
		
		consumerBuffer.add(1);
		consumerBuffer.add(2);
		consumerBuffer.add(3);
		
		assertEquals(1, consumer.consume().intValue());
		assertEquals(2, consumer.consume().intValue());
		assertEquals(3, consumer.consume().intValue());
	}

}
