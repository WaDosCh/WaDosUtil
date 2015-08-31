package ch.judos.examples.udp_portforwarding;

import ch.judos.generic.network.udp.Udp4Forwarded;
import ch.judos.generic.network.udp.UdpLib;

/**
 * @since 31.08.2015
 * @author Julian Schelker
 */
public class TestGenericUpnpForwarding {
	public static void main(String[] args) throws Exception {
		new TestGenericUpnpForwarding();
	}

	public TestGenericUpnpForwarding() throws Exception {
		Udp4Forwarded lib = UdpLib.createForwarded();
		System.out.println("Created udp socket on port: " + lib.getLocalPort());
		System.out.println("Mapped with portforwarding to external port: "
			+ lib.getExternalPort());

		synchronized (this) {
			this.wait(1000);
		}
		lib.dispose();

		System.out.println();
		System.out.println("Trying to forward specific port:");

		lib = UdpLib.createForwarded(50001);
		System.out.println("Created udp socket on port: " + lib.getLocalPort());
		System.out.println("Mapped with portforwarding to external port: "
			+ lib.getExternalPort());

		synchronized (this) {
			this.wait(1000);
		}
		lib.dispose();
	}
}
