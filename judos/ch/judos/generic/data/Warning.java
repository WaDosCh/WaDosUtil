package ch.judos.generic.data;

/**
 * @since 10.02.2014
 * @author Julian Schelker
 */
public class Warning extends Exception {

	private static final long serialVersionUID = 4349839465652178175L;

	public Warning(String msg) {
		super(msg);
	}

}
