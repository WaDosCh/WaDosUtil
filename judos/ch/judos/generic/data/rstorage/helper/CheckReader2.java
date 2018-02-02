package ch.judos.generic.data.rstorage.helper;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @since 20.04.2015
 * @author Julian Schelker
 */
public class CheckReader2 extends Reader /* implements AutoCloseable */ {

	protected Reader readFrom;

	public CheckReader2(Reader in) {
		this.readFrom = in;
	}

	@Override
	public void mark(int readAheadLimit) throws IOException {
		this.readFrom.mark(readAheadLimit);
	}

	@Override
	public void reset() throws IOException {
		this.readFrom.reset();
	}

	/**
	 * if false the read is reverted
	 * 
	 * @param shouldBe
	 * @return
	 * @throws IOException
	 */
	public boolean readIfNextMatchesThenConsume(String shouldBe) throws IOException {
		// string is consumed only if matched -> identity predicate
		return readNextMatchesShouldConsume(shouldBe, c -> c);
	}

	/**
	 * reads ahead if the stream matches the given string and eventually
	 * consumes it from the stream
	 * 
	 * @param shouldBe
	 * @param shouldConsume
	 *            did the string match -&gt; should the string be consumed in
	 *            the stream
	 * @return did the string match
	 * @throws IOException
	 */
	public boolean readNextMatchesShouldConsume(String shouldBe,
		Predicate<Boolean> shouldConsume) throws IOException {
		mark(shouldBe.length());
		char[] buffer = new char[shouldBe.length()];
		read(buffer, 0, shouldBe.length());
		String got = new String(buffer);
		boolean result = shouldBe.equals(got);
		if (!shouldConsume.test(result))
			reset();
		return result;
	}

	/**
	 * looks ahead if the string matches the given parameter, does not consume
	 * anything in the stream
	 * 
	 * @param shouldBe
	 * @return
	 * @throws IOException
	 */
	public boolean readNextMatches(String shouldBe) throws IOException {
		mark(shouldBe.length());
		char[] buffer = new char[shouldBe.length()];
		read(buffer, 0, shouldBe.length());
		reset();
		String got = new String(buffer);
		return shouldBe.equals(got);
	}

	public String readUntilNonCharacter() throws IOException {
		return readWhile(c -> Character.isLetter(c));
	}

	public String readWhile(Function<Character, Boolean> whileC) throws IOException {
		StringBuffer result = new StringBuffer();
		mark(1);
		do {
			char c = (char) read();
			if (!whileC.apply(c)) {
				reset();
				break;
			}
			result.append(c);
			mark(1);
		} while (true);
		return result.toString().trim();
	}

	/**
	 * reads all the whitespace until non whitespace is reached
	 * 
	 * @throws IOException
	 */
	public void readTrim() throws IOException {
		Pattern p = Pattern.compile("\\s");
		readWhile(c -> p.matcher(Character.toString(c)).matches());
	}

	public void readMustMatch(String shouldBe) throws IOException {
		char[] buffer = new char[shouldBe.length()];
		read(buffer, 0, shouldBe.length());
		String got = new String(buffer);
		boolean matches = shouldBe.equals(got);
		if (!matches)
			throw new IOException("readMustMatch failed got: " + got + " expected: "
				+ shouldBe);
	}

	@Override
	public void close() throws IOException {
		this.readFrom.close();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return this.readFrom.read(cbuf, off, len);
	}
}
