package ch.judos.generic.files.config;

import ch.judos.generic.data.Serializer;
import ch.judos.generic.data.SerializerException;
import ch.judos.generic.math.ConvertNumber;

/**
 * a property is some information that remains as configuration of your software
 * after termination and until the next use of the software
 * 
 * @since 21.02.2013
 * @author Julian Schelker
 */
public class Property {

	/**
	 * how to initialize the value if no value is yet stored
	 */
	protected String defaultValue;
	/**
	 * if set to true no changes are allowed afterwards
	 */
	protected boolean readOnly;
	/**
	 * how this property should be called
	 */
	protected final String name;
	/**
	 * the current value of the property
	 */
	protected String value;
	private boolean changeDisabled;

	/**
	 * @param name
	 *            how this property should be called
	 * @param readOnly
	 *            if set to true no changes are allowed afterwards
	 * @param defaultValue
	 *            how to initialize the value if no value is yet stored
	 */
	public Property(String name, boolean readOnly, boolean defaultValue) {
		this(name, readOnly, String.valueOf(defaultValue));
	}

	/**
	 * @param name
	 *            how this property should be called
	 * @param readOnly
	 *            if set to true no changes are allowed afterwards
	 * @param defaultValue
	 *            how to initialize the value if no value is yet stored
	 */
	public Property(String name, boolean readOnly, int defaultValue) {
		this(name, readOnly, String.valueOf(defaultValue));
	}

	/**
	 * @param name
	 *            how this property should be called
	 * @param readOnly
	 *            if set to true no changes are allowed afterwards
	 * @param defaultValue
	 *            how to initialize the value if no value is yet stored
	 */
	@SuppressWarnings("all")
	public Property(String name, boolean readOnly, String defaultValue) {
		this.name = name;
		this.readOnly = readOnly;
		if (defaultValue == null)
			defaultValue = "";
		this.defaultValue = defaultValue;
		this.value = null;
	}

	/**
	 * no setValue(x) will do anything until enableChanges() is called<br>
	 * can be useful while building up a ui. changes on the ui don't get stored
	 * until the ui is complete
	 */
	public void disableChanges() {
		this.changeDisabled = true;
	}

	/**
	 * undoes disableChanges()
	 */
	public void enableChanges() {
		this.changeDisabled = false;
	}

	/**
	 * @return the value of this property interpretated as boolean
	 */
	public boolean getBoolean() {
		validate();
		return Boolean.valueOf(this.value);
	}

	public double getDouble() {
		validate();
		return Double.valueOf(this.value);
	}

	/**
	 * @return the name of the property
	 */
	public String getID() {
		validate();
		return this.name;
	}

	/**
	 * @return the value as integer
	 * @throws NumberFormatException
	 *             - if the string cannot be parsed as an integer.
	 */
	public int getInt() {
		validate();
		return Integer.valueOf(this.value);
	}

	public Object getObject() throws SerializerException {
		String back = this.value.replaceAll("@r", "\r");
		back = back.replaceAll("@n", "\n");
		back = back.replaceAll("@@", "@");

		return Serializer.bytes2object(back.getBytes());
	}

	/**
	 * @return the value of this property
	 */
	public String getString() {
		validate();
		return this.value;
	}

	public boolean isValidDouble() {
		return ConvertNumber.isDouble(this.value);
	}

	public boolean isValidInt() {
		return ConvertNumber.isInt(this.value);
	}

	/**
	 * on value=true no setValue(x) will do anything until enableChanges() is
	 * called<br>
	 * can be useful while building up a ui. changes on the ui don't get stored
	 * until the ui is complete
	 * 
	 * @param value
	 */
	public void setChangeEnabled(boolean value) {
		this.changeDisabled = !value;
	}

	/**
	 * changes the value
	 * 
	 * @param newValue
	 * @throws RuntimeException
	 *             - if the property is readonly
	 */
	public void setValue(boolean newValue) {
		setValue(String.valueOf(newValue));
	}

	public void setValue(double newValue) {
		setValue(String.valueOf(newValue));
	}

	/**
	 * changes the value
	 * 
	 * @param newValue
	 * @throws RuntimeException
	 *             - if the property is readonly
	 */
	public void setValue(int newValue) {
		setValue(String.valueOf(newValue));
	}

	public void setValue(Object obj) throws SerializerException {
		setValue(new String(Serializer.object2Bytes(obj)));
		this.value = this.value.replaceAll("@", "@@");
		this.value = this.value.replaceAll("\n", "@n");
		this.value = this.value.replaceAll("\r", "@r");
	}

	/**
	 * changes the value
	 * 
	 * @param newValue
	 * @throws RuntimeException
	 *             - if the property is readonly
	 */
	public void setValue(String newValue) {
		validate();
		if (this.changeDisabled)
			return;
		if (this.readOnly)
			throw new RuntimeException("This property is readonly");
		this.value = newValue;
	}

	/**
	 * checks whether the propety was initialized correctly (by the config
	 * object)
	 */
	protected void validate() {
		if (this.value == null)
			throw new RuntimeException(
				"Invalid Property object, Properties can only be generated by the ConfigSettings object and must be passed to the Config object.");
	}

}
