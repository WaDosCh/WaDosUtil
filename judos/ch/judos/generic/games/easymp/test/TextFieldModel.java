package ch.judos.generic.games.easymp.test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.JTextField;

import ch.judos.generic.games.easymp.Monitor;
import ch.judos.generic.games.easymp.model.UpdatableI;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public class TextFieldModel extends KeyAdapter implements UpdatableI, Serializable {

	private static final long serialVersionUID = -8659183434463452484L;

	public String text;

	public transient JTextField textField;

	public TextFieldModel(JTextField textField) {
		this.textField = textField;
		setup(textField);
	}

	public void setup(JTextField textField) {
		this.textField = textField;
		this.textField.removeKeyListener(this);
		this.textField.addKeyListener(this);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.text = this.textField.getText();
		Monitor.getMonitor().forceUpdate(this);
	}

	@Override
	public void wasUpdated() {
		if (this.textField != null)
			this.textField.setText(this.text);
		else {
			System.err.println("Can't update text, no textfield is set for: " + this);
		}
	}
}
