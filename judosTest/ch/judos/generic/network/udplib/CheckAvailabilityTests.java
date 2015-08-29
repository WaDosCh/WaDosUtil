package ch.judos.generic.network.udplib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.model.reachability.Reachability;

/**
 * @created 04.05.2012
 * @author Julian Schelker
 */
public class CheckAvailabilityTests {

	private Udp4I udp;
	private InetSocketAddress target;
	private InetSocketAddress wrongTarget;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		this.udp = UdpLib.createDefault();
		this.target = new InetSocketAddress("127.0.0.1", this.udp.getLocalPort());
		this.wrongTarget = new InetSocketAddress("10.0.0.1", 1000);
	}

	@Test
	public void test() {
		Reachability r = this.udp.getReachability(this.target, 100);
		assertTrue(r.isReachable());
		System.out.println("Ping to self: " + r.getPingMS() + " ms");
		r = this.udp.getReachability(this.wrongTarget, 500);
		assertFalse(r.isReachable());
	}

	@After
	public void tearDown() {
		this.udp.dispose();
	}

}
