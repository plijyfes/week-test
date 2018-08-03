/* AuObsolete.java

	Purpose:
		
	Description:
		
	History:
		Wed Oct 12 23:45:30     2005, Created by tomyeh

Copyright (C) 2004 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.au.out;

import org.zkoss.zk.au.AuResponse;

/**
 * A response to denote the desktop might become obsolete.
 * <p>data[0]: the desktop ID.<br/>
 * data[1]: the message
 *
 * @author tomyeh
 * @since 3.0.0
 */
public class AuObsolete extends AuResponse {
	public AuObsolete(String dtid, String message) {
		super("obsolete", new String[] {dtid, message});
	}

	/** Default: zk.obsolete (i.e., only one response of this class will
	 * be sent to the client in an execution)
	 * @since 5.0.2
	 */
	public final String getOverrideKey() {
		return "zk.obsolete";
	}
}
