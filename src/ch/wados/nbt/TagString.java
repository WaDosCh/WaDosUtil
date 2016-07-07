package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * String Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 */
public final class TagString implements Tag<String> {

	private String value;
	private String name;

	/**
	 * Creates a new String tag with given name and value
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public TagString(String name, String value) {
		this.value = value;
		this.name = name;
	}

	/**
	 * Creates a new String without value.
	 * 
	 * The value defaults to null
	 * 
	 * @param name
	 *            the name
	 */
	public TagString(String name) {
		this.name = name;
		this.value = null;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
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
		int len = (stream.read() << 8);
		len |= stream.read();
		byte[] buffer = new byte[len];
		for (int i = 0; i < len; i++)
			buffer[i] = (byte) stream.read();
		this.value = new String(buffer, "UTF-8");
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		byte[] arr = this.value.getBytes("UTF-8");
		short len = (short) arr.length;
		stream.write((len >> 8) & 0xFF);
		stream.write(len & 0xFF);
		for (byte b : arr)
			stream.write(b);
	}

	@Override
	public byte getTagID() {
		return 0x08;
	}

	@Override
	public String toString() {
		return "TAG_STRING(\"" + this.name + "\") : " + this.value;
	}

}
