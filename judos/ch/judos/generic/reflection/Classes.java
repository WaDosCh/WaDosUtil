package ch.judos.generic.reflection;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;

import sun.reflect.ReflectionFactory;
import ch.judos.generic.data.HashMapR;

/**
 * useful methods to handle class types and find out nearest common superclass
 * 
 * @since 21.02.2013
 * @author Julian Schelker
 * @version 1.1 / 02.03.2013
 */
public class Classes {
	/**
	 * this is where the compiled classes can be found
	 */
	private static final String binFolder = "bin";

	private static HashMapR<Class<?>, Class<?>> autoBoxMap;

	public static HashMapR<Class<?>, Class<?>> getAutoBoxMap() {
		if (autoBoxMap == null) {
			autoBoxMap = new HashMapR<>();
			autoBoxMap.put(Boolean.class, boolean.class);
			autoBoxMap.put(Character.class, char.class);
			autoBoxMap.put(Integer.class, int.class);
			autoBoxMap.put(Long.class, long.class);
			autoBoxMap.put(Float.class, float.class);
			autoBoxMap.put(Double.class, double.class);
		}
		return autoBoxMap;
	}

	/**
	 * @param classes
	 * @return a list of the nearest superclasses
	 */
	public static List<Class<?>> commonSuperClass(Class<?>... classes) {
		// start off with set from first hierarchy
		Set<Class<?>> rollingIntersect = new LinkedHashSet<>(getClassesBfs(classes[0]));
		// intersect with next
		for (int i = 1; i < classes.length; i++) {
			rollingIntersect.retainAll(getClassesBfs(classes[i]));
		}
		return new LinkedList<>(rollingIntersect);
	}

	/**
	 * @param list
	 * @return the nearest common superclass of all classes from the objects in
	 *         the list
	 */
	public static Class<?> commonSuperClassOfObjects(List<?> list) {
		Class<?>[] cl = new Class<?>[list.size()];
		int pos = 0;
		for (Object o : list)
			cl[pos++] = o.getClass();
		return commonSuperClass(cl).get(0);
	}

	/**
	 * <b>Notice:</b> the compiled classes must be found in the defined folder,
	 * this this to work
	 * 
	 * @param packageName
	 * @return all classes directly in this folder, does not go down recursively
	 */
	public static Class<?>[] getClassesFromPackage(String packageName) {
		List<Class<?>> classes = new ArrayList<>();
		File dir = new File(binFolder + "/" + packageName);
		File[] files = dir.listFiles();
		for (File f : files) {
			String fileWithoutType = f.getName().substring(0, f.getName().length() - 6);
			try {
				classes.add(Class.forName(packageName + "." + fileWithoutType));
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return classes.toArray(new Class<?>[]{});
	}

	/**
	 * @param clazz
	 * @return all interfaces and all superclasses of this- and all
	 *         super-classes
	 */
	protected static Set<Class<?>> getClassesBfs(Class<?> clazz) {
		Set<Class<?>> classes = new LinkedHashSet<>();
		Set<Class<?>> nextLevel = new LinkedHashSet<>();
		nextLevel.add(clazz);
		do {
			classes.addAll(nextLevel);
			Set<Class<?>> thisLevel = new LinkedHashSet<>(nextLevel);
			nextLevel.clear();
			for (Class<?> each : thisLevel) {
				Class<?> superClass = each.getSuperclass();
				if (superClass != null && superClass != Object.class) {
					nextLevel.add(superClass);
				}
				for (Class<?> eachInt : each.getInterfaces()) {
					nextLevel.add(eachInt);
				}
			}
		} while (!nextLevel.isEmpty());
		return classes;
	}

	/**
	 * Creates a serialization constructor for a class. This constructor will
	 * create a blank instance without calling any constructor or initialization
	 * block.
	 *
	 * @param clazz
	 *            the class to create a constructor for
	 * @return the created serialization constructor
	 * @throws SecurityException
	 *             if reflection access to the object is denied by the VM
	 */
	public static Constructor<? extends Object> getSerializationConstructor(
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
