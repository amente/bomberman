// Tests both double buffer and single buffer

package bomberman.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import Buffer.Consumer;
import Buffer.DoubleBuffer;
import Buffer.IBuffer;
import Buffer.Producer;
import Buffer.SingleBuffer;

public class BufferTests implements Runnable {

	private int numProducers = 0;
	private int numConsumers = 0;
	private int operationsPerProducer = 0;
	private int operationsPerConsumer = 0;
	private IBuffer<Integer> buffer;
	private int expectedSize = 0;
	private Integer testNum = -1;
	private boolean flush = false;
	
	private int numTestRuns = 1000; // 10000 tests takes ~ 140 seconds on my machine
									// 1000 tests ~ 10 seconds
	
	@Test
	public void OneProducerOneConsumerDoubleBuffer() {
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				1,
				1,
				1000,
				1000,
				true,
				false
			);
		}
	}
	
	@Test
	public void MultipleProducersOneConsumerDoubleBuffer(){
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				4,
				1,
				250,
				1000,
				true,
				false
			);
		}
	}
	
	@Test
	public void MultipleConsumersOneProducerDoubleBuffer(){
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				1,
				4,
				1000,
				250,
				true,
				false
			);
		}
	}
	
	@Test
	public void MultipleConsumersMultipleProducersDoubleBuffer(){
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				4,
				4,
				250,
				250,
				true,
				false
			);
		}
	}
	
	@Test
	public void OneProducerOneConsumerSingleBuffer() {
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				1,
				1,
				1000,
				1000,
				false,
				false
			);
		}
	}
	
	@Test
	public void MultipleProducersOneConsumerSingleBuffer(){
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				4,
				1,
				250,
				1000,
				false,
				false
			);
		}
	}
	
	@Test
	public void MultipleConsumersOneProducerSingleBuffer(){
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				1,
				4,
				1000,
				250,
				false,
				false
			);
		}
	}
	
	@Test
	public void MultipleConsumersMultipleProducersSingleBuffer(){
		for(int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				4,
				4,
				250,
				250,
				false,
				false
			);
		}
	}
	
	@Test
	public void FlushDoubleBuffer(){
		for (int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				1,
				1,
				1001,
				1001,
				true,
				true
			);
		}
	}
	
	@Test
	public void FlushSingleBuffer(){
		for (int i = 0; i < numTestRuns; i++){
			RunMultipleThreadTest(
				1,
				1,
				1001,
				1001,
				true,
				true
			);
		}
	}
	
	private void RunMultipleThreadTest(
		int numProducers,
		int numConsumers,
		int opsProducer,
		int opsConsumer,
		boolean isDoubleBuffer,
		boolean flushBuffer
	) {
		int maxSize = numProducers * opsProducer;
		if (isDoubleBuffer) {
			this.buffer = new DoubleBuffer<Integer>(maxSize/10);
		} else {
			this.buffer = new SingleBuffer<Integer>(maxSize/10);
		}
		
		this.numProducers = numProducers;
		this.numConsumers = numConsumers;
		this.operationsPerConsumer = opsConsumer;
		this.operationsPerProducer = opsProducer;
		this.testNum = -1;
		this.flush = flushBuffer;
		
		ThreadedTestRunner.RunTest(this, numProducers + numConsumers);
		
		expectedSize = (numProducers * operationsPerProducer - 
						numConsumers * operationsPerConsumer);
		
		assertEquals(expectedSize, 
			buffer.getProducerBuffer().size() + 
			buffer.getConsumerBuffer().size()
		);
	}
	
	public void run() {
		boolean isConsumer = true;
		
		synchronized(this){
			if (numProducers == 0 && numConsumers == 0){
				return;
			} 
			
			int random = 0;
			if (numProducers > 0 && numConsumers > 0){
				random = new Random().nextInt(2) + 1;
			}
	
			if (numProducers == 0 || random == 1){
				numConsumers--;
			} else if (numConsumers == 0 || random == 2){
				numProducers--;
				isConsumer = false;
				synchronized(testNum){
					testNum++;
				}
			} 
		}
		
		if (isConsumer){
			Consumer<Integer> consumer = new Consumer<Integer>(buffer);
			for (int i = 0; i < operationsPerConsumer; i++) {
				consumer.consume();
				/*
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
				*/
			}
		} else {
			Producer<Integer> producer = new Producer<Integer>(buffer);
			for (int i = 0; i < operationsPerProducer; i++) {
				producer.produce(i+testNum*operationsPerProducer);
				/*
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
				*/
			}
			
			if (flush){
				buffer.flush();
			}
			
		}
	}
}
