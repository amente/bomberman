package bomberman.test;

import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import bomberman.game.GameAction;
import bomberman.game.GameProtocol;
import bomberman.game.GameResolver;
import bomberman.game.GameServer;
import bomberman.game.floor.Player;
import bomberman.game.network.NetworkAddress;

public class GameResolverTests {
	@Test public void GameServerProduce_GameResolverConsumes() { 
		RunTwoPlayerMovementTest( 
			"Move DOWN", 
			"Move RIGHT", 
			2, 2, 
			2, 3,
			5, 5,
			6, 5
		); 
	}
	
	private void RunTwoPlayerMovementTest(
			String player1Action,
			String player2Action,
			int initialXP1, 
			int initialYP1, 
			int expectedXP1,
			int expectedYP1,
			int initialXP2, 
			int initialYP2,
			int expectedXP2,
			int expectedYP2
	) {
		GameServer server = null;
		try { server = new GameServer(6000); } catch (Exception e) { fail(); }
		if (server == null) { fail(); }
		
		GameResolver resolver = new GameResolver(server, true);
		
		NetworkAddress address1 = null;
		NetworkAddress address2 = null;
		
		try {
			address1 = new NetworkAddress(
					InetAddress.getLocalHost().getHostAddress(), 
					6000
			);
		} catch (UnknownHostException e) { fail(); }
		if (address1 == null) { fail(); }
		
		try {
			address2 = new NetworkAddress(
					"166.6.6.7", 
					6000
			);
		} catch (UnknownHostException e) { fail(); }
		if (address2 == null) { fail(); }
		
		resolver.getGameFloor().addPlayer(address1, initialXP1, initialYP1);
		Player player1 = resolver.getGameFloor().getPlayer("Player1");
		if (player1 == null) { fail(); }
		
		resolver.getGameFloor().addPlayer(address2, initialXP2, initialYP2);
		Player player2 = resolver.getGameFloor().getPlayer("Player2");
		if (player2 == null) { fail(); }
		
		final GameAction action1 = GameProtocol.getInstance().getAction(player1Action);
		action1.setSenderAddress(player1.getAddress());
		
		final GameAction action2 = GameProtocol.getInstance().getAction(player2Action);
		action2.setSenderAddress(player2.getAddress());
		
		Runnable[] runnables = new Runnable[2];
		int count = 0;
		
		final GameServer tempServer = server;
		
		if (player1Action != null ) {
			runnables[count++] = 
				new Runnable() {
					@Override
					public void run() {
						tempServer.addAction(action1);
					}
				};
		}
				
		if ( player2Action != null) {
			runnables[count++] = 
				new Runnable() {
					@Override
					public void run() {
						tempServer.addAction(action2);
					}
				};
		}
			
		resolver.start();
		ThreadedTestRunner.RunTests(runnables);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) { fail(); }
		
		if (player1.getX() != expectedXP1 || player1.getY() != expectedYP1) {
			fail("x should be " + expectedXP1 + " but is " + player1.getX() + ". y should be " + expectedYP1 + " but is " + player1.getY());
		}
		
		if (player2.getX() != expectedXP2 || player2.getY() != expectedYP2) {
			fail("x should be " + expectedXP2 + " but is " + player2.getX() + ". y should be " + expectedYP2 + " but is " + player2.getY());
		}
	}
}
