package ch.judos.generic.data;

/**
 * @since 06.07.2013
 * @author Julian Schelker
 */
public class DuplicateFilter {

	private int cleanUpCounter;
	private Group gMaster;
	protected int groupStart;

	public DuplicateFilter() {
		this.gMaster = new Group(3);
		this.groupStart = 0;
		this.cleanUpCounter = 0;
	}

	/**
	 * @param index
	 *            checks if this index has been hit before
	 * @return true if index was not hit, false otherwise
	 */
	public boolean check(int index) {
		return this.gMaster.check(index);
	}

	/**
	 * it is assumed that the indices circulate from Integer.MIN_VALUE to
	 * Integer.MAX_VALUE. to be able to use indices again, this function needs
	 * to be called
	 */
	public void cleanup() {
		int used = 0;
		int i = this.groupStart;
		while (this.gMaster.f[i] || this.gMaster.g[i] != null) {
			used++;
			i++;
		}
		int cleanGroups = used - 10;
		for (int j = 0; j < cleanGroups; j++) {
			int cleanIndex = this.groupStart + j;
			if (this.gMaster.f[cleanIndex])
				this.gMaster.finished--;
			this.gMaster.f[cleanIndex] = false;
			this.gMaster.g[cleanIndex] = null;
		}
	}

	/**
	 * @param index
	 *            this index is hit, check(index) will return false after hit()
	 * @return true if this index has not been hit before, false otherwise
	 */
	public boolean hit(int index) {
		this.cleanUpCounter++;
		if (this.cleanUpCounter % 1000000 == 0)
			cleanup();
		return this.gMaster.hit(index);
	}

	protected static class Group {

		private int div;
		boolean[] f;
		int finished;
		Group[] g;
		private int lvl;

		public Group(int lvl) {
			this.lvl = lvl;
			if (lvl > 0) {
				this.g = new Group[256];
				this.div = (int) Math.pow(2, 8 * lvl);
			}
			this.f = new boolean[256];
			this.finished = 0;
		}

		public boolean check(int index) {
			if (this.lvl == 0)
				return !this.f[index];

			int gIndex = index / this.div;
			if (this.lvl == 3) // since integers start at -2^31
				gIndex += 128;
			if (this.f[gIndex]) // if whole group is already marked as
								// hit/finished
				return false;
			if (this.g[gIndex] == null) // if group doesn't exist yet
				return true;

			int lowerIndex = index % this.div;
			return this.g[gIndex].check(lowerIndex);
		}

		private boolean finished() {
			return this.finished == 256;
		}

		public boolean hit(int index) {
			if (this.lvl == 0) {
				boolean unhitBefore = !this.f[index];
				if (unhitBefore) {
					this.finished++;
					this.f[index] = true;
				}
				return unhitBefore;
			}
			int gIndex = index / this.div;
			if (this.lvl == 3) // since integers start at -2^31
				gIndex += 128;
			if (this.f[gIndex]) // if whole group is already marked as
								// hit/finished
				return false;

			int lowerIndex = index % this.div;
			if (this.g[gIndex] == null)
				this.g[gIndex] = new Group(this.lvl - 1);
			boolean unhitBefore = this.g[gIndex].hit(lowerIndex);
			if (this.g[gIndex].finished()) {
				this.g[gIndex] = null;
				this.f[gIndex] = true;
				this.finished++;
			}
			return unhitBefore;
		}

	}

}
