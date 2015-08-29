package ch.judos.generic.data.rstorage.types;

import java.io.IOException;
import java.util.regex.Pattern;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;
import ch.judos.generic.data.rstorage.interfaces.RStorableWrapper;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;

/**
 * @since 26.04.2015
 * @author Julian Schelker
 */
public class RStorableFloat2 implements RStorableWrapper {

	private float number;

	@Override
	public boolean isTrackedAsObject() {
		return false;
	}

	@Override
	public boolean showOnOneLine() {
		return true;
	}

	@Override
	public void read(CheckReader2 r, RStoreInternal storage) throws IOException {
		// use word charater (+INFINITY, -INFINITY, 1.0E10)
		// point as decimal separator
		// dash as negative postfix sign
		Pattern p = Pattern.compile("[\\w\\.-]");
		String s = r.readWhile(c -> p.matcher(Character.toString(c)).matches());
		this.number = Float.parseFloat(s);
	}

	@Override
	public void store(RuntimeWriter2 w, RStoreInternal storage) throws IOException {
		w.write(String.valueOf(this.number));
	}

	@Override
	public void initWrapped(Object o) {
		this.number = (float) o;
	}

	@Override
	public Object getWrapped() {
		return this.number;
	}
}
