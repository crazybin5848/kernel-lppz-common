package com.lppz.util.logback;

import java.io.File;
import java.io.IOException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RolloverFailure;

public class ErrorRollingFileAppender<E> extends RollingFileAppender<E> {
	File currentlyActiveFile;

	public void start() {
		currentlyActiveFile = new File(getFile());
		super.start();
	}

	/**
	 * Implemented by delegating most of the rollover work to a rolling policy.
	 */
	public void rollover() {
		synchronized (lock) {
			// Note: This method needs to be synchronized because it needs
			// exclusive
			// access while it closes and then re-opens the target file.
			//
			// make sure to close the hereto active log file! Renaming under
			// windows
			// does not work for open files.
			this.closeOutputStream();

			try {
				super.getRollingPolicy().rollover();
			} catch (RolloverFailure rf) {
				addWarn("RolloverFailure occurred. Deferring roll-over.");
				// we failed to roll-over, let us not truncate and risk data
				// loss
				this.append = true;
			}

			try {
				// update the currentlyActiveFile
				// http://jira.qos.ch/browse/LBCORE-90
				currentlyActiveFile = new File(super.getRollingPolicy().getActiveFileName());

				// This will also close the file. This is OK since multiple
				// close operations are safe.
				this.openFile(super.getRollingPolicy().getActiveFileName());
			} catch (IOException e) {
				addError("setFile(" + fileName + ", false) call failed.", e);
			}
		}
	}

	/**
	 * This method differentiates RollingFileAppender from its super class.
	 */
	@Override
	protected void subAppend(E event) {
		if(event instanceof LoggingEvent){
			LoggingEvent tempLoggingEvent = (LoggingEvent) event;
			if(!tempLoggingEvent.getLevel().toString().equals(Level.ERROR.toString()))
				return;
		}
		
		// The roll-over check must precede actual writing. This is the
		// only correct behavior for time driven triggers.

		// We need to synchronize on triggeringPolicy so that only one rollover
		// occurs at a time
//		synchronized (super.getTriggeringPolicy()) {
//			if (super.getTriggeringPolicy().isTriggeringEvent(currentlyActiveFile, event)) {
//				rollover();
//			}
//		}
		
		  super.subAppend(event);
	}
}
