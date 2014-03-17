package bomberman.test;

import bomberman.game.GameResolver;

public class TestResolverFactory {
	public static GameResolver CreateGameResolver() {
				
		GameResolver resolver = new GameResolver();	
		resolver.setGameIsRunning(true);
		resolver.start();
		
		return resolver;
	}
}
