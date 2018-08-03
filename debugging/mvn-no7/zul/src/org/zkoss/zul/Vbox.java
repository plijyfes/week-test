/* Vbox.java

	Purpose:
		
	Description:
		
	History:
		Mon Jun 20 22:17:09     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zk.ui.Component;

/**
 * A vbox.
 * <p>Default {@link #getZclass}: z-vbox.(since 3.5.0)
 *
 * @author tomyeh
 */
public class Vbox extends Box {
	public Vbox() {
	}
	/** Constructor a vertical box by assigning an array of children.
	 *
	 * @param children an array of children to be added
	 * @since 2.4.0
	 */
	public Vbox(Component[] children) {
		super(children);
	}
}
