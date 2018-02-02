package ch.judos.generic.network.udplib;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import ch.judos.generic.data.Serializer;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;

/**
 * @author Julian Schelker
 */
public class ShortDocumentSendTest implements UdpListener {

	private Udp4I udp;
	private Object received;
	private int sendPackages = 100;
	private int receivedPackages = 0;

	@Before
	public void setUp() throws Exception {
		this.udp = UdpLib.createDefault();
	}

	@Test
	public void testSendAndReceive() throws InterruptedException, SerializerException,
		IOException {
		int port = this.udp.getLocalPort();
		String xml =
			"<root><type>helloWorld</type><data><text>Hallo Welt!</text></data></root>";
		Document sent = Serializer.text2Document(xml);
		this.udp.addObjectListener(this);
		InetSocketAddress target = new InetSocketAddress("127.0.0.1", port);
		for (int i = 0; i < this.sendPackages; i++) {
			this.udp.sendObjectConfirmTo(sent, false, target);
			Thread.sleep(20);
		}

		String xml1 = Serializer.document2Text(sent);
		String xml2 = Serializer.document2Text((Document) this.received);
		assertEquals(xml1, xml2);
		Thread.sleep(20);
		System.out.println("Packages lost: " + (this.sendPackages - this.receivedPackages));
	}

	@After
	public void tearDown() {
		this.udp.dispose();
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		this.received = data;
		this.receivedPackages++;
	}
}
