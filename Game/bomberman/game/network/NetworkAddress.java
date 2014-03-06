package bomberman.game.network;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * 
 * Wraps different kinds of addresses into a general one, usable by NetworkManager to construct appropriate
 * datagram packets  
 *
 */
public class NetworkAddress{
	
	private boolean isInetAddress;
	private int port;
	private InetAddress inetAddr;
	private boolean isSocketAddress;
	private SocketAddress socketAddr;
	
	public NetworkAddress(SocketAddress socketAddr){
		setSocketAddress(true);
		this.setSocketAddr(socketAddr);			
	}
	
	public NetworkAddress(String addr,int port) throws UnknownHostException{
		setInetAddress(true);
		setInetAddr(InetAddress.getByName(addr));
		this.setPort(port);				
	}

	public boolean isInetAddress() {
		return isInetAddress;
	}

	public void setInetAddress(boolean isInetAddress) {
		this.isInetAddress = isInetAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getInetAddr() {
		return inetAddr;
	}

	public void setInetAddr(InetAddress inetAddr) {
		this.inetAddr = inetAddr;
	}

	public boolean isSocketAddress() {
		return isSocketAddress;
	}

	public void setSocketAddress(boolean isSocketAddress) {
		this.isSocketAddress = isSocketAddress;
	}

	public SocketAddress getSocketAddr() {
		return socketAddr;
	}

	public void setSocketAddr(SocketAddress socketAddr) {
		this.socketAddr = socketAddr;
	}
	
	
}
