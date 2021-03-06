package ch.judos.generic.os.fileDrop;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;

/**
 * A simple example showing how to use {@link FileDrop}
 * 
 * @author Robert Harder, rob@iharder.net
 */
public class Example {

	/** Runs a sample program that shows dropped files */
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("FileDrop");
		Border dragBorder = BorderFactory.createLineBorder(Color.GREEN, 2);
		final javax.swing.JTextArea text = new javax.swing.JTextArea();
		frame.getContentPane().add(new javax.swing.JScrollPane(text), java.awt.BorderLayout.CENTER);

		new FileDrop(System.out, text, dragBorder, new FileDrop.Listener() {
			@Override
			public void filesDropped(java.io.File[] files) {
				for (int i = 0; i < files.length; i++) {
					try {
						text.append(files[i].getCanonicalPath() + "\n");
					} // end try
					catch (java.io.IOException e) {
						// ok
					}
				} // end for: through each dropped file
			} // end filesDropped
		}); // end FileDrop.Listener

		frame.setBounds(100, 100, 300, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	} // end main

}
