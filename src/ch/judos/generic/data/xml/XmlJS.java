package ch.judos.generic.data.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @since 19.02.2012
 * @author Julian Schelker
 * @version 1.0 / 19.02.2012
 * 
 */
public class XmlJS {

	public static Document text2Document(String text) {
		try {
			StringReader xmlStream = new StringReader(text);
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(xmlStream));
			return d;
		}
		catch (Exception e) {
			System.err.println("JS_Xml: Could not translate XML");
			e.printStackTrace();
			return null;
		}
	}

	public static String document2Text(Document doc) {
		Source source = new DOMSource(doc.getDocumentElement());
		StringWriter stringWriter = new StringWriter();
		Result result = new StreamResult(stringWriter);
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(source, result);
			String xml = stringWriter.getBuffer().toString();
			return xml;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getXmlInnerInt(Document doc, String tagName) {
		return Integer.valueOf(getXmlInnerText(doc, tagName));
	}

	public static String getXmlInnerText(Document dom, String tagName) {
		org.w3c.dom.Element docEle = dom.getDocumentElement();

		String value = docEle.getElementsByTagName(tagName).item(0).getFirstChild()
			.getNodeValue();
		return value;
	}

}
