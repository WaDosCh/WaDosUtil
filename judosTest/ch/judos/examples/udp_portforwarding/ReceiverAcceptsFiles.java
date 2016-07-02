package ch.judos.examples.udp_portforwarding;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketException;

import ch.judos.generic.data.format.ByteData;
import ch.judos.generic.files.FileUtils;
import ch.judos.generic.math.MathJS;
import ch.judos.generic.network.udp.UdpConfig;
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
public class ReceiverAcceptsFiles implements FileTransmissionHandler, UdpFileTransferListener,
	UdpListener {

	public static int targetPort = 50001;

	public static void main(String[] args) throws SocketException {
		new ReceiverAcceptsFiles();
	}
	private Udp4I udpLib;
	public ReceiverAcceptsFiles() throws SocketException {
		this.udpLib = UdpLib.createOnPort(targetPort);

		this.udpLib.setFileHandler(this);

		this.udpLib.addObjectListener(this);
	}

	@Override
	public File requestFileTransmission(FileDescription fd) {
		System.out.println("Incoming request for file transmission:");
		System.out.println("Description: " + fd.getDescription());
		System.out.println("File path of sender: " + fd.getPath());
		System.out.println("Total size: " + ByteData.autoFormat(fd.getLength()));
		System.out.println("Parts à " + ByteData.autoFormat(UdpConfig.PACKET_SIZE_BYTES)
			+ ": " + fd.getParts());
		String extension = FileUtils.getExtension(new File(fd.getPath()));
		return new File("temp." + extension);
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
		System.out.println(MathJS.round(percentage, 1) + "% avgSpeed: "
			+ ByteData.autoFormat(avgSpeed) + "/s  progress: "
			+ ByteData.autoFormat(transmitted) + "/" + ByteData.autoFormat(total));
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		System.out.println("received object from: " + source);
		System.out.println("from: " + from);
		System.out.println("data: " + data);
	}

}
