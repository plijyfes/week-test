/* Caption.java

	Purpose:
		
	Description:
		
	History:
		Tue Oct 11 14:31:07     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

import org.zkoss.zul.impl.LabelImageElement;

/**
 *  A header for a {@link Groupbox}.
 * It may contain either a text label, using {@link #setLabel},
 * or child elements for a more complex caption.
 * <p>Default {@link #getZclass}: z-caption.(since 3.5.0)
 *
 * @author tomyeh
 */
public class Caption extends LabelImageElement {
	public Caption() {
	}
	public Caption(String label) {
		super(label);
	}
	public Caption(String label, String src) {
		super(label, src);
	}

	// super
	public String getZclass() {
		return _zclass == null ? "z-caption" : _zclass;
	}

	//-- Component --//
	public void beforeParentChanged(Component parent) {
		if (parent != null && !(parent instanceof Window)
		&& !(parent instanceof Groupbox) && !(parent instanceof Panel)
		&& !(parent instanceof Tab) && !(parent instanceof LayoutRegion))
			throw new UiException("Wrong parent: "+parent);
		super.beforeParentChanged(parent);
	}
}
