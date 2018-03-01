package ch.wados.io;

import java.io.InputStream;
import java.net.URL;

import ch.wados.util.Result;

@Deprecated
final class URLSource extends Source {

	private final String url;

	URLSource(String url) {
		this.url = url;
	}

	@Override
	public Result<InputStream> mkStream() {
		return Result.of(this.url).map(URL::new).map(URL::openStream);
	}

	@Override
	public String toString() {
		return "URL Source ( " + this.url + " )";
	}

}
