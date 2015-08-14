package ch.judos.generic.data.rstorage.types;

import java.io.Reader;
import java.io.Writer;
import ch.judos.generic.data.rstorage.ReadableStorage2;
import ch.judos.generic.data.rstorage.helper.RSerializerException;

/**
 * @since 03.05.2015
 * @author Julian Schelker
 */
public class RStorableList implements ReadableStorage2 {

	@Override
	public Object read(Reader r) throws RSerializerException {
		// XXX RStorage implement List serialization
		return null;
	}

	@Override
	public void store(Object o, Writer w) throws RSerializerException {
		// XXX RStorage implement List serialization
		// Map<Class<?>, Long> classesUsed = Arrays.stream(array).collect(
		// Collectors.groupingBy(Object::getClass, Collectors.counting()));
		// Class<?> mostUsed = classesUsed.entrySet().stream().max(
		// (a, b) -> (int) (a.getValue() - b.getValue())).get().getKey();
		// wr.write(" contains=" + mostUsed.getName());

	}

}
