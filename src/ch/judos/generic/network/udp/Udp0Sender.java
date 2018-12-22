package ch.judos.generic.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @since 26.04.2012
 * @author Julian Schelker
 */
public class Udp0Sender {

	protected DatagramSocket ds;

	protected Udp0Sender(DatagramSocket ds) throws SocketException {
		this.ds = ds;
		ds.setReceiveBufferSize(UdpConfig.PACKET_SIZE_BYTES);
		ds.setSendBufferSize(UdpConfig.PACKET_SIZE_BYTES);
	}

	/**
	 * @return the local port this udp connection is established on
	 */
	public int getLocalPort() {
		return this.ds.getLocalPort();
	}

	/**
	 * @return the maximal packet size which can be sent
	 */
	public int getMaxPacketSize() {
		return UdpConfig.PACKET_SIZE_BYTES;
	}

	public void sendTo(byte[] data, InetSocketAddress dest) throws IOException {
		if (data.length > UdpConfig.PACKET_SIZE_BYTES)
			throw new IOException("Exceeded SendLimit: " + UdpConfig.PACKET_SIZE_BYTES
				+ " bytes of data");
		DatagramPacket packet = new DatagramPacket(data, data.length, dest);
		this.ds.send(packet);
		// UDP: research and improve throughput
		// try {
		// value based on gigabit network
		// Thread.sleep(0, 10000);
		// } catch (InterruptedException e) {}
	}

}
