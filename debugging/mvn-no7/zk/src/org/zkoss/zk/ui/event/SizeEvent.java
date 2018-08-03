/* SizeEvent.java

	Purpose:
		
	Description:
		
	History:
		Thu Dec  7 11:46:12     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;

/**
 * Represents an event caused by a component being re-sized.
 *
 * @author tomyeh
 */
public class SizeEvent extends Event {
	private final String _width, _height;
	private final int _keys;

	/** Indicates whether the Alt key is pressed.
	 * It might be returned as part of {@link #getKeys}.
	 */
	public static final int ALT_KEY = MouseEvent.ALT_KEY;
	/** Indicates whether the Ctrl key is pressed.
	 * It might be returned as part of {@link #getKeys}.
	 */
	public static final int CTRL_KEY = MouseEvent.CTRL_KEY;
	/** Indicates whether the Shift key is pressed.
	 * It might be returned as part of {@link #getKeys}.
	 */
	public static final int SHIFT_KEY = MouseEvent.SHIFT_KEY;

	/** Converts an AU request to a size event.
	 * @since 5.0.0
	 */
	public static final SizeEvent getSizeEvent(AuRequest request) {
		final java.util.Map<String, Object> data = request.getData();
		return new SizeEvent(request.getCommand(), request.getComponent(),
			(String)data.get("width"), (String)data.get("height"),
			AuRequests.parseKeys(data));
	}

	/** Constructs a mouse relevant event.
	 */
	public SizeEvent(String name, Component target, String width, String height,
	int keys) {
		super(name, target);
		_width = width;
		_height = height;
		_keys = keys;
	}
	/** Returns the width of the component after re-sized.
	 */
	public final String getWidth() {
		return _width;
	}
	/** Returns the height of the component after re-sized.
	 */
	public final String getHeight() {
		return _height;
	}
	/** Returns what keys were pressed when the component is resized, or 0 if
	 * none of them was pressed.
	 * It is a combination of {@link #CTRL_KEY}, {@link #SHIFT_KEY}
	 * and {@link #ALT_KEY}.
	 */
	public final int getKeys() {
		return _keys;
	}
}
