/* EventQueues.java

	Purpose:
		
	Description:
		
	History:
		Fri May  2 15:30:36     2008, Created by tomyeh

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zk.ui.event;

import org.zkoss.lang.Library;
import org.zkoss.lang.Classes;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;

import org.zkoss.zk.ui.event.impl.EventQueueProvider;
import org.zkoss.zk.ui.event.impl.EventQueueProviderImpl;

/**
 * Utilities to access the event queue.
 *
 * <h3>Customization:</h3>
 *
 * <p>The implementation of {@link EventQueue} and even the scope
 * are customizable. To customize, specify the name of a class implementing
 * {@link EventQueueProvider} in the library property called
 * "org.zkoss.zk.ui.event.EventQueueProvider.class".
 * For example, you can use JMS to extend the queue to be able to communicate
 * with applications running in different JVM.
 * @author tomyeh
 * @since 5.0.0
 */
public class EventQueues {
	/** Represents the event queue in the desktop scope.
	 * In other words, the events published to this kind of queues
	 * can be passed around only in the same desktop.
	 */
	public static final String DESKTOP = "desktop";
	/** Represents a group of desktops that belongs to the same browser.
	 * It is formed if iframe or frameset is used.
	 * <p>Unlike {@link #APPLICATION} and {@link #SESSION}, it does NOT require
	 * the server push, so there is no overhead.
	 * However, it cannot communicate with desktops that belongs to other top
	 * browser windows/tabs. Since there is no way to detect two desktops
	 * (of the same session) belongs to the same top browser window, the developer
	 * has to make sure of it by himself.
	 * <p>Some portal container, such as Liferay, also forms a group of desktops
	 * (they all belongs to the same browser window). Notice that
	 * org.zkoss.zkmax.zul.Portallayout is a component and it won't cause
	 * additional desktop to be created, unless iframe is used as the content.
	 * <p>Note: this feature requires ZK EE.
	 * @since 5.0.4
	 */
	public static final String GROUP = "group";
	/** Represents the event queue in the application scope.
	 * In other words, the events published to this kind of queues
	 * can be passed around to any desktops of the same application.
	 * <p>Notice that this feature will enable the server push
	 * ({@link org.zkoss.zk.ui.sys.ServerPush}.
	 */
	public static final String APPLICATION = "application";
	/** Represents the event queue in the sessions cope.
	 * In other words, the events published to this kind of queues
	 * can be passed around to any desktops of the same session.
	 * <p>Notice that this feature will enable the server push
	 * ({@link org.zkoss.zk.ui.sys.ServerPush}.
	 */
	public static final String SESSION = "session";

	/** Returns the event queue with the specified name in the
	 * specified scope.
	 *
	 * <p>There are several kinds of event scopes: {@link #DESKTOP},
	 * {@link #GROUP}, {@link #SESSION}, and {@link #APPLICATION}.
	 *
	 * <p>If the {@link #DESKTOP} scope is specified, the event queue is
	 * associated with the desktop of the current execution.
	 * And, the event queue is gone if the desktop is removed,
	 * or removed manually by {@link #remove}.
	 *
	 * <p>If the {@link #SESSION} or {@link #GROUP} scope is specified,
	 * the event queue is associated with the current session.
	 * And, the event queue is gone if the session is invalidated,
	 * or removed manually by {@link #remove}.
	 *
	 * <p>If the {@link #APPLICATION} scope is specified, the event queue is
	 * associated with the application, and remains until the application
	 * stops or removed manually by {@link #remove}.
	 *
	 * <p>When an execution subscribes an event queue of {@link #SESSION}
	 * or {@link #APPLICATION}, the server push is enabled automatically.
	 * On the other hand, {@link #DESKTOP} and {@link #GROUP} does NOT
	 * require the server push -- they use the AU requests for communication.
	 *
	 * <p>Note:
	 * <ul>
	 * <li>This method can be called only in an activated execution,
	 * i.e., {@link org.zkoss.zk.ui.Executions#getCurrent} not null.
	 * If you want to use it without an execution, please use
	 * 	{@link #lookup(String, Session, boolean)} or {@link #lookup(String, WebApp, boolean)}
	 * instead (depending on your scope).</li>
	 * </ul>
	 *
	 * @param name the queue name.
	 * @param scope the scope of the event queue. Currently,
	 * it supports {@link #DESKTOP}, {@link #GROUP},
	 * {@link #SESSION}, and {@link #APPLICATION}.
	 * Note: {@link #GROUP} requires ZK EE.
	 * @param autoCreate whether to create the event queue if not found.
	 * @return the event queue with the associated name, or null if
	 * not found and autoCreate is false
	 * @see #lookup(String, Session, boolean)
	 * @see #lookup(String, WebApp, boolean)
	 * @exception IllegalStateException if not in an activated execution
	 * @exception UnsupportedOperationException if the scope is not supported
	 */
	public static <T extends Event>
	EventQueue<T> lookup(String name, String scope, boolean autoCreate) {
		return getProvider().lookup(name, scope, autoCreate);
	}
	/** Returns the event queue with the specified name in the
	 * give session (i.e., the session scope).
	 * <p>Unlike {@link #lookup(String, String, boolean)}, this method
	 * can be called without an activated execution.
	 * @param sess the session that the event queue is located (i.e.,
	 * the session scope)
	 * @see #lookup(String, String, boolean)
	 * @see #lookup(String, WebApp, boolean)
	 * @since 5.0.2
	 */
	public static <T extends Event>
	EventQueue<T> lookup(String name, Session sess, boolean autoCreate) {
		return getProvider().lookup(name, sess, autoCreate);
	}
	/** Returns the event queue with the specified name in the
	 * give application (i.e., the application scope).
	 * <p>Unlike {@link #lookup(String, String, boolean)}, this method
	 * can be called without an activated execution.
	 * @param wapp the Web application that the event queue is located (i.e.,
	 * the application scope)
	 * @see #lookup(String, String, boolean)
	 * @see #lookup(String, Session, boolean)
	 * @since 5.0.2
	 */
	public static <T extends Event>
	EventQueue<T> lookup(String name, WebApp wapp, boolean autoCreate) {
		return getProvider().lookup(name, wapp, autoCreate);
	}
	/** Returns the desktop-level event queue with the specified name in the current
	 * desktop.
	 * It is a shortcut of <code>lookup(name, DESKTOP, autoCreate)</code>.
	 */
	public static <T extends Event>
	EventQueue<T> lookup(String name, boolean autoCreate) {
		return lookup(name, DESKTOP, autoCreate);
	}
	/** Returns the desktop-level event queue with the specified name in the current
	 * desktop, or if no such event queue, create one.
	 * It is a shortcut of <code>lookup(name, DESKTOP, true)</code>.
	 */
	public static <T extends Event> EventQueue<T> lookup(String name) {
		return lookup(name, DESKTOP, true);
	}

