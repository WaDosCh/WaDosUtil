package ch.judos.generic.network.upnp;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

import org.xml.sax.SAXException;

import ch.judos.generic.network.IP;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class UdpPortForwarder {
	protected static GatewayDevice device;
	private static String PROTOCOL = "UDP";

	public static boolean addPortMapping(int localPort, int externalPort, String description) {
		GatewayDevice d = getGatewayDevice();
		InetAddress ipv4 = null;
		for (InetAddress ip : IP.getLocalIps()) {
			if (ip instanceof Inet4Address) {
				ipv4 = ip;
				break;
			}
		}
		if (ipv4 == null) {
			new Exception("Ipv4 address could not be found").printStackTrace();
			return false;
		}
		try {
			return d.addPortMapping(externalPort, localPort, ipv4.getHostAddress(), PROTOCOL,
				description);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getExternalIpAddress() {
		GatewayDevice d = getGatewayDevice();
		try {
			return d.getExternalIPAddress();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return the gatewayDevice or null if no valid device could be found
	 */
	public static GatewayDevice getGatewayDevice() {
		if (device == null) {
			GatewayDiscover discover = new GatewayDiscover();
			// logger.info("Looking for Gateway Devices");
			try {
				discover.discover();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			device = discover.getValidGateway();
		}
		return device;
	}

	/**
	 * @param externalPort
	 * @return null if no port mapping is defined already on this port
	 * @throws IOException
	 * @throws SAXException
	 */
	public static PortMappingEntry getPortMappingAlreadyInUse(int externalPort)
		throws IOException, SAXException {
		GatewayDevice d = getGatewayDevice();
		PortMappingEntry portMapping = new PortMappingEntry();
		if (!d.getSpecificPortMappingEntry(externalPort, PROTOCOL, portMapping))
			return null;
		return portMapping;
	}

	public static void removePortMapping(int externalPort) {
		GatewayDevice d = getGatewayDevice();
		try {
			if (!d.deletePortMapping(externalPort, PROTOCOL))
				new Exception("could not remove Port Mapping on port " + externalPort)
					.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
