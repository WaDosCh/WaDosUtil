package ch.judos.generic.games.easymp.test;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public class Frame extends JFrame {

	private JTextField textfield;
	public JTextField textfield2;
	Runnable close;
	private static final long serialVersionUID = -5346724217979287597L;

	public Frame(Runnable close) {
		this.close = close;
		this.textfield = new JTextField();
		this.textfield2 = new JTextField();
		this.textfield2.setBackground(new Color(0.9f, 0.9f, 1f));
		this.setLayout(new GridLayout(2, 1));
		this.add(this.textfield);
		this.add(this.textfield2);

		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.addWindowListener(getWindowAdapter());
	}

	public JTextField getTextField() {
		return this.textfield;
	}

	private WindowListener getWindowAdapter() {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Frame.this.close.run();
			}
		};
	}

}
