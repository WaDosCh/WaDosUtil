package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Float Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 */
public final class TagFloat implements Tag<Float> {

	private String name;
	private Float value;

	/**
	 * new float tag without value (Defaults to zero)
	 * 
	 * @param name
	 *            the name
	 */
	public TagFloat(String name) {
		this.name = name;
		this.value = 0f;
	}

	/**
	 * new float tag
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TagFloat(String name, Float value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Float getValue() {
		return this.value;
	}

	@Override
	public void read(InputStream stream) throws IOException {
		int val = stream.read() << 24;
		val |= stream.read() << 16;
		val |= stream.read() << 8;
		val |= stream.read();
		this.value = Float.valueOf(Float.intBitsToFloat(val));
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		int val = Float.floatToRawIntBits(this.value.floatValue());
		stream.write((val >> 24) & 0xFF);
		stream.write((val >> 16) & 0xFF);
		stream.write((val >> 8) & 0xFF);
		stream.write(val & 0xFF);
	}

	@Override
	public byte getTagID() {
		return 0x05;
	}

	@Override
	public String toString() {
		return "TAG_FLOAT(\"" + this.name + "\") : " + this.value.floatValue();
	}

}
