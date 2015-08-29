package ch.judos.generic.games.easymp;

import java.util.HashSet;

/**
 * @since 23.05.2015
 * @author Julian Schelker
 */
public class MonitoredObject {

	public ObjectId id;
	public Object data;

	// use Array here, such that it is clear what field referenced which object
	public ObjectId[] references;
	public HashSet<ObjectId> referencedBy;

	public MonitoredObject(Object o, ObjectId id) {
		int amountOfFields = FieldInformation.getRelevantFieldsOf(o).size();
		this.data = o;
		this.id = id;
		this.references = new ObjectId[amountOfFields];
		this.referencedBy = new HashSet<>();
	}

	/**
	 * @param fieldIndex
	 * @deprecated is this still used?
	 * @return
	 */
	@Deprecated
	public ObjectId getObjectForField(int fieldIndex) {
		if (fieldIndex >= this.references.length)
			throw new RuntimeException("Field index " + fieldIndex
				+ " out of range for object: " + this.data);
		return this.references[fieldIndex];
	}
}
