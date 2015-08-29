package ch.waan.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Keyboard Implementation. Registers Action listeners to the GUI
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-02-27
 */
final class KeyboardImpl implements Keyboard {

	final @NonNull Set<Integer> keyList;

	private final KeyAdapter adapter = new KeyAdapter() {

		@Override
		public void keyPressed(final KeyEvent e) {
			KeyboardImpl.this.keyList.add(Integer.valueOf(e.getKeyCode()));
		}

		@Override
		public void keyReleased(final KeyEvent e) {
			KeyboardImpl.this.keyList.remove(Integer.valueOf(e.getKeyCode()));
		}

	};

	KeyboardImpl(final @NonNull JFrame frame) {
		frame.addKeyListener(this.adapter);
		this.keyList = new HashSet<>();
	}

	@Override
	public boolean isPressed(final int keyCode) {
		return this.keyList.contains(Integer.valueOf(keyCode));
	}

}
