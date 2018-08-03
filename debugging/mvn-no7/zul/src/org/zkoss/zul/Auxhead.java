/* Auxhead.java

	Purpose:
		
	Description:
		
	History:
		Wed Oct 24 09:55:47     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

import org.zkoss.zul.impl.XulElement;

/**
 * Used to define a collection of auxiliary headers ({@link Auxheader}).
 *
 * <p>Non XUL element.
 * <p>Default {@link #getZclass}: z-auxhead.(since 3.5.0)
 * 
 * @since 3.0.0
 * @author tomyeh
 */
public class Auxhead extends XulElement {
	public Auxhead() {
	}

	public String getZclass() {
		return _zclass == null ? "z-auxhead" : _zclass;
	}
	//super//
	public void beforeChildAdded(Component child, Component refChild) {
		if (!(child instanceof Auxheader))
			throw new UiException("Unsupported child: "+child);
		super.beforeChildAdded(child, refChild);
	}
}
