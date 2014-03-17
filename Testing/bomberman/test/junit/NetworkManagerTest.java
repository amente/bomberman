package bomberman.test.junit;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bomberman.game.JoinResolver;
import bomberman.game.network.NetworkAddress;
import bomberman.game.network.NetworkManager;
import bomberman.game.network.PacketProcessor;
import bomberman.test.ThreadedTestRunner;

public class NetworkManagerTest {
	
	private NetworkManager manager = null;
	private NetworkAddress address = null;

	@Before
	public void setUp() throws Exception {
		try { manager = new NetworkManager(6000); } catch (SocketException e) { fail(); }
		try { address = new NetworkAddress("127.0.0.1", 6000); } catch (UnknownHostException e) { fail(); }
	}

	@After
	public void tearDown() throws Exception {
		manager.close();
	}

	@Test
	public void testNetworkManager() {
		fail("Not yet implemented");
	}

	@Test
	public void testNetworkManagerInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendAndReceiveAsynchronous() {
		manager.sendAsynchronous("test123", address, false);
		DatagramPacket p = manager.receiveAsynchronous(5000, false);
		
		String s = new String(p.getData());
		
		assertEquals("test123", s.trim());
	}

	@Test
	public void testSendAndReceiveSynchronous() {
		Runnable[] runnables = new Runnable[2];
		
		final NetworkManager finalManager = manager;
		final NetworkAddress finalAddress = address;
		
		NetworkManager sendManager = null;
		try { sendManager = new NetworkManager(6001);} catch (SocketException e) {}
		
		final NetworkManager finalSendManager = sendManager;
		
		runnables[0] = new Runnable() {
			@Override
			public void run() {
				DatagramPacket p = finalManager.receiveSynchronous("ack", 5000, 1024, false);
				assertEquals(new String(p.getData()).trim(), "test321");
			}
		};
		
		runnables[1] = new Runnable() {
			@Override
			public void run() {
				finalSendManager.sendSynchronous("test321", finalAddress, "ack", 30, 100, false);
			}
		};
		
		ThreadedTestRunner.RunTests(runnables, true);
	}

	@Test
	public void testReceiveAsynchronous() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveSynchronousIntIntBooleanPacketProcessor() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveSynchronousStringIntIntBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSendCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReceiveCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSendCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetReceiveCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeDatagramPacket() {
		fail("Not yet implemented");
	}

}
