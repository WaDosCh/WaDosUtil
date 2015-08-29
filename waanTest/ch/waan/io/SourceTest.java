/**
 * 
 */
package ch.waan.io;

import ch.waan.xml.XML;

/**
 * @author Andreas Wälchli
 * @version 1.1, May 10, 2015
 *
 */
public class SourceTest {

	public static void main(String[] args) {

		XML XML = Source.fromFile(".classpath").mkXML().orNull();

		if (XML == null)
			return;

		XML.query().addNode("classpathentry").setAttribute("kind", "myKind");

		XML.query().node("classpathentry").attribute("kind").filter(r -> r.isPresent()).map(
			r -> r.get()).distinct().forEach(System.out::println);

	}
}
