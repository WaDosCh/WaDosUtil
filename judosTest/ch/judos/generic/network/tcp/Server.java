package ch.judos.generic.network.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @since 17.07.2013
 * @author Julian Schelker
 */
public class Server implements Runnable {

	public static void main(String argv[]) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(60000);
		boolean running = true;

		while (running) {
			Socket cs = welcomeSocket.accept();
			System.out.println("got connection from " + cs.getInetAddress() + ":"
				+ cs.getPort() + " with local port: " + cs.getLocalPort());
			new Server(cs);
		}

		welcomeSocket.close();
	}

	private BufferedReader	in;
	private BufferedWriter	out;

	public Server(Socket cs) throws IOException {
		this.in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(cs.getOutputStream()));

		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			boolean run = true;
			while (run) {
				String msg = in.readLine();
				System.out.println("received: " + msg);
				if (msg.equals("hello"))
					out.write("re hello!");
				if (msg.equals("bye")) {
					out.write("byebye!");
					run = false;
				}
				out.newLine();
				out.flush();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}