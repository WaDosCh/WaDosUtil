package ch.judos.generic.network.udp;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ch.judos.generic.data.Serializer;
import ch.judos.generic.network.udp.model.FileOutgoingTransmission;

/**
 * @since 12.07.2013
 * @author Julian Schelker
 */
public class FileSender implements Runnable {

	private ArrayList<FileOutgoingTransmission> newFiles;
	private int packetSize;
	private HashMap<InetSocketAddress, FileOutgoingTransmission> runningFileTrans;
	private Thread thread;
	private Udp4 u;
	/**
	 * used to force stop the execution of sending thread
	 */
	private boolean running;

	public FileSender(Udp4 u, int packetSize) {
		this.u = u;
		this.newFiles = new ArrayList<>();
		this.runningFileTrans = new HashMap<>();
		this.packetSize = packetSize;
		this.running = true;
	}

	public void addToQueue(FileOutgoingTransmission fileTransmission) {
		synchronized (this.newFiles) {
			this.newFiles.add(fileTransmission);
		}
		wakeupThread();
	}

	public int getPacketSize() {
		return this.packetSize;
	}

	public void receiveMsg(int typ, byte[] data, InetSocketAddress from) {
		FileOutgoingTransmission ft = this.runningFileTrans.get(from);
		if (ft == null)
			throw new RuntimeException("got an type " + typ + " response for " + from
				+ " where no filetransmission is in progress ");
		if (typ == Udp4.FileTransfer_ACCEPTED)
			ft.acceptedAndStart();
		else if (typ == Udp4.FileTransfer_DENIED)
			ft.denyTransmissionAndStop();
		else if (typ == Udp4.FileTransfer_REQUEST_DATA)
			ft.requestParts(data);
		else if (typ == Udp4.FileTransfer_COMPLETED)
			ft.setToCompleted();
		else {
			throw new RuntimeException("got an invalid type message: " + typ + " from " + from);
		}
		wakeupThread();
	}

	@Override
	public void run() {
		boolean timeout = false;
		while (this.running) {
			sendFileDescriptorAwaitAccept();
			sendCurrentTransmissions();
			if (this.runningFileTrans.isEmpty()) {
				// if no work is available, and thread has waited once, stop
				// execution of this thread
				if (timeout)
					break;
				synchronized (this.thread) {
					long ms = System.currentTimeMillis();
					try {
						this.thread.wait(1000);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					timeout = System.currentTimeMillis() - ms >= 1000;
				}
			}
			else
				timeout = false;
			Thread.yield();
		}
	}

	private void sendCurrentTransmissions() {
		Iterator<FileOutgoingTransmission> it = this.runningFileTrans.values().iterator();
		while (it.hasNext()) {
			FileOutgoingTransmission ft = it.next();
			switch (ft.getStatus()) {
				case COMPLETED :
					it.remove();
					break;
				case RUNNING :
					ft.sendFileMessages(this.u);
					break;
				case STOPPED :
					it.remove();
					break;
				case WAITING_FOR_ACCEPTANCE :
					break;
				default :
					new Exception("unknown fileTransfer status enum: " + ft.getStatus())
						.printStackTrace();
					break;
			}
		}
	}

	private void sendFileDescriptorAwaitAccept() {
		Iterator<FileOutgoingTransmission> it = this.newFiles.iterator();
		while (it.hasNext()) {
			FileOutgoingTransmission nft = it.next();
			if (!this.runningFileTrans.containsKey(nft.getTarget())) {
				it.remove();
				this.runningFileTrans.put(nft.getTarget(), nft);
				byte[] data;
				try {
					data = Serializer.object2Bytes(nft.getFileDescription());
					this.u.sendFileMessage(-1, data, nft.getTarget());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void wakeupThread() {
		if (this.thread == null || !this.thread.isAlive()) {
			this.thread = new Thread(this, "Udp4FileSender");
			this.thread.setDaemon(false);
			this.thread.start();
			return;
		}
		synchronized (this.thread) {
			this.thread.notify();
		}
	}

	public void dispose() {
		this.running = false;
	}

}
