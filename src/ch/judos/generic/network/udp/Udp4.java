package ch.judos.generic.network.udp;

import static ch.judos.generic.network.udp.UdpConfig.out;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ch.judos.generic.data.Serializer;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.udp.interfaces.*;
import ch.judos.generic.network.udp.model.FileOutgoingTransmission;
import ch.judos.generic.network.udp.model.reachability.*;

/**
 * handles serializing and sending of objects, raw data, files
 * 
 * @since 11.07.2013
 * @author Julian Schelker
 */
public class Udp4 implements Layer3Listener, Udp4I {

	public static final int FileTransfer_ACCEPTED = -2;
	public static final int FileTransfer_COMPLETED = -5;
	public static final int FileTransfer_DENIED = -3;
	public static final int FileTransfer_REQUEST_DATA = -4;
	public static final int FileTransfer_REQUEST_FILE_TRANSFER = -1;

	/**
	 * these numbers are used as identifier for the udp messages<br>
	 * available are: 1-63
	 */
	private static final int TYPE_FILE = 3;

	private static final int TYPE_OBJECT = 1;
	private static final int TYPE_PING = 4;
	private static final int TYPE_RAW_DATA = 2;

	private FileReceiver fileReceiver;
	private FileSender fileSender;
	private HashMap<Integer, List<UdpListener>> listeners;
	private HashMap<ReachabilityRequest, CheckReachThread> reachabilityRequests;
	private Udp3I u;

	public Udp4(Udp3I u) {
		this.u = u;
		this.u.addListener(this);
		this.listeners = new HashMap<>();
		// -4 because 4 control bytes are used for the fileMessageType
		// (partNr/controlNr)
		this.fileSender = new FileSender(this, u.getMaxUnsplitPacketSize() - 4);
		this.fileReceiver = new FileReceiver(this, u.getMaxUnsplitPacketSize() - 4);
		this.reachabilityRequests = new HashMap<>();
	}

	@Override
	public void addDataListener(UdpListener listener) {
		addListener(listener, TYPE_RAW_DATA);
	}

	protected void addListener(UdpListener listener, int typeOfMessagesToReceive) {
		List<UdpListener> list = this.listeners.get(typeOfMessagesToReceive);
		if (list == null) {
			list = new ArrayList<>();
			this.listeners.put(typeOfMessagesToReceive, list);
		}
		list.add(listener);
	}

