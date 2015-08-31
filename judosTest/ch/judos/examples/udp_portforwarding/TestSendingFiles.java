package ch.judos.examples.udp_portforwarding;
import java.io.File;
import java.net.InetSocketAddress;

import ch.judos.generic.data.format.ByteData;
import ch.judos.generic.files.FileUtils;
import ch.judos.generic.math.MathJS;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpFileTransferListener;

/**
 * @since 31.08.2015
 * @author Julian Schelker
 */
public class TestSendingFiles implements UdpFileTransferListener {

	private Udp4I udpLib;
	public static boolean usePublic = true;
	public static String targetPublicIP = "5.149.32.249";
	public static String targetIp = "192.168.1.222";
	public static int targetPort = 50001;

	public static void main(String[] args) throws Exception {
		new TestSendingFiles();
	}

	public TestSendingFiles() throws Exception {
		this.udpLib = UdpLib.createDefault();
		String ip = targetPublicIP;
		if (!usePublic)
			ip = targetIp;
		File selectedFile = FileUtils.requestFile();
		this.udpLib.sendFileTo(selectedFile, "random file", new InetSocketAddress(ip,
			targetPort), this);
	}

	@Override
	public int getUpdateEveryMS() {
		return 1000;
	}

	@Override
	public void transmissionAcceptedAndStarted() {
		System.out.println("File transfer was accepted by receiver.");
	}

	@Override
	public void transmissionCompleted() {
		System.out.println("File successfully sent.");
		this.udpLib.dispose();
	}

	@Override
	public void transmissionDeniedAndCanceled() {
		System.out.println("File transfer denied by receiver");
		this.udpLib.dispose();
	}

	@Override
	public void transmissionProgress(float percentage, float avgSpeed, long transmitted,
		long total) {
		System.out.println(MathJS.round(percentage, 1) + "% avgSpeed: "
			+ ByteData.autoFormat(avgSpeed) + "/s  progress: "
			+ ByteData.autoFormat(transmitted) + "/" + ByteData.autoFormat(total));
	}

}
