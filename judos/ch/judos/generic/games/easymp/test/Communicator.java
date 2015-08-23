package ch.judos.generic.games.easymp.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.judos.generic.data.ArraysJS;
import ch.judos.generic.data.HashMapR;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.games.easymp.api.CommunicatorI;
import ch.judos.generic.games.easymp.api.PlayerI;
import ch.judos.generic.games.easymp.msgs.Message;
import ch.judos.generic.games.easymp.msgs.UpdateMsg;
import ch.judos.generic.network.IP;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public class Communicator implements CommunicatorI, UdpListener {

	private Udp4I									udp;
	private HashMapR<InetSocketAddress, PlayerI>	playerList;
	private ConcurrentLinkedQueue<Message>			messages;

	public Communicator(Udp4I udp, HashMapR<InetSocketAddress, PlayerI> playerList) {
		this.udp = udp;
		this.playerList = playerList;
		this.udp.addObjectListener(this);
		this.messages = new ConcurrentLinkedQueue<>();
	}

	@Override
	public Message receive() {
		return this.messages.poll();
	}

	@Override
	public void send(Object data, PlayerI to) {
		InetSocketAddress target = this.playerList.getFromValue(to);
		try {
			this.udp.sendObjectConfirmTo(data, false, target);
		}
		catch (SerializerException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendToAll(Object data, PlayerI... exclude) {
		for (PlayerI player : this.playerList.getValueSet()) {
			if (!ArraysJS.contains(exclude, player)) {
				send(data, player);
			}
		}
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		if (data instanceof UpdateMsg) {
			UpdateMsg msg = (UpdateMsg) data;
			Message m = new Message(this.playerList.getFromKey(from), msg);
			this.messages.add(m);
		}
		else {
			new Exception("unknown message object received: " + data).printStackTrace();
		}
	}

	@Override
	public String getClientId() {
		return IP.getLocalIpsAsStrings()[0] + ":" + this.udp.getLocalPort();
	}

}
