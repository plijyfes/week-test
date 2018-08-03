/* MaximizeEvent.java

	Purpose:
		
	Description:
		
	History:
		Jun 23, 2008 5:36:42 PM , Created by jumperchen

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.event;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;

/**
 * Represents an event caused by a component being maximized.
 *
 * @author jumperchen
 * @since 3.5.0
 */
public class MaximizeEvent extends Event {
	private final String _width, _height, _left, _top;
	private final boolean _maximized;

	/** Converts an AU request to a maximize event.
	 * @since 5.0.0
	 */
	public static final MaximizeEvent getMaximizeEvent(AuRequest request) {
		final Map<String, Object> data = request.getData();
		return new MaximizeEvent(request.getCommand(), request.getComponent(),
			(String)data.get("left"), (String)data.get("top"),
			(String)data.get("width"), (String)data.get("height"),
			AuRequests.getBoolean(data, "maximized"));
	}

	public MaximizeEvent(String name, Component target, String left, String top,
			String width, String height, boolean maximized) {
		super(name, target);
		_left = left;
		_top = top;
		_width = width;
		_height = height;
		_maximized = maximized;
	}
	/** Returns the width of the component, which is its original width.
	 */
	public final String getWidth() {
		return _width;
	}
	/** Returns the height of the component, which is its original height.
	 */
	public final String getHeight() {
		return _height;
	}
	/** Returns the left of the component, which is its original left.
	 */
	public final String getLeft() {
		return _left;
	}
	/** Returns the top of the component, which is its original top.
	 */
	public final String getTop() {
		return _top;
	}
	/** Returns whether to be maximized.
	 */
	public final boolean isMaximized() {
		return _maximized;
	}
}
