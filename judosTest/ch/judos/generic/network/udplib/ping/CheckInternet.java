package ch.judos.generic.network.udplib.ping;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CheckInternet {

	public static void main(String[] args) {
		Thread.currentThread().setName("MainThread");
		new CheckInternet();
	}

	private JTextArea		textArea;
	private StringBuffer	text;
	private boolean		finished;
	private Process		process;

	public CheckInternet() {
		initGui();
		initPing();
	}

	private void initPing() {
		this.text = new StringBuffer();
		String pingCmd = "ping www.google.ch -t";
		this.finished = false;
		try {
			Runtime r = Runtime.getRuntime();
			this.process = r.exec(pingCmd);

			try (BufferedReader in =
				new BufferedReader(new InputStreamReader(this.process.getInputStream()))) {
				String inputLine;
				while ((inputLine = in.readLine()) != null && !this.finished) {
					addText(inputLine);
				}
			}

		}// try
		catch (IOException e) {
			System.out.println(e);
		}
	}

	private void addText(String ntext) {
		this.text.append("\n" + ntext);
		this.textArea.setText(this.text.toString());
		this.textArea.validate();
	}

	private void initGui() {
		JFrame f = new JFrame("Google ping");
		f.addWindowListener(new WindowListener(this));
		this.textArea = new JTextArea();
		JScrollPane p = new JScrollPane(this.textArea);
		this.textArea.setPreferredSize(new Dimension(300, 500));
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void close() {
		this.finished = true;
		this.process.destroy();
		System.out.println("terminated");
	}

}
