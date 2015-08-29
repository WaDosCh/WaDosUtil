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

/**
 * A <tt>PortMappingEntry</tt> is the class used to represent port mappings on
 * the GatewayDevice.
 * 
 * A port mapping on the GatewayDevice will allow all packets directed to port
 * <tt>externalPort</tt> of the external IP address of the GatewayDevice using
 * the specified <tt>protocol</tt> to be redirected to port
 * <tt>internalPort</tt> of <tt>internalClient</tt>.
 * 
 * @see ch.judos.generic.network.upnp.GatewayDevice
 * @see ch.judos.generic.network.upnp.GatewayDevice#getExternalIPAddress()
 */
public class PortMappingEntry {

	/**
	 * The internal port
	 */
	private int internalPort;
	/**
	 * The external port of the mapping (the one on the GatewayDevice)
	 */
	private int externalPort;
	/**
	 * The remote host this mapping is associated with
	 */
	private String remoteHost;
	/**
	 * The internal host this mapping is associated with
	 */
	private String internalClient;
	/**
	 * The protocol associated with this mapping (i.e. <tt>TCP</tt> or
	 * <tt>udp</tt>)
	 */
	private String protocol;
	/**
	 * A flag that tells whether the mapping is enabled or not (<tt>"1"</tt> for
	 * enabled, <tt>"0"</tt> for disabled)
	 */
	private String enabled;
	/**
	 * A human readable description of the port mapping (used for display
	 * purposes)
	 */
	private String portMappingDescription;

	/**
	 * Creates a new PortMappingEntry
	 */
	public PortMappingEntry() {
	}

	/**
	 * Gets the internal port for this mapping
	 * 
	 * @return the internalPort
	 */
	public int getInternalPort() {
		return this.internalPort;
	}

	/**
	 * Sets the internalPort
	 * 
	 * @param internalPort
	 *            the port to use
	 */
	public void setInternalPort(int internalPort) {
		this.internalPort = internalPort;
	}

	/**
	 * Gets the external (remote) port for this mapping
	 * 
	 * @return the externalPort
	 */
	public int getExternalPort() {
		return this.externalPort;
	}

	/**
	 * Sets the externalPort
	 * 
	 * @param externalPort
	 *            the port to use
	 */
	public void setExternalPort(int externalPort) {
		this.externalPort = externalPort;
	}

	/**
	 * Gets the remote host this mapping is associated with
	 * 
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return this.remoteHost;
	}

	/**
	 * Sets the remoteHost
	 * 
	 * @param remoteHost
	 *            the host to set
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * Gets the internal host this mapping is associated with
	 * 
	 * @return the internalClient
	 */
	public String getInternalClient() {
		return this.internalClient;
	}

	/**
	 * Sets the internalClient
	 * 
	 * @param internalClient
	 *            the client to set
	 */
	public void setInternalClient(String internalClient) {
		this.internalClient = internalClient;
	}

	/**
	 * Gets the protocol associated with this mapping
	 * 
	 * @return protocol
	 */
	public String getProtocol() {
		return this.protocol;
	}

	/**
	 * Sets the protocol associated with this mapping
	 * 
	 * @param protocol
	 *            one of <tt>TCP</tt> or <tt>udp</tt>
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * Gets the enabled flag (<tt>"1"</tt> if enabled, <tt>"0"</tt> otherwise)
	 * 
	 * @return enabled
	 */
	public String getEnabled() {
		return this.enabled;
	}

	/**
	 * Sets the enabled flag
	 * 
	 * @param enabled
	 *            <tt>"1"</tt> for enabled, <tt>"0"</tt> for disabled
	 */
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the port mapping description
	 * 
	 * @return portMappingDescription
	 */
	public String getPortMappingDescription() {
		return this.portMappingDescription;
	}

	/**
	 * Sets the portMappingDescription
	 * 
	 * @param portMappingDescription
	 *            the description to set
	 */
	public void setPortMappingDescription(String portMappingDescription) {
		this.portMappingDescription = portMappingDescription;
	}
}
