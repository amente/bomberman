package bomberman.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import DiscardingList.DiscardingList;

public class UDPWrapper {
	private DatagramSocket socket;
	private List<Integer> receivedPackets;
	private int packetListSize = 500;
	private String packetNumDelimiter = "/pnum/";
	private static int packetNum = 0;
	
	public UDPWrapper(){
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {}
	}
	
	public UDPWrapper(int port, boolean hasPacketList) {
		try {
			socket = new DatagramSocket(port);
			
			if (hasPacketList) {
				receivedPackets = new DiscardingList<Integer>(packetListSize);
			} else {
				receivedPackets = null;
			}
		} catch (SocketException e) {
			System.out.println("Failed to bind socket");
		}
	}
	
	public void sendAsynchronous(String message, int port, String address) {
		try {
			InetAddress ip = InetAddress.getByName(address);
			byte[] bytes = message.getBytes();
			DatagramPacket p = new DatagramPacket(bytes, message.length(), ip, port);
			System.out.println("Sending to address: " + ip + " port: " + port);
			socket.send(p);
		} catch (IOException e) {}
	}
	
	public DatagramPacket receiveAsynchronous() {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		try {
			socket.setSoTimeout(0);
		} catch (SocketException e1) {}
		
		try {
			System.out.println("Receiving on address: " + socket.getLocalAddress() + " port: " + socket.getLocalPort());
			socket.receive(packet);
			
			return packet;
		} catch (IOException e) {
			System.out.println("Receive asynchronous failed in UDPWrapper due to IOException");
			return null;
		}
	}
	
	public void sendSynchronous(String message){
		message = packetNum++ + packetNumDelimiter + message;
		byte[] bytes = message.getBytes();
		DatagramPacket p = new DatagramPacket(bytes, bytes.length);
		sendSynchronous(p);
	}
	
	public void sendSynchronous(String message, int port, String address) {
		try {
			InetAddress ip = InetAddress.getByName(address);
			
			message = packetNum++ + packetNumDelimiter + message;
			byte[] bytes = message.getBytes();
			DatagramPacket p = new DatagramPacket(bytes, bytes.length, ip, port);
			
			sendSynchronous(p);
		} catch (UnknownHostException e) {}
	}
	
	private void sendSynchronous(DatagramPacket p) {
		try {
			socket.setSoTimeout(200);
		} catch (SocketException e1) {}
		
		int attempts = 0;
		while(attempts < 30){
			try {
				socket.send(p);

				byte[] buf = new byte[10];
				DatagramPacket ack = new DatagramPacket(buf, buf.length);
				
				socket.receive(ack);
				
				String ackString = new String(ack.getData());
				System.out.println("Client received ack: " + ackString);
				
				if (ackString.trim().equals("ACK")) {
					break;
				}
			} catch (SocketTimeoutException e) {
				System.out.println("Did not receive ack, resending");
				attempts++;
				continue;  
			} catch (IOException e) {
				break; 
			}
		}
		
		if (attempts == 30){
			System.out.println("Did not send message " + new String(p.getData()));
		}
	}
	
	public DatagramPacket receiveSynchronous() {
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		try {
			socket.setSoTimeout(0);
		} catch (SocketException e1) {}
		
		try {
			socket.receive(packet);
			
			byte[] bytes = packet.getData();
			
			String byteString = new String(bytes);
			
			int packetNumber = getPacketNumber(byteString);
			
			byte[] ackBytes = "ACK".getBytes();
			
			DatagramPacket ack = new DatagramPacket(
				ackBytes, 
				ackBytes.length,
				packet.getAddress(), 
				packet.getPort()
			);
				
			socket.send(ack);
			
			if (hasPacketList()) {
				if (receivedPackets.contains(packetNumber)){
					return null;
				}
			}
			
			return packet; 
			 
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private int getPacketNumber(String data) {
		int packetNum = Integer.parseInt(data.split("\\" + packetNumDelimiter)[0]);
		return packetNum;
	}
	
	private String getPacketMessage(String data) {
		return data.split("\\" + packetNumDelimiter)[1];
	}
	
	public String getPacketMessage(DatagramPacket data) {
		String message = new String(data.getData());
		return getPacketMessage(message);
	}

	private boolean hasPacketList() {
		return receivedPackets != null;
	}
}
