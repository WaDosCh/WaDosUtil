package ch.judos.generic.games.easymp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import ch.judos.generic.exception.RethrowedException;

/**
 * @since 23.05.2015
 * @author Julian Schelker
 */
public class FieldInformation {

	public static boolean isFieldPrimitive(Field f) {
		Class<?> type = f.getType();
		return type.isPrimitive() || type == String.class;
	}

	public static ArrayList<Field> getRelevantFieldsOf(Object obj) {
		ArrayList<Field> result = new ArrayList<>();
		for (Field field : obj.getClass().getFields()) {
			// skip fields
			if (Modifier.isTransient(field.getModifiers()))
				continue;
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			result.add(field);
		}
		return result;
	}

	public static Field getField(Object obj, int fieldNr) {
		ArrayList<Field> list = getRelevantFieldsOf(obj);
		return list.get(fieldNr);
	}

	public static Object getObjectOfField(Object obj, Field field) {
		field.setAccessible(true);
		try {
			return field.get(obj);
		}
		catch (Exception e) {
			throw new RethrowedException(e);
		}
	}
	public static Object getObjectOfField(Object obj, int fieldNr) {
		Field f = getField(obj, fieldNr);
		return getObjectOfField(obj, f);
	}

}