	/** Tests if the specified event queue has been created.
	 */
	public static boolean exists(String name, String scope) {
		return lookup(name, scope, false) != null;
	}
	/** Tests if the specified event queue has been created
	 * in the current desktop.
	 * It is a shortcut of <code>exists(name, DESKTOP)</code>
	 */
	public static boolean exists(String name) {
		return lookup(name, false) != null;
	}

	/** Removes the event queue.
	 * It is the same as <code>remove(name, DESKTOP)</code>.
	 * @param name the queue name.
	 * @return true if it is removed successfully
	 */
	public static boolean remove(String name) {
		return remove(name, DESKTOP);
	}
	/** Removes the event queue of the specified scope.
	 * @param name the queue name.
	 * @param scope the scope of the event queue. Currently,
	 * it supports {@link #DESKTOP}, {@link #GROUP},
	 * {@link #SESSION}, and {@link #APPLICATION}.
	 * Note: {@link #GROUP} requires ZK EE.
	 * @return true if it is removed successfully
	 */
	public static boolean remove(String name, String scope) {
		return getProvider().remove(name, scope);
	}
	/** Removes the event queue of the specified session.
	 * <p>Unlike {@link #remove(String, String)}, this method
	 * can be called without an activated execution.
	 * @param name the queue name.
	 * @param sess the session that the event queue is located (i.e.,
	 * the session scope)
	 * @return true if it is removed successfully
	 * @since 5.0.4
	 */
	public static boolean remove(String name, Session sess) {
		return getProvider().remove(name, sess);
	}
	/** Removes the event queue of the specified application.
	 * <p>Unlike {@link #remove(String, String)}, this method
	 * can be called without an activated execution.
	 * @param name the queue name.
	 * @param wapp the Web application that the event queue is located (i.e.,
	 * the application scope)
	 * @return true if it is removed successfully
	 * @since 5.0.4
	 */
	public static boolean remove(String name, WebApp wapp) {
		return getProvider().remove(name, wapp);
	}
	private static final EventQueueProvider getProvider() {
		if (_provider == null)
			synchronized (EventQueues.class) {
				if (_provider == null) {
					EventQueueProvider provider = null;
					String clsnm = Library.getProperty("org.zkoss.zk.ui.event.EventQueueProvider.class");
					if (clsnm == null)
						clsnm = Library.getProperty("org.zkoss.zkmax.ui.EventQueueProvider.class");
							//backward compatible
					if (clsnm != null)
						try {
							final Object o = Classes.newInstanceByThread(clsnm);
									//try zkex first
							if (!(o instanceof EventQueueProvider))
								throw new UiException(o.getClass().getName()+" must implement "+EventQueueProvider.class.getName());
							provider = (EventQueueProvider)o;
						} catch (UiException ex) {
							throw ex;
						} catch (Throwable ex) {
							throw UiException.Aide.wrap(ex, "Unable to load "+clsnm);
						}
					if (provider == null)
						provider = new EventQueueProviderImpl();
					_provider = provider;
				}
			}
		return _provider;
	}
	private static volatile EventQueueProvider _provider;
}
