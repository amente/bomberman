package bomberman.test;

import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;

import bomberman.game.GameResolver;
import bomberman.game.floor.Player;
import bomberman.game.network.NetworkAddress;

public class TestPlayerFactory {
	public static int playerNum = 1;
	private static String fakeAddress = "166.6.6.";
	
	public static Player createPlayer(
			GameResolver resolver,
			int initialX,
			int initialY
	) {
		NetworkAddress address = null;
		
		try {
			address = new NetworkAddress(
					fakeAddress + playerNum, 
					6000
			);
		} catch (UnknownHostException e) { fail(); }
		if (address == null) { fail(); }
		
		if (initialX == -1) {
			initialX = resolver.getGameFloor().getXSize() - 2;
			initialY = resolver.getGameFloor().getYSize() - 2;
		}
		
		resolver.getGameFloor().addPlayer(address, initialX, initialY);
		Player player = resolver.getGameFloor().getPlayer("Player" + playerNum++);
		if (player == null) { fail(); }
		
		return player;
		
	}
}
