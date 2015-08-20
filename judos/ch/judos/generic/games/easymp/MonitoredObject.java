package ch.judos.generic.games.easymp;

import java.util.HashSet;

/**
 * @since 23.05.2015
 * @author Julian Schelker
 */
public class MonitoredObject {

	public Object				data;

	private ObjectId[]			references;
	private HashSet<ObjectId>	referencedBy;

	public MonitoredObject(Object o) {
		int amountOfFields = FieldInformation.getRelevantFieldsOf(o).size();
		this.data = o;
		this.references = new ObjectId[amountOfFields];
		this.referencedBy = new HashSet<>();
	}

	public ObjectId getObjectForField(int fieldIndex) {
		if (fieldIndex >= this.references.length)
			throw new RuntimeException("Field index " + fieldIndex
				+ " out of range for object: " + this.data);
		return this.references[fieldIndex];
	}
}
