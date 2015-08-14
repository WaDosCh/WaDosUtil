package ch.judos.generic.games.easymp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * @since 23.05.2015
 * @author Julian Schelker
 */
public class FieldInformation {

	public static boolean isFieldPrimitive(Field f) {
		Class<?> type = f.getType();
		return type.isPrimitive() || type == String.class;
	}

	public static ArrayList<Field> getRelevantFieldsFor(Object obj) {
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

}
