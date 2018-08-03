/* AuMoveBy.java

	Purpose:
		
	Description:
		
	History:
		Fri Jul 28 16:12:12     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.au.out;

import org.zkoss.zk.au.AuResponse;

/**
 * A response to ask the client to move the desktop (a.k.a., the browser window)
 * relatively (in pixels).
 *
 * <p>data[0]: x<br/>
 * data[1]: y
 * 
 * @author tomyeh
 * @since 3.0.0
 */
public class AuMoveBy extends AuResponse {
	public AuMoveBy(int x, int y) {
		super("moveBy", new Integer[] {new Integer(x), new Integer(y)});
	}
}
