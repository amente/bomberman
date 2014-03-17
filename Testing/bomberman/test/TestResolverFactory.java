package bomberman.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bomberman.game.GameEvent;
import bomberman.game.GameProtocol;
import bomberman.game.GameResolver;
import bomberman.game.GameServer;
import bomberman.game.floor.Player;

public class TestResolverFactory {
	public static GameResolver CreateGameResolver() {
		GameServer server = null;
		try { server = new GameServer(6000); } catch (Exception e) { fail(); }
		if (server == null) { fail(); }
		
		GameResolver resolver = new GameResolver(server, true);
		resolver.setGameIsRunning(true);
		resolver.start();
		
		return resolver;
	}
}
