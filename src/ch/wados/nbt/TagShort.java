package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Short Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 */
public final class TagShort implements Tag<Short> {

	private Short value;
	private String name;

	@Override
	public byte getTagID() {
		return 0x02;
	}

	/**
	 * new short tag without value (default to zero)
	 * 
	 * @param name
	 *            the name
	 */
	public TagShort(String name) {
		this.name = name;
		this.value = 0;
	}

	/**
	 * new short tag
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TagShort(String name, Short value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setValue(Short value) {
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Short getValue() {
		return this.value;
	}

	@Override
	public void read(InputStream stream) throws IOException {
		int val = 0;
		val = (stream.read() << 8);
		val |= stream.read();
		this.value = (short) val;
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		short val = this.value.shortValue();
		stream.write((val >> 8) & 0xFF);
		stream.write(val & 0xFF);
	}

	@Override
	public String toString() {
		return "TAG_SHORT(\"" + this.name + "\") : " + this.value.shortValue();
	}

}
