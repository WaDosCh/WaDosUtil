package ch.judos.generic.network.upnp;

import java.io.IOException;
import java.net.InetSocketAddress;

import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.IP;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;

public class NoPortMapping implements UdpListener {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws SerializerException
	 */
	public static void main(String[] args) throws InterruptedException, SerializerException,
		IOException {
		new NoPortMapping().showPortMappingInfo(60000);
	}

	private boolean received;

	public void showPortMappingInfo(int port) throws SerializerException, IOException,
		InterruptedException {
		String externalIp = IP.getWanIp();
		System.out.println("public IP: " + externalIp);
		System.out.println("PC behind NAT: " + IP.isBehindNat());

		Udp4I u = UdpLib.createOnPort(port);
		// int port = u.getLocalPort();
		u.addDataListener(this);
		System.out.println("listening on port: " + port);

		System.out.println("sending byte[0] to itself via public IP...");
		this.received = false;
		synchronized (this) {
			u.sendRawDataConfirmTo(new byte[0], true, new InetSocketAddress(externalIp, port));
			this.wait(2000);
		}
		if (!this.received)
			System.out.println("Timeout. no message received.");
		System.out.println("shutting down udpLib");
		u.dispose();
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		// byte[] bdata = (byte[]) data;
		System.out.println("received message from: " + from + " data:");// +
		// bdata.length);
		synchronized (this) {
			this.received = true;
			this.notify();
		}
	}

}
