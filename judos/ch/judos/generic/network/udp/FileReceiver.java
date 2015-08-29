package ch.judos.generic.network.udp;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

import ch.judos.generic.data.Serializer;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.udp.interfaces.FileTransmissionHandler;
import ch.judos.generic.network.udp.interfaces.UdpFileTransferListener;
import ch.judos.generic.network.udp.model.FileDescription;
import ch.judos.generic.network.udp.model.FileIncomingTransmission;

/**
 * @since 14.07.2013
 * @author Julian Schelker
 */
public class FileReceiver {

	private FileTransmissionHandler fileHandler;
	private int packetSize;
	private HashMap<InetSocketAddress, FileIncomingTransmission> receiveTransfers;
	private Udp4 u;

	public FileReceiver(Udp4 udp4, int packetSize) {
		this.u = udp4;
		this.receiveTransfers = new HashMap<>();
		this.packetSize = packetSize;
	}

	private void checkTransferAndRequestFileHandler(byte[] data, InetSocketAddress from) {
		try {
			FileDescription fd = (FileDescription) Serializer.bytes2object(data);

			// if there's already an incoming transfer from that client
			if (this.receiveTransfers.containsKey(from)) {
				// deny and return
				this.u.sendFileMessage(Udp4.FileTransfer_DENIED, new byte[0], from);
				return;
			}
			UdpFileTransferListener listener = this.fileHandler
				.requestTransferFileListener(fd);
			File targetFile = this.fileHandler.requestFileTransmission(fd);
			if (targetFile == null) { // fileHandler has canceled transmission
				listener.transmissionDeniedAndCanceled();
				this.u.sendFileMessage(Udp4.FileTransfer_DENIED, new byte[0], from);
				return;
			}
			// otherwise start transfer
			listener.transmissionAcceptedAndStarted();
			this.u.sendFileMessage(Udp4.FileTransfer_ACCEPTED, new byte[0], from);

			FileIncomingTransmission inc = new FileIncomingTransmission(targetFile, fd,
				listener, this.packetSize, from);
			this.receiveTransfers.put(from, inc);

		}
		catch (SerializerException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveMsg(int type, byte[] data, InetSocketAddress from) {
		if (type == Udp4.FileTransfer_REQUEST_FILE_TRANSFER)
			checkTransferAndRequestFileHandler(data, from);
		else {
			FileIncomingTransmission inc = this.receiveTransfers.get(from);
			if (inc == null) {
				try {
					throw new RuntimeException("received fileMsg t=" + type
						+ " but no transfer ongoing for connection " + from);
				}
				catch (Exception e) {
					System.err.println("Warning: received fileMsg t=" + type
						+ " but no transfer ongoing for connection " + from);
					return;
				}
			}
			inc.receivePacketAndAnswer(type, data, this.u);
			if (inc.isFinished()) {
				inc.sendCompleteMsg(this.u);
				this.receiveTransfers.remove(from);
			}
		}
	}

	public void setFileHandler(FileTransmissionHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

}
