package ch.judos.generic.data;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 * @param <P>
 */
public class Progress<P> {
	private float progress;
	private P p;

	/**
	 * @param f
	 *            progress
	 * @param p
	 *            the data
	 */
	public Progress(float f, P p) {
		this.progress = f;
		this.p = p;
	}

	/**
	 * @return the data
	 */
	public P getObject() {
		return this.p;
	}

	/**
	 * @return progress [0,1]
	 */
	public float getProgress() {
		return this.progress;
	}
}