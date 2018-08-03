/* AuWrongValue.java

	Purpose:
		
	Description:
		
	History:
		Wed May  2 10:51:43     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.au.out;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.au.AuResponse;
/**
 * A response to tell the client a component's value is wrong.
 *
 * <p>data[0]: component
 * data[1]: the error message
 * 
 * @author tomyeh
 * @since 3.0.0
 */
public class AuWrongValue extends AuResponse {
	public AuWrongValue(Component comp, String message) {
		super("wrongValue", comp, new Object[] {comp, message});
	}
	/**
	 * Constructor for multiple wrong values.
	 * @param data a string array, the data in the array shall be
	 * ["uuid1", "message1", "uuid2", "message2"...]
	 * @since 3.6.0
	 */
	public AuWrongValue(String[] data) {
		super("wrongValue", data);
	}

	/** Default: "zk.wrongValue" if {@link #getDepends} is not null (component-level),
	 * or null if {@link #getDepends} is null (desktop level)
	 * @since 5.0.2
	 */
	public String getOverrideKey() {
		return getDepends() != null ? "zk.wrongValue": null;
	}
}
