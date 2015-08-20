package ch.judos.generic.games.easymp.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import ch.judos.generic.games.easymp.FieldInformation;
import ch.judos.generic.games.easymp.MonitoredObjectStorage;
import ch.judos.generic.games.easymp.ObjectId;

/**
 * @since 31.05.2015
 * @author Julian Schelker
 */
public class ObjectWithMetaData implements Serializable {

	private static final long	serialVersionUID	= 8772239159170236859L;

	public ObjectId	id;

	/**
	 * for primitive fields contains the value directly<br>
	 * for object fields this contains a reference to another ObjectWithMetaData
	 * object
	 */
	public Object[]	fields;

	/**
	 * stores the actual class type of this object
	 */
	public Class<?>	clazz;

	public static ObjectWithMetaData fromObject(Object obj, MonitoredObjectStorage storage) {
		try {
			ObjectWithMetaData result = new ObjectWithMetaData();
			result.id = storage.getIdOf(obj);
			if (result.id == null)
				throw new RuntimeException("No id found in storage for object: " + obj);
			result.clazz = obj.getClass();

			ArrayList<Field> fieldsList = FieldInformation.getRelevantFieldsOf(obj);
			result.fields = new Object[fieldsList.size()];

			for (int i = 0; i < fieldsList.size(); i++) {
				Field field = fieldsList.get(i);
				if (FieldInformation.isFieldPrimitive(field))
					result.fields[i] = field.get(obj);
				else
					result.fields[i] = fromObject(field.get(obj), storage);
			}
			return result;
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
