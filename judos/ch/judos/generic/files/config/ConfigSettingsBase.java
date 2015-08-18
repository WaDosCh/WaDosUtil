package ch.judos.generic.files.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Set;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.reflection.Fields;

/**
 * must be implemented by the class that stores the configuration properties and
 * their default values
 * 
 * @since 21.02.2013
 * @author Julian Schelker
 */
public abstract class ConfigSettingsBase {

	/**
	 * @return the class with public property fields
	 */
	public Class<?> getConfigSettingsClass() {
		return getConfigSettingsObject().getClass();
	}

	/**
	 * @return the configSettings object<br>
	 *         this object should have public property fields. these will be used
	 *         for the config
	 */
	public abstract Object getConfigSettingsObject();

	/**
	 * gets all public property fields from the class that is returned by
	 * getConfigSettingsClass
	 * 
	 * @return the Set of properties to be used
	 */
	public Set<Property> getConfigurationEntries() {
		DynamicList<Field> fields = Fields.getPublicFieldsOfClass(getConfigSettingsClass());
		DynamicList<Object> values = Fields
			.getValuesForFields(getConfigSettingsObject(), fields);
		values = values.filterByClass(Property.class);
		DynamicList<Property> result = values.castOrOmit(new Property[]{});
		return result.toSet();
	}

	// in order to store multiple configurations for different computers
	// in the same folder use some hardware number as part of filename
	/**
	 * @return the file where to store the config data
	 */
	public abstract File getFile();

	/**
	 * @return if dynamic creation of new properties through code is allowed
	 */
	public abstract boolean isRuntimeNewPropertiesAllowed();

	/**
	 * do some custom stuff you want before the save is performed on the config
	 */
	public abstract void save();
}
