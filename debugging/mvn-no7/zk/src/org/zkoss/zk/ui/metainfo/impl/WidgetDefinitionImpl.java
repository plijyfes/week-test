/* WidgetDefinitionImpl.java

	Purpose:
		
	Description:
		
	History:
		Thu Oct 16 11:06:05     2008, Created by tomyeh

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.metainfo.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.zkoss.zk.ui.metainfo.*;

/**
 * An implementation of WidgetDefinition.
 *
 * @author tomyeh
 * @since 5.0.0
 */
public class WidgetDefinitionImpl implements WidgetDefinition {
	/** The widget class. */
	private final String _class;
	/** A map of molds (String mold, String moldURI). */
	private Map<String, String> _molds;
	/** Whether to preserve the blank text. */
	private final boolean _blankpresv;

	public WidgetDefinitionImpl(String klass, boolean blankPreserved) {
		_class = klass;
		_blankpresv = blankPreserved;
	}

	//WidgetDefinition//
	public String getWidgetClass() {
		return _class;
	}
	public boolean isBlankPreserved() {
		return _blankpresv;
	}
	public void addMold(String name, String moldURI) {
		if (name == null || name.length() == 0)
			throw new IllegalArgumentException();

		if (_molds == null)
			_molds = new HashMap<String, String>(2);
		_molds.put(name, moldURI);
	}
	public String getMoldURI(String name) {
		if (_molds == null)
			return null;

		return _molds.get(name);
	}
	public boolean hasMold(String name) {
		return _molds != null && _molds.containsKey(name);
	}
	public Collection<String> getMoldNames() {
		if (_molds != null)
			return _molds.keySet();
		return Collections.emptyList();
	}
}
