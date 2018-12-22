package ch.judos.generic.data.parse;

/**
 * @since 02.07.2013
 * @author Julian Schelker
 * @version 1.0 / 02.07.2013
 */
public class ChoiceFormat extends ComposedFormat {

	private int choice;

	/**
	 * @param parts
	 * @see ch.judos.generic.data.parse.ComposedFormat#ComposedFormat(Object...)
	 */
	public ChoiceFormat(Object... parts) {
		super(parts);
		this.choice = -1;
	}

	/**
	 */
	protected ChoiceFormat() {
		this.choice = -1;
	}

	/**
	 * @return the parsed format
	 */
	public Format getChoice() {
		if (this.choice == -1)
			return null;
		return this.parts[this.choice];
	}

	/**
	 * @return the index of the parsed format
	 */
	public int getChoiceIndex() {
		return this.choice;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.Format#matchAndConsume(java.lang.String)
	 */
	@Override
	public String matchAndConsume(String input) {
		String out = null;
		int index = 0;
		for (Format p : this.parts) {
			out = p.matchAndConsume(input);
			if ("".equals(out)) {
				this.choice = index;
				break;
			}
			index++;
		}
		this.set = true;
		return out;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.ComposedFormat#removeData()
	 */
	@Override
	public void removeData() {
		this.choice = -1;
		super.removeData();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.judos.generic.data.parse.ComposedFormat#getObject()
	 */
	@Override
	protected Object getObject() {
		if (this.choice == -1)
			return null;
		return getChoice().get();
	}

}
