package ch.wados.nbt;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * End tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 */
public final class TagEnd implements Tag<Void> {

	@Override
	public byte getTagID() {
		return 0x00;
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException("TAG_END is nameless");
	}

	@Override
	public void setValue(Void value) {
		throw new UnsupportedOperationException("TAG_END is nameless");
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException("TAG_END is nameless");
	}

	@Override
	public Void getValue() {
		throw new UnsupportedOperationException("TAG_END is nameless");
	}

	@Override
	public void read(InputStream stream) {
		// nothing to do here
	}

	@Override
	public void write(OutputStream stream) {
		// nothing to do here
	}

	@Override
	public String toString() {
		return "TAG_END";
	}

}
