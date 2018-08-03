/* AuSelect.java

	Purpose:
		
	Description:
		
	History:
		Thu Jul 30 17:39:43     2009, Created by tomyeh

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.zk.au.out;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.au.AuResponse;

/**
 * A response to select a portion of the specified component at the client.
 * <p>data[0]: the uuid of the component to select<br/>
 * data[1]: the start position (optional)</br>
 * data[2]: the end position (optional)
 * 
 * @author tomyeh
 * @since 5.0.0
 */
public class AuSelect extends AuResponse {
	public AuSelect(Component comp) {
		super("select", comp, comp);
	}
	public AuSelect(Component comp, int beg, int end) {
		super("select", comp,
			new Object[] {comp, new Integer(beg), new Integer(end)});
	}

	/** Default: zk.select (i.e., only one response of this class
	 * for the same component will be sent to the client in an execution)
	 * @since 5.0.2
	 */
	public final String getOverrideKey() {
		return "zk.select";
	}
}
