package ch.wados.util.id;

/**
 * high-level IDProvider. This implementation holds multiple lower-level
 * Providers and distributes IDs in a nearly optimal way.
 *
 * @author Andreas Wälchli
 * @version 1.2, 2014-11-17
 */
class LayeredIDProvider implements IDProvider {

	private final int layer;
	private final long maxCount;
	private long usedCount;

	private IDProvider[] subProviders;

	LayeredIDProvider(int layer, boolean defaults) {
		this.layer = layer;
		this.subProviders = new IDProvider[256];
		CompressedProvider p = defaults ? CompressedProvider.TRUE : CompressedProvider.FALSE;
		for (int i = 0; i < 256; i++)
			this.subProviders[i] = p;
		long mxCnt = 0;
		for (int i = 0; i <= layer; i++)
			mxCnt |= 0xFFL << (8 * i);
		this.maxCount = mxCnt + 1L;
		this.usedCount = defaults ? this.maxCount : 0;
	}

	@Override
	public boolean isFull() {
		return this.usedCount == this.maxCount;
	}

	@Override
	public boolean isUsed(int id) {
		if (this.isFull())
			return true;
		if (this.isEmpty())
			return false;
		int addr = IDProvider.localAddress(id, (byte) this.layer);
		return this.subProviders[addr].isUsed(id);
	}

	@Override
	public boolean set(int id) {
		if (this.isUsed(id))
			return false;
		int addr = IDProvider.localAddress(id, (byte) this.layer);
		if (this.subProviders[addr] instanceof CompressedProvider)
			this.expand(addr, false);
		boolean success = this.subProviders[addr].set(id);
		if (success)
			this.usedCount++;
		this.compress(addr);
		return success;
	}

	@Override
	public boolean free(int id) {
		if (!this.isUsed(id))
			return false;
		int addr = IDProvider.localAddress(id, (byte) this.layer);
		if (this.subProviders[addr] instanceof CompressedProvider)
			this.expand(addr, true);
		boolean success = this.subProviders[addr].free(id);
		if (success)
			this.usedCount--;
		this.compress(addr);
		return success;
	}

	@Override
	public int get() {
		int potential = this.getCompressionCapability();
		int chosenID = 0;
		int subAdd = 0;

		if (potential != -this.layer) {
			// can fill subprovider without top-level expansion
			// find optimal subprovider to fill
			subAdd = this.getProviderIDWithPotential(potential);
			// fill subprovider
			chosenID = this.subProviders[subAdd].get();
		}
		else {
			// top-level expansion required
			for (subAdd = 0; subAdd < 256; subAdd++) {
				if (this.subProviders[subAdd] instanceof CompressedProvider
					&& !this.subProviders[subAdd].isFull()) {
					this.expand(subAdd, false);
					break;
				}
			}
			// fill newly generated sub-layer
			chosenID = this.subProviders[subAdd].get();
		}
		chosenID |= subAdd << (8 * this.layer);
		this.compress(subAdd);
		this.usedCount++;
		return chosenID;
	}

	private int getProviderIDWithPotential(int potential) {
		for (int i = 0; i < 256; i++) {
			if (this.subProviders[i] instanceof CompressedProvider)
				continue;
			if (this.subProviders[i] instanceof BaseIDProvider) {
				if ((this.subProviders[i].getFreeIDCount() == 1 ? 1 : 0) == potential)
					return i;
				continue;
			}
			if (!(this.subProviders[i] instanceof LayeredIDProvider))
				continue;
			if (((LayeredIDProvider) this.subProviders[i]).getCompressionCapability() == potential)
				return i;

		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return this.usedCount == 0;
	}

	private void expand(int addr, boolean state) {
		this.subProviders[addr] = IDProviderFactory.getIDProvider((byte) this.layer, state);
	}

	private void compress(int addr) {
		if (this.subProviders[addr].isFull())
			this.subProviders[addr] = CompressedProvider.TRUE;
		else if (this.subProviders[addr].isEmpty())
			this.subProviders[addr] = CompressedProvider.FALSE;
	}

	@Override
	public long getFreeIDCount() {
		return this.maxCount - this.usedCount;
	}

	int getCompressionCapability() {
		if (this.maxCount - this.usedCount == 1)
			return 1;
		if (this.isFull())
			return Integer.MIN_VALUE; // should not be used externally
		int val = -this.layer;
		for (int i = 0; i < 256; i++) {
			if (this.subProviders[i].isFull() || this.subProviders[i].isEmpty())
				continue;
			if (this.subProviders[i] instanceof BaseIDProvider) {
				if (this.subProviders[i].getFreeIDCount() > 1) {
					if (val < 0)
						val = 0;
				}
				else if (this.subProviders[i].getFreeIDCount() == 1) {
					if (val < 1)
						val = 1;
				}
				continue;
			}
			if (!(this.subProviders[i] instanceof LayeredIDProvider))
				continue;
			int subVal = ((LayeredIDProvider) this.subProviders[i]).getCompressionCapability();
			if (val < subVal)
				val = subVal;
		}
		return val;
	}

}
