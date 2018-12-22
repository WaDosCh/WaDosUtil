package ch.judos.generic.data.rstorage.helper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * @since 20.04.2015
 * @author Julian Schelker
 */
public class RuntimeWriter2 extends Writer {

	protected BufferedWriter redirectTo;
	private ArrayList<Object> content;
	protected boolean escaped;

	public RuntimeWriter2(BufferedWriter redirectTo) {
		this.redirectTo = redirectTo;
		this.content = new ArrayList<>();
		this.escaped = false;
	}

	@Override
	public void write(String str) {
		this.content.add(str);
	}

	public void write(Object obj) {
		this.content.add(obj);
	}

	public void newLine() {
		this.content.add("\n");
	}

	@Override
	public void close() throws IOException {
		for (Object o : this.content) {
			this.redirectTo.write(o.toString());
		}
		this.redirectTo.close();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		String s = new String(cbuf, off, len);
		write(s);
	}

	@Override
	public void flush() throws IOException {
		// not intended to be called
	}

	public void setEscaped(boolean b) {
		this.escaped = b;
	}
}
