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

public class MovementTests {
	@Test public void MoveUp_FreeToMove() { RunSingleMoveTest( "Move UP", 2, 2, 2, 1 ); }
	@Test public void MoveUp_WallInTheWay() { RunSingleMoveTest( "Move UP", 1, 1, 1, 1 ); }
	@Test public void MoveDown_FreeToMove() { RunSingleMoveTest( "Move DOWN", 1, 1, 1, 2 ); }
	@Test public void MoveDown_WallInTheWay() { RunSingleMoveTest( "Move DOWN", -1, -1, -1, -1 ); }
	@Test public void MoveLeft_FreeToMove() { RunSingleMoveTest( "Move LEFT", 2, 2, 1, 2 ); }
	@Test public void MoveLeft_WallInTheWay() { RunSingleMoveTest( "Move LEFT", 1, 1, 1, 1 ); }
	@Test public void MoveRight_FreeToMove() { RunSingleMoveTest( "Move RIGHT", 1, 1, 2, 1 ); }
	@Test public void MoveRight_WallInTheWay() { RunSingleMoveTest( "Move RIGHT", -1, -1, -1, -1 ); }
	
	private void RunSingleMoveTest(
			String actionString,
			int initialX,
			int initialY,
			int expectedX,
			int expectedY
	) {
		GameResolver resolver = new GameResolver(null, true);
		
		NetworkAddress address = null;
		try {
			address = new NetworkAddress(
					InetAddress.getLocalHost().getHostAddress(), 
					6000
			);
		} catch (UnknownHostException e) { fail(); }
		if (address == null) { fail(); }
		
		if (initialX == -1) {
			initialX = resolver.getGameFloor().getXSize() - 2;
			initialY = resolver.getGameFloor().getYSize() - 2;
			expectedX = initialX;
			expectedY = initialY;
		}
		
		resolver.getGameFloor().addPlayer(address, initialX, initialY);
		Player player = resolver.getGameFloor().getPlayer("Player1");
		if (player == null) { fail(); }
		
		GameAction action = GameProtocol.getInstance().getAction(actionString);
		action.setSenderAddress(player.getAddress());
		
		resolver.processAction(action);
		
		if (player.getX() != expectedX || player.getY() != expectedY) {
			fail("x should be " + expectedX + " but is " + player.getX() + ". y should be " + expectedY + " but is " + player.getY());
		}
	}
}
