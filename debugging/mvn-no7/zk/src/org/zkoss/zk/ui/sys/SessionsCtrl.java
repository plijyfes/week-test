/* SessionsCtrl.java

	Purpose:
		
	Description:
		
	History:
		Mon May 30 22:03:45     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.sys;

import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.http.SimpleSession;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zk.ui.util.Monitor;

/**
 * An additional utilities for implementation related to {@link Session}.
 *
 * @author tomyeh
 */
public class SessionsCtrl extends Sessions {
	private static final Log log = Log.lookup(SessionsCtrl.class);
	
	protected SessionsCtrl() {} //prevent from instantiation

	/** Sets the session for the current thread.
	 * Called only internally.
	 */
	public static final void setCurrent(Session sess) {
		_sess.set(sess);
	}
	/** Sets the session for the current thread.
	 * Unlike {@link #setCurrent(Session)}, the session is resolved
	 * later (when {@link #getCurrent} is called).
	 * @since 5.0.0
	 */
	public static final void setCurrent(SessionResolver sr) {
		_sess.set(sr);
	}
	/** Returns the current {@link SessionCtrl}.
	 */
	public static final SessionCtrl getCurrentCtrl() {
		return (SessionCtrl)getCurrent();
	}

	/** Returns the current session or the current session resolver.
	 * The returned value is either {@link Session} or {@link SessionResolver}.
	 * @see #setRawCurrent
	 * @see #setCurrent(Session)
	 * @see #setCurrent(SessionResolver)
	 * @since 5.0.0
	 */
	public static final Object getRawCurrent() {
		return _sess.get();
	}
	/** Sets the current session or the current session resolver.
	 * @param rawsess the raw session. It can be null, an instance of
	 * {@link Session} and {@link SessionResolver}.
	 * Notice: it is usually the return value of {@link #getRawCurrent}.
	 * @see #getRawCurrent
	 * @since 5.0.0
	 */
	public static final void setRawCurrent(Object rawsess) {
		if (rawsess != null && !(rawsess instanceof Session) && !(rawsess instanceof SessionResolver))
			throw new IllegalArgumentException(rawsess.getClass().getName());
		_sess.set(rawsess);
	}

	/** Update the session count.
	 * <p>Called only internally.
	 * @since 5.0.0
	 */
	public static final void updateCount(boolean inc) {
		synchronized (SessionsCtrl.class) {
			if (inc) ++_cnt;
			else --_cnt;
		}
	}

	/** Called when a servlet/portlet starts to serve a request.
	 * It checks whether the number of concurrent requests per session
	 * exceeds the number specified in
	 * {@link org.zkoss.zk.ui.util.Configuration#getSessionMaxRequests}.
	 * If exceeded, false is returned, and the servlet/portlet shall stop
	 * processing and return an error code back the client.
	 *
	 * <p>If not exceeded, true is returned, and the servlet/portlet
	 * can continue the processing and it shall invoke {@link #requestExit}
	 * in the finally clause.
	 *
	 * @since 3.0.1
	 */
	public static boolean requestEnter(Session sess) {
		final Integer v = (Integer)sess.getAttribute(ATTR_REQUEST_COUNT);
		final int i = v != null ? v.intValue() + 1: 1;
		final int max = sess.getWebApp().getConfiguration().getSessionMaxRequests();
		if (max < 0 || i <= max) {
			sess.setAttribute(ATTR_REQUEST_COUNT, new Integer(i));
			return true;
		}
		return false;
	}
	/**
	 * Called when a servlet/portlet completes the service of a request.
	 * This method must be called if {@link #requestEnter} is called
	 * and returns true. This method shall not be called, otherwise.
	 *
	 * @since 3.0.1
	 */
	public static void requestExit(Session sess) {
		final Integer v = (Integer)sess.getAttribute(ATTR_REQUEST_COUNT);
		final int i = v != null ? v.intValue() - 1: 0;
		sess.setAttribute(ATTR_REQUEST_COUNT, new Integer(i >= 0 ? i: 0));
	}
	private static final String ATTR_REQUEST_COUNT
		= "org.zkoss.zk.ui.sys.RequestCount";

	/** Returns the ZK session associated with the specified native session,
	 * or null if not found.
	 *
	 * @param navsess the native session (never null).
	 * If HTTP, it is HttpSession. If portlet, it is PortletSession.
	 * @since 3.0.5
	 */
	public static final Session getSession(WebApp wapp, Object navsess) {
		final SessionCache sc = ((WebAppCtrl)wapp).getSessionCache();
		if (sc == null) return null;
			//bug 2668190: happens when destroying app in WebSphere 7

		final Session sess = sc.get(navsess);
		if (sess != null && sess.getNativeSession() != navsess)
			((SessionCtrl)sess).recover(navsess);
		return sess;
	}
	/** Instantiates a ZK session that is associated with the specified
	 * native session and request.
	 *
	 * @param navsess the native session (never null).
	 * If HTTP, it is HttpSession. If portlet, it is PortletSession.
	 * @since 3.0.5
	 */
	public static final
	Session newSession(WebApp wapp, Object navsess, Object request) {
		final WebAppCtrl wappc = (WebAppCtrl)wapp;
		final Session sess =
			wappc.getUiFactory().newSession(wapp, navsess, request);
		wappc.getSessionCache().put(sess);

		final Configuration config = wapp.getConfiguration();
		config.invokeSessionInits(sess, request); //it might throw exception

		final Monitor monitor = config.getMonitor();
		if (monitor != null) {
			try {
				monitor.sessionCreated(sess);
			} catch (Throwable ex) {
				log.error(ex);
			}
		}
		
		//Note: we set timeout here, because HttpSession might have been created
		//by other servlet or filter
		final int v = wapp.getConfiguration().getSessionMaxInactiveInterval();
		if (v != 0) sess.setMaxInactiveInterval(v);
		return sess;
	}
}
