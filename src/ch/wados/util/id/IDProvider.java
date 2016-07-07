package ch.wados.util.id;

/**
 * An ID Provider is a structure with the ability to mark numbers as used,
 * unused, check if a number is used and provide a new unused number with a
 * length of up to 32 bit. The implementation and the generated number sequence
 * do not have to satisfy any further conditions, however the data volume must
 * be considered in any implementation.
 * 
 * @author Andreas Wälchli
 * @version 1.1, 2014-10-21
 */
public interface IDProvider {

	/**
	 * indicates if the Provider has exhausted its ID range.
	 * 
	 * @return {@code true} if the Provider has no free IDs
	 */
	boolean isFull();

	/**
	 * indicates if the given ID is registered. A provider is allowed to only
	 * respect selected bits of the {@code id}.
	 * 
	 * @param id
	 *            the {@code id} to check for.
	 * @return {@code true} if the given{@code id} is used, {@code false}
	 *         otherwise.
	 */
	boolean isUsed(int id);

	/**
	 * mark the given ID as used.
	 * 
	 * @param id
	 *            the {@code id} to register. A provider is allowed to only
	 *            respect selected bits of the {@code id}.
	 * @return {@code true} if the ID actually was set. This can be used
	 *         internally to optimise compression cycles.
	 */
	boolean set(int id);

	/**
	 * mark the given ID as unused.
	 * 
	 * @param id
	 *            the {@code id} to unregister. A provider is allowed to only
	 *            respect selected bits of the {@code id}.
	 * @return {@code true} if the ID actually was freed. This can be used
	 *         internally to optimise compression cycles.
	 */
	boolean free(int id);

	/**
	 * mark an ID as used and return it. The Provider is free in its
	 * implementation. and the generated {@code id} sequence does not have to
	 * satisfy any further conditions.
	 * 
	 * @return the newly locked {@code id}
	 */
	int get();

	/**
	 * indicates if there's no ID used
	 * 
	 * @return {@code true} if no ID has been set.
	 */
	boolean isEmpty();

	/**
	 * retrieves the number of free IDs as an unsigned 32-bit integer.
	 * 
	 * @return the amount of free IDs
	 */
	long getFreeIDCount();

	/**
	 * extracts the local ID from an address
	 * 
	 * @param id
	 *            the ID to extract from
	 * @param layer
	 *            the address layer to extract
	 * @return the ID component associated with the given layer
	 */
	static short localAddress(int id, byte layer) {
		return (short) ((id >>> (8 * layer)) & 0xFF);
	}

}
