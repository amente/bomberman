package bomberman.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

public class GameServer extends Thread {
	
	private DatagramSocket recieveSocket;
	private DatagramPacket packet;	
	
		
	private boolean isStopped;
	
	public ArrayBlockingQueue<DatagramPacket> messageQueue; // Thread safe FIFO Queue	
	
	public GameServer(int listenPort,int broadcastPort){			
		try {
			recieveSocket = new DatagramSocket(listenPort);			
			messageQueue = new ArrayBlockingQueue<DatagramPacket>(Application.QUEUE_CAPACITY);		
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}	
	
	/**
	 * Send the game status to clients
	 * @return
	 */
	public void sendMessageToClients(byte[] message){		
		
	}
	
	public boolean listen(){		
		try {		
			byte[] recieveBuffer = new byte[Application.MAX_DTATAGRAM_SIZE];
			packet  = new DatagramPacket(recieveBuffer,recieveBuffer.length);
			recieveSocket.receive(packet);			
			messageQueue.add(packet);			
		} catch (IOException e) {	
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public boolean isRunning(){
		return !isStopped;
	}

	@Override
	public void run(){			
		while(!isStopped){
			listen();
		}		
	}
	
	
	public void stopGracefully(){
		isStopped = true;		
	}
	
}
