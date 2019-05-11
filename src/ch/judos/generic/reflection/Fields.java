package ch.judos.generic.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import ch.judos.generic.data.DynamicList;

/**
 * provides useful methods in relation to fields
 * 
 * @since 02.03.2013
 * @author Julian Schelker
 * @version 1.0 / 02.03.2013
 */
public class Fields {
	/**
	 * @param c
	 * @return all public fields for the given class
	 */
	public static DynamicList<Field> getPublicFieldsOfClass(Class<?> c) {
		DynamicList<Field> r = new DynamicList<>();
		for (Field f : c.getFields()) {
			if (Modifier.isPublic(f.getModifiers()))
				r.add(f);
		}
		return r;
	}

	/**
	 * @param obj
	 * @param fields
	 * @return all values for the given object and fields in the same order
	 */
	public static DynamicList<Object> getValuesForFields(Object obj, List<Field> fields) {
		DynamicList<Object> r = new DynamicList<>();
		for (Field f : fields) {
			try {
				r.add(f.get(obj));
			}
			catch (Exception e) {
				e.printStackTrace();
				r.add(null);
			}
		}
		return r;
	}

	@SuppressWarnings("all")
	public static Field getAnyField(Class<?> c, String name) {
		while (c != null) {
			for (Field f : c.getDeclaredFields())
				if (f.getName().equals(name))
					return f;
			c = c.getSuperclass();
		}
		return null;
	}

	public static DynamicList<Field> getAllFieldsAndInherited(Object obj) {
		return getAllFieldsAndInherited(obj.getClass());
	}

	@SuppressWarnings("all")
	public static DynamicList<Field> getAllFieldsAndInherited(Class<?> clazz) {
		DynamicList<Field> r = new DynamicList<>();
		while (clazz != null) {
			r.addAll(clazz.getDeclaredFields());
			clazz = clazz.getSuperclass();
		}
		return r;
	}

}
