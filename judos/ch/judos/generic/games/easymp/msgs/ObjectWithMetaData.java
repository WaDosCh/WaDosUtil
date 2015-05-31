package ch.judos.generic.games.easymp.msgs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import ch.judos.generic.games.easymp.FieldInformation;
import ch.judos.generic.games.easymp.MonitoredObjectStorage;
import ch.judos.generic.games.easymp.ObjectId;

/**
 * @since 31.05.2015
 * @author Julian Schelker
 */
public class ObjectWithMetaData {

	ObjectId	id;

	/**
	 * for primitive fields contains the value directly<br>
	 * for object fields this contains a reference to another ObjectWithMetaData
	 * object
	 */
	Object[]	fields;

	/**
	 * stores the actual class type of this object
	 */
	Class<?>	clazz;

	public static ObjectWithMetaData fromObject(Object obj, MonitoredObjectStorage storage) {
		try {
			ObjectWithMetaData result = new ObjectWithMetaData();
			result.id = storage.getIdOf(obj);
			if (result.id == null)
				throw new RuntimeException("No id found in storage for object: " + obj);
			result.clazz = obj.getClass();

			ArrayList<Field> fieldsList = FieldInformation.getRelevantFieldsFor(obj);
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
