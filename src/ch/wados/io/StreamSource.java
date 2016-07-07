package ch.wados.io;

import java.io.InputStream;

import ch.wados.util.Result;

class StreamSource extends Source {

	private final InputStream stream;

	StreamSource(InputStream stream) {
		this.stream = stream;
	}

	@Override
	public Result<InputStream> mkStream() {
		return Result.ofNullable(this.stream);
	}

}
