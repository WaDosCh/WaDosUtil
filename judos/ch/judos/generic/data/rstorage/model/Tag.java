package ch.judos.generic.data.rstorage.model;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Optional;

import ch.judos.generic.data.rstorage.helper.CheckReader2;
import ch.judos.generic.data.rstorage.helper.RSerializerException;
import ch.judos.generic.data.rstorage.helper.RSerializerException.Type;

/**
 * @since 06.05.2015
 * @author Julian Schelker
 */
public class Tag {
	protected int referenceNumber;
	protected Optional<String> classId = Optional.empty();
	protected boolean hasSetReference = false;
	protected boolean hasGetReference = false;

	private Tag() {
	}

	public Tag(Optional<String> classId, boolean setReference, boolean getReference,
		int referenceNumber) {
		this.classId = classId;
		this.referenceNumber = referenceNumber;
		this.hasSetReference = setReference;
		this.hasGetReference = getReference;
		if (setReference && getReference)
			throw new InvalidParameterException(
				"Can't contain both, set and get reference number for a tag!");
	}

	@Override
	public String toString() {
		String currentClassId = this.classId.orElse("");
		String ref1 = "", ref2 = "";

		if (this.hasSetReference)
			ref1 = "=" + this.referenceNumber;
		if (this.hasGetReference)
			ref2 = "$" + this.referenceNumber;

		String tag = (ref2 + " " + ref1 + " " + currentClassId).trim();
		if (tag.equals(""))
			return "";
		return "(" + tag + ")";
	}

	public static Tag parse(CheckReader2 reader, Class<?> assumeType) throws IOException,
		RSerializerException {
		Tag tag = new Tag();
		boolean tagged = reader.readIfNextMatchesThenConsume("(");
		if (tagged) {
			if (reader.readIfNextMatchesThenConsume("$")) {
				tag.referenceNumber = Integer.valueOf(reader.readWhile(c -> c != ' '
					&& c != ')'));
				tag.hasGetReference = true;
				if (!reader.readIfNextMatchesThenConsume(" "))
					tagged = false;
			}
			if (tagged && reader.readIfNextMatchesThenConsume("=")) {
				tag.referenceNumber = Integer.valueOf(reader.readWhile(c -> c != ' '
					&& c != ')'));
				tag.hasSetReference = true;
				if (!reader.readIfNextMatchesThenConsume(" "))
					tagged = false;
			}
			if (tagged)
				tag.classId = Optional.of(reader.readWhile(c -> c != ')'));
			reader.readMustMatch(")");
		}
		if (!tagged) {
			if (assumeType == null)
				throw new RSerializerException(
					"trying to read an untagged object without an assumed type.",
					Type.ReadingUntagged);
			tag.classId = Optional.of(assumeType.getCanonicalName());
		}
		return tag;
	}

	public String getClassId() {
		return this.classId.get();
	}

	public int getReferenceNumber() {
		return this.referenceNumber;
	}

	public boolean hasSetReference() {
		return this.hasSetReference;
	}

	public boolean hasGetReference() {
		return this.hasGetReference;
	}

}
