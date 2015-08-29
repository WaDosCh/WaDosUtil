package ch.judos.generic.games.easymp.test;

import ch.judos.generic.games.easymp.model.UpdatableI;

/**
 * @since 23.08.2015
 * @author Julian Schelker
 */
public class Data implements UpdatableI {
	public TextFieldModel t0, t1, t2;

	public transient Frame frame;

	public Data() {
	}

	@Override
	public void wasUpdated() {
		if (this.t0 != null) {
			this.t0.setup(this.frame.getTextField());
		}
		if (this.t1 != null) {
			this.t1.setup(this.frame.textfield2);
		}
	}
}
