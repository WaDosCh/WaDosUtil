package ch.judos.generic.network.upnp;

import java.net.InetSocketAddress;

import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;
import ch.judos.generic.network.upnp.UdpPortForwarder;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class WithPortMapping {
	public static void main(String[] args) throws Exception {
		final Udp4I u = UdpLib.createDefault();
		final Udp4I u3 = UdpLib.createDefault();
		int externalPort = 60000;
		UdpPortForwarder.addPortMapping(u.getLocalPort(), externalPort,
			"UdpPortForwarder test mapping");
		u.addObjectListener(new UdpListener() {
			@Override
			public void receiveMsg(Object source, InetSocketAddress from, Object data) {
				System.out.println("U received msg from " + from + ": " + data);
				try {
					u.sendObjectConfirmTo("re plop!", true, from);
					u3.sendObjectConfirmTo("found backdoor in your NAT :D", true, from);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Udp4I u2 = UdpLib.createDefault();
		u2.addObjectListener(new UdpListener() {
			@Override
			public void receiveMsg(Object source, InetSocketAddress from, Object data) {
				System.out.println("U2 received msg from " + from + ": " + data);
			}
		});
		u2.sendObjectConfirmTo("hallo", true, new InetSocketAddress(UdpPortForwarder
			.getExternalIpAddress(), externalPort));
		Thread.sleep(1000);
		u.dispose();
		u2.dispose();
		u3.dispose();
		UdpPortForwarder.removePortMapping(externalPort);
	}
}
