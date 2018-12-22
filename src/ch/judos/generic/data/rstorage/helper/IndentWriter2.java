package ch.judos.generic.data.rstorage.helper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @since 16.10.2014
 * @author Julian Schelker
 */
public class IndentWriter2 extends BufferedWriter {

	private boolean onANewLine;
	int indent;

	public IndentWriter2(Writer redirectTo) {
		super(redirectTo);
		this.indent = 0;
		this.onANewLine = true;
	}

	@Override
	public void newLine() throws IOException {
		super.newLine();
		this.onANewLine = true;
	}

	public static String repeat(String x, int times) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < times; i++)
			s.append(x);
		return s.toString();
	}

	@Override
	public void write(String str) throws IOException {
		if (str.trim().equals("}"))
			this.indent--;
		if (this.onANewLine) {
			this.onANewLine = false;
			String s = repeat("\t", this.indent);
			super.write(s + str);
		}
		else
			super.write(str);

		if (str.trim().endsWith("{"))
			this.indent++;
	}

	@Override
	public void close() throws IOException {
		super.flush();
	}

}
