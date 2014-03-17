package bomberman.game.network;

import java.net.DatagramPacket;
/**
 * 
 * Any class that implements this, returns a string message after processing a packet
 * e.g Returning an ACK message for synchronous sending
 *
 */
public interface PacketProcessor {
		public String process(DatagramPacket packet);
}
