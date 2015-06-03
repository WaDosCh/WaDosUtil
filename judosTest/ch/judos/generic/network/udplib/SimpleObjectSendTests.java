package ch.judos.generic.network.udplib;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;

/**
 * @created 30.04.2012
 * @author Julian Schelker
 */
public class SimpleObjectSendTests implements UdpListener {

	private Udp4I	udp;
	private Object	received;
	private int		receivedPackges	= 0;
	private int		sendPackages		= 100;

	@Before
	public void setUp() throws Exception {
		this.udp = UdpLib.createDefault();
	}

	@Test
	public void testSendAndReceive() throws InterruptedException, SerializerException,
		IOException {
		int port = this.udp.getLocalPort();
		String sent = "Hallo Welt!";
		this.udp.addObjectListener(this);
		for (int i = 0; i < this.sendPackages; i++) {
			this.udp.sendObjectConfirmTo(sent, false, new InetSocketAddress("127.0.0.1", port));
			Thread.sleep(20);
		}
		assertEquals(this.received, sent);

		this.udp.removeObjectListener(this);
		Thread.sleep(20);
		System.out.println("Packages lost: " + (this.sendPackages - this.receivedPackges));
	}

	@After
	public void tearDown() {
		this.udp.dispose();
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		this.received = data;
		this.receivedPackges++;
	}
}
