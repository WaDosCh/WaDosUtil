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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX handler used to parse XML data representing a GatewayDevice
 * 
 * @see org.xml.sax.helpers.DefaultHandler
 */
public class GatewayDeviceHandler extends DefaultHandler {

	/** state variables */
	private String currentElement;

	/**
	 * The device that should be populated with data coming from the stream
	 * being parsed
	 */
	private GatewayDevice device;

	@SuppressWarnings("unused")
	private int level = 0;
	private short state = 0;

	/**
	 * Creates a new instance of GatewayDeviceHandler that will populate the
	 * fields of the supplied device
	 * 
	 * @param device
	 *            the device to configure
	 */
	public GatewayDeviceHandler(final GatewayDevice device) {
		this.device = device;
	}

	/**
	 * Receive notification of character data inside an element.
	 * 
	 * It is used to read the values of the relevant fields of the device being
	 * configured.
	 * 
	 * @param ch
	 *            The characters.
	 * @param start
	 *            The start position in the character array.
	 * @param length
	 *            The number of characters to use from the character array.
	 * @exception org.xml.sax.SAXException
	 *                Any SAX exception, possibly wrapping another exception.
	 * @see org.xml.sax.ContentHandler#characters
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (this.currentElement.compareTo("URLBase") == 0)
			this.device.setURLBase(new String(ch, start, length));
		else if (this.state <= 1) {
			if (this.state == 0) {
				if ("friendlyName".compareTo(this.currentElement) == 0)
					this.device.setFriendlyName(new String(ch, start, length));
				else if ("manufacturer".compareTo(this.currentElement) == 0)
					this.device.setManufacturer(new String(ch, start, length));
				else if ("modelDescription".compareTo(this.currentElement) == 0)
					this.device.setModelDescription(new String(ch, start, length));
				else if ("presentationURL".compareTo(this.currentElement) == 0)
					this.device.setPresentationURL(new String(ch, start, length));
				else if ("modelNumber".compareTo(this.currentElement) == 0)
					this.device.setModelNumber(new String(ch, start, length));
				else if ("modelName".compareTo(this.currentElement) == 0)
					this.device.setModelName(new String(ch, start, length));
			}
			if (this.currentElement.compareTo("serviceType") == 0)
				this.device.setServiceTypeCIF(new String(ch, start, length));
			else if (this.currentElement.compareTo("controlURL") == 0)
				this.device.setControlURLCIF(new String(ch, start, length));
			else if (this.currentElement.compareTo("eventSubURL") == 0)
				this.device.setEventSubURLCIF(new String(ch, start, length));
			else if (this.currentElement.compareTo("SCPDURL") == 0)
				this.device.setSCPDURLCIF(new String(ch, start, length));
			else if (this.currentElement.compareTo("deviceType") == 0)
				this.device.setDeviceTypeCIF(new String(ch, start, length));
		}
		else if (this.state == 2) {
			if (this.currentElement.compareTo("serviceType") == 0)
				this.device.setServiceType(new String(ch, start, length));
			else if (this.currentElement.compareTo("controlURL") == 0)
				this.device.setControlURL(new String(ch, start, length));
			else if (this.currentElement.compareTo("eventSubURL") == 0)
				this.device.setEventSubURL(new String(ch, start, length));
			else if (this.currentElement.compareTo("SCPDURL") == 0)
				this.device.setSCPDURL(new String(ch, start, length));
			else if (this.currentElement.compareTo("deviceType") == 0)
				this.device.setDeviceType(new String(ch, start, length));

		}
	}

	/**
	 * Receive notification of the end of an element.
	 * 
	 * Used to update state information.
	 * 
	 * <p>
	 * By default, do nothing. Application writers may override this method in a
	 * subclass to take specific actions at the end of each element (such as
	 * finalising a tree node or writing output to a file).
	 * </p>
	 * 
	 * @param uri
	 *            The Namespace URI, or the empty string if the element has no
	 *            Namespace URI or if Namespace processing is not being
	 *            performed.
	 * @param localName
	 *            The local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed.
	 * @param qName
	 *            The qualified name (with prefix), or the empty string if
	 *            qualified names are not available.
	 * @exception org.xml.sax.SAXException
	 *                Any SAX exception, possibly wrapping another exception.
	 * @see org.xml.sax.ContentHandler#endElement
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		this.currentElement = "";
		this.level--;
		if (localName.compareTo("service") == 0) {
			if (this.device.getServiceTypeCIF() != null
				&& this.device.getServiceTypeCIF().compareTo(
					"urn:schemas-upnp-org:service:WANCommonInterfaceConfig:1") == 0)
				this.state = 2;
			if (this.device.getServiceType() != null
				&& this.device.getServiceType().compareTo(
					"urn:schemas-upnp-org:service:WANIPConnection:1") == 0)
				this.state = 3;
		}
	}

	/**
	 * Receive notification of the start of an element.
	 * 
	 * Caches the element as currentElement, and keeps track of some basic state
	 * information.
	 * 
	 * @param uri
	 *            The Namespace URI, or the empty string if the element has no
	 *            Namespace URI or if Namespace processing is not being
	 *            performed.
	 * @param localName
	 *            The local name (without prefix), or the empty string if
	 *            Namespace processing is not being performed.
	 * @param qName
	 *            The qualified name (with prefix), or the empty string if
	 *            qualified names are not available.
	 * @param attributes
	 *            The attributes attached to the element. If there are no
	 *            attributes, it shall be an empty Attributes object.
	 * @exception org.xml.sax.SAXException
	 *                Any SAX exception, possibly wrapping another exception.
	 * @see org.xml.sax.ContentHandler#startElement
	 */
	@Override
	public void
		startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		this.currentElement = localName;
		this.level++;
		if (this.state < 1 && "serviceList".compareTo(this.currentElement) == 0) {
			this.state = 1;
		}
	}

}
