package ch.judos.generic.network.udp;

import java.io.File;
import java.net.SocketException;

import ch.judos.generic.data.format.ByteData;
import ch.judos.generic.math.MathJS;
import ch.judos.generic.network.udp.interfaces.FileTransmissionHandler;
import ch.judos.generic.network.udp.interfaces.UdpFileTransferListener;
import ch.judos.generic.network.udp.model.FileDescription;

/**
 * @since 17.07.2013
 * @author Julian Schelker
 */
public class FileSendTestReceiver implements FileTransmissionHandler, UdpFileTransferListener {

	/**
	 * @param args
	 * @throws SocketException
	 */
	public static void main(String[] args) throws SocketException {
		new FileSendTestReceiver().startTest();
	}

	public void startTest() throws SocketException {
		Udp4Forwarded u = UdpLib.createForwarded(60000);

		u.setFileHandler(this);

	}

	@Override
	public int getUpdateEveryMS() {
		return 100;
	}

	@Override
	public File requestFileTransmission(FileDescription fd) {
		System.out.println("requested file transmission:");
		System.out.println(fd.getDescription());
		System.out.println("size: " + fd.getLength() + " bytes");
		return null;
	}

	@Override
	public UdpFileTransferListener requestTransferFileListener(FileDescription fd) {
		return this;
	}

	@Override
	public void transmissionAcceptedAndStarted() {
		System.out.println("accepted and started");
	}

	@Override
	public void transmissionCompleted() {
		System.out.println("complete");
	}

	@Override
	public void transmissionDeniedAndCanceled() {
		System.out.println("denied and canceled");
	}

	@Override
	public void transmissionProgress(float percentage, float avgSpeed, long transmitted,
		long total) {
		System.out.println(MathJS.round(percentage, 0) + "% speed: "
			+ ByteData.autoFormat(avgSpeed) + "  " + ByteData.autoFormat(transmitted) + " / "
			+ ByteData.autoFormat(total));
	}

}
