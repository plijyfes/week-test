/* ErrorEvent.java

	Purpose:
		
	Description:
		
	History:
		Fri Oct 21 15:49:42     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.event;

import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.au.AuRequest;

/**
 * Represents an event cause by user's entering a wrong data
 * or clearing the last wrong data.
 * 
 * @author tomyeh
 */
public class ErrorEvent extends InputEvent {
	private final String _msg;

	/** Converts an AU request to an error event.
	 * @since 5.0.4
	 */
	public static final
	ErrorEvent getErrorEvent(AuRequest request, Object oldValue) {
		final Map<String, Object> data = request.getData();
		return new ErrorEvent(request.getCommand(), request.getComponent(),
			Objects.toString(data.get("value")), oldValue,
			//Bug ZK-908: replace String.valueOf by Objects.toString
			Objects.toString(data.get("message")));
	}
	/** Constructs an error-relevant event.
	 * @param val the new value that caused the error
	 * @param oldValue the previous value.
	 * @param msg the error message if not null. If null, it means the
	 * error (notified by previous {@link ErrorEvent}) is cleared.
	 * @since 5.0.4
	 */
	public ErrorEvent(String name, Component target, String val,
	Object oldValue, String msg) {
		super(name, target, val, oldValue);
		_msg = msg;
	}

	/** Returns the error message if this event is caused by a wrong data,
	 * or null if it is to clear message.
	 */
	public final String getMessage() {
		return _msg;
	}
}