	@Override
	public void addObjectListener(UdpListener listener) {
		addListener(listener, TYPE_OBJECT);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp4I#checkReachability(java.net.InetSocketAddress,
	 *      ch.judos.generic.network.udp.interfaces.ReachabilityListener)
	 */
	@Override
	public void
		checkReachability(InetSocketAddress target, final ReachabilityListener listener) {
		byte[] data;
		try {
			ReachabilityRequest rr = new ReachabilityRequest(target);
			// the thread times out after some time and notifies the listener
			this.reachabilityRequests.put(rr, new CheckReachThread(this, rr, listener));
			data = Serializer.object2Bytes(rr);
			this.u.sendDataTo(TYPE_PING, data, false, target);
		}
		catch (SerializerException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp4I#dispose()
	 */
	@Override
	public void dispose() {
		this.fileSender.dispose();
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

	private void notifyListeners(int type, List<UdpListener> list, Object result,
		InetSocketAddress from) {
		if (list != null) {
			for (UdpListener l : list)
				l.receiveMsg(this, from, result);
		}
		else {
			new Exception("Warning: no listener for type " + type).printStackTrace();
		}
	}

	public void reachabilityReqTimedOut(ReachabilityRequest rr) {
		this.reachabilityRequests.remove(rr);
	}

	@Override
	public void receivedMsg(int type, byte[] packetData, InetSocketAddress from) {
		List<UdpListener> list = this.listeners.get(type);

		Object result = null;
		switch (type) {
			case TYPE_OBJECT :
				try {
					result = Serializer.bytes2object(packetData);
				}
				catch (SerializerException e) {
					e.printStackTrace();
				}
				break;
			case TYPE_RAW_DATA :
				result = packetData;
				break;
			case TYPE_FILE :
				int fileType = Serializer.bytes2int(packetData, 0);
				byte[] data = new byte[packetData.length - 4];
				System.arraycopy(packetData, 4, data, 0, data.length);
				out("in fileMsg t=" + fileType + " size=" + data.length);

				if (fileType < -1)
					this.fileSender.receiveMsg(fileType, data, from);
				else
					this.fileReceiver.receiveMsg(fileType, data, from);
				return;
			case TYPE_PING :
				try {
					Object o = Serializer.bytes2object(packetData);
					if (o instanceof ReachabilityRequest) {
						ReachabilityResponse rr = new ReachabilityResponse(
							(ReachabilityRequest) o);
						byte[] data1 = Serializer.object2Bytes(rr);
						this.u.sendDataTo(TYPE_PING, data1, true, from);
					}
					else if (o instanceof ReachabilityResponse) {
						ReachabilityRequest request = ((ReachabilityResponse) o).getRequest();
						CheckReachThread t = this.reachabilityRequests.remove(request);
						if (t != null)
							t.pingReceived();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return;
			default :
				new Exception("Unknown message type: " + type).printStackTrace();
				break;
		}
		notifyListeners(type, list, result, from);
	}

	@Override
	public void removeDataListener(UdpListener listener) {
		removeListener(listener, TYPE_RAW_DATA);
	}

	public void removeListener(UdpListener listener, int typeOfMessagesToReceive) {
		List<UdpListener> list = this.listeners.get(typeOfMessagesToReceive);
		if (list == null)
			return;
		list.remove(listener);
		if (list.isEmpty())
			this.listeners.remove(typeOfMessagesToReceive);
	}

	@Override
	public void removeObjectListener(UdpListener listener) {
		removeListener(listener, TYPE_OBJECT);
	}

	/**
	 * NOTE: system usage only
	 * 
	 * @param type
	 *            of file message
	 * @param data
	 * @param target
	 * @throws IOException
	 */
	public void sendFileMessage(int type, byte[] data, InetSocketAddress target)
		throws IOException {
		byte[] packet = new byte[data.length + 4];
		Serializer.int2bytes(packet, 0, type);
		System.arraycopy(data, 0, packet, 4, data.length);
		out("out FileMsg t=" + type + " size=" + data.length + " to:" + target);
		this.u.sendDataTo(TYPE_FILE, packet, type < 0, target);
	}

	@Override
	public void sendFileTo(File f, String description, InetSocketAddress to,
		UdpFileTransferListener fileListener) throws FileNotFoundException {
		this.fileSender.addToQueue(new FileOutgoingTransmission(f, description,
			this.fileSender.getPacketSize(), to, fileListener));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp4I#sendObjectConfirmTo(java.lang.Object,
	 *      boolean, java.net.InetSocketAddress)
	 */
	@Override
	public void sendObjectConfirmTo(Object obj, boolean confirmation, InetSocketAddress to)
		throws SerializerException, IOException {
		byte[] data = Serializer.object2Bytes(obj);
		this.u.sendDataTo(TYPE_OBJECT, data, confirmation, to);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.network.udp.interfaces.Udp4I#sendRawDataConfirmTo(byte[],
	 *      boolean, java.net.InetSocketAddress)
	 */
	@Override
	public void sendRawDataConfirmTo(byte[] data, boolean confirmation, InetSocketAddress to)
		throws SerializerException, IOException {
		this.u.sendDataTo(TYPE_RAW_DATA, data, confirmation, to);
	}

	@Override
	public void setFileHandler(FileTransmissionHandler fileHandler) {
		this.fileReceiver.setFileHandler(fileHandler);
	}

	@Override
	public Reachability getReachability(InetSocketAddress target, int timeoutMs) {
		return new ReachabilitySync(target, timeoutMs, this).waitUntilDone();
	}

	@Override
	public void addConnectionIssueListener(ConnectionIssueListener c) {
		this.u.addConnectionIssueListener(c);
	}

	@Override
	public void removeConnectionIssueListener(ConnectionIssueListener c) {
		this.u.removeConnectionIssueListener(c);
	}
}
