package ch.judos.generic.network.udp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.LinkedList;

import ch.judos.generic.data.Serializer;
import ch.judos.generic.network.udp.Udp4;
import ch.judos.generic.network.udp.UdpConfig;
import ch.judos.generic.network.udp.interfaces.UdpFileTransferListener;

/**
 * @since 12.07.2013
 * @author Julian Schelker
 */
public class FileOutgoingTransmission {

	private int currentPos;

	private String description;
	private File file;
	private UdpFileTransferListener fileListener;
	private long lastSentLastPart;
	private long listenerLastUpdate;

	private LinkedList<Integer> missing;

	private int packetSize;

	private int parts;

	private RandomAccessFile rFile;

	private int speed;

	private SpeedMeasurement speedMeasure;

	private Status status;

	private InetSocketAddress to;

	public FileOutgoingTransmission(File file, String description, int packetSize,
		InetSocketAddress to, UdpFileTransferListener fileListener)
		throws FileNotFoundException {
		this.file = file;

		int packets = (int) (file.length() / packetSize);
		if (file.length() % packetSize > 0)
			packets++;

		this.parts = packets;
		this.packetSize = packetSize;
		this.description = description;
		this.to = to;
		this.fileListener = fileListener;
		this.status = Status.WAITING_FOR_ACCEPTANCE;
		this.missing = new LinkedList<>();
		this.speed = UdpConfig.SEND_SPEED_PACKETS;
		this.currentPos = 0;
		this.rFile = new RandomAccessFile(file, "r");
		this.listenerLastUpdate = System.currentTimeMillis();
		this.speedMeasure = new SpeedMeasurement();
	}

	public void acceptedAndStart() {
		this.status = Status.RUNNING;
		if (this.fileListener != null)
			this.fileListener.transmissionAcceptedAndStarted();
	}

	public void denyTransmissionAndStop() {
		this.status = Status.STOPPED;
		if (this.fileListener != null)
			this.fileListener.transmissionDeniedAndCanceled();
	}

	public FileDescription getFileDescription() {
		return new FileDescription(this.file.length(), this.parts,
			this.file.getAbsolutePath(), this.description);
	}

	private byte[] getPart(int partNr) throws IOException {
		this.rFile.seek((long) partNr * this.packetSize);
		int size = this.packetSize;
		if (partNr == this.parts - 1)
			size = (int) (this.file.length() % this.packetSize);

		byte[] data = new byte[size];
		this.rFile.readFully(data, 0, size);
		return data;
	}

	public Status getStatus() {
		return this.status;
	}

	public InetSocketAddress getTarget() {
		return this.to;
	}

	public void requestParts(byte[] data) {
		// int lastReceivedPart = Serializer.bytes2int(data, 0);
		// UDP: calculate lost ratio and adjust speed
		synchronized (this.missing) {
			for (int i = 4; i < data.length; i += 4)
				this.missing.add(Serializer.bytes2int(data, i));
		}
	}

	public void sendFileMessages(Udp4 u) {
		int remainingPackets = this.speed;
		synchronized (this.missing) {
			while (remainingPackets > 0 && this.missing.size() > 0) {
				int partNr = this.missing.remove();
				try {
					u.sendFileMessage(partNr, getPart(partNr), this.to);
					remainingPackets--;
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (this.currentPos < this.parts) {
			while (remainingPackets > 0) {
				try {
					int partNr = this.currentPos++;
					u.sendFileMessage(partNr, getPart(partNr), this.to);
					remainingPackets--;
					if (this.currentPos == this.parts) {
						this.lastSentLastPart = System.currentTimeMillis();
						break;
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else { // awaiting request messages or complete confirmation
			long delay = System.currentTimeMillis() - this.lastSentLastPart;
			if (delay > UdpConfig.RESEND_FINAL_PACKET_MS) {
				int partNr = this.parts - 1;
				try {
					u.sendFileMessage(partNr, getPart(partNr), this.to);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				this.lastSentLastPart = System.currentTimeMillis();
			}
		}
		updateProgressListener();
	}

	public void setToCompleted() {
		this.status = Status.COMPLETED;
		if (this.fileListener != null)
			this.fileListener.transmissionCompleted();
	}

	private void updateProgressListener() {
		if (this.fileListener == null)
			return;
		int ms = this.fileListener.getUpdateEveryMS();
		long delay = System.currentTimeMillis() - this.listenerLastUpdate;
		if (delay >= ms) {
			this.listenerLastUpdate += ms;
			long transmitted = (this.currentPos - this.missing.size()) * this.packetSize;
			float curSpeed = this.speedMeasure.update(transmitted);
			float percentage = (float) ((double) transmitted / this.file.length() * 100);
			this.fileListener.transmissionProgress(percentage, curSpeed, transmitted,
				this.file.length());
		}
	}

	public enum Status {
		COMPLETED, RUNNING, STOPPED, WAITING_FOR_ACCEPTANCE;
	}
}