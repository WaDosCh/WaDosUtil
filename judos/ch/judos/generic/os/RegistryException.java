package ch.judos.generic.os;

/**
 * @since 23.11.2015
 * @author Julian Schelker
 */
public class RegistryException extends Exception {

	public RegistryException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 2266864801697438778L;

	public static class RethrownRegistryException extends RegistryException {
		private Exception exception;
		private static final long serialVersionUID = -5519843016652348719L;

		public RethrownRegistryException(String string, Exception exception) {
			super(string);
			this.exception = exception;
			this.setStackTrace(exception.getStackTrace());
		}

		public Exception getEncapsulatedException() {
			return this.exception;
		}

		@Override
		public String toString() {
			return super.toString() + " (encapsulated: " + this.exception + ")";
		}
	}
}
