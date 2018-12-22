package ch.judos.generic.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * @since 28.04.2012
 * @author Julian Schelker
 * @version 1.0 / 23.02.2013
 */
public class Cloner {
	/**
	 * @param obj
	 * @return the cloned object if possible
	 * @throws CloneNotSupportedException
	 */
	public static Object tryClone(Object obj) throws CloneNotSupportedException {
		if (obj instanceof CloneableJS) {
			CloneableJS c = (CloneableJS) obj;
			return c.clone();
		}
		try {
			return clonePrimObject(obj);
		}
		catch (InvalidParameterException e) {
			// continue as expected
		}

		Class<?> c = obj.getClass();
		if (c.isArray())
			return cloneArrayObjects(obj, c);

		// try to find method to clone
		try {
			Method m = c.getMethod("clone");
			return m.invoke(obj);
		}
		catch (Exception e) {
			// continue
		}
		// try to call a constructor with one argument of the type we want to
		// clone
		try {
			Constructor<?> constr = c.getConstructor(c);
			return constr.newInstance(obj);
		}
		catch (Exception e1) {
			// continue
		}
		// try to call any other constructor
		Constructor<?>[] constrArr = c.getConstructors();
		for (Constructor<?> constr : constrArr) {
			try {
				return constr.newInstance(obj);
			}
			catch (Exception e) {
				// continue
			}
		}
		throw new CloneNotSupportedException("Could not clone element: " + obj);
	}

	private static Object cloneArrayObjects(Object obj, Class<?> c)
		throws CloneNotSupportedException {
		if (!c.isPrimitive()) {
			try {
				Object[] z = (Object[]) obj;
				return z.clone();
			}
			catch (Exception e) {
				// continue
			}
		}
		try {
			if (obj instanceof int[]) {
				int[] t = (int[]) obj;
				return Arrays.copyOf(t, t.length);
			}
			if (obj instanceof double[]) {
				double[] t = (double[]) obj;
				return Arrays.copyOf(t, t.length);
			}
			if (obj instanceof float[]) {
				float[] t = (float[]) obj;
				return Arrays.copyOf(t, t.length);
			}
			if (obj instanceof long[]) {
				long[] t = (long[]) obj;
				return Arrays.copyOf(t, t.length);
			}
			throw new Exception("primitive type not supported yet");
		}
		catch (Exception e) {
			throw new CloneNotSupportedException("Could not clone array");
		}
	}

	private static Object clonePrimObject(Object obj) {
		if (obj instanceof Integer)
			return new Integer((Integer) obj);
		if (obj instanceof Double)
			return new Double((Double) obj);
		if (obj instanceof Long)
			return new Long((Long) obj);
		if (obj instanceof Float)
			return new Float((Float) obj);
		throw new InvalidParameterException("Not an boxing class for an primitive object.");
	}
}
