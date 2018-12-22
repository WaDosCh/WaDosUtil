package ch.judos.generic.network.udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import junit.framework.TestCase;
import ch.judos.generic.network.udp.interfaces.Layer3Listener;
import ch.judos.generic.network.udp.interfaces.Udp3I;

/**
 * @since 04.07.2013
 * @author Julian Schelker
 */
public class Udp3Tests extends TestCase {

	public static final int PORT = 60000;
	boolean success;
	private Udp3I u;

	void assertArrayEquals(byte[] senddata, byte[] data) {
		for (int index = 0; index < data.length; index++)
			assertEquals(senddata[index], data[index]);
	}

	private void emptyData() {
		this.success = false;
		Layer3Listener listener = new Layer3Listener() {

			@Override
			public void receivedMsg(int type, byte[] data, InetSocketAddress from) {
				assertEquals(1, type);
				assertEquals(0, data.length);
				assertEquals(60000, from.getPort());
				synchronized (this) {
					Udp3Tests.this.success = true;
					notifyAll();
				}
			}
		};
		this.u.addListener(listener);

		try {
			this.u
				.sendDataTo(1, new byte[0], false, new InetSocketAddress("localhost", 60000));
		}
		catch (IOException e) {
			fail();
		}
		try {
			synchronized (listener) {
				listener.wait(1000);
			}
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(this.success);
		this.u.removeListener(listener);
	}

	public void excessiveData() {
		final byte[] senddata = new byte[24000];
		this.success = false;
		Layer3Listener listener = new Layer3Listener() {

			@Override
			public void receivedMsg(int type, byte[] data, InetSocketAddress from) {
				assertEquals(5, type);
				assertEquals(senddata.length, data.length);
				assertArrayEquals(senddata, data);
				synchronized (this) {
					Udp3Tests.this.success = true;
					notifyAll();
				}
			}
		};
		this.u.addListener(listener);

		try {
			this.u.sendDataTo(5, senddata, true, new InetSocketAddress("localhost", 60000));
		}
		catch (IOException e) {
			fail();
		}
		try {
			synchronized (listener) {
				listener.wait(1000);
			}
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(this.success);
		this.u.removeListener(listener);
	}

	@Override
	protected void setUp() throws Exception {
		@SuppressWarnings("resource")
		DatagramSocket s = new DatagramSocket(PORT);
		this.u = new Udp3(new Udp2(new Udp1(new Udp0Reader(s))));
	}

	@Override
	protected void tearDown() throws Exception {
		this.u.dispose();
	}

	private void someConfirmedData() {
		this.success = false;
		final byte[] senddata = new byte[]{0, 5, 100, -100, 127};
		Layer3Listener listener = new Layer3Listener() {

			@Override
			public void receivedMsg(int type, byte[] data, InetSocketAddress from) {
				assertEquals(5, type);
				assertEquals(senddata.length, data.length);
				assertArrayEquals(senddata, data);
				synchronized (this) {
					Udp3Tests.this.success = true;
					notifyAll();
				}
			}
		};
		this.u.addListener(listener);

		try {
			this.u.sendDataTo(5, senddata, true, new InetSocketAddress("localhost", 60000));
		}
		catch (IOException e) {
			fail();
		}
		try {
			synchronized (listener) {
				listener.wait(1000);
			}
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(this.success);
		this.u.removeListener(listener);
	}

	private void someData() {
		this.success = false;
		final byte[] senddata = new byte[]{0, 5, 100, -100, 127};
		Layer3Listener listener = new Layer3Listener() {

			@Override
			public void receivedMsg(int type, byte[] data, InetSocketAddress from) {
				assertEquals(5, type);
				assertEquals(senddata.length, data.length);
				assertArrayEquals(senddata, data);
				synchronized (this) {
					Udp3Tests.this.success = true;
					notifyAll();
				}
			}
		};
		this.u.addListener(listener);

		try {
			this.u.sendDataTo(5, senddata, false, new InetSocketAddress("localhost", 60000));
		}
		catch (IOException e) {
			fail();
		}
		try {
			synchronized (listener) {
				listener.wait(1000);
			}
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(this.success);
		this.u.removeListener(listener);
	}

	public void testAll() {
		emptyData();
		someData();
		someConfirmedData();
		excessiveData();
	}
}
