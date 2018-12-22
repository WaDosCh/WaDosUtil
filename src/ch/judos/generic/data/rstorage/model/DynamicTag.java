package ch.judos.generic.data.rstorage.model;

import java.util.Optional;
import java.util.function.Supplier;

import ch.judos.generic.data.rstorage.ObjectTracker2;
import ch.judos.generic.data.rstorage.interfaces.RStorableWrapper;

/**
 * @since 06.05.2015
 * @author Julian Schelker
 */
public class DynamicTag extends Tag {

	public static Tag createTag(RStorableWrapper store, ObjectTracker2 objectTracker,
		boolean storeType) {
		// check whether object has already been stored once
		Supplier<Boolean> isObjReferenced = () -> false;
		Object object = store.getWrapped();
		int objectNr = 0;
		if (store.isTrackedAsObject()) {
			if (objectTracker.isMappedAlready(object))
				return new Tag(Optional.empty(), false, true, objectTracker
					.getIndexForObject(object));
			// else {
			objectNr = objectTracker.mapObjectAndGetIndex(object);
			isObjReferenced = () -> (objectTracker.getUsageForObject(object) > 0);
			// }
		}
		Optional<String> type = Optional.empty();
		if (storeType)
			type = Optional.of(object.getClass().getCanonicalName());
		return new DynamicTag(type, isObjReferenced, objectNr);
	}

	protected Supplier<Boolean> setRefDynamic;

	public DynamicTag(Optional<String> type, Supplier<Boolean> referenceUsed,
		int referenceNumber) {
		super(type, false, false, referenceNumber);
		this.setRefDynamic = referenceUsed;
	}

	@Override
	public String toString() {
		this.hasSetReference = this.setRefDynamic.get();
		return super.toString();
	}
}
