package ch.judos.generic.network.udp;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import junit.framework.TestCase;
import ch.judos.generic.TestConstants;
import ch.judos.generic.data.format.ByteData;
import ch.judos.generic.math.MathJS;
import ch.judos.generic.network.udp.interfaces.FileTransmissionHandler;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpFileTransferListener;
import ch.judos.generic.network.udp.model.FileDescription;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class Udp4FileTest extends TestCase implements FileTransmissionHandler,
	UdpFileTransferListener {

	public static final boolean SILENT = true;
	public static File testFile;
	public static final int PORT = 60000;
	private long start;
	private Udp4I u;
	private static File testFileReceive;

	@Override
	public int getUpdateEveryMS() {
		return 100;
	}

	@Override
	public File requestFileTransmission(FileDescription fd) {
		return testFileReceive;
	}

	@Override
	public UdpFileTransferListener requestTransferFileListener(FileDescription fd) {
		return this;
	}

	@Override
	protected void setUp() throws Exception {
		@SuppressWarnings("resource")
		Udp2 u2 = new Udp2(new Udp1(new Udp0Reader(new DatagramSocket(PORT))));
		this.u = new Udp4(new Udp3(u2));
		testFile = TestConstants.getTestData(3 * 1024 * 1024);
		testFileReceive = TestConstants.getTestFileForWriting();
	}

	@Override
	protected void tearDown() throws Exception {
		this.u.dispose();
		testFile.delete();
		testFileReceive.delete();
	}

	public void testSomeThing() throws FileNotFoundException, InterruptedException {
		this.u.setFileHandler(this);

		this.u.sendFileTo(testFile, "some test file",
			new InetSocketAddress("localhost", PORT), null);

		synchronized (this) {
			wait();
		}
	}

	@Override
	public void transmissionAcceptedAndStarted() {
		if (!SILENT)
			System.out.println("accepted and started");
		this.start = System.currentTimeMillis();
	}

	@Override
	public void transmissionCompleted() {
		long took = System.currentTimeMillis() - this.start;
		if (!SILENT)
			System.out.println("Complete, file transfer took: " + took + " ms");
		synchronized (this) {
			this.notify();
		}
	}

	@Override
	public void transmissionDeniedAndCanceled() {
		if (!SILENT)
			System.out.println("denied and canceled");
	}

	@Override
	public void transmissionProgress(float percentage, float avgSpeed, long transmitted,
		long total) {
		if (!SILENT)
			System.out.println(MathJS.round(percentage, 0) + "% speed: "
				+ ByteData.autoFormat(avgSpeed) + "  " + ByteData.autoFormat(transmitted)
				+ " / " + ByteData.autoFormat(total));
	}

}
