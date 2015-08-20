package ch.judos.generic.games.easymp.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Timer;
import ch.judos.generic.data.HashMapR;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.games.easymp.Monitor;
import ch.judos.generic.games.easymp.api.PlayerI;
import ch.judos.generic.network.udp.UdpLib;
import ch.judos.generic.network.udp.interfaces.Udp4I;
import ch.judos.generic.network.udp.interfaces.UdpListener;
import ch.judos.generic.wrappers.WrappedTimerTask;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public class Launcher implements UdpListener {

	public static final int							START_PORT	= 20000;

	private Udp4I									udp;
	private boolean									isServer;

	private HashMapR<InetSocketAddress, PlayerI>	playerList;

	private Communicator							communicator;

	private Timer									timer;

	public static void main(String[] args) {
		new Launcher().start();
	}

	private void start() {
		this.playerList = new HashMapR<>();
		initNetwork();
		initMonitor();

		Frame f = new Frame(this::frameWasClosed);
		f.setTitle((this.isServer ? "Server" : "Client") + " " + this.udp.getLocalPort());

		Data d = new Data(f.getTextField());
		Monitor.getMonitor().addMonitoredObject(d);

		if (!this.isServer)
			sendClientJoinToServer();

		Runnable r = () -> Monitor.getMonitor().update();
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new WrappedTimerTask(r), 0, 50);
	}

	private void sendClientJoinToServer() {
		try {
			this.udp.sendRawDataConfirmTo(new byte[]{0}, true, getClient(0));
		}
		catch (SerializerException | IOException e) {
			e.printStackTrace();
		}
	}

	private void initMonitor() {
		this.communicator = new Communicator(this.udp, this.playerList);
		if (this.isServer) {
			Monitor.initializeServer(this.communicator);
		}
		else {
			PlayerI server = new Player();
			this.playerList.put(getClient(0), server);
			Monitor.initializeClient(this.communicator);
		}
	}

	private void frameWasClosed() {
		this.timer.cancel();
		if (!this.isServer)
			sendClientLeaveToServer();
		this.udp.dispose();
		System.exit(0);
	}

	private void sendClientLeaveToServer() {
		try {
			this.udp.sendRawDataConfirmTo(new byte[]{}, false, getClient(0));
			Thread.sleep(100);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private InetSocketAddress getClient(int index) {
		return new InetSocketAddress("localhost", START_PORT + index);
	}

	private void initNetwork() {
		int port = START_PORT;
		do {
			try {
				this.udp = UdpLib.createOnPort(port++);
			}
			catch (SocketException e) {
				System.out.println("port " + (port - 1) + " is not free");
			}
		} while (this.udp == null);
		this.isServer = (this.udp.getLocalPort() == START_PORT);
		if (this.isServer)
			this.udp.addDataListener(this);
	}

	@Override
	public void receiveMsg(Object source, InetSocketAddress from, Object data) {
		byte[] arr = (byte[]) data;
		if (arr.length == 0) {
			System.out.println("Player left: " + from);
			this.playerList.removeByKey(from);
		}
		else if (arr.length == 1) {
			System.out.println("Player joined: " + from);
			Player newClient = new Player();
			this.playerList.put(from, newClient);
			Monitor.getMonitor().syncNewPlayer(newClient);
		}
		else {
			new Exception("Invalid raw data msg received: " + arr).printStackTrace();
		}
	}

}
