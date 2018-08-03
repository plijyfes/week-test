/* Sessions.java

	Purpose:
		
	Description:
		
	History:
		Mon May 30 21:33:01     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui;

import org.zkoss.zk.ui.sys.SessionResolver;

/**
 * Utilities to access {@link Session}.
 *
 * @author tomyeh
 */
public class Sessions {
	/** Used to the store the session for the current thread. */
	protected static final ThreadLocal<Object> _sess = new ThreadLocal<Object>();
	protected static int _cnt;

	protected Sessions() {} //prevent from instantiated

	/** Returns the number of total alive session.
	 * @since 5.0.0
	 */
	public static final int getCount() {
		return _cnt;
	}

	/** Returns the session for the current thread, or null if not available.
	 * It is the same as getSession(true).
	 * <p>Note: it returns null if it is called in a working thread.
	 */
	public static final Session getCurrent() {
		return getCurrent(true);
	}
	/** Returns the session for the current thread, or null if not available.
	 *
	 * @param create whether to create a new session if not available.
	 * Notice that this method might return null if it is called in
	 * a working thread, even though create is true,
	 * @since 5.0.0
	 */
	public static final Session getCurrent(boolean create) {
		final Object o = _sess.get();
		if (o instanceof SessionResolver) {
			Session sess = ((SessionResolver)o).getSession(create);
			if (sess != null) _sess.set(sess);
			return sess;
		}
		return (Session)o;
	}
}
