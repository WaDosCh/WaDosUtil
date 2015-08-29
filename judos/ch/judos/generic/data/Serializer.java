package ch.judos.generic.data;

import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * helps to serialize different data types
 * 
 * @since 26.04.2012
 * @author Julian Schelker
 * @version 1.01 / 22.02.2013
 */
public class Serializer {

	/**
	 * @param data
	 *            bytes of the xml document
	 * @return the xml document retrieved from the byte data
	 * @throws SerializerException
	 */
	public static Document bytes2Document(byte[] data) throws SerializerException {
		String s = new String(data);
		return text2Document(s);
	}

	/**
	 * reads a series of bytes and returns the corresponding integer
	 * 
	 * @param array
	 * @param offset
	 *            at what position the integer is written at
	 * @return integer value retrieved
	 */
	public static int bytes2int(byte[] array, int offset) {
		int val = (0xFF & array[offset + 0]) << 24;
		val += (0xFF & array[offset + 1]) << 16;
		val += (0xFF & array[offset + 2]) << 8;
		val += 0xFF & array[offset + 3];
		return val;
	}

	/**
	 * reads the byte array and retrieves the object that was written as bytes
	 * 
	 * @param data
	 * @return the object
	 * @throws SerializerException
	 *             in case the bytes don't represent a valid object
	 */
	public static Object bytes2object(byte[] data) throws SerializerException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, data.length);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e) {
			throw new SerializerException("Could not deserialize object", e);
		}
	}

	/**
	 * reads a series of bytes and returns the corresponding short
	 * 
	 * @param array
	 * @param offset
	 *            at what position the short is written at
	 * @return short value retrieved
	 */
	public static short bytes2short(byte[] array, int offset) {
		short val = (short) ((0xFF & array[offset + 0]) << 8);
		val += 0xFF & array[offset + 1];
		return val;
	}

	/**
	 * @param doc
	 * @return the xml document represented as bytes
	 * @throws SerializerException
	 */
	public static byte[] document2Bytes(Document doc) throws SerializerException {
		String s = document2Text(doc);
		return s.getBytes();
	}

	/**
	 * @param doc
	 * @return the xml code of the document as string
	 * @throws SerializerException
	 */
	public static String document2Text(Document doc) throws SerializerException {
		try {
			Source source = new DOMSource(doc.getDocumentElement());
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(source, result);
			String xml = stringWriter.getBuffer().toString();
			return xml;
		}
		catch (Exception e) {
			throw new SerializerException("Could not generate text from xml", e);
		}
	}

	/**
	 * writes the integer as series of bytes into the given array
	 * 
	 * @param array
	 * @param offset
	 *            at what position the int should be written
	 * @param value
	 *            what should be written into the array
	 */
	public static void int2bytes(byte[] array, int offset, int value) {
		array[offset + 0] = (byte) (value >>> 24);
		array[offset + 1] = (byte) (value >>> 16);
		array[offset + 2] = (byte) (value >>> 8);
		array[offset + 3] = (byte) (value);
	}

	/**
	 * serializes an object into a series of bytes
	 * 
	 * @param obj
	 * @return the object represented as bytes
	 * @throws SerializerException
	 */
	public static byte[] object2Bytes(Object obj) throws SerializerException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.flush();
		}
		catch (IOException e) {
			throw new SerializerException("Could not serialize object (" + obj + ")", e);
		}
		return baos.toByteArray();
	}

	public static void object2Stream(Object obj, OutputStream str) throws SerializerException {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(str);
			oos.writeObject(obj);
			oos.flush();
		}
		catch (IOException e) {
			throw new SerializerException("Could not serialize object (" + obj + ")", e);
		}
	}

	/**
	 * writes the short as series of bytes into the given array
	 * 
	 * @param array
	 * @param offset
	 *            at what position the int should be written
	 * @param value
	 *            what should be written into the array
	 */
	public static void short2bytes(byte[] array, int offset, short value) {
		array[offset + 0] = (byte) (value >>> 8);
		array[offset + 1] = (byte) (value);
	}

	public static Object stream2object(InputStream str) throws SerializerException {
		try {
			ObjectInputStream ois = new ObjectInputStream(str);
			return ois.readObject();
		}
		catch (Exception e) {
			throw new SerializerException("Could not deserialize object", e);
		}
	}

	/**
	 * uses the given text to create an xml document object
	 * 
	 * @param text
	 *            must be xml code
	 * @return the xml document object
	 * @throws SerializerException
	 */
	public static Document text2Document(String text) throws SerializerException {
		try {
			StringReader xmlStream = new StringReader(text);
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new InputSource(xmlStream));
			return d;
		}
		catch (Exception e) {
			throw new SerializerException("Could not parse xml", e);
		}
	}
}
