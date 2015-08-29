package ch.judos.generic.data.rstorage.helper;

/**
 * @since 03.05.2015
 * @author Julian Schelker
 */
public class RSerializerException extends Exception {
	private static final long serialVersionUID = -479085228278371501L;

	private Type type;

	public enum Type {
		IO // generic io problem while reading
			, ReadingUntagged // reading an untagged object without an assumed
								// type -> cannot interfere what type to read
			, ClassNotFound // a class to deserialize an object could not be
							// found
			, Instantiation // could not create an instance of a certain type
			, Wrapper // a wrapper object could not be found for this object
			,
	}

	public RSerializerException(String msg, Type t) {
		super(msg);
		this.type = t;
	}

	public RSerializerException(String msg, Exception e, Type t) {
		this(msg + ": " + e.getMessage(), t);
		setStackTrace(e.getStackTrace());
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}

}
