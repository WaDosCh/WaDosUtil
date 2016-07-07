package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Byte Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 */
public final class TagByte implements Tag<Byte> {

	private Byte value;
	private String name;

	@Override
	public byte getTagID() {
		return 0x01;
	}

	/**
	 * new byte tag
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TagByte(String name, Byte value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * new byte tag without value (defaults to zero)
	 * 
	 * @param name
	 *            the name
	 */
	public TagByte(String name) {
		this.name = name;
		this.value = 0;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setValue(Byte value) {
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Byte getValue() {
		return this.value;
	}

	@Override
	public void read(InputStream stream) throws IOException {
		this.value = (byte) stream.read();
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		stream.write(new byte[]{this.value});
	}

	@Override
	public String toString() {
		return "TAG_BYTE(\"" + this.name + "\") : " + this.value.byteValue();
	}

}
