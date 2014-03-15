package bomberman.test;

import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import bomberman.game.GameAction;
import bomberman.game.GameProtocol;
import bomberman.game.GameResolver;
import bomberman.game.floor.Player;
import bomberman.game.network.NetworkAddress;

public class GameResolverTests {
	@Test
	public void RunTest() {
		GameResolver resolver = new GameResolver(null);
		
		
		NetworkAddress address = null;
		try {
			address = new NetworkAddress(
					InetAddress.getLocalHost().getHostAddress(), 
					6000
			);
		} catch (UnknownHostException e) { fail(); }
		if (address == null) { fail(); }
		
		resolver.getGameFloor().addPlayer(address, 1, 1);
		Player player = resolver.getGameFloor().getPlayer("Player1");
		if (player == null) { fail(); }
		
		GameAction action = GameProtocol.getInstance().getAction("Move UP");
		action.setSenderAddress(player.getAddress());
		
		resolver.processAction(action);
		
		if (player.getX() != 1 || player.getY() != 2) {
			fail("x should be 1 but is " + player.getX() + ". y should be 2 but is " + player.getY());
		}
	}
}
