package ch.waan.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Long Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 */
public final class TagLong implements Tag<Long> {

	private Long value;
	private String name;

	@Override
	public byte getTagID() {
		return 0x04;
	}

	/**
	 * new long tag without value (defaults to zero)
	 * 
	 * @param name
	 *            the name
	 */
	public TagLong(String name) {
		this.name = name;
		this.value = 0L;
	}

	/**
	 * new long tag
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TagLong(String name, Long value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public Long getValue() {
		return this.value;
	}

	@Override
	public void setValue(Long value) {
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void read(InputStream stream) throws IOException {
		long val = ((long) stream.read()) << 56;
		val |= ((long) stream.read()) << 48;
		val |= ((long) stream.read()) << 40;
		val |= ((long) stream.read()) << 32;
		val |= ((long) stream.read()) << 24;
		val |= ((long) stream.read()) << 16;
		val |= ((long) stream.read()) << 8;
		val |= (stream.read());
		this.value = val;
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		long val = this.value.longValue();
		stream.write((int) ((val >> 56) & 0xFFL));
		stream.write((int) ((val >> 48) & 0xFFL));
		stream.write((int) ((val >> 40) & 0xFFL));
		stream.write((int) ((val >> 32) & 0xFFL));
		stream.write((int) ((val >> 24) & 0xFFL));
		stream.write((int) ((val >> 16) & 0xFFL));
		stream.write((int) ((val >> 8) & 0xFFL));
		stream.write((int) (val & 0xFFL));
	}

	@Override
	public String toString() {
		return "TAG_LONG(\"" + this.name + "\") : " + this.value.longValue();
	}
}
