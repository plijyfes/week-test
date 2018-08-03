/* Menuseparator.java

	Purpose:
		
	Description:
		
	History:
		Thu Sep 22 10:59:00     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zul.impl.XulElement;

/**
 * Used to create a separator between menu items.
 *
 *<p>Default {@link #getZclass}: z-menuseparator. (since 3.5.0)
 * 
 * @author tomyeh
 */
public class Menuseparator extends XulElement {

	//-- Component --//
	public String getZclass() {
		return _zclass == null ? "z-menuseparator" : _zclass;
	}
	/** Not childable. */
	protected boolean isChildable() {
		return false;
	}
	public boolean isPopup(){
		return getParent() instanceof Menupopup;
	}
}
