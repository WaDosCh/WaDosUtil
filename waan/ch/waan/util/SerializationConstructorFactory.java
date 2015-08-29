package ch.waan.util;

import java.lang.reflect.Constructor;

import sun.reflect.ReflectionFactory;

/**
 * This factory allows for the generation of empty constructors that bypass any
 * init blocks.
 *
 * <b>This feature should be used with care!</b>
 *
 * @author Andreas Wälchli
 * @version 1.1, 2014-11-21
 */
public final class SerializationConstructorFactory {

	/**
	 * Creates a serialisation constructor for a class. This constructor will
	 * create a blank instance without calling any constructor or initialisation
	 * block.
	 *
	 * @param clazz
	 *            the class to create a constructor for
	 * @return the created serialisation constructor
	 * @throws SecurityException
	 *             if reflection access to the object is denied by the VM
	 */
	public static Constructor<? extends Object> getSerialisationConstructor(
		Class<? extends Object> clazz) {
		try {
			return ReflectionFactory.getReflectionFactory().newConstructorForSerialization(
				clazz, Object.class.getDeclaredConstructor());
		}
		catch (NoSuchMethodException e) {
			return null;
		}
	}

}
