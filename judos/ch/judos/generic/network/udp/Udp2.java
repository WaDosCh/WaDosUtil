package ch.judos.generic.network.udp;

import static ch.judos.generic.network.udp.UdpConfig.out;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import ch.judos.generic.data.RandomJS;
import ch.judos.generic.data.Serializer;
import ch.judos.generic.math.ConvertNumber;
import ch.judos.generic.network.udp.interfaces.Layer1Listener;
import ch.judos.generic.network.udp.interfaces.Layer2Listener;
import ch.judos.generic.network.udp.interfaces.Udp1I;
import ch.judos.generic.network.udp.interfaces.Udp2I;
import ch.judos.generic.network.udp.model.DupFilterOnConnection;
import ch.judos.generic.network.udp.model.Packet2A;
import ch.judos.generic.network.udp.model.Packet2ResendConfirmed;

/**
 * handles confirmation of messages<br>
 * types:<br>
 * flag +128 = confirmation required <br>
 * nr 0 = special confirmation message<br>
 * <br>
 * memory in msg: NrOfMsg(int) , data(bytes...)<br>
 * the Nr is 0 for unconfirmed messages
 * 
 * @since 04.07.2013
 * @author Julian Schelker
 */
public class Udp2 implements Layer1Listener, Runnable, Udp2I {
	private DupFilterOnConnection						confirmedPacket;
	private HashMap<InetSocketAddress, Long>		connectionIssue;
	private ArrayList<ConnectionIssueListener>	connectionIssueListeners;
	private List<Layer2Listener>						listeners;
	private HashMap<InetSocketAddress, Integer>	nextPacketNr;
	private DupFilterOnConnection						receiveFilter;
	public PriorityQueue<Packet2A>					resendPackets;
	private boolean										running;
	private Thread											thread;
	private Udp1I											u;

	// UDP: use a cach to confirm packets
	// private HashMap<InetSocketAddress, Packet2CacheConfirmation>
	// cachConfirmationPackets;

	public Udp2(Udp1I u) {
		this.u = u;
		this.u.addListener(this);
		// this.cachConfirmationPackets = new HashMap<InetSocketAddress,
		// Packet2CacheConfirmation>();
		this.listeners = new ArrayList<>();
		this.resendPackets = new PriorityQueue<>();
		this.nextPacketNr = new HashMap<>();
		this.connectionIssue = new HashMap<>();
		this.receiveFilter = new DupFilterOnConnection();
		this.confirmedPacket = new DupFilterOnConnection();
		this.connectionIssueListeners = new ArrayList<>();
		this.thread = new Thread(this, "Udp2ResendThread");
		this.thread.setDaemon(false);
		this.running = true;
		this.thread.start();
	}

	public void addConnectionIssueListener(ConnectionIssueListener c) {
		this.connectionIssueListeners.add(c);
	}

