package ch.waan.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Double Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 * @version 1.1, 2015-05-09
 *
 */
public final class TagDouble implements Tag<Double> {

	private Double value;
	private String name;

	@Override
	public byte getTagID() {
		return 0x06;
	}

	/**
	 * new double tag without value (Defaults to zero)
	 * 
	 * @param name
	 *            the name
	 */
	public TagDouble(String name) {
		this.name = name;
		this.value = 0.0;
	}

	/**
	 * new double tag
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TagDouble(String name, Double value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void setValue(Double value) {
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
		this.value = Double.valueOf(Double.longBitsToDouble(val));
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		long val = Double.doubleToRawLongBits(this.value.doubleValue());
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
		return "TAG_DOUBLE(\"" + this.name + "\") : " + this.value.doubleValue();
	}
}
