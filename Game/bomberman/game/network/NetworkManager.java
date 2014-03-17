package bomberman.game.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;


/**
 * 
 * NetworkManger
 * Provides wrapped UDP network communication
 *  Keeps count of sent and received packets for every unique address  
 *  
 */
public class NetworkManager {
	private DatagramSocket socket;		
	private HashMap<NetworkAddress,Integer> sendCount;
	private HashMap<NetworkAddress,Integer> recieveCount;
	
	
	public NetworkManager() throws SocketException{
		socket = new DatagramSocket();	
		initialize();
	}
	
	/**
	 * Bind the network manager to a port
	 * @param port
	 * @throws SocketException 
	 */
	public NetworkManager(int port) throws SocketException {		
			socket = new DatagramSocket(port);		
			initialize();
	}
	
	private void initialize(){
		sendCount = new HashMap<NetworkAddress,Integer>();
		recieveCount = new HashMap<NetworkAddress,Integer>();
	}
	
	/**
	 * Send asynchronous message to receiver, does not wait ACK
	 * @param message the message
	 * @param recvAddress the reciver address
	 * @param doCount true if send count should be incremented
	 */
	public synchronized void sendAsynchronous(String message, NetworkAddress recvAddress,boolean doCount) {
		try {			
			DatagramPacket p = makeDatagramPacket(message,recvAddress);
			socket.send(p);
			if(doCount){				
				if(sendCount.containsKey(recvAddress)){
					sendCount.put(recvAddress,sendCount.get(recvAddress)+1);
				}else{
					sendCount.put(recvAddress,0);
				}
			}
		} catch (IOException e) {
			System.out.println("Failed to send message"
					);
			e.printStackTrace();
		}
	}
		
	/**
	 * Send Synchronous message to receiver, Wait for ACK
	 * @param message the message
	 * @param recvAddress the reciever address
	 * @param expectedACK the expected ACK message
	 * @param numAttempts the number of attempts 
	 * @param timeOut the timeout in ms for each attempt to wait form ACK from receiver
	 * @param doCount true if send count should be incremented when success
	 * @return the ACK message
	 */
	public synchronized String sendSynchronous(String message,NetworkAddress recvAddress,String expectedACK,int numAttempts,int timeOut,boolean doCount){		
		DatagramPacket p;
		String ackString =null;
		try {
			p = makeDatagramPacket(message,recvAddress);
						
			int attempt= 1;
			while(attempt <= numAttempts){
				try {
					socket.send(p);

					socket.setSoTimeout(timeOut);
					
					byte[] buf = new byte[expectedACK!=null?expectedACK.length():50];
					DatagramPacket ack = new DatagramPacket(buf, buf.length);					
					socket.receive(ack);
					
					ackString = new String(ack.getData(),ack.getOffset(),ack.getLength());					
					if (expectedACK==null || ackString.trim().equals(expectedACK)) {
						//System.out.println("Sending Message : Success " + message);
						if(doCount){				
							if(sendCount.containsKey(recvAddress)){
								sendCount.put(recvAddress,sendCount.get(recvAddress)+1);
							}else{
								sendCount.put(recvAddress,0);
							}
						}
						break;
					}else{
						//System.out.println("Message Sending Failed " + message);
					}
				} catch (SocketTimeoutException e) {
					//System.out.println("Attempt:"+attempt);
					attempt++;
					continue;  
				} catch (IOException e) {
					break; 
				}
			}			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ackString;
	}
	
	/**
	 * Send ASynchronous messages, no ACK
	 * @param timeOut the time out , set to 0 to wait indefinitely
	 * @param doCount true if receive count should be incremented
	 * @return
	 */
	public synchronized DatagramPacket receiveAsynchronous(int timeOut,boolean doCount) {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		try {
			socket.setSoTimeout(timeOut);
		} catch (SocketException e1) {}
		
		try {
			socket.receive(packet);
			if(doCount){
				NetworkAddress addr = new NetworkAddress(packet.getSocketAddress());
				if(recieveCount.containsKey(addr)){
					recieveCount.put(addr,recieveCount.get(addr)+1);
				}else{
					recieveCount.put(addr,0);
				}
			}
			return packet;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Receive Synchronous messages, ACK for each message
	 * @param ackMessage  the ACK message
	 * @param timeOut	the time out , set to 0 to wait indefinitely
	 * @param buffSize  the buffer size for expected message
	 * @param doCount   true if receive count should be incremented 
	 * @return
	 */
	public synchronized DatagramPacket receiveSynchronous(int timeOut,int buffSize,boolean doCount,PacketProcessor p) {
		byte[] buf = new byte[buffSize];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);		
		try {
			socket.setSoTimeout(timeOut);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		try {
			socket.receive(packet);			
			String ackMessage  = p.process(packet);
			
			if(ackMessage!=null){
				
				byte[] ackBytes = ackMessage.getBytes();

				DatagramPacket ack = new DatagramPacket(ackBytes,
						ackBytes.length, packet.getSocketAddress());

				socket.send(ack);
			}
			if(doCount){
				NetworkAddress addr = new NetworkAddress(packet.getSocketAddress());
				if(recieveCount.containsKey(addr)){
					recieveCount.put(addr,recieveCount.get(addr)+1);
				}else{
					recieveCount.put(addr,0);
				}
			}						
			return packet; 
			 
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	/**
	 * Receive Synchronous messages, ACK for each message
	 * @param ackMessage  the ACK message
	 * @param timeOut	the time out , set to 0 to wait indefinitely
	 * @param buffSize  the buffer size for expected message
	 * @param doCount   true if receive count should be incremented 
	 * @return
	 */
	public synchronized DatagramPacket receiveSynchronous(String ackMessage,int timeOut,int buffSize,boolean doCount) {
		byte[] buf = new byte[buffSize];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);		
		
		try {
			socket.setSoTimeout(timeOut);
		} catch (SocketException e1) {}
		
		try {
			socket.receive(packet);	
			
		
				
			byte[] ackBytes = ackMessage.getBytes();

			DatagramPacket ack = new DatagramPacket(ackBytes,
			ackBytes.length, packet.getSocketAddress());
			socket.send(ack);
			
			if(doCount){
				NetworkAddress addr = new NetworkAddress(packet.getSocketAddress());
				if(recieveCount.containsKey(addr)){
					recieveCount.put(addr,recieveCount.get(addr)+1);
				}else{
					recieveCount.put(addr,0);
				}
			}						
			return packet; 
			 
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}	
	public void close() {
		socket.close();
	}
	
	public int getSendCount(NetworkAddress addr){
		return(sendCount.get(addr));
	}
	
	public int getReceiveCount(SocketAddress addr){
		return(recieveCount.get(addr));
	}
	
	public void setSendCount(NetworkAddress addr,int num){
		sendCount.put(addr, num);
	}
	
	public void setReceiveCount(NetworkAddress addr,int num){
		recieveCount.put(addr,num);
	}
	
	public DatagramPacket makeDatagramPacket(String message,NetworkAddress address) throws SocketException{
		if(address.isSocketAddress()){
			return new DatagramPacket(message.getBytes(), message.length(),address.getSocketAddr());
		}else if(address.isInetAddress()){
			return  new DatagramPacket(message.getBytes(), message.length(),address.getInetAddr(),address.getPort());
		}
		return null;
	}	
	

	
	
}
