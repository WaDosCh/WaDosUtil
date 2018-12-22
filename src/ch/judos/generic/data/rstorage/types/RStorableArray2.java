package ch.judos.generic.data.rstorage.types;

import java.io.IOException;
import java.util.ArrayList;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;
import ch.judos.generic.data.rstorage.interfaces.RStorableWrapper;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;

/**
 * @since 27.04.2015
 * @author Julian Schelker
 */
public class RStorableArray2 implements RStorableWrapper {

	private Object[] array;

	@Override
	public boolean isTrackedAsObject() {
		return true;
	}

	@Override
	public boolean showOnOneLine() {
		return this.array.length < 5;
	}

	@Override
	public void read(CheckReader2 r, RStoreInternal storage) throws IOException {
		ArrayList<Object> objects = new ArrayList<>();
		Class<?> assumeType = this.array.getClass().getComponentType();
		r.readMustMatch("{");
		do {
			if (r.readNextMatches("}"))
				break;
			Object x = storage.read(r, assumeType);
			objects.add(x);
			if (!r.readIfNextMatchesThenConsume(" "))
				break;
		} while (true);
		r.readMustMatch("}");

		this.array = objects.toArray(this.array);
	}

	@Override
	public void store(RuntimeWriter2 wr, RStoreInternal storage) throws IOException {
		Class<?> contentClass = this.array.getClass().getComponentType();
		wr.write("{");
		boolean firstEntry = true;
		for (Object o : this.array) {
			if (!firstEntry)
				wr.write(" ");
			firstEntry = false;
			boolean storeTyp = !contentClass.equals(o.getClass());
			storage.store(o, wr, storeTyp);
		}
		wr.write("}");
	}
	@Override
	public void initWrapped(Object o) {
		this.array = (Object[]) o;
	}

	@Override
	public Object getWrapped() {
		return this.array;
	}

}
