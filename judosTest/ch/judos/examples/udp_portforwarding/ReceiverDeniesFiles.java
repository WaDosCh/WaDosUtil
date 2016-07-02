package ch.judos.examples.udp_portforwarding;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketException;

import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.FileTransmissionHandler;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpFileTransferListener;
import ch.judos.generic.network.udp.interfaces.UdpListener;
import ch.judos.generic.network.udp.model.FileDescription;

/**
 * @since 31.08.2015
 * @author Julian Schelker
 */
public class ReceiverDeniesFiles implements FileTransmissionHandler, UdpFileTransferListener, UdpListener {

	public static int targetPort = 50001;

	public static void main(String[] args) throws SocketException {
		new ReceiverDeniesFiles();
	}

	public ReceiverDeniesFiles() throws SocketException {
		Udp4I udpLib = UdpLib.createOnPort(targetPort);

		udpLib.setFileHandler(this);

		udpLib.addObjectListener(this);
	}

	@Override
	public File requestFileTransmission(FileDescription fd) {
		System.out.println(fd.getDescription());
		System.out.println(fd.getPath());
		System.out.println("Length: " + fd.getLength());
		System.out.println("Parts: " + fd.getParts());
		return null;
	}

	@Override
	public UdpFileTransferListener requestTransferFileListener(FileDescription fd) {
		return this;
	}

	@Override
	public int getUpdateEveryMS() {
		return 1000;
	}

	@Override
	public void transmissionAcceptedAndStarted() {
		System.out.println("Accepted and started");
	}

	@Override
	public void transmissionCompleted() {
		System.out.println("Completed");
	}

	@Override
	public void transmissionDeniedAndCanceled() {
		System.out.println("Denied and canceled");
	}

	@Override
	public void transmissionProgress(float percentage, float avgSpeed, long transmitted,
		long total) {
		System.out.println(percentage + "% avgSpeed: " + avgSpeed + "  progress: "
			+ transmitted + "/" + total);
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		System.out.println("received object from: " + source);
		System.out.println("from: " + from);
		System.out.println("data: " + data);
	}

}
