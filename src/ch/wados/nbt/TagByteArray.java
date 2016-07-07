package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * new byte array tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 */
public final class TagByteArray implements Tag<Byte[]> {

	private Byte[] value;
	private String name;

	@Override
	public byte getTagID() {
		return 0x07;
	}

	/**
	 * new byte array tag without content (defaults to an empty zero-length
	 * array)
	 * 
	 * @param name
	 *            the name
	 */
	public TagByteArray(String name) {
		this.name = name;
		this.value = new Byte[0];
	}

	/**
	 * new byte array
	 * 
	 * @param name
	 *            the name
	 * @param values
	 *            the values
	 */
	public TagByteArray(String name, Byte... values) {
		this.name = name;
		this.value = values;
	}

	@Override
	public Byte[] getValue() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setValue(Byte[] value) {
		this.value = value;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void read(InputStream stream) throws IOException {
		int size = stream.read() << 24;
		size |= stream.read() << 16;
		size |= stream.read() << 8;
		size |= stream.read();
		this.value = new Byte[size];
		for (int i = 0; i < size; i++)
			this.value[i] = (byte) stream.read();
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		int size = this.value.length;
		stream.write((size >> 24) & 0xFF);
		stream.write((size >> 16) & 0xFF);
		stream.write((size >> 8) & 0xFF);
		stream.write(size & 0xFF);
		for (Byte b : this.value)
			stream.write(b.byteValue());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TAG_BYTE[](\"" + this.name + "\") : ("
			+ this.value.length + (this.value.length == 1 ? " entry)" : "entries)"));
		int i = 0;
		for (Byte b : this.value) {
			sb.append("\n - [" + (i++) + "] = " + b.byteValue());
		}
		return sb.toString();
	}
}
