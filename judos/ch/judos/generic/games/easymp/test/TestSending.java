package ch.judos.generic.games.easymp.test;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.games.easymp.MonitoredObjectStorage;
import ch.judos.generic.games.easymp.msgs.ObjectUpdateMsg;
import ch.judos.generic.games.easymp.msgs.UpdateMsg;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;


/**
 * @since 07.06.2015
 * @author Julian Schelker
 */
public class TestSending {

	public static void main(String[] args) throws SerializerException, IOException {
		Udp4I udp = UdpLib.createDefault();
		
		MonitoredObjectStorage storage = new MonitoredObjectStorage("");
		A monitored = new A();
		storage.addStaticObject(monitored);
		UpdateMsg msg = new ObjectUpdateMsg(monitored,storage);
		
		udp.sendObjectConfirmTo(msg, false, new InetSocketAddress("localhost", udp.getLocalPort()));
		
		udp.dispose();
	}
	
	
	
	public static class A implements Serializable {

		private static final long	serialVersionUID	= 1L;
	
		public int number = 0;
	}

}
