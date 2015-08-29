package ch.judos.generic.network.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import ch.judos.generic.network.udp.interfaces.Layer0Listener;

/**
 * @since 27.04.2012
 * @author Julian Schelker
 */
public class Udp0Reader extends Udp0Sender implements Runnable {

	protected ArrayList<Layer0Listener> listeners;
	protected Thread reader;
	protected boolean readingRunning;

	public Udp0Reader(DatagramSocket ds) throws SocketException {
		super(ds);
		this.listeners = new ArrayList<>();
		this.readingRunning = true;
		this.reader = new Thread(this, "(UdpLayer0Reader)");
		this.reader.setDaemon(false);
		this.reader.start();
	}

	/**
	 * @param listener
	 */
	public void addListener(Layer0Listener listener) {
		this.listeners.add(listener);
	}

	@SuppressWarnings("deprecation")
	protected void checkDisposed() {
		this.readingRunning = false;
		// close socket, causes SocketException in reader
		this.ds.close();
		// check that reader has stopped
		int timeOutMS = 1000;
		long startTimeMS = System.currentTimeMillis();
		while (this.reader.isAlive()) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				// do nothing
			}
			// timeout after some time if reader is still alive
			if (System.currentTimeMillis() - startTimeMS > timeOutMS)
				break;
		}
		if (this.reader.isAlive())
			this.reader.stop(); // force shutdown
	}

	/**
	 * asynchronous shutdown of udp connection
	 */
	public void dispose() {
		Runnable r = () -> checkDisposed();
		Thread t = new Thread(r, "Shutdown Udp0");
		t.start();
	}

	@Override
	protected void finalize() throws Throwable {
		this.dispose();
	}

	private void received(InetSocketAddress socketAddress, byte[] data) {
		for (Layer0Listener l : this.listeners)
			l.receivedFrom(data, socketAddress);
	}

	public void removeListener(Layer0Listener toRemove) {
		this.listeners.remove(toRemove);
	}

	@Override
	public void run() {
		DatagramPacket packet = new DatagramPacket(new byte[UdpConfig.PACKET_SIZE_BYTES],
			UdpConfig.PACKET_SIZE_BYTES);
		while (this.readingRunning) {
			try {
				this.ds.receive(packet);
				byte[] data = new byte[packet.getLength()];
				System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());
				received((InetSocketAddress) packet.getSocketAddress(), data);
			}
			catch (Exception e) {
				if (!this.readingRunning)
					break;
				e.printStackTrace();
			}
			packet.setData(new byte[UdpConfig.PACKET_SIZE_BYTES]);
		}
	}

}
