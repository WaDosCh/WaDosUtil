package controller.portforwarding;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import lib.data.JS_Random;

/**
 * @author Julian Schelker
 * @lastUpdate 23.04.2012
 */
public class MiniUpnpPortForwarding implements PortForwarderI {
	protected static final String fileNamePath = "upnpc-exe-win32-20120121/upnpc-static.exe";
	protected static List<PortMapping> existingPortMappingsCache;
	
	protected int externPort;
	protected String error;
	protected DatagramSocket socket;
	
	public MiniUpnpPortForwarding() {
		this.externPort = -1;
		this.error = "";
		this.socket = null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see model.portforwarding.PortforwardingI#getExternalPort()
	 */
	@Override
	public int getExternalPort() {
		return this.externPort;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see model.portforwarding.PortforwardingI#getInternalPort()
	 */
	public int getInternalPort() {
		if (this.socket != null)
			return this.socket.getLocalPort();
		return -1;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see model.portforwarding.PortforwardingI#isSucceeded()
	 */
	@Override
	public boolean isSucceeded() {
		return this.socket != null;
	}
	
	public DatagramSocket tryForwarding() throws SocketException {
		return tryForwarding(0);
	}
	
	/**
	 * @param lifetimeSeconds
	 * @param port
	 *            default value is 0 to select a random port
	 * @throws Exception
	 */
	public DatagramSocket tryForwarding(int externPort) throws SocketException {
		try {
			if (isPortOwnedByOtherHost(externPort)) {
				throw new SocketException(
					"Port is already forwarded to a different host.");
			}
		} catch (IOException e1) {
			throw new SocketException("Could not get port list on router.");
		}
		DatagramSocket result = getSocketForForwardedPort(externPort);
		if (result != null) {
			this.socket = result;
			this.externPort = externPort;
			return result;
		}
		try {
			int useTries = 5;
			int tries = useTries;
			boolean ok = false;
			boolean randomPort = (externPort == 0);
			this.socket = new DatagramSocket();
			int localPort = this.socket.getLocalPort();
			List<Integer> checkPorts = new ArrayList<Integer>();
			do {
				if (randomPort) {
					do {
						externPort = JS_Random.getInt(49152, 65535);
					} while (checkPorts.contains(externPort));
					checkPorts.add(externPort);
				}
				ok = addUdpPortForwarding(getLocalIP(), localPort, externPort);
				if (ok) {
					this.externPort = externPort;
					return this.socket;
				}
				tries--;
			} while (tries > 0);
			this.socket.close();
			this.socket = null;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static DatagramSocket getSocketForForwardedPort(int port)
		throws SocketException {
		List<PortMapping> list;
		try {
			list = getExistingPortForwardings();
		} catch (IOException e1) {
			return null;
		}
		for (PortMapping p : list) {
			if (p.getExternPort() == port || port == 0) {
				try {
					return new DatagramSocket(p.getLocalPort());
				} catch (SocketException e) {
					if (port != 0)
						throw new SocketException(
							"Port is already bound to a different local socket.");
					// go on and try other already forwarded ports
				}
			}
		}
		return null;
	}
	
	public static boolean isPortForwarded(int port) throws Exception {
		List<PortMapping> list = getExistingPortForwardings();
		for (PortMapping p : list) {
			if (p.getExternPort() == port || port == 0)
				return true;
		}
		return false;
	}
	
	private boolean isPortOwnedByOtherHost(int port) throws SocketException {
		if (port == 0)
			return false;
		List<PortMapping> list = getExistingPortForwardings();
		for (PortMapping p : list) {
			if (p.getExternPort() == port && !p.getIp().equals(getLocalIP()))
				return true;
		}
		return false;
	}
	
	private boolean addUdpPortForwarding(String ips, int localPort,
		int externPort) {
		try {
			Process process = Runtime.getRuntime().exec(
				fileNamePath + " -a " + ips + " " + localPort + " "
					+ externPort + " UDP");
			String err = MiniUpnpParser.waitToTerminateAndGetError(process);
			this.error = err;
			return err.equals("");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see model.portforwarding.PortforwardingI#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return this.error;
	}
	
	private static List<PortMapping> getExistingPortForwardings()
		throws SocketException {
		if (existingPortMappingsCache == null)
			existingPortMappingsCache = getExistingPortForwardingsUncached();
		return existingPortMappingsCache;
	}
	
	private static List<PortMapping> getExistingPortForwardingsUncached()
		throws SocketException {
		try {
			Process process = Runtime.getRuntime().exec(fileNamePath + " -l");
			List<String> lines = MiniUpnpParser
				.waitToTerminateandGetOutput(process);
			boolean createEntries = false;
			ArrayList<PortMapping> mappings = new ArrayList<PortMapping>();
			for (String line : lines) {
				if (createEntries)
					MiniUpnpParser.tryToParseMappingAndAddTo(line, mappings);
				if (line
					.contains("i protocol exPort->inAddr:inPort description remoteHost leaseTime"))
					createEntries = true;
			}
			return mappings;
		} catch (IOException e) {
			throw new SocketException(
				"Could not get existing mappings from router: "
					+ e.getMessage());
		}
	}
	
}
