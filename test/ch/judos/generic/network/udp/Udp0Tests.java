package ch.judos.generic.network.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import junit.framework.TestCase;
import ch.judos.generic.network.udp.interfaces.Layer0Listener;

/**
 * @since 04.07.2013
 * @author Julian Schelker
 */
public class Udp0Tests extends TestCase {

	public static final int PORT = 60000;
	private Udp0Reader u;
	boolean success;

	@Override
	protected void setUp() throws Exception {
		@SuppressWarnings("resource")
		DatagramSocket ds = new DatagramSocket(PORT);
		this.u = new Udp0Reader(ds);
	}

	@Override
	protected void tearDown() throws Exception {
		this.u.dispose();
	}

	public void testEmptyByteData() {
		emptyData();
		someData();
		excessiveData();
	}

	private void someData() {
		this.success = false;
		final byte[] senddata = new byte[]{0, 5, 100, -100, 127};
		Layer0Listener listener = new Layer0Listener() {
			@Override
			public void receivedFrom(byte[] data, InetSocketAddress socketAddress) {
				assertEquals(senddata.length, data.length);
				assertArrayEquals(senddata, data);
				synchronized (this) {
					Udp0Tests.this.success = true;
					notifyAll();
				}
			}
		};
		this.u.addListener(listener);

		try {
			this.u.sendTo(senddata, new InetSocketAddress("localhost", 60000));
		}
		catch (IOException e) {
			fail();
		}
		try {
			synchronized (this) {
				this.wait(1000);
			}
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(this.success);
		this.u.removeListener(listener);
	}

	private void emptyData() {
		this.success = false;
		Layer0Listener listener = new Layer0Listener() {
			@Override
			public void receivedFrom(byte[] data, InetSocketAddress socketAddress) {
				assertEquals(0, data.length);
				assertEquals(60000, socketAddress.getPort());
				synchronized (this) {
					Udp0Tests.this.success = true;
					notifyAll();
				}
			}
		};
		this.u.addListener(listener);

		try {
			this.u.sendTo(new byte[0], new InetSocketAddress("localhost", 60000));
		}
		catch (IOException e) {
			fail();
		}
		try {
			synchronized (this) {
				this.wait(1000);
			}
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(this.success);
		this.u.removeListener(listener);
	}

	public void excessiveData() {
		final byte[] senddata = new byte[UdpConfig.PACKET_SIZE_BYTES * 2];
		try {
			this.u.sendTo(senddata, new InetSocketAddress("localhost", 60000));
			fail();
		}
		catch (IOException e) {
			// supposed to throw this exception
		}
	}

	void assertArrayEquals(byte[] senddata, byte[] data) {
		for (int index = 0; index < data.length; index++)
			assertEquals(senddata[index], data[index]);
	}
}
