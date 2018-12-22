package ch.judos.generic.files;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

/**
 * @since 18.07.2014
 * @author Julian Schelker
 */
public class FileUtilsTests extends TestCase {

	public void testMd5() throws NoSuchAlgorithmException, IOException {
		String s = "test some md5 code sequence";
		String md5 = FileUtils.getMd5HexForStream(new ByteArrayInputStream(s.getBytes()));
		assertEquals("AE5E11A1BA642E4AF2382461E2CCA86D", md5);
	}
}
