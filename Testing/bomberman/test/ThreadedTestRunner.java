/* AUTHOR: Andrew Belu
	Launches a test with input number of threads
	For example usage, see: Tests/DoubleBufferTests.java
*/

package bomberman.test;

import java.util.ArrayList;
import java.util.List;

import bomberman.game.GameEvent;
import bomberman.game.GameProtocol;
import bomberman.game.GameResolver;
import bomberman.game.floor.Player;

public class ThreadedTestRunner {
	public static boolean RunTest(Runnable test, int numThreads) {
		Thread[] threads = new Thread[numThreads];
		
		for (int i = 0; i < numThreads; i++){
			Thread t = new Thread(test);
			threads[i] = t;
			t.start();
		}
		
		for (int i = 0; i < numThreads; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean RunTests(Runnable[] tests, boolean join) {
		Thread[] threads = new Thread[tests.length];
		
		for (int i = 0; i < tests.length; i++) {
			Thread t = new Thread(tests[i]);
			threads[i] = t;
			t.start();
		}
		
		if (join) {
			for (int i = 0; i < tests.length; i++){
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static void RunGameTest(
			String[] player1Actions, int initialXP1, int initialYP1,
			String[] player2Actions, int initialXP2, int initialYP2,
			IGameStateAssertion preAssertion, IGameStateAssertion postAssertion,
			int expectedTestTime
	) {
		GameResolver resolver = TestResolverFactory.CreateGameResolver();
		
		Player player1 = TestPlayerFactory.createPlayer(resolver, initialXP1, initialYP1);
		Player player2 = TestPlayerFactory.createPlayer(resolver, initialXP2, initialYP2);
		
		List<GameEvent> events = new ArrayList<GameEvent>();
		for(int i = 0; i < player1Actions.length; i++) {
			GameEvent p1Event = GetEvent(player1Actions[i], player1);
			events.add(p1Event);
		}
		
		for(int i = 0; i < player2Actions.length; i++) {
			GameEvent p2Event = GetEvent(player2Actions[i], player2);
			events.add(p2Event);
		}
		
		if (preAssertion != null) {
			preAssertion.AssertState(resolver);
		}
		
		ThreadedTestRunner.RunGameTest(events, resolver);

		try { Thread.sleep(expectedTestTime); } catch (InterruptedException e) {}

		TestPlayerFactory.playerNum = 1;
		resolver.setGameIsRunning(false);		
		postAssertion.AssertState(resolver);
		
		try { Thread.sleep(100); } catch (InterruptedException e) {}
	}
	
	private static GameEvent GetEvent(String eventString, Player player) {
		GameEvent action = GameProtocol.getInstance().getEvent(eventString);
		action.setSenderAddress(player.getAddress());
		
		return action;
	}

	public static void RunGameTest(List<GameEvent> events, final GameResolver resolver) {
		Thread[] threads = new Thread[events.size()];
		
				
		int counter = 0;
		
		for(final GameEvent event : events) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					resolver.addEvent(event);
				}
			};
			
			threads[counter++] = new Thread(
				r	
			);
		}
		RunTests(threads, false);
		
		try { Thread.sleep(100); } catch (InterruptedException e) {}
	}
}
