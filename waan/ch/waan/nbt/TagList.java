package ch.waan.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import ch.waan.util.SerializationConstructorFactory;

/**
 * Tag List
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 */
public final class TagList implements Tag<ArrayList<Tag<?>>> {

	private String name;
	private ArrayList<Tag<?>> value;
	private byte contentType = -1;

	@Override
	public byte getTagID() {
		return 0x09;
	}

	/**
	 * new tag list without name or content
	 */
	public TagList() {
		this.name = null;
		this.value = new ArrayList<>();
	}

	/**
	 * new tag list without type. The type can be bound later
	 * 
	 * @param name
	 *            the name
	 */
	public TagList(String name) {
		this.name = name;
		this.value = new ArrayList<>();
	}

	/**
	 * new tag list with type
	 * 
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 */
	public TagList(String name, byte type) {
		this.name = name;
		this.contentType = type;
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
	public ArrayList<Tag<?>> getValue() {
		return this.value;
	}

	@Override
	public void setValue(ArrayList<Tag<?>> value) {
		this.value = new ArrayList<>();
		for (Tag<?> t : value) {
			if (this.contentType < 0) {
				this.contentType = t.getTagID();
			}
			if (this.contentType != t.getTagID())
				throw new RuntimeException("TAG_LIST only supports one type at a time");
			this.value.add(t);
		}
	}

	/**
	 * appends a tag to the list
	 * 
	 * @param t
	 *            the tag to add
	 * @throws RuntimeException
	 *             if the tag type does not conform to the type of the list
	 */
	public void addValue(Tag<?> t) {
		if (this.contentType < 0) {
			this.contentType = t.getTagID();
		}
		if (this.contentType != t.getTagID())
			throw new RuntimeException("TAG_LIST only supports one type at a time");
		this.value.add(t);
	}

	@Override
	public void read(InputStream stream) throws IOException {
		this.contentType = (byte) stream.read();
		int len = stream.read() << 24;
		len |= stream.read() << 16;
		len |= stream.read() << 8;
		len |= stream.read();
		this.value = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			Tag<?> tag = NBTDecoder.instantiateShallow(this.contentType);
			tag.read(stream);
			this.value.add(tag);
		}
	}

	@Override
	public void write(OutputStream stream) throws IOException {
		stream.write(this.value.isEmpty() ? getID() : this.value.get(0).getTagID());
		int len = this.value.size();
		stream.write((len >> 24) & 0xFF);
		stream.write((len >> 16) & 0xFF);
		stream.write((len >> 8) & 0xFF);
		stream.write(len & 0xFF);
		for (Tag<?> item : this.value) {
			item.write(stream);
		}
	}

	private static byte getID() {
		try {
			return ((Tag<?>) SerializationConstructorFactory.getSerialisationConstructor(
				(Class<?>) ((ParameterizedType) TagList.class.getDeclaredField("value")
					.getGenericType()).getActualTypeArguments()[0]).newInstance()).getTagID();
		}
		catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException("whoops");
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return -1;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TAG_LIST<"
			+ NBTDecoder.getNameTag(this.contentType) + ">(\"" + this.name + "\") : "
			+ this.value.size() + (this.value.size() == 1 ? " entry" : " entries"));
		int i = 0;
		for (Tag<?> e : this.value)
			sb.append("\n [" + (i++) + "] : " + e.toString());
		return sb.toString();
	}

}
