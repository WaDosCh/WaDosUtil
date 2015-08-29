package ch.judos.generic.network.udp.model;

import static ch.judos.generic.network.udp.UdpConfig.out;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.HashSet;

import ch.judos.generic.data.Serializer;
import ch.judos.generic.network.udp.Udp4;
import ch.judos.generic.network.udp.UdpConfig;
import ch.judos.generic.network.udp.interfaces.UdpFileTransferListener;

/**
 * @since 14.07.2013
 * @author Julian Schelker
 */
public class FileIncomingTransmission {

	private InetSocketAddress from;
	private int lastPacket;
	private long lastRequestedParts;
	private UdpFileTransferListener listener;
	private long listenerLastUpdate;
	private HashSet<Integer> missing;
	private int packetSize;
	private int parts;
	private int partsReceived;
	private HashSet<Integer> requestedAlready;
	private RandomAccessFile rFile;
	private SpeedMeasurement speedMeasure;
	private long totalSize;

	public FileIncomingTransmission(File file, FileDescription fd,
		UdpFileTransferListener listener, int packetSize, InetSocketAddress from) {
		try {
			this.rFile = new RandomAccessFile(file, "rw");
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.listener = listener;
		this.packetSize = packetSize;
		this.parts = fd.getParts();
		this.totalSize = fd.getLength();
		this.partsReceived = 0;
		this.lastPacket = -1;
		this.missing = new HashSet<>();
		this.requestedAlready = new HashSet<>();
		this.lastRequestedParts = System.currentTimeMillis();
		this.from = from;
		this.listenerLastUpdate = System.currentTimeMillis();
		this.speedMeasure = new SpeedMeasurement();
	}

	public boolean isFinished() {
		return this.partsReceived == this.parts;
	}

	public void receivePacketAndAnswer(int partNr, byte[] data, Udp4 u) {
		boolean save = true;
		out("receivedFilePart: " + partNr);

		if (partNr > this.lastPacket) {
			for (int i = this.lastPacket + 1; i < partNr; i++)
				this.missing.add(i);
			this.lastPacket = partNr;
		}
		else if (!this.missing.remove(partNr) && !this.requestedAlready.remove(partNr))
			save = false;

		if (save) {
			try {
				savePart(partNr, data);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		requestPartsIfNeeded(u);
	}

	private void requestPartsIfNeeded(Udp4 u) {
		int totalMissing = this.missing.size() + this.requestedAlready.size();
		long delay = System.currentTimeMillis() - this.lastRequestedParts;
		out("missing: " + this.missing.size() + " requestedOnce:"
			+ this.requestedAlready.size() + "  d:" + delay + " ms");
		if (this.missing.size() >= UdpConfig.MINIMAL_AMOUNT_REQUEST
			|| (totalMissing > 0 && delay > UdpConfig.REQUEST_MISSING_MS)) {
			byte[] data = new byte[totalMissing * 4 + 4];
			Serializer.int2bytes(data, 0, this.lastPacket);
			int index = 4;
			out("missing: " + this.requestedAlready + ", " + this.missing);
			for (int partNr : this.requestedAlready) {
				Serializer.int2bytes(data, index, partNr);
				index += 4;
			}
			for (int partNr : this.missing) {
				Serializer.int2bytes(data, index, partNr);
				this.requestedAlready.add(partNr);
				index += 4;
			}
			this.missing.clear();
			try {
				u.sendFileMessage(Udp4.FileTransfer_REQUEST_DATA, data, this.from);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			this.lastRequestedParts = System.currentTimeMillis();
		}
	}

	private void savePart(int partNr, byte[] data) throws IOException {
		this.rFile.seek((long) partNr * this.packetSize);
		int size = this.packetSize;
		if (partNr == this.parts - 1)
			size = (int) (this.totalSize % this.packetSize);

		this.rFile.write(data, 0, size);
		this.partsReceived++;
		updateProgressListener();
	}

	public void sendCompleteMsg(Udp4 u) {
		try {
			this.listener.transmissionCompleted();
			u.sendFileMessage(Udp4.FileTransfer_COMPLETED, new byte[0], this.from);
			// do this after sending complete message since it takes some time
			this.rFile.getFD().sync();
			this.rFile.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateProgressListener() {
		if (this.listener == null)
			return;
		int ms = this.listener.getUpdateEveryMS();
		long delay = System.currentTimeMillis() - this.listenerLastUpdate;
		if (delay >= ms) {
			this.listenerLastUpdate += ms;
			long transmitted = this.partsReceived * this.packetSize;
			float curSpeed = this.speedMeasure.update(transmitted);
			float percentage = (float) ((double) transmitted / this.totalSize * 100);
			this.listener.transmissionProgress(percentage, curSpeed, transmitted,
				this.totalSize);
		}
	}
}
