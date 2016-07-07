package ch.wados.util.id;

/**
 * This Factory is designed to provide {@link IDProvider} instances with a
 * desired range.
 *
 * @author Andreas Wälchli
 * @version 1.1, 2014-10-21
 */
public final class IDProviderFactory {

	/**
	 * generates and returns an {@link IDProvider IDProvider} able to hold the
	 * specified address length.<br>
	 * For any value {@code n} of {@code size} the Provider will be able to hold
	 * {@code 2^8n} addresses. The minimal size however is 8 bits (complete
	 * {@code short} range) and the maximal size is 32 bit (complete {@code int}
	 * range). Therefore the available Provider sizes are 8, 16, 24 and 32 bit.
	 *
	 * @param size
	 *            the address size in bytes (must be 1, 2, 3 or 4). Any other
	 *            value will throw an Exception.
	 * @return the newly generated {@link IDProvider IDProvider} of the
	 *         specified size
	 * @throws IllegalArgumentException
	 *             if the requested size is larger than 4 or smaller than 1
	 */
	public static IDProvider getIDProvider(byte size) {
		return IDProviderFactory.getIDProvider(size, false);
	}

	static IDProvider getIDProvider(byte size, boolean preset) {
		switch (size) {
			case 1 :
				return new BaseIDProvider(preset);
			case 2 :
			case 3 :
			case 4 :
				return new LayeredIDProvider(size - 1, preset);
			default :
				throw new IllegalArgumentException(
					"Provider size must be at least 1 and at most 4. " + size
						+ " is no valid size!");
		}
	}
}
