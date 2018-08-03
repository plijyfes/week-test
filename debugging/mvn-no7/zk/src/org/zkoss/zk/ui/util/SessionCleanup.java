/* SessionCleanup.java

	Purpose:
		
	Description:
		
	History:
		Wed Mar 22 11:34:15     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.util;

import org.zkoss.zk.ui.Session;

/**
 * Used to clean up a session.
 *
 * <p>How this interface is used.
 * <ol>
 * <li>First, you specify a class that implements this interface
 * in WEB-INF/zk.xml as a listener.
 * </li>
 * <li>Then, even time ZK loader is destroying a session, an instance of
 * the specified class is instantiated and {@link #cleanup} is called.</li>
 * </ol>
 *
 * @author tomyeh
 */
public interface SessionCleanup {
	/** called when a session is about to be destroyed.
	 *
	 * <p>If this method throws an exception, the error message is
	 * only logged (user won't see it).
	 */
	public void cleanup(Session sess) throws Exception;
}
