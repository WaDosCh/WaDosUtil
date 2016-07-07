package ch.wados.nbt;

/**
 * Decoder for parsing raw NBT tag headers
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2015-05-09
 */
public class NBTDecoder {

	/**
	 * Initialises a raw type of the type defined by the code parameter
	 * 
	 * @param code
	 *            the byte id of the desired tag
	 * @return the tag
	 */
	public static Tag<?> instantiateShallow(byte code) {
		switch (code) {
			case 0x0 :
				return new TagEnd();
			case 0x1 :
				return new TagByte(null);
			case 0x2 :
				return new TagShort(null);
			case 0x3 :
				return new TagInt(null);
			case 0x4 :
				return new TagLong(null);
			case 0x5 :
				return new TagFloat(null);
			case 0x6 :
				return new TagDouble(null);
			case 0x7 :
				return new TagByteArray(null);
			case 0x8 :
				return new TagString(null);
			case 0x9 :
				return new TagList(null);
			case 0xA :
				return new TagCompound(null);
			default :
				return null;
		}
	}

	/**
	 * Determines the readable tag name for a given byte id
	 * 
	 * @param code
	 *            the id the name is desired for
	 * @return the name
	 */
	public static String getNameTag(byte code) {
		switch (code) {
			case 0x0 :
				return "TAG_END";
			case 0x1 :
				return "TAG_BYTE";
			case 0x2 :
				return "TAG_SHORT";
			case 0x3 :
				return "TAG_INT";
			case 0x4 :
				return "TAG_LONG";
			case 0x5 :
				return "TAG_FLOAT";
			case 0x6 :
				return "TAG_DOUBLE";
			case 0x7 :
				return "TAG_BYTE[]";
			case 0x8 :
				return "TAG_STRING";
			case 0x9 :
				return "TAG_LIST";
			case 0xA :
				return "TAG_COMPOUND";
			default :
				return null;
		}
	}

}
