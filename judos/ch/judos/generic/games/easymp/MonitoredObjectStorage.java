package ch.judos.generic.games.easymp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @since 23.05.2015
 * @author Julian Schelker
 */
public class MonitoredObjectStorage {

	/**
	 * maps ObjectId to MonitoredObject
	 */
	protected HashMap<ObjectId, MonitoredObject>	stored;

	/**
	 * maps Object to their ObjectId
	 */
	protected HashMap<Object, ObjectId>				mappings;

	private IdGenerator								idGenerator;

	public MonitoredObjectStorage(String clientId) {
		this.stored = new HashMap<>();
		this.mappings = new HashMap<>();
		this.idGenerator = new IdGenerator(clientId);
	}

	public void addStaticObject(Object o) {
		ObjectId id = this.idGenerator.generateStaticId();
		addMonitoredObject(o, id);
	}

	public void addMonitoredObject(Object o, ObjectId id) {
		MonitoredObject mon = new MonitoredObject(o, id);
		this.stored.put(id, mon);
		this.mappings.put(o, id);
		addSubObjectsToStorage(mon);
	}

	private void addSubObjectsToStorage(MonitoredObject mon) {
		ArrayList<Field> fields = FieldInformation.getRelevantFieldsOf(mon.data);
		for (int fieldNr = 0; fieldNr < fields.size(); fieldNr++) {
			Object subObject = FieldInformation.getObjectOfField(mon.data, fieldNr);
			if (subObject == null)
				continue;
			ObjectId subId = getIdOrCreate(subObject);
			MonitoredObject subObjectMonitored = this.stored.get(subId);

			// add references from objects
			mon.references[fieldNr] = subId;
			subObjectMonitored.referencedBy.add(mon.id);
		}
	}

	public ObjectId getIdOrCreate(Object o) {
		ObjectId id = this.mappings.get(o);
		if (id == null) {
			id = this.idGenerator.generateDynamicId();
			addMonitoredObject(o, id);
		}
		return id;
	}

	public List<Object> getStaticObjects() {
		List<Object> result = new ArrayList<>();
		for (ObjectId id : this.idGenerator.getAllStaticIds()) {
			result.add(this.stored.get(id).data);
		}
		return result;
	}

	public ObjectId getIdOf(Object object) {
		return this.mappings.get(object);
	}

	public Object getObjectById(ObjectId id) {
		MonitoredObject m = this.stored.get(id);
		if (m == null)
			return null;
		return m.data;
	}

}
