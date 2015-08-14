package ch.judos.generic.games.easymp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @since 23.05.2015
 * @author Julian Schelker
 */
public class MonitoredObjectStorage {

	protected HashMap<ObjectId, MonitoredObject>	stored;
	protected HashMap<Object, ObjectId>				mappings;
	private IdGenerator									idGenerator;

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
		MonitoredObject mon = new MonitoredObject(o);
		this.stored.put(id, mon);
		this.mappings.put(o, id);
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

	public Object getObjectById(ObjectId id) {
		return this.stored.get(id).data;
	}

	public ObjectId getIdOf(Object object) {
		return this.mappings.get(object);
	}

	public Object getById(ObjectId id) {
		MonitoredObject m = this.stored.get(id);
		if (m == null)
			return null;
		return m.data;
	}

}
