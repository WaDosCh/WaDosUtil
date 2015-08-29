package ch.judos.generic.data.rstorage.model;

import java.util.function.Supplier;

import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;

/**
 * @since 10.05.2015
 * @author Julian Schelker
 */
public class DynEscapeSeq {

	private RuntimeWriter2 writer;
	private Supplier<Boolean> test;

	public DynEscapeSeq(Supplier<Boolean> test, RuntimeWriter2 wr) {
		this.test = test;
		this.writer = wr;
	}

	@Override
	public String toString() {
		if (this.test.get()) {
			this.writer.setEscaped(true);
		}
		return "";
	}

}
