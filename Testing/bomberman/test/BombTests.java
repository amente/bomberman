package bomberman.test;

import org.junit.Assert;
import org.junit.Test;

import bomberman.game.GameResolver;

public class BombTests {
	
	@Test
	public void HitPlayerWithBomb() {
		IGameStateAssertion preAssertion = new IGameStateAssertion() {
			@Override
			public void AssertState(GameResolver r) {
				boolean b = r.getPlayer("Player2").isAlive();
				Assert.assertTrue(b);
			}
		};
		
		IGameStateAssertion postAssertion = new IGameStateAssertion() {
			@Override
			public void AssertState(GameResolver r) {
				boolean b = r.getPlayer("Player2").isAlive();
				Assert.assertFalse(b);
			}
		};
		
		ThreadedTestRunner.RunGameTest(
			new String[] { "Bomb" },
			3, 3,
			new String[] {},
			4, 3,
			preAssertion,
			postAssertion,
			2500
		);
	}
	
	@Test
	public void PlaceBombAndMovePlayer() {
		IGameStateAssertion preAssertion = new IGameStateAssertion() {
			@Override
			public void AssertState(GameResolver r) {
				boolean b = r.getPlayer("Player2").isAlive();
				Assert.assertTrue(b);
			}
		};
		
		IGameStateAssertion postAssertion = new IGameStateAssertion() {
			@Override
			public void AssertState(GameResolver r) {
				boolean b = r.getPlayer("Player2").isAlive();
				Assert.assertTrue(b);
			}
		};
		
		ThreadedTestRunner.RunGameTest(
			new String[] { "Bomb" }, 3, 3,
			new String[] { "Move UP"}, 4, 3,
			preAssertion, postAssertion,
			2500
		);
	}
}
