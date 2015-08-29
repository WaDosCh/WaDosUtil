package ch.judos.generic.data.rstorage;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import ch.judos.generic.data.StringUtils;
import ch.judos.generic.data.rstorage.helper.*;
import ch.judos.generic.data.rstorage.helper.RSerializerException.Type;
import ch.judos.generic.data.rstorage.interfaces.RStorableManual2;
import ch.judos.generic.data.rstorage.interfaces.RStorableWrapper;
import ch.judos.generic.data.rstorage.interfaces.RStoreInternal;
import ch.judos.generic.data.rstorage.model.DynEscapeSeq;
import ch.judos.generic.data.rstorage.model.DynamicTag;
import ch.judos.generic.data.rstorage.model.Tag;
import ch.judos.generic.data.rstorage.types.*;
import ch.judos.generic.reflection.Classes;

/**
 * @since 20.04.2015
 * @author Julian Schelker
 */
public class ReadableStorage2Impl implements ReadableStorage2 {

	protected HashMap<Class<?>, Class<? extends RStorableWrapper>> wrappers;
	private RStoreInternal internalStorage;
	protected static ThreadLocal<ObjectTracker2> objectTracker = new ThreadLocal<>();

	public ReadableStorage2Impl() {
		this.wrappers = new HashMap<>();
		initPrimitiveTypes();
	}

	protected void initPrimitiveTypes() {
		addStorableWrapper(Boolean.class, RStorableBoolean2.class);
		addStorableWrapper(Integer.class, RStorableInteger2.class);
		addStorableWrapper(String.class, RStorableString2.class);
		addStorableWrapper(Float.class, RStorableFloat2.class);
	}

	/**
	 * declares a class as RStorable and uses the given Wrapper class and
	 * factory object to do the work
	 * 
	 * @param wrapped
	 * @param wrapper
	 */
	public void
		addStorableWrapper(Class<?> wrapped, Class<? extends RStorableWrapper> wrapper) {
		this.wrappers.put(wrapped, wrapper);
	}

	// /**
	// * just declare any other class as RStorable (useful if you can't change
	// * that class)
	// *
	// * @param storable
	// */
	// public void addStorableClasses(Class<?> storable) {
	// this.suppliers.put(storable, new ObjectWrapper(storable));
	// }

	@Override
	public Object read(Reader reader) throws RSerializerException {
		try (CheckReader2 rr = new CheckReader2(reader)) {
			objectTracker.set(new ObjectTracker2());
			return readWithTags(rr, null);
		}
		catch (IOException e) {
			throw new RSerializerException("IO Exception during deserialization", e, Type.IO);
		}
	}
	/**
	 * @param reader
	 * @param assumeType
	 *            if the object is untyped an initial type reference is needed
	 * @return
	 * @throws RSerializerException
	 */
	protected Object readWithTags(CheckReader2 reader, Class<?> assumeType)
		throws RSerializerException {
		try {
			Tag t = Tag.parse(reader, assumeType);

			if (t.hasGetReference())
				return objectTracker.get().getObjectForIndex(t.getReferenceNumber());

			Object object = initializeByClassId(t.getClassId());
			RStorableWrapper store = getStorableForObject(object);

			store.read(reader, getInternal());
			Object result = store.getWrapped();
			if (t.hasSetReference())
				objectTracker.get().mapObjectToIndex(result, t.getReferenceNumber());

			return result;
		}
		catch (IOException e) {
			throw new RSerializerException("", e, Type.IO);
		}
	}
	private Object initializeByClassId(String classId) throws RSerializerException {
		try {
			if (classId.endsWith("[]")) {
				Class<?> cl = Class.forName(StringUtils.substr(classId, 0, -2));
				return Array.newInstance(cl, 0);
			}
			return initializeByClass(Class.forName(classId));
		}
		catch (ClassNotFoundException e) {
			throw new RSerializerException("Class not found for deserialization: " + classId,
				Type.ClassNotFound);
		}
	}

	@Override
	public void store(Object object, Writer writer) throws RSerializerException {
		objectTracker.set(new ObjectTracker2());
		try (RuntimeWriter2 wr = new RuntimeWriter2(new IndentWriter2(writer))) {
			storeWithTags(object, wr, true);
			wr.close();
		}
		catch (IOException e) {
			throw new RSerializerException(
				"Could not close and flush stream after writing object " + object, e, Type.IO);
		}
	}
	protected void storeWithTags(Object object, RuntimeWriter2 wr, boolean storeType)
		throws RSerializerException {
		// get serializer impl for this object
		RStorableWrapper store = getStorableForObject(object);

		try {
			Tag tag = DynamicTag.createTag(store, objectTracker.get(), storeType);
			wr.write(tag);
			if (tag.hasGetReference())
				return;

			wr.write(new DynEscapeSeq(() -> tag.toString().equals(""), wr));
			store.store(wr, getInternal());
		}
		catch (IOException e) {
			throw new RSerializerException("Could not write to stream", e, Type.IO);
		}
	}

	private RStorableWrapper getStorableForObject(Object o) throws RSerializerException {
		try {
			if (o instanceof RStorableManual2) {
				return new NoWrapperWrapper((RStorableManual2) o);
			}
			else if (o.getClass().isArray()) {
				RStorableArray2 r = new RStorableArray2();
				r.initWrapped(o);
				return r;
			}
			else if (this.wrappers.containsKey(o.getClass())) {
				Class<? extends RStorableWrapper> wrapperClass = this.wrappers.get(o
					.getClass());
				RStorableWrapper store = (RStorableWrapper) Classes
					.getSerializationConstructor(wrapperClass).newInstance();
				store.initWrapped(o);
				return store;
			}
		}
		catch (SecurityException | InstantiationException | IllegalAccessException
			| IllegalArgumentException | InvocationTargetException io) {
			throw new RSerializerException(
				"Exception while trying to find wrapper for object: " + o + " of type "
					+ o.getClass(), io, Type.Wrapper);
		}
		throw new RSerializerException("Could not find wrapper for object: " + o + " of type "
			+ o.getClass(), Type.Wrapper);
	}

	private Object initializeByClass(Class<?> c) throws RSerializerException {
		try {
			Constructor<? extends Object> c1 = Classes.getSerializationConstructor(c);
			return c1.newInstance();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RSerializerException("Could not generate instance of class "
				+ c.getName(), e, Type.Instantiation);
		}
	}

	protected void rethrowI(String msg, Exception e) throws IOException {
		IOException ex = new IOException(msg + e.getMessage(), e);
		ex.setStackTrace(e.getStackTrace());
		throw ex;
	}

	private RStoreInternal getInternal() {
		if (this.internalStorage == null) {
			this.internalStorage = new RInternalImpl(this);
		}
		return this.internalStorage;
	}

}
