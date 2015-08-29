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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Alessandro Bahgat Shehata - ale dot bahgat at gmail dot com
 * Daniele Castagna - daniele dot castagna at gmail dot com
 * 
 */
package ch.judos.generic.network.upnp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A <tt>GatewayDevice</tt> is a class that abstracts UPnP-compliant gateways
 * 
 * It holds all the information that comes back as UPnP responses, and provides
 * methods to issue UPnP commands to a gateway.
 * 
 * @author casta
 */
@SuppressWarnings("all")
public class GatewayDevice {

	/**
	 * Issues UPnP commands to a GatewayDevice that can be reached at the
	 * specified <tt>url</tt>
	 * 
	 * The command is identified by a <tt>service</tt> and an <tt>action</tt>
	 * and can receive arguments
	 * 
	 * @param url
	 *            the url to use to contact the device
	 * @param service
	 *            the service to invoke
	 * @param action
	 *            the specific action to perform
	 * @param args
	 *            the command arguments
	 * @return the response to the performed command, as a name-value map. In
	 *         case errors occur, the returned map will be <i>empty.</i>
	 * @throws IOException
	 *             on communication errors
	 * @throws SAXException
	 *             if errors occur while parsing the response
	 */
	public static Map<String, String> simpleUPnPcommand(String url, String service,
		String action, Map<String, String> args) throws IOException, SAXException {
		String soapAction = service + "#" + action;
		StringBuffer soapBody = new StringBuffer();

		soapBody.append("<?xml version=\"1.0\"?>\r\n" + "<SOAP-ENV:Envelope "
			+ "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
			+ "SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
			+ "<SOAP-ENV:Body>" + "<m:" + action + " xmlns:m=\"" + service + "\">");

		if (args != null && args.size() > 0) {

			Set<Map.Entry<String, String>> entrySet = args.entrySet();

			for (Map.Entry<String, String> entry : entrySet) {
				soapBody.append("<" + entry.getKey() + ">" + entry.getValue() + "</"
					+ entry.getKey() + ">");
			}

		}

		soapBody.append("</m:" + action + ">");
		soapBody.append("</SOAP-ENV:Body></SOAP-ENV:Envelope>");

		URL postUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml");
		conn.setRequestProperty("SOAPAction", soapAction);
		conn.setRequestProperty("Connection", "Close");

		byte[] soapBodyBytes = soapBody.toString().getBytes();

		conn.setRequestProperty("Content-Length", String.valueOf(soapBodyBytes.length));

		conn.getOutputStream().write(soapBodyBytes);

		Map<String, String> nameValue = new HashMap<>();
		XMLReader parser = XMLReaderFactory.createXMLReader();
		parser.setContentHandler(new NameValueHandler(nameValue));
		if (conn.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
			try {
				// attempt to parse the error message
				parser.parse(new InputSource(conn.getErrorStream()));
			}
			catch (SAXException e) {
				e.printStackTrace();
				// ignore the exception
				// We probably need to find a better way to return
				// significant information when we reach this point
			}
			conn.disconnect();
			return nameValue;
		}
		// else {
		parser.parse(new InputSource(conn.getInputStream()));
		conn.disconnect();
		return nameValue;
		// }
	}

	private String controlURL;
	private String controlURLCIF;
	private String deviceType;
	private String deviceTypeCIF;
	private String eventSubURL;
	private String eventSubURLCIF;
	/**
	 * The friendly (human readable) name associated with this device
	 */
	private String friendlyName;
	/**
	 * The address used to reach this machine from the GatewayDevice
	 */
	private InetAddress localAddress;
	private String location;
	/**
	 * The device manufacturer name
	 */
	private String manufacturer;
	/**
	 * The model description as a string
	 */
	private String modelDescription;
	/**
	 * The model name
	 */
	private String modelName;

	// description data

	/**
	 * The model number (used by the manufacturer to identify the product)
	 */
	private String modelNumber;

	/**
	 * The URL that can be used to access the IGD interface
	 */
	private String presentationURL;

	private String sCPDURL;

	private String sCPDURLCIF;

	private String serviceType;

	private String serviceTypeCIF;

	private String st;

	private String urlBase;

	/**
	 * Creates a new instance of GatewayDevice
	 */
	public GatewayDevice() {
	}

