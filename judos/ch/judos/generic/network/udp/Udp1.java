package ch.judos.generic.network.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import ch.judos.generic.math.ConvertNumber;
import ch.judos.generic.network.udp.interfaces.Layer0Listener;
import ch.judos.generic.network.udp.interfaces.Layer1Listener;
import ch.judos.generic.network.udp.interfaces.Udp1I;

/**
 * handles only message type nrs
 * 
 * @since 04.07.2013
 * @author Julian Schelker
 */
public class Udp1 implements Layer0Listener, Udp1I {

	private List<Layer1Listener> listeners;
	private Udp0Reader u;

	public Udp1(Udp0Reader u) {
		this.u = u;
		this.u.addListener(this);
		this.listeners = new ArrayList<>();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp1I#addListener(ch.judos.generic.network.udp.interfaces.Layer1Listener)
	 */
	@Override
	public void addListener(Layer1Listener listener) {
		this.listeners.add(listener);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp1I#dispose()
	 */
	@Override
	public void dispose() {
		this.u.dispose();
	}

	@Override
	protected void finalize() throws Throwable {
		this.dispose();
	}

	@Override
	public int getLocalPort() {
		return this.u.getLocalPort();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp1I#getMaxPacketSize()
	 */
	@Override
	public int getMaxPacketSize() {
		// 1 bit is reserved for typeNr of Packet
		return this.u.getMaxPacketSize() - 1;
	}

	@Override
	public void receivedFrom(byte[] data, InetSocketAddress from) {
		int type = ConvertNumber.unsignedByte2Int(data[0]);
		byte[] packetData = new byte[data.length - 1];
		System.arraycopy(data, 1, packetData, 0, data.length - 1);
		for (Layer1Listener listener : this.listeners)
			listener.receivedMsg(type, packetData, from);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp1I#removeListener(ch.judos.generic.network.udp.interfaces.Layer1Listener)
	 */
	@Override
	public void removeListener(Layer1Listener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp1I#sendDataTo(int,
	 *      byte[], java.net.InetSocketAddress)
	 */
	@Override
	public void sendDataTo(int type, byte[] data, InetSocketAddress dest) throws IOException {
		byte[] sendData = new byte[data.length + 1];
		sendData[0] = ConvertNumber.int2UnsignedByte(type);
		System.arraycopy(data, 0, sendData, 1, data.length);
		this.u.sendTo(sendData, dest);
	}

}
