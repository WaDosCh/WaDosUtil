/* 
 *              weupnp - Trivial upnp java library 
 *
 * Copyright (C) 2008 Alessandro Bahgat Shehata, Daniele Castagna
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, FΩifth Floor, Boston, MA  02110-1301  USA
 * 
 * Alessandro Bahgat Shehata - ale dot bahgat at gmail dot com
 * Daniele Castagna - daniele dot castagna at gmail dot com
 * 
 */
/*
 * refer to miniupnpc-1.0-RC8
 */
package ch.judos.generic.network.upnp;

import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.JFrame;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;

import ch.judos.generic.network.IP;

/**
 * This class contains a trivial main method that can be used to test whether
 * weupnp is able to manipulate port mappings on a IGD (Internet Gateway Device)
 * on the same network.
 * 
 * @author Alessandro Bahgat Shehata
 */
public class MainGui {
	private static int SAMPLE_PORT = 6991;
	private static short WAIT_TIME = 10;
	private static boolean LIST_ALL_MAPPINGS = false;
	private static InetAddress use;
	private static JFrame frame;
	private static TextArea ta;

	public static void main(String[] args) throws Exception {
		frame = new JFrame("Auto Portforwarding with weupnp lib");
		ta = new TextArea();
		frame.add(ta);
		frame.setSize(500, 400);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}

		});
		for (InetAddress ip : IP.getLocalIps()) {
			if (ip instanceof Inet4Address)
				use = ip;
		}
		if (use == null)
			System.out.println("No IPv4 found, try using IPv6");
		a();
	}

	public static void a() throws Exception {
		addLogLine("Starting weupnp");
		GatewayDiscover gatewayDiscover = new GatewayDiscover();
		addLogLine("Looking for Gateway Devices...");
		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();
		if (gateways.isEmpty()) {
			addLogLine("No gateways found");
			addLogLine("Stopping weupnp");
			return;
		}
		addLogLine(gateways.size() + " gateway(s) found\n");
		int counter = 0;
		for (GatewayDevice gw : gateways.values()) {
			counter++;
			addLogLine("Listing gateway details of device #" + counter + "\n\tFriendly name: "
				+ gw.getFriendlyName() + "\n\tPresentation URL: " + gw.getPresentationURL()
				+ "\n\tModel name: " + gw.getModelName() + "\n\tModel number: "
				+ gw.getModelNumber() + "\n\tLocal interface address: "
				+ gw.getLocalAddress().getHostAddress() + "\n");
		}
		// choose the first active gateway for the tests
		GatewayDevice activeGW = gatewayDiscover.getValidGateway();
		if (null != activeGW) {
			addLogLine("Using gateway: " + activeGW.getFriendlyName());
		}
		else {
			addLogLine("No active gateway device found");
			addLogLine("Stopping weupnp");
			return;
		}
		// testing PortMappingNumberOfEntries
		Integer portMapCount = activeGW.getPortMappingNumberOfEntries();
		addLogLine("GetPortMappingNumberOfEntries: "
			+ (portMapCount != null ? portMapCount.toString() : "(unsupported)"));
		// testing getGenericPortMappingEntry
		PortMappingEntry portMapping = new PortMappingEntry();
		if (LIST_ALL_MAPPINGS) {
			int pmCount = 0;
			do {
				if (activeGW.getGenericPortMappingEntry(pmCount, portMapping))
					addLogLine("Portmapping #" + pmCount + " successfully retrieved ("
						+ portMapping.getPortMappingDescription() + ":"
						+ portMapping.getExternalPort() + ")");
				else {
					addLogLine("Portmapping #" + pmCount + " retrieval failed");
					break;
				}
				pmCount++;
			} while (true); // while (portMapping != null);
		}
		else {
			if (activeGW.getGenericPortMappingEntry(0, portMapping))
				addLogLine("Portmapping #0 successfully retrieved ("
					+ portMapping.getPortMappingDescription() + ":"
					+ portMapping.getExternalPort() + ")");
			else
				addLogLine("Portmapping #0 retrival failed");
		}
		InetAddress localAddress = activeGW.getLocalAddress();
		addLogLine("Detected local address: " + localAddress.getHostAddress());
		if (!use.equals(localAddress))
			addLogLine("WARNING: the local detected address might be wrong!");
		addLogLine("Using local address: " + use.getHostAddress());
		String externalIPAddress = activeGW.getExternalIPAddress();
		addLogLine("External address: " + externalIPAddress);

		addLine("");

		addLogLine("Querying device to see if a port mapping already exists for port "
			+ SAMPLE_PORT);
		if (activeGW.getSpecificPortMappingEntry(SAMPLE_PORT, "TCP", portMapping)) {
			addLogLine("Port " + SAMPLE_PORT + " is already mapped.");
			if (activeGW.deletePortMapping(SAMPLE_PORT, "TCP")) {
				addLogLine("Port mapping removed, SUCCESSFUL");
			}
			else {
				addLogLine("Port mapping removal FAILED");
			}
		}
		else {
			addLogLine("Mapping free. Sending port mapping request for port " + SAMPLE_PORT);
			// test static lease duration mapping
			if (activeGW.addPortMapping(SAMPLE_PORT, SAMPLE_PORT, use.getHostAddress(), "TCP",
				"test")) {
				addLogLine("Mapping SUCCESSFUL. Waiting " + WAIT_TIME
					+ " seconds before removing mapping...");
				Thread.sleep(1000 * WAIT_TIME);
				if (activeGW.deletePortMapping(SAMPLE_PORT, "TCP")) {
					addLogLine("Port mapping removed, test SUCCESSFUL");
				}
				else {
					addLogLine("Port mapping removal FAILED");
				}
			}
			else
				System.err.println("PortMapping couldn't be added");
		}
		addLogLine("Stopping weupnp");
		addLine("");
		addLine("DONE");

	}

	private static void addLine(String line) {
		ta.setText(line + "\n" + ta.getText());
	}

	private static void addLogLine(String line) {
		String timeStamp = DateFormat.getTimeInstance().format(new Date());
		String logline = timeStamp + ": " + line;
		ta.setText(logline + "\n" + ta.getText());
	}
}
