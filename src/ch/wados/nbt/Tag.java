package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base NBT-Tag
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 *
 * @param <T>
 *            tag type
 */
public interface Tag<T> {

	/**
	 * Sets the name of the tag
	 * 
	 * @param name
	 *            the name
	 */
	void setName(String name);

	/**
	 * Sets the value of the tag
	 * 
	 * @param value
	 *            the value
	 */
	void setValue(T value);

	/**
	 * Gets the name of the tag
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the value of the tag
	 * 
	 * @return the value
	 */
	T getValue();

	/**
	 * reads the tag content from the stream
	 * 
	 * @param stream
	 *            the stream to read from
	 * @throws IOException
	 *             if any i/o-exception occurs
	 */
	void read(InputStream stream) throws IOException;

	/**
	 * writes the tag content to the stream
	 * 
	 * @param stream
	 *            the stream to write to
	 * @throws IOException
	 *             if any i/o-exception occurs
	 */
	void write(OutputStream stream) throws IOException;

	/**
	 * returns the tag byte id
	 * 
	 * @return the tag id
	 */
	byte getTagID();

}
