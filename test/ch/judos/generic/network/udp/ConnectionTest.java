package ch.judos.generic.network.udp;

import java.net.InetSocketAddress;
import java.net.SocketException;

import junit.framework.TestCase;
import ch.judos.generic.network.udp.interfaces.ReachabilityListener;
import ch.judos.generic.network.udp.interfaces.Udp4I;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class ConnectionTest extends TestCase {

	private static final boolean SILENT = true;

	public void testConnection() throws SocketException, InterruptedException {
		Udp4I u = UdpLib.createDefault();

		InetSocketAddress own = new InetSocketAddress("localhost", u.getLocalPort());
		InetSocketAddress offline = new InetSocketAddress("10.0.0.1", u.getLocalPort());

		u.checkReachability(own, new ReachabilityListener() {
			@Override
			public void connectionActive(InetSocketAddress target, int pingMS) {
				if (!SILENT)
					System.out.println("ping: " + pingMS + " from :" + target);
				assertTrue(pingMS < getTimeoutMS());
			}

			@Override
			public void connectionTimedOut(InetSocketAddress target) {
				fail();
			}

			@Override
			public int getTimeoutMS() {
				return 100;
			}
		});

		u.checkReachability(offline, new ReachabilityListener() {
			@Override
			public void connectionActive(InetSocketAddress target, int pingMS) {
				fail();
			}

			@Override
			public void connectionTimedOut(InetSocketAddress target) {
				if (!SILENT)
					System.out.println(target + " not reachable, as expected");
			}

			@Override
			public int getTimeoutMS() {
				return 100;
			}
		});

		Thread.sleep(1000);
		u.dispose();
	}
}
