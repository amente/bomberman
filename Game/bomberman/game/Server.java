package bomberman.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;




public class Server {
	
	private static final int QUEUE_CAPACITY = 10;
	private static final int MAX_DTATAGRAM_SIZE = 1024; // In Bytes
	
	private static DatagramSocket socket;
	private static DatagramPacket packet;
	
	byte[] recieveBuffer;
	
	
	
	ArrayBlockingQueue<DatagramPacket> recieveQueue; // Thread safe FIFO Queue
	
	
	public Server(int port){
		recieveBuffer = new byte[MAX_DTATAGRAM_SIZE];	
		try {
			socket = new DatagramSocket(port);
			packet  = new DatagramPacket(recieveBuffer,recieveBuffer.length);
			recieveQueue = new ArrayBlockingQueue<DatagramPacket>(QUEUE_CAPACITY);
		} catch (SocketException e) {			
			e.printStackTrace();
		}
	}	
	
	public boolean listen(){		
		try {			
			socket.receive(packet);					
			recieveQueue.add(packet);
		} catch (IOException e) {	
			e.printStackTrace();
			return false;
		}		
		return true;
	}

	
		
		
	public static void main(String args[]) throws IOException{
		
		if(args.length<1){
			System.out.println("Usage: java Server port ");
		}
		
		int port = Integer.parseInt(args[0]);
		
		
		boolean gameRunning = false;
		
		Server server = new Server(port);
	    
		while(gameRunning){
			server.listen();			
		}	    
	    
		socket.close();			
	}	
	
	
	private class Worker extends Thread{
		
		
		@Override
		public void run(){
			
		}
		
		
	}
	
	

}
