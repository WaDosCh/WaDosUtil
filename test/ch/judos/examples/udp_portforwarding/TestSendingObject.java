package ch.judos.examples.udp_portforwarding;
import java.net.InetSocketAddress;

import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;

/**
 * @since 31.08.2015
 * @author Julian Schelker
 */
public class TestSendingObject {

	public static boolean usePublic = true;
	public static String targetPublicIP = "5.149.32.249";
	public static String targetIp = "192.168.1.222";
	public static int targetPort = 50001;

	public static void main(String[] args) throws Exception {
		new TestSendingObject();
	}

	public TestSendingObject() throws Exception {
		Udp4I udpLib = UdpLib.createDefault();
		String ip = targetPublicIP;
		if (!usePublic)
			ip = targetIp;
		udpLib.sendObjectConfirmTo("Hello World", true, new InetSocketAddress(ip, targetPort));
		synchronized (this) {
			this.wait(2500);
		}
		udpLib.dispose();
	}

}
