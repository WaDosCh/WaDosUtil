package ch.judos.generic.network.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @since 17.07.2013
 * @author Julian Schelker
 */
public class Server implements Runnable {

	public static void main(String argv[]) throws Exception {
		try (ServerSocket welcomeSocket = new ServerSocket(60000)) {
			boolean running = true;

			while (running) {
				try (Socket cs = welcomeSocket.accept()) {
					System.out.println("got connection from " + cs.getInetAddress() + ":"
						+ cs.getPort() + " with local port: " + cs.getLocalPort());
					new Server(cs).start();
				}
			}
		}
	}

	private void start() {
		new Thread(this).start();
	}

	private BufferedReader in;
	private BufferedWriter out;

	public Server(Socket cs) throws IOException {
		this.in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(cs.getOutputStream()));
	}

	@Override
	public void run() {
		try {
			boolean run = true;
			while (run) {
				String msg = this.in.readLine();
				System.out.println("received: " + msg);
				if (msg.equals("hello"))
					this.out.write("re hello!");
				if (msg.equals("bye")) {
					this.out.write("byebye!");
					run = false;
				}
				this.out.newLine();
				this.out.flush();
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
