package ch.wados.util;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import ch.wados.collection.mutable.PriorityQueue;

/**
 * This class serves as a wrapper for the Java-side Shutdown handlers with the
 * added option to define the shutdown order.
 *
 * @author Andreas Wälchli
 * @version 1.3, 2015-05-10
 */
public final class Shutdown extends PriorityQueue<@NonNull Runnable> {

	/**
	 * Creates a new Shutdown instance. On instantiation the shutdown instance
	 * is automatically registered to the global shutdown handling.
	 */
	public Shutdown() {
		super(false);
		Runtime.getRuntime().addShutdownHook(new Thread(this::runShutdown));
	}

	/**
	 * The default priority.
	 *
	 * @see #add(Runnable)
	 * @see #add(Runnable, int)
	 */
	public static final int DEFAULT = 0;

	private static final Shutdown defaultShutdown = new Shutdown();

	/**
	 * The default priority for operations that should be performed very early
	 * in the sequence.
	 *
	 * @see #add(Runnable, int)
	 */
	public static final int EARLIER = 10000;

	/**
	 * The default priority for operations that should be performed rather early
	 * in the sequence.
	 *
	 * @see #add(Runnable, int)
	 */
	public static final int EARLY = 1000;

	/**
	 * The highest possible priority. This should only be used if an operation
	 * has to be performed strictly first.
	 *
	 * @see #add(Runnable, int)
	 */
	public static final int FIRST = Integer.MAX_VALUE;

	/**
	 * The lowest possible priority. This should only be used if an operation
	 * has to be performed strictly last.
	 *
	 * @see #add(Runnable, int)
	 */
	public static final int LAST = Integer.MIN_VALUE;

	/**
	 * The default priority for operations that should be performed rather late
	 * in the sequence.
	 *
	 * @see #add(Runnable, int)
	 */
	public static final int LATE = -1000;

	/**
	 * The default priority for operations that should be performed very late in
	 * the sequence.
	 *
	 * @see #add(Runnable, int)
	 */
	public static final int LATER = -10000;

	/**
	 * Provides a global Shutdown handler.
	 *
	 * @return the global Shutdown handler
	 */
	public static Shutdown getDefaultShutdown() {
		return Shutdown.defaultShutdown;
	}

	/**
	 * adds a {@link Runnable} with default priority ({@code 0}) to the shutdown
	 * system.<br/>
	 * {@inheritDoc}
	 *
	 * @throws NullPointerException
	 *             if the {@code r} argument is {@code null}
	 */
	@Override
	public boolean add(@NonNull Runnable r) {
		Objects.requireNonNull(r, "no null runnable allowed");
		super.add(r, DEFAULT);
		return true;
	}

	/**
	 * @throws NullPointerException
	 *             if the {@code r} argument is {@code null}
	 * @since 1.3
	 */
	@Override
	public void add(@NonNull Runnable r, double priority) {
		Objects.requireNonNull(r, "no null runnable allowed");
		super.add(r, priority);
	}

	/**
	 * adds a {@link Runnable} with a defined priority to the shutdown system.
	 *
	 * @param r
	 *            the runnable to add
	 * @param priority
	 *            the priority of the runnable
	 * @throws NullPointerException
	 *             if the {@code r} argument is {@code null}
	 * @deprecated since 1.3 - use {@link Shutdown#add(Runnable, double)}
	 *             instead
	 */
	@Deprecated
	public void add(@NonNull Runnable r, int priority) {
		Objects.requireNonNull(r, "no null runnable allowed");
		super.add(r, priority);
	}

	private final void runShutdown() {
		while (!this.isEmpty()) {
			this.element().run();
		}
	}

}
