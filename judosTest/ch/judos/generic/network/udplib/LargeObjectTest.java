package ch.judos.generic.network.udplib;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.udp.ConnectionIssueListener;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;

/**
 * @author Julian Schelker
 */
public class LargeObjectTest implements UdpListener, ConnectionIssueListener {

	private Udp4I udp;
	private int port;
	private Object firstReceived;
	private InetSocketAddress target;
	private int receivedObjects = 0;
	private int sendObjects = 10;

	@Before
	public void setUp() throws Exception {
		this.udp = UdpLib.createDefault();
		this.port = this.udp.getLocalPort();
		this.target = new InetSocketAddress("127.0.0.1", this.port);
		this.udp.addConnectionIssueListener(this);
	}

	@Test
	public void testLargeString() throws InterruptedException, SerializerException,
		IOException {
		String send = genLargeString();
		this.udp.addObjectListener(this);
		Thread.sleep(20);
		for (int i = 0; i < this.sendObjects; i++) {
			this.udp.sendObjectConfirmTo(send, false, this.target);
			Thread.sleep(50);
		}
		synchronized (this) {
			this.wait(250);
		}
		assertEquals(send, this.firstReceived);
		System.out.println("Lost (big) Packges: " + (this.sendObjects - this.receivedObjects)
			+ " / " + this.sendObjects
			+ " (don't care about losage because no confirmation and few time is allowed)");
	}

	@After
	public void tearDown() {
		this.udp.dispose();
	}

	public String genLargeString() {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < 5000; i++) {
			b.append("123456789 ");
		}
		return b.toString();
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		if (this.firstReceived == null) {
			this.firstReceived = data;
			synchronized (this) {
				this.notify();
			}
		}
		this.receivedObjects++;
	}

	@Override
	public void connectionIsBroken(InetSocketAddress destination) {
		// do nothing
	}

	@Override
	public void connectionReconnected(InetSocketAddress from) {
		// do nothing
	}

}
