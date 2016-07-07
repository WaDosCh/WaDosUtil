package ch.wados.util.id;

/**
 * Empty Placeholder for the CompositeIDProvider Arrays
 *
 * @author Andreas Wälchli
 * @version 1.2, 2014-11-17
 */
class CompressedProvider implements IDProvider {

	public static final CompressedProvider FALSE = new CompressedProvider(false);
	public static final CompressedProvider TRUE = new CompressedProvider(true);

	private static void err() {
		throw new UnsupportedOperationException("Placeholder!");
	}

	private final boolean state;

	private CompressedProvider(boolean value) {
		this.state = value;
	}

	@Override
	public boolean free(int id) {
		if (this.state)
			CompressedProvider.err();
		return false;
	}

	@Override
	public boolean isEmpty() {
		return !this.state;
	}

	@Override
	public boolean isFull() {
		return this.state;
	}

	@Override
	public boolean isUsed(int id) {
		return this.state;
	}

	@Override
	public int get() {
		CompressedProvider.err();
		return 0;
	}

	@Override
	public boolean set(int id) {
		if (!this.state)
			CompressedProvider.err();
		return false;
	}

	@Override
	public long getFreeIDCount() {
		CompressedProvider.err();
		return 0;
	}

}
