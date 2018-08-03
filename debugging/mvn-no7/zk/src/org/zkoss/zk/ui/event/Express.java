/* Express.java

	Purpose:
		
	Description:
		
	History:
		Tue Dec 12 15:35:17     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.event;

/**
 * @deprecated As of release 6.0.0, replaced with
 * {@link org.zkoss.zk.ui.Component#addEventListener(int, String, EventListener)}
 * (and specifying the priority to 1000 or greater).
 * Decorates an event listener ({@link EventListener}) shall be invoked
 * before other event listeners, including the onXxx member declared in the
 * ZUML pages.
 *
 * @author tomyeh
 */
public interface Express {
}
