package ch.judos.generic.network.udp;

import java.net.DatagramSocket;
import java.net.SocketException;

import ch.judos.generic.data.RandomJS;
import ch.judos.generic.network.udp.interfaces.Udp3I;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.upnp.UdpPortForwarder;

/**
 * 
 * @since 04.07.2013
 * @author Julian Schelker
 */
public abstract class UdpLib {

	// XXX: add "udp" in uppercase as task tag to see more tasks

	/**
	 * creates a default Udp4 object on some random local port
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static Udp4I createDefault() throws SocketException {
		return new Udp4(createU3(new DatagramSocket()));
	}

	/**
	 * creates a port mapping on some random external port and returns the
	 * Udp4Forwarded object.<br>
	 * The port mapping is removed automatically when dispose() is called.
	 * 
	 * @return null if the port mapping could not be created
	 * @throws SocketException
	 */
	public static Udp4Forwarded createForwarded() throws SocketException {
		Udp3I u3 = createU3(new DatagramSocket());
		int trials = 3;
		while (trials > 0) {
			int externalPort = RandomJS.getInt(49152, 65535);
			if (UdpPortForwarder.addPortMapping(u3.getLocalPort(), externalPort,
				"UdpLib port mapping")) {
				return new Udp4Forwarded(u3, externalPort);
			}
			trials--;
		}
		return null;
	}

	/**
	 * creates a port mapping on the given external port and returns the
	 * Udp4Forwarded object.<br>
	 * The port mapping is removed automatically when dispose() is called.
	 * 
	 * @param externalPort
	 * @return null if the port mapping could not be created
	 * @throws SocketException
	 */
	public static Udp4Forwarded createForwarded(int externalPort) throws SocketException {
		Udp3I u3 = createU3(new DatagramSocket());
		if (!UdpPortForwarder.addPortMapping(u3.getLocalPort(), externalPort,
			"UdpLib port mapping")) {
			u3.dispose();
			return null;
		}
		return new Udp4Forwarded(u3, externalPort);
	}

	/**
	 * creates a Udp4, listening to the given local port
	 * 
	 * @param port
	 * @return
	 * @throws SocketException
	 */
	public static Udp4I createOnPort(int port) throws SocketException {
		return new Udp4(createU3(new DatagramSocket(port)));
	}

	protected static Udp3I createU3(DatagramSocket d) throws SocketException {
		return new Udp3(new Udp2(new Udp1(new Udp0Reader(d))));
	}

}
