package ch.judos.generic.data.csv;

/**
 * Provides the standard characters for separation and unescaping characters.<br>
 * <br>
 * attribute names or values of entries are referred to as fields. csv-file are
 * supposed to follow the following rules: <br>
 * 1. they contain a header defining the attributes <br>
 * 2. fields are separated using always the same character, normally the comma
 * sign (,) <br>
 * 3. if a field contains the separator it is replaced by "@s" <br>
 * 4. if a field contains the unescape it is replaced by "@u" <br>
 * 5. if a field contains a line break it is replaced by "@l" <br>
 * 
 * @since 04.01.2012
 * @author Julian Schelker
 * @version 1.0 / 04.01.2012
 */
public class CSVFile {

	/**
	 * the standard character used as separator between fields
	 */
	public static String separator = ";";
	/**
	 * the standard character to escape characters equals to linebreak or
	 * separators in fields
	 */
	public static String escape = "@";

	/**
	 * the standard character to separate entries
	 */
	public static String linebreak = "\r\n";

	/**
	 * escapes all critical characters in this field
	 * 
	 * @param field
	 * @return escaped string
	 */
	@SuppressWarnings("all")
	public static String encodeForFile(String field) {
		field = field.replaceAll(escape, escape + "u");
		field = field.replaceAll(separator, escape + "s");
		field = field.replaceAll(linebreak, escape + "l");
		return field;
	}

	/**
	 * unescapes all critical characters
	 * 
	 * @param field
	 * @return unescaped string
	 */
	@SuppressWarnings("all")
	public static String decodeForValue(String field) {
		field = field.replaceAll(escape + "l", linebreak);
		field = field.replaceAll(escape + "s", separator);
		field = field.replaceAll(escape + "u", escape);
		return field;
	}

	/**
	 * escapes all critical characters in this field
	 * 
	 * @param fields
	 * @return escaped string array
	 */
	public static String[] encodeForFile(String[] fields) {
		for (int i = 0; i < fields.length; i++)
			fields[i] = encodeForFile(fields[i]);
		return fields;
	}

	/**
	 * unescapes all critical characters
	 * 
	 * @param fields
	 * @return unescaped string array
	 */
	public static String[] decodeForValue(String[] fields) {
		for (int i = 0; i < fields.length; i++)
			fields[i] = decodeForValue(fields[i]);
		return fields;
	}

	private CSVFile() {
	}

}
