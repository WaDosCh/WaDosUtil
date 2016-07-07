package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Compound Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 */
@SuppressWarnings("hiding")
public final class TagCompound implements Tag<HashMap<String, Tag<?>>> {

	@Override
	public byte getTagID() {
		return 0x0A;
	}

	private String name;
	private HashMap<String, Tag<?>> entries;

	/**
	 * creates a new compound tag
	 * 
	 * @param name
	 *            the tag name
	 */
	public TagCompound(String name) {
		this.name = name;
		this.entries = new HashMap<>();
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
	public HashMap<String, Tag<?>> getValue() {
		return this.entries;
	}

	@Override
	public void setValue(HashMap<String, Tag<?>> entries) {
		this.entries = entries;
	}

	public void addTag(Tag<?> tag) {
		this.entries.put(tag.getName(), tag);
	}

	public Tag<?> getTag(String name) {
		return this.entries.get(name);
	}

	public void setByte(String name, byte value) {
		this.addTag(new TagByte(name, value));
	}

	public byte getByte(String name) {
		return ((TagByte) this.entries.get(name)).getValue();
	}

	public void setShort(String name, short value) {
		this.addTag(new TagShort(name, value));
	}

	public short getShort(String name) {
		return ((TagShort) this.getTag(name)).getValue();
	}

	public void setInt(String name, int value) {
		this.addTag(new TagInt(name, value));
	}

	public int getInt(String name) {
		return ((TagInt) this.getTag(name)).getValue();
	}

	public void setLong(String name, long value) {
		this.addTag(new TagLong(name, value));
	}

	public long getLong(String name) {
		return ((TagLong) this.getTag(name)).getValue();
	}

	public void setFloat(String name, float value) {
		this.addTag(new TagFloat(name, value));
	}

	public float getFloat(String name) {
		return ((TagFloat) this.getTag(name)).getValue();
	}

	public void setDouble(String name, double value) {
		this.addTag(new TagDouble(name, value));
	}

	public double getDouble(String name) {
		return ((TagDouble) this.getTag(name)).getValue();
	}

	public void setByteArray(String name, byte[] value) {
		Byte[] arr = new Byte[value.length];
		for (int i = 0; i < value.length; i++)
			arr[i] = value[i];
		this.addTag(new TagByteArray(name, arr));
	}

	public byte[] getByteArray(String name) {
		Byte[] box = ((TagByteArray) this.getTag(name)).getValue();
		byte[] arr = new byte[box.length];
		for (int i = 0; i < arr.length; i++)
			arr[i] = box[i].byteValue();
		return arr;
	}

	public void setString(String name, String value) {
		this.addTag(new TagString(name, value));
	}

	public String getString(String name) {
		return ((TagString) this.getTag(name)).getValue();
	}

	public void setCompound(TagCompound compound) {
		this.addTag(compound);
	}

	public TagCompound getCompound(String name) {
		return (TagCompound) this.getTag(name);
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		for (Tag<?> t : this.entries.values())
			NBTIO.writeTag(t, stream);
		stream.write(0x00);
	}

	@Override
	public void read(InputStream stream) throws IOException {
		this.entries = new HashMap<>();
		Tag<?> tag = null;
		while (!((tag = NBTIO.readTag(stream)) instanceof TagEnd)) {
			this.addTag(tag);
		}
	}

	// TODO: toString implementation
}
