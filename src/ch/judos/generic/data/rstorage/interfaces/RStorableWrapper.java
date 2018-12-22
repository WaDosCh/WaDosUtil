package ch.judos.generic.data.rstorage.interfaces;

/**
 * @since 24.04.2015
 * @author Julian Schelker
 */
public interface RStorableWrapper extends RStorableManual2 {

	public void initWrapped(Object o);

	public Object getWrapped();
}
