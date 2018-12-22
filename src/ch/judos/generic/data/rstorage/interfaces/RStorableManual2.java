package ch.judos.generic.data.rstorage.interfaces;

import java.io.IOException;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RuntimeWriter2;

/**
 * import this interface to manually overwrite the RSerialization.<br>
 * Use the interface RStorable if you want to use default serialization
 * 
 * @since 20.04.2015
 * @author Julian Schelker
 */
public interface RStorableManual2 {

	/**
	 * @return if true, the storage will serialize one object only once
	 */
	public boolean isTrackedAsObject();

	/**
	 * @return if the objects serializes on one line
	 */
	public boolean showOnOneLine();

	public void read(CheckReader2 r, RStoreInternal storage) throws IOException;
	public void store(final RuntimeWriter2 w, RStoreInternal storage) throws IOException;

}