package bomberman.test;

import bomberman.game.GameEvent;

public class TimedEvent {
	private GameEvent event;
	private int timeout;
	
	public GameEvent getEvent() { return event; }
	public int getTimeout() { return timeout; }
	
	public TimedEvent(
		GameEvent g,
		int t
	) {
		event = g;
		timeout = t;
	}
}
