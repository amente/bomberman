package bomberman.test;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import bomberman.game.GameEvent;
import bomberman.game.GameProtocol;
import bomberman.game.GameResolver;
import bomberman.game.floor.Player;

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
		GameResolver resolver = new GameResolver();
		
		Player player = TestPlayerFactory.createPlayer(resolver, initialX, initialY);
		if (expectedX == -1) { expectedX = player.getX(); expectedY = player.getY(); }
		
		GameEvent action = GameProtocol.getInstance().getEvent(actionString);
		action.setSenderAddress(player.getAddress());
		
		resolver.processEvent(action);
		
		if (player.getX() != expectedX || player.getY() != expectedY) {
			fail("x should be " + expectedX + " but is " + player.getX() + ". y should be " + expectedY + " but is " + player.getY());
		}
		
		TestPlayerFactory.playerNum = 1;
	}
	
	@Test public void TwoPlayersMove() { 
		IGameStateAssertion postAssertion = new IGameStateAssertion() {
			@Override
			public void AssertState(GameResolver r) {
				Player p1 = r.getPlayer("Player1");
				Player p2 = r.getPlayer("Player2");
				Assert.assertEquals(p1.getX(), 2);
				Assert.assertEquals(p1.getY(), 3);
				
				Assert.assertEquals(p2.getX(), 6);
				Assert.assertEquals(p2.getY(), 5);
			}
		};
		
		ThreadedTestRunner.RunGameTest(
			new String[] { "Move DOWN" },
			2,
			2, 
			new String[] { "Move RIGHT" }, 
			5, 
			5, 
			null, 
			postAssertion, 
			0
		);
	}
	
	@Test public void TwoPlayersMoveIntoEachOther() { 
		IGameStateAssertion postAssertion = new IGameStateAssertion() {
			@Override
			public void AssertState(GameResolver r) {
				Player p1 = r.getPlayer("Player1");
				Player p2 = r.getPlayer("Player2");
				Assert.assertFalse(p1.isAlive());
				Assert.assertFalse(p2.isAlive());
			}
		};
		
		ThreadedTestRunner.RunGameTest(
			new String[] { "Move RIGHT" },
			2,
			2, 
			new String[] {}, 
			3, 
			2, 
			null, 
			postAssertion, 
			100
		);
	}
}
