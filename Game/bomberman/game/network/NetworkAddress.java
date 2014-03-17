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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inetAddr == null) ? 0 : inetAddr.hashCode());
		result = prime * result + (isInetAddress ? 1231 : 1237);
		result = prime * result + (isSocketAddress ? 1231 : 1237);
		result = prime * result + port;
		result = prime * result
				+ ((socketAddr == null) ? 0 : socketAddr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetworkAddress other = (NetworkAddress) obj;
		if (inetAddr == null) {
			if (other.inetAddr != null)
				return false;
		} else if (!inetAddr.equals(other.inetAddr))
			return false;
		if (isInetAddress != other.isInetAddress)
			return false;
		if (isSocketAddress != other.isSocketAddress)
			return false;
		if (port != other.port)
			return false;
		if (socketAddr == null) {
			if (other.socketAddr != null)
				return false;
		} else if (!socketAddr.equals(other.socketAddr))
			return false;
		return true;
	}
	
}
