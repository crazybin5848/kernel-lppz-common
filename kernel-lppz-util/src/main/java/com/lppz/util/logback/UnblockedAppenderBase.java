package com.lppz.util.logback;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.WarnStatus;

public abstract class UnblockedAppenderBase<E> extends ContextAwareBase implements Appender<E> {

	protected boolean started = false;

	/**
	 * The guard prevents an appender from repeatedly calling its own doAppend
	 * method.
	 */
	private boolean guard = false;
	
//	private ThreadLocal<Boolean> localGuard = new ThreadLocal<Boolean>(){
//		@Override protected Boolean initialValue() {
//			return false;
//		}
//	};

	/**
	 * Appenders are named.
	 */
	protected String name;

	private FilterAttachableImpl<E> fai = new FilterAttachableImpl<E>();

	public String getName() {
		return name;
	}

	private int statusRepeatCount = 0;
	private int exceptionCount = 0;

	static final int ALLOWED_REPEATS = 5;

	public  void doAppend(E eventObject) {
		// WARNING: The guard check MUST be the first statement in the
		// doAppend() method.

		// prevent re-entry.
//		if (localGuard.get()) {
//			return;
//		}

		try {
//			localGuard.set( true);

			if (!this.started) {
				if (statusRepeatCount++ < ALLOWED_REPEATS) {
					addStatus(new WarnStatus("Attempted to append to non started appender [" + name + "].", this));
				}
				return;
			}

			if (getFilterChainDecision(eventObject) == FilterReply.DENY) {
				return;
			}

			// ok, we now invoke derived class' implementation of append
			this.append(eventObject);

		} catch (Exception e) {
			if (exceptionCount++ < ALLOWED_REPEATS) {
				addError("Appender [" + name + "] failed to append.", e);
			}
		} finally {
//			localGuard.set(false);
		}
	}

	abstract protected void append(E eventObject);

	/**
	 * Set the name of this appender.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void start() {
		started = true;
	}

	public void stop() {
		started = false;
	}

	public boolean isStarted() {
		return started;
	}

	public String toString() {
		return this.getClass().getName() + "[" + name + "]";
	}

	public void addFilter(Filter<E> newFilter) {
		fai.addFilter(newFilter);
	}

	public void clearAllFilters() {
		fai.clearAllFilters();
	}

	public List<Filter<E>> getCopyOfAttachedFiltersList() {
		return fai.getCopyOfAttachedFiltersList();
	}

	public FilterReply getFilterChainDecision(E event) {
		return fai.getFilterChainDecision(event);
	}

}
