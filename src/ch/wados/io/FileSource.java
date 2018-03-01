package ch.wados.io;

import java.io.FileInputStream;
import java.io.InputStream;

import ch.wados.util.Result;

@Deprecated
final class FileSource extends Source {

	private final String filename;

	FileSource(String filename) {
		this.filename = filename;
	}

	@Override
	public Result<InputStream> mkStream() {
		return Result.eval(() -> new FileInputStream(this.filename));
	}

	@Override
	public String toString() {
		return "File Source ( " + this.filename + " )";
	}

}
