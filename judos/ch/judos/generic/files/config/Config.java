package ch.judos.generic.files.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import ch.judos.generic.data.Warning;

/**
 * reads the defined configuration file with all data in it<br>
 * provides methods to set or get configuration variables<br>
 * call save() before ending the program
 * 
 * @since 30.09.2011
 * @author Julian Schelker
 * @version 1.11 / 28.03.2012
 */
public class Config {
	private static final String SPLIT_EXPRESSION = " = ";

	/**
	 * valid properties are stored here
	 */
	protected HashMap<String, Property> data;

	/**
	 * the settings for the config to use
	 */
	private ConfigSettingsBase settings;

	/**
	 * @param settings
	 */
	public Config(ConfigSettingsBase settings) {
		this.settings = settings;
		this.data = new HashMap<>();
		init();
		load();
	}

	/**
	 * @param name
	 *            of the property
	 * @return the property - if not found null is returned
	 */
	public Property getPropertyByName(String name) {
		if (!this.data.containsKey(name)) {
			if (this.settings.isRuntimeNewPropertiesAllowed()) {
				Property p = new Property(name, false, "");
				p.value = p.defaultValue;
				this.data.put(name, p);
				return p;
			}
		}
		return this.data.get(name);
	}

	/**
	 * initializes all properties defined by the ConfigSettings object
	 */
	protected void init() {
		for (Property p : this.settings.getConfigurationEntries()) {
			this.data.put(p.name, p);
			p.value = p.defaultValue;
		}
	}

	/**
	 * loads all configurations out of the file
	 */
	protected void load() {
		try (BufferedReader r = new BufferedReader(new FileReader(this.settings.getFile()))) {

			String line;
			String[] m;
			while ((line = r.readLine()) != null) {
				m = line.split(SPLIT_EXPRESSION, 2);
				Property p = getPropertyByName(m[0]);
				if (p != null)
					p.value = m[1];
				else {
					Warning e = new Warning("Property " + m[0]
						+ " not found in ConfigSettings, value from file is ignored");
					e.printStackTrace();
				}
			}
		}
		catch (FileNotFoundException e1) {
			// just do nothing, because no configs are set yet
			return;
		}
		catch (IOException e) {
			Exception e2 = new RuntimeException(
				"Config file is corrupt, ignoring remaining properties, setting their value to defaultValue.\n"
					+ e.getMessage());
			e2.printStackTrace();
		}
	}

	/**
	 * saves all set data into the config file
	 * 
	 * @return false if an IOException was thrown
	 */
	public boolean save() {
		this.settings.save();
		try (BufferedWriter w = new BufferedWriter(new FileWriter(this.settings.getFile()))) {
			Set<Entry<String, Property>> entries = this.data.entrySet();
			for (Entry<String, Property> entry : entries) {
				w.write(entry.getKey() + SPLIT_EXPRESSION + entry.getValue().getString());
				w.newLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
