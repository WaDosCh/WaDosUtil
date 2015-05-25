package ch.judos.generic.network.udp;

import static ch.judos.generic.network.udp.UdpConfig.out;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import ch.judos.generic.network.udp.interfaces.Layer2Listener;
import ch.judos.generic.network.udp.interfaces.Layer3Listener;
import ch.judos.generic.network.udp.interfaces.Udp2I;
import ch.judos.generic.network.udp.interfaces.Udp3I;
import ch.judos.generic.network.udp.model.BigPacketRec;
import ch.judos.generic.network.udp.model.BigPacketSend;
import ch.judos.generic.network.udp.model.IAddressAndId;

/**
 * handles big objects<br>
 * packge type numbers:<br>
 * flag +64 big packet: split into pieces
 * 
 * 
 * UDP: ConnectionIssueListener in Udp2 should be used and offered further on
 * 
 * @since 08.07.2013
 * @author Julian Schelker
 */
public class Udp3 implements Layer2Listener, Udp3I {

	protected HashMap<InetSocketAddress, Short>		idsBigPacket;
	private ArrayList<Layer3Listener>				listeners;
	private HashMap<IAddressAndId, BigPacketRec>	receiveBig;
	protected Udp2I									u;

	public Udp3(Udp2I u) {
		this.u = u;
		this.u.addListener(this);
		this.idsBigPacket = new HashMap<>();
		this.listeners = new ArrayList<>();
		this.receiveBig = new HashMap<>();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp3I#addListener(ch.judos.generic.network.udp.interfaces.Layer3Listener)
	 */
	@Override
	public void addListener(Layer3Listener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void dispose() {
		this.u.dispose();
	}

	@Override
	protected void finalize() throws Throwable {
		this.dispose();
	}

	private short getIdForNextBigPacket(InetSocketAddress to) {
		Short s = this.idsBigPacket.get(to);
		if (s == null)
			s = 0;
		this.idsBigPacket.put(to, (short) (s + 1));
		return s;
	}

	@Override
	public int getLocalPort() {
		return this.u.getLocalPort();
	}

	/**
	 * @return the maximal amount of bytes that can be sent at once (big packets
	 *         are forced to be confirmed, because it is very likely that one
	 *         part is lost)
	 */
	@Override
	public int getMaxConfirmedPacketSize() {
		return (this.u.getMaxPacketSize() - 6) * Short.MAX_VALUE;
	}

	/**
	 * @return the maximal amount of bytes that can be sent at once without
	 *         splitting the packet into parts. This is also the biggest
	 *         possible unconfirmed packet, since confirmation for big packets
	 *         is forced.
	 */
	@Override
	public int getMaxUnsplitPacketSize() {
		return this.u.getMaxPacketSize();
	}

	private void notifyListeners(int type, byte[] packetData, InetSocketAddress from) {
		for (Layer3Listener l : this.listeners)
			l.receivedMsg(type, packetData, from);
	}

	@Override
	public void receivedMsg(int type, byte[] packetData, InetSocketAddress from) {
		out("in t=" + type + " size=" + packetData.length + " from:" + from);
		if (type < 64) {
			notifyListeners(type, packetData, from);
			return;
		}

		IAddressAndId hashObject = BigPacketRec.getHashObject(packetData, from);
		BigPacketRec r = this.receiveBig.get(hashObject);
		if (r == null) {
			r = new BigPacketRec();
			this.receiveBig.put(hashObject, r);
		}
		int index = r.addPart(packetData);
		out("  bigPacket id: " + r.getId() + " part: " + index + "/" + r.getParts());
		if (r.isFinished()) {
			this.receiveBig.remove(hashObject);
			notifyListeners(type - 64, r.getData(), from);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp3I#removeListener(ch.judos.generic.network.udp.interfaces.Layer3Listener)
	 */
	@Override
	public void removeListener(Layer3Listener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * (non-Javadoc)
	 */
	@Override
	public void sendDataTo(int type, byte[] data, boolean confirmation, InetSocketAddress to)
		throws IOException {
		if (type < 1 || type > 63)
			throw new RuntimeException(
				"Type must be between 1 and 63 (inclusive) the rest is system reserved.");
		int size = data.length;
		out("out t=" + type + " size=" + data.length + " to:" + to);
		if (size <= this.u.getMaxPacketSize())
			this.u.sendDataTo(type, data, confirmation, to);
		else
			splitAndSendData(type, data, to);
	}

	private void splitAndSendData(int type, byte[] data, InetSocketAddress to)
		throws IOException {
		short id = getIdForNextBigPacket(to);
		BigPacketSend big = new BigPacketSend(id, data, this.u.getMaxPacketSize());
		byte[][] packets = big.getPackets();
		out("  data split into " + packets.length + " part");
		for (byte[] packet : packets)
			this.u.sendDataTo(type + 64, packet, true, to);
	}
}