	public void removeConnectionIssueListener(ConnectionIssueListener c) {
		this.connectionIssueListeners.remove(c);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp2I#addListener(ch.judos.generic.network.udp.interfaces.Layer2Listener)
	 */
	@Override
	public void addListener(Layer2Listener listener) {
		this.listeners.add(listener);
	}

	// Order of 3 actions is important!
	private void confirmFilterDuplicatesAndNotifyListeners(int type, byte[] data,
		InetSocketAddress from, int nr) {
		/*
		 * confirm again always (might be confirmed twice, if a confirmation is
		 * lost)
		 */
		confirmPacketReceived(nr, from);
		if (this.receiveFilter.hit(from, nr))
			notifyListeners(type, data, from);
	}

	private void confirmPacketReceived(int nr, InetSocketAddress from) {
		// UDP: might be cashed in order to increase performance (send back
		// confirmation for multiple packets at once, needs to be good with
		// timeout for resending packets)

		// UDP: needs still to be added in run(), such that cache is updated
		// correctly
		// synchronized (this.cachConfirmationPackets) {
		// Packet2CacheConfirmation p = this.cachConfirmationPackets.get(from);
		// // no cache yet
		// if (p == null) {
		// p = new Packet2CacheConfirmation(from, nr);
		// this.cachConfirmationPackets.put(from, p);
		// synchronized(this.resendPackets){
		// this.resendPackets.add(p);
		// }
		// }
		// else {
		// synchronized (p) {
		// p.addConfirms(nr);
		// }
		// }
		// }

		out("out confirmation " + nr);
		byte[] data = new byte[4];
		Serializer.int2bytes(data, 0, nr);
		try {
			sendDataToUnchecked(0, data, false, from);
		}
		catch (IOException e) {
			// do nothing, packet will arrive again and confirmation is tried
			// again
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp2I#dispose()
	 */
	@Override
	public void dispose() {
		this.u.dispose();
		this.resendPackets.clear();
		this.running = false;
		synchronized (this.resendPackets) {
			this.resendPackets.notify();
		}
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
	 * @see ch.judos.generic.network.udp.interfaces.Udp2I#getMaxPacketSize()
	 */
	@Override
	public int getMaxPacketSize() {
		// an integer is added to mark the number of this packet
		return this.u.getMaxPacketSize() - 5;
	}

	private int getNextPacketNr(InetSocketAddress dest) {
		Integer i = this.nextPacketNr.get(dest);
		if (i == null)
			i = UdpConfig.FIRST_PACKET_NR;
		this.nextPacketNr.put(dest, i + 1);
		return i;
	}

	private void memorizeConfirmationsData(byte[] data, InetSocketAddress from) {
		for (int i = 0; i < data.length; i += 4) {
			int confirmedNr = Serializer.bytes2int(data, i);
			this.confirmedPacket.hit(from, confirmedNr);
			out("in " + confirmedNr + " was confirmed");
		}
	}

	private void memorizePacketToResendAfterTimeout(Packet2ResendConfirmed packet) {
		synchronized (this.resendPackets) {
			this.resendPackets.add(packet);
			this.resendPackets.notify();
		}
	}

	private void notifyListeners(int type, byte[] packetData, InetSocketAddress from) {
		for (Layer2Listener listener : this.listeners)
			listener.receivedMsg(type, packetData, from);
	}

	private void notifyListenersAboutBrokenConnection(InetSocketAddress destination) {
		for (ConnectionIssueListener l : this.connectionIssueListeners)
			l.connectionIsBroken(destination);
		if (this.connectionIssueListeners.isEmpty())
			System.err.println("Experienced problems with connection " + destination
				+ " but no connectionIssueListener is set for Udp2 object");
	}

	private void notifyListenersAboutReconnectedConnection(InetSocketAddress from) {
		for (ConnectionIssueListener l : this.connectionIssueListeners)
			l.connectionReconnected(from);
		if (this.connectionIssueListeners.isEmpty())
			System.err.println("connection reconnected: " + from
				+ " but no connectionIssueListener is set for Udp2 object");
	}

	@Override
	@SuppressWarnings("all")
	public void receivedMsg(int type, byte[] packetData, InetSocketAddress from) {
		byte[] data = new byte[packetData.length - 5];
		System.arraycopy(packetData, 5, data, 0, data.length);
		out("in t=" + type + " size=" + packetData.length + " from:" + from);
		Object removed = null;
		synchronized (this.connectionIssue) {
			removed = this.connectionIssue.remove(from);
		}
		if (removed != null)
			notifyListenersAboutReconnectedConnection(from);
		if (type == 128) { //flash duplicate filter
			type = ConvertNumber.unsignedByte2Int(packetData[0]);
			this.receiveFilter.resetForConnection(from);
		}
		if (type == 0)
			memorizeConfirmationsData(data, from);
		else if (type>0 && type < 128)
			notifyListeners(type, data, from);
		else { //confirmed packets
			int nr = Serializer.bytes2int(packetData, 1);
			confirmFilterDuplicatesAndNotifyListeners(type - 128, data, from, nr);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp2I#removeListener(ch.judos.generic.network.udp.interfaces.Layer2Listener)
	 */
	@Override
	public void removeListener(Layer2Listener listener) {
		this.listeners.remove(listener);
	}

	@SuppressWarnings(value = {"all"})
	private void reportConnectionIssues(InetSocketAddress destination) {
		long lastIssue = 0;
		boolean hadConnectionIssueBefore = false;
		synchronized (this.connectionIssue) {
			// get base information
			hadConnectionIssueBefore = this.connectionIssue.containsKey(destination);
			if (hadConnectionIssueBefore)
				lastIssue = System.currentTimeMillis() - this.connectionIssue.get(destination);
			// update values
			this.connectionIssue.put(destination, System.currentTimeMillis());
		}
		// report
		if (lastIssue >= UdpConfig.REPORT_CONNECTION_ISSUE_EVERY_MS
			&& (UdpConfig.REPORT_CONNECTION_ISSUE_EVERY_MS > 0 || !hadConnectionIssueBefore)) {
			notifyListenersAboutBrokenConnection(destination);
		}
	}

	@Override
	public void run() {
		while (this.running) {
			synchronized (this.resendPackets) {
				if (this.resendPackets.size() == 0) {
					try {
						this.resendPackets.wait();
					}
					catch (InterruptedException e) {
						System.err.println("Error while waiting for next sending period:");
						e.printStackTrace();
					}
				}
				else {
					Packet2A x = this.resendPackets.peek();
					if (x.getResendOn() > System.currentTimeMillis()) {
						try {
							this.resendPackets.wait(x.getResendOn() - System.currentTimeMillis());
						}
						catch (InterruptedException e) {
							System.err.println("Error while resending packet:");
							e.printStackTrace();
						}
					}
					else {
						x = this.resendPackets.poll();
						if (!x.needsConfirmation()
							|| this.confirmedPacket.check(x.getDestination(), x.getNr())) {
							try {
								send(x);
							}
							catch (IOException e) {
								System.err.println("Error while sending packet:");
								e.printStackTrace();
							}
							x.wasResentNow();
							if (x.hasConnectionIssues())
								reportConnectionIssues(x.getDestination());

							if (x.needsConfirmation())
								this.resendPackets.add(x);
						}
						// if this packet was hit as confirmed, it will not be
						// queued up again
					}
				}
			}
		}
	}

	private void send(Packet2A data) throws IOException {
		out("out t=" + data.getType() + " size=" + data.getSendData().length + " to "
			+ data.getDestination());
		if (UdpConfig.DEBUG_BEHAVIOUR) {
			if (RandomJS.getDouble(1) < 0.333) {
				out("   ** DEBUG_BEHAVIOUR: packet not sent");
				return;
			}
		}
		this.u.sendDataTo(data.getType(), data.getSendData(), data.getDestination());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp2I#sendDataTo(int, byte[],
	 *      boolean, java.net.InetSocketAddress)
	 */
	@Override
	public void sendDataTo(int type, byte[] data, boolean confirmation, InetSocketAddress dest)
		throws IOException {
		if (type < 1 || type > 127)
			throw new IOException("invalid type Nr, only 1-127 are allowed for free use");
		sendDataToUnchecked(type, data, confirmation, dest);
	}

	@SuppressWarnings("all")
	private void sendDataToUnchecked(int type, byte[] data, boolean confirmation,
		InetSocketAddress dest) throws IOException {
		byte[] sendData = new byte[data.length + 5];
		System.arraycopy(data, 0, sendData, 5, data.length);
		int nr = -1;
		if (confirmation) {
			nr = getNextPacketNr(dest);
			type += 128;
			if (nr==0) { //reset duplicate filter on receiver side
				sendData[0] = ConvertNumber.int2UnsignedByte(type);
				type = 128;
			}
			Serializer.int2bytes(sendData, 1, nr);
		}
		Packet2ResendConfirmed packet = new Packet2ResendConfirmed(type, sendData, dest, nr);
		send(packet);
		if (confirmation)
			memorizePacketToResendAfterTimeout(packet);
	}

}