	/**
	 * Adds a new port mapping to the GatewayDevices using the supplied
	 * parameters.
	 * 
	 * @param externalPort
	 *            the external associated with the new mapping
	 * @param internalPort
	 *            the internal port associated with the new mapping
	 * @param internalClient
	 *            the internal client associated with the new mapping
	 * @param protocol
	 *            the protocol associated with the new mapping
	 * @param description
	 *            the mapping description
	 * @return true if the mapping was succesfully added, false otherwise
	 * @throws IOException
	 * @throws SAXException
	 * @see #simpleUPnPcommand(java.lang.String, java.lang.String,
	 *      java.lang.String, java.util.Map)
	 * @see PortMappingEntry
	 */
	public boolean addPortMapping(int externalPort, int internalPort, String internalClient,
		String protocol, String description) throws IOException, SAXException {
		Map<String, String> args = new HashMap<>();
		// Why necessarily the empty string?
		args.put("NewRemoteHost", "");
		args.put("NewExternalPort", Integer.toString(externalPort));
		args.put("NewProtocol", protocol);
		args.put("NewInternalPort", Integer.toString(internalPort));
		args.put("NewInternalClient", internalClient);
		args.put("NewEnabled", Integer.toString(1));
		args.put("NewPortMappingDescription", description);
		args.put("NewLeaseDuration", Integer.toString(0));

		Map<String, String> nameValue = simpleUPnPcommand(this.controlURL, this.serviceType,
			"AddPortMapping", args);

		return nameValue.get("errorCode") == null;
	}

	// private methods
	private String copyOrCatUrl(String dst, String src) {
		if (src != null) {
			if (src.startsWith("http://")) {
				dst = src;
			}
			else {
				if (!src.startsWith("/")) {
					dst += "/";
				}
				dst += src;
			}
		}
		return dst;
	}

	/**
	 * Deletes the port mapping associated to <tt>externalPort</tt> and
	 * <tt>protocol</tt>
	 * 
	 * @param externalPort
	 *            the external port
	 * @param protocol
	 *            the protocol
	 * @return true if removal was successful
	 * @throws IOException
	 * @throws SAXException
	 */
	public boolean deletePortMapping(int externalPort, String protocol) throws IOException,
		SAXException {
		Map<String, String> args = new HashMap<>();
		args.put("NewRemoteHost", "");
		args.put("NewExternalPort", Integer.toString(externalPort));
		args.put("NewProtocol", protocol);
		// Map<String, String> nameValue =
		simpleUPnPcommand(this.controlURL, this.serviceType, "DeletePortMapping", args);

		return true;
	}

	public String getControlURL() {
		return this.controlURL;
	}

	public String getControlURLCIF() {
		return this.controlURLCIF;
	}

	public String getDeviceType() {
		return this.deviceType;
	}

	public String getDeviceTypeCIF() {
		return this.deviceTypeCIF;
	}

	public String getEventSubURL() {
		return this.eventSubURL;
	}

	public String getEventSubURLCIF() {
		return this.eventSubURLCIF;
	}

