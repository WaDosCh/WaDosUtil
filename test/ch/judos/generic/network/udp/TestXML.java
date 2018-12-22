package ch.judos.generic.network.udp;

import java.io.IOException;
import java.net.InetSocketAddress;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import ch.judos.generic.data.Serializer;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;

/**
 * @since 15.07.2013
 * @author Julian Schelker
 */
public class TestXML extends TestCase implements UdpListener {
	private boolean received;
	private String text;

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		synchronized (this) {
			this.received = true;
			Document d = (Document) data;
			try {
				assertEquals(this.text, Serializer.document2Text(d));
			}
			catch (SerializerException e) {
				fail();
			}
			notify();
		}
	}

	public void testXML() throws SerializerException, IOException, InterruptedException {

		Udp4I u = UdpLib.createDefault();
		u.addObjectListener(this);

		this.text = "<root>Hallo XML Welt!</root>";
		Document d = Serializer.text2Document(this.text);
		this.received = false;
		u.sendObjectConfirmTo(d, true, new InetSocketAddress("localhost", u.getLocalPort()));

		synchronized (this) {
			wait(500);
		}
		assertTrue(this.received);
	}
}
