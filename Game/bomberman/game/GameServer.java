package bomberman.game;

import java.net.DatagramPacket;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.test.TestDriver;

public class GameServer extends Thread {
	
	private UDPWrapper udpWrapper;
	private boolean isStopped = false;
	private boolean testDriverSetup = false;
	
	public ArrayBlockingQueue<String> messageQueue; // Thread safe FIFO Queue	
	
	public GameServer(int port){			
		udpWrapper = new UDPWrapper(port, true);
		messageQueue = new ArrayBlockingQueue<String>(Application.QUEUE_CAPACITY);		
	}	
	
	public void setup(){
		DatagramPacket packet = udpWrapper.receiveSynchronous();
		handleSetupMessage(packet);
	}
	
	private void handleSetupMessage(DatagramPacket packet) {
		String message = udpWrapper.getPacketMessage(packet);
		String[] messageArr = message.split("\\" + Application.MESSAGE_DELIMITER);
		String messageCode = messageArr[0];
		
		if (messageCode.equals(TestDriver.Protocol.SETUP_CONNECTION)){
			testDriverSetup = true;
		} 
	}

	public boolean listen(){		
		DatagramPacket packet = udpWrapper.receiveAsynchronous();
		String message = new String(packet.getData());
		System.out.println(message);
		messageQueue.add(message);
			
		return true;
	}
	
	public boolean isRunning(){
		return !isStopped;
	}

	@Override
	public void run(){	
		while(!gameIsSetup()){
			setup();
		}
		System.out.println("Game setup correctly");
		while(!isStopped){
			listen();
		}		
	}
	
	private boolean gameIsSetup() {
		return testDriverSetup;
	}
	
	
	public void stopGracefully(){
		isStopped = true;	
		testDriverSetup = true;
	}
	
}