	/**
	 * Retrieves the external IP address associated with this device
	 * 
	 * The external address is the address that can be used to connect to the
	 * GatewayDevice from the external network
	 * 
	 * @return the external IP
	 * @throws IOException
	 * @throws SAXException
	 * @see #simpleUPnPcommand(java.lang.String, java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	public String getExternalIPAddress() throws IOException, SAXException {
		Map<String, String> nameValue = simpleUPnPcommand(this.controlURL, this.serviceType,
			"GetExternalIPAddress", null);

		return nameValue.get("NewExternalIPAddress");
	}

	public String getFriendlyName() {
		return this.friendlyName;
	}

	/**
	 * Returns a specific port mapping entry, depending on a the supplied index.
	 * 
	 * @param index
	 *            the index of the desired port mapping
	 * @param portMappingEntry
	 *            the entry containing the details, in any is present,
	 *            <i>null</i> otherwise. <i>(used as return value)</i>
	 * @return true if a valid mapping is found
	 * @throws IOException
	 * @throws SAXException
	 * @see #simpleUPnPcommand(java.lang.String, java.lang.String,
	 *      java.lang.String, java.util.Map)
	 * @see PortMappingEntry
	 * @todo consider refactoring this method to make it consistend with Java
	 *       practices (return the port mapping)
	 */
	public boolean getGenericPortMappingEntry(int index,
		final PortMappingEntry portMappingEntry) throws IOException, SAXException {
		Map<String, String> args = new HashMap<>();
		args.put("NewPortMappingIndex", Integer.toString(index));

		Map<String, String> nameValue = simpleUPnPcommand(this.controlURL, this.serviceType,
			"GetGenericPortMappingEntry", args);

		try {
			portMappingEntry.setExternalPort(Integer
				.parseInt(nameValue.get("NewExternalPort")));
		}
		catch (Exception e) {
		}

		portMappingEntry.setRemoteHost(nameValue.get("NewRemoteHost"));
		portMappingEntry.setInternalClient(nameValue.get("NewInternalClient"));
		portMappingEntry.setProtocol(nameValue.get("NewProtocol"));

		try {
			portMappingEntry.setInternalPort(Integer
				.parseInt(nameValue.get("NewInternalPort")));
		}
		catch (Exception e) {
		}
		portMappingEntry.setEnabled(nameValue.get("NewEnabled"));
		portMappingEntry.setPortMappingDescription(nameValue.get("NewPortMappingDescription"));
		/* portMappingEntry.set(nameValue.get("NewLeaseDuration")); */

		return nameValue.get("errorCode") == null;
	}

	// getters and setters
	/**
	 * Gets the local address
	 * 
	 * @return the localAddress
	 */
	public InetAddress getLocalAddress() {
		return this.localAddress;
	}

