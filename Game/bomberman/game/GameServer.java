package bomberman.game;

import java.net.DatagramPacket;
import java.util.concurrent.ArrayBlockingQueue;

import bomberman.test.TestDriver;

public class GameServer extends Thread {
	
	private UDPWrapper udpWrapper;
	private boolean isStopped = false;
	private boolean gameSetup = false;
	private Logger logger = null;
	
	public ArrayBlockingQueue<String> messageQueue; // Thread safe FIFO Queue	
	
	public void setLogger(Logger l){
		logger = l;
	}
	
	public GameServer(int port){			
		udpWrapper = new UDPWrapper(port, true);
		messageQueue = new ArrayBlockingQueue<String>(Application.QUEUE_CAPACITY);		
	}	
	
	public boolean setup(){
		DatagramPacket packet = udpWrapper.receiveSynchronous();
		
		if (packet == null) { return false; }
		
		handleSetupMessage(packet);
		
		return true;
	}
	
	private void handleSetupMessage(DatagramPacket packet) {
		String message = udpWrapper.getPacketMessage(packet).trim();
		String[] messageArr = message.split(" ");
		if (messageArr[1].trim().equals("START_GAME")){
			System.out.println("Starting game");
			gameSetup = true;
			if (logger != null) { logger.addToLog(message); }
		} else if (messageArr[0].trim().equals("Join")){
			messageQueue.add(message);
		}
	}

	public boolean listen(){		
		DatagramPacket packet = udpWrapper.receiveAsynchronous();
		
		if (packet == null) { return false; }
		
		String message = new String(packet.getData()).trim();
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
		while(!isStopped){
			listen();
		}		
	}
	
	private boolean gameIsSetup() {
		return gameSetup;
	}
	
	
	public void stopGracefully(){
		isStopped = true;	
		gameSetup = true;
		udpWrapper.interrupt();
	}
	
}
