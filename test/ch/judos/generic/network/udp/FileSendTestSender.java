package ch.judos.generic.network.udp;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class FileSendTestSender {
	public static void main(String[] args) throws SocketException, FileNotFoundException {
		new FileSendTestSender().runTest();
	}

	public void runTest() throws SocketException, FileNotFoundException {
		Udp4Forwarded u = UdpLib.createForwarded(60000);

		u.sendFileTo(new File("test.abc"), "tescht", new InetSocketAddress("84.227.98.109",
			60000), null);

	}
}
