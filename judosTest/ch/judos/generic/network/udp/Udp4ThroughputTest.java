package ch.judos.generic.network.udp;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;
import ch.judos.generic.control.TimerJS;
import ch.judos.generic.network.udp.interfaces.UdpListener;

/**
 * @since 11.07.2013
 * @author Julian Schelker
 */
public class Udp4ThroughputTest extends TestCase implements UdpListener,
	ConnectionIssueListener {

	public static final boolean SILENT = true;

	private static final int DATA = 5;
	private static final int MB = 1024 * 1024;
	public static final int PORT = 60000;
	private TimerJS t;
	private Udp4 u;
	private AtomicBoolean finished;

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		assertEquals(this.u, source);
		assertTrue(data instanceof byte[]);
		byte[] x = (byte[]) data;
		assertEquals(DATA * MB, x.length);
		for (int i = 0; i < DATA * MB; i++)
			assertEquals((byte) i, x[i]);
		if (!SILENT)
			System.out.println("message received " + this.t.getS() + "s " + this.t.getMS()
				% 1000 + " ms");
		this.finished.set(true);
		synchronized (this.finished) {
			this.finished.notify();
		}
	}

	@Override
	protected void setUp() throws Exception {
		@SuppressWarnings("resource")
		DatagramSocket ds = new DatagramSocket(PORT);
		Udp2 u2 = new Udp2(new Udp1(new Udp0Reader(ds)));
		this.u = new Udp4(new Udp3(u2));
		this.u.addDataListener(this);
		u2.addConnectionIssueListener(this);
		// displays amount of packages in queue to be resend/confirmed
		// new Udp2Monitor(u2);

	}

	@Override
	protected void tearDown() throws Exception {
		this.u.dispose();
	}

	public void testThroughput() {
		this.finished = new AtomicBoolean(false);
		byte[] data = new byte[DATA * MB];
		for (int i = 0; i < DATA * MB; i++)
			data[i] = (byte) i;

		try {
			this.t = new TimerJS("sending " + DATA + "MB");
			this.u.sendRawDataConfirmTo(data, false, new InetSocketAddress("localhost", PORT));
			data = null;
			if (!SILENT)
				this.t.printMS();
			this.t.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		synchronized (this.finished) {
			try {
				this.finished.wait(5 * 1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertTrue(this.finished.get());
	}

	@Override
	public void connectionIsBroken(InetSocketAddress destination) {
		// not needed
	}

	@Override
	public void connectionReconnected(InetSocketAddress from) {
		// not needed
	}
}
