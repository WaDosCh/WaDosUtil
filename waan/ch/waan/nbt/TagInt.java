package ch.waan.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Int Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 */
public final class TagInt implements Tag<Integer> {

	private String name;
	private Integer value;

	@Override
	public byte getTagID() {
		return 0x03;
	}

	/**
	 * new int tag without value (defaults to zero)
	 * 
	 * @param name
	 *            the name
	 */
	public TagInt(String name) {
		this.name = name;
		this.value = 0;
	}

	/**
	 * new int tag
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TagInt(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Integer getValue() {
		return this.value;
	}

	@Override
	public void read(InputStream stream) throws IOException {
		int val = stream.read() << 24;
		val |= stream.read() << 16;
		val |= stream.read() << 8;
		val |= stream.read();
		this.value = val;
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		int val = this.value.intValue();
		stream.write((val >> 24) & 0xFF);
		stream.write((val >> 16) & 0xFF);
		stream.write((val >> 8) & 0xFF);
		stream.write(val & 0xFF);
	}

	@Override
	public String toString() {
		return "TAG_INT(\"" + this.name + "\") : " + this.value.intValue();
	}
}
