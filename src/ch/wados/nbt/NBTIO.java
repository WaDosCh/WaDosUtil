package ch.wados.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO handler for NBT data
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 */
public class NBTIO {

	/**
	 * Writes the tag to the stream
	 * 
	 * @param tag
	 *            the tag to write
	 * @param stream
	 *            the target stream
	 * @throws IOException
	 *             if anything goes wrong
	 */
	public static void writeTag(Tag<?> tag, OutputStream stream) throws IOException {
		stream.write(tag.getTagID());
		if (tag instanceof TagEnd)
			return;
		byte[] arr = tag.getName().getBytes("UTF-8");
		short len = (short) arr.length;
		stream.write((len >> 8) & 0xFF);
		stream.write(len & 0xFF);
		for (byte b : arr)
			stream.write(b);
		tag.write(stream);
	}

	/**
	 * Reads a tag from the given input stream
	 * 
	 * @param stream
	 *            the stream to read from
	 * @return the read tag
	 * @throws IOException
	 *             if anything goes wrong
	 */
	public static Tag<?> readTag(InputStream stream) throws IOException {
		byte code = (byte) stream.read();
		Tag<?> t = NBTDecoder.instantiateShallow(code);
		if (code == 0)
			return t;
		int len = (stream.read() << 8);
		len |= stream.read();
		byte[] buffer = new byte[len];
		for (int i = 0; i < len; i++)
			buffer[i] = (byte) stream.read();
		String name = new String(buffer, "UTF-8");
		t.setName(name);
		t.read(stream);
		return t;
	}

}