	public String getLocation() {
		return this.location;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public String getModelDescription() {
		return this.modelDescription;
	}

	public String getModelName() {
		return this.modelName;
	}

	public String getModelNumber() {
		return this.modelNumber;
	}

	/**
	 * Retrieves the number of port mappings that are registered on the
	 * GatewayDevice.
	 * 
	 * @return the number of port mappings
	 * @throws IOException
	 * @throws SAXException
	 */
	public Integer getPortMappingNumberOfEntries() throws IOException, SAXException {
		Map<String, String> nameValue = simpleUPnPcommand(this.controlURL, this.serviceType,
			"GetPortMappingNumberOfEntries", null);

		Integer portMappingNumber = null;

		try {
			portMappingNumber = Integer
				.valueOf(nameValue.get("NewPortMappingNumberOfEntries"));
		}
		catch (Exception e) {
		}

		return portMappingNumber;
	}

	public String getPresentationURL() {
		return this.presentationURL;
	}

	public String getSCPDURL() {
		return this.sCPDURL;
	}

	public String getSCPDURLCIF() {
		return this.sCPDURLCIF;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public String getServiceTypeCIF() {
		return this.serviceTypeCIF;
	}

	/**
	 * Queries the GatewayDevice to retrieve a specific port mapping entry,
	 * corresponding to specified criteria, if present.
	 * 
	 * Retrieves the <tt>PortMappingEntry</tt> associated with
	 * <tt>externalPort</tt> and <tt>protocol</tt>, if present.
	 * 
	 * @param externalPort
	 *            the external port
	 * @param protocol
	 *            the protocol (TCP or udp)
	 * @param portMappingEntry
	 *            the entry containing the details, in any is present,
	 *            <i>null</i> otherwise. <i>(used as return value)</i>
	 * @return true if a valid mapping is found
	 * @throws IOException
	 * @throws SAXException
	 * @see #simpleUPnPcommand(java.lang.String, java.lang.String,
	 *      java.lang.String, java.util.Map)
	 * @see PortMappingEntry
	 * @todo consider refactoring this method to make it consistent with Java
	 *       practices (return the port mapping)
	 */
	public boolean getSpecificPortMappingEntry(int externalPort, String protocol,
		final PortMappingEntry portMappingEntry) throws IOException, SAXException {
		portMappingEntry.setExternalPort(externalPort);
		portMappingEntry.setProtocol(protocol);

		Map<String, String> args = new HashMap<>();
		// Why necessarily the empty string?
		args.put("NewRemoteHost", "");
		args.put("NewExternalPort", Integer.toString(externalPort));
		args.put("NewProtocol", protocol);

		Map<String, String> nameValue = simpleUPnPcommand(this.controlURL, this.serviceType,
			"GetSpecificPortMappingEntry", args);
		String internalClient = nameValue.get("NewInternalClient");
		String internalPort = nameValue.get("NewInternalPort");
		// are the description and other details really missing?

		if (internalClient != null) {
			portMappingEntry.setInternalClient(internalClient);
		}

		if (internalPort != null) {
			try {
				portMappingEntry.setInternalPort(Integer.parseInt(internalPort));
			}
			catch (Exception e) {
			}
		}

		return internalClient != null && internalPort != null;
	}

	public String getSt() {
		return this.st;
	}

	public String getURLBase() {
		return this.urlBase;
	}

	/**
	 * Retrieves the connection status of this device
	 * 
	 * @return true if connected, false otherwise
	 * @throws IOException
	 * @throws SAXException
	 * @see #simpleUPnPcommand(java.lang.String, java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	public boolean isConnected() throws IOException, SAXException {
		Map<String, String> nameValue = simpleUPnPcommand(this.controlURL, this.serviceType,
			"GetStatusInfo", null);

		String connectionStatus = nameValue.get("NewConnectionStatus");
		if (connectionStatus != null && connectionStatus.equalsIgnoreCase("Connected")) {
			return true;
		}

		return false;
	}

	/**
	 * Retrieves the properties and description of the GatewayDevice.
	 * 
	 * Connects to the device's location and parses the response using a
	 * {@link GatewayDeviceHandler} to populate the fields of this class
	 * 
	 * @throws SAXException
	 *             if an error occurs while parsing the request
	 * @throws IOException
	 *             on communication errors
	 * @see ch.judos.generic.network.upnp.GatewayDeviceHandler
	 */
	public void loadDescription() throws SAXException, IOException {

		URLConnection urlConn = new URL(getLocation()).openConnection();

		XMLReader parser = XMLReaderFactory.createXMLReader();
		parser.setContentHandler(new GatewayDeviceHandler(this));
		parser.parse(new InputSource(urlConn.getInputStream()));

		/* fix urls */
		String ipConDescURL;
		if (this.urlBase != null && this.urlBase.trim().length() > 0) {
			ipConDescURL = this.urlBase;
		}
		else {
			ipConDescURL = this.location;
		}

		int lastSlashIndex = ipConDescURL.indexOf('/', 7);
		if (lastSlashIndex > 0) {
			ipConDescURL = ipConDescURL.substring(0, lastSlashIndex);
		}

		this.sCPDURL = copyOrCatUrl(ipConDescURL, this.sCPDURL);
		this.controlURL = copyOrCatUrl(ipConDescURL, this.controlURL);
		this.controlURLCIF = copyOrCatUrl(ipConDescURL, this.controlURLCIF);
		this.presentationURL = copyOrCatUrl(ipConDescURL, this.presentationURL);
	}

	public void setControlURL(String controlURL) {
		this.controlURL = controlURL;
	}

	public void setControlURLCIF(String controlURLCIF) {
		this.controlURLCIF = controlURLCIF;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setDeviceTypeCIF(String deviceTypeCIF) {
		this.deviceTypeCIF = deviceTypeCIF;
	}

	public void setEventSubURL(String eventSubURL) {
		this.eventSubURL = eventSubURL;
	}

	public void setEventSubURLCIF(String eventSubURLCIF) {
		this.eventSubURLCIF = eventSubURLCIF;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	/**
	 * Sets the localAddress
	 * 
	 * @param localAddress
	 *            the address to set
	 */
	public void setLocalAddress(InetAddress localAddress) {
		this.localAddress = localAddress;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public void setPresentationURL(String presentationURL) {
		this.presentationURL = presentationURL;
	}

	public void setSCPDURL(String sCPDURL) {
		this.sCPDURL = sCPDURL;
	}

	public void setSCPDURLCIF(String sCPDURLCIF) {
		this.sCPDURLCIF = sCPDURLCIF;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public void setServiceTypeCIF(String serviceTypeCIF) {
		this.serviceTypeCIF = serviceTypeCIF;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public void setURLBase(String uRLBase) {
		this.urlBase = uRLBase;
	}
}
