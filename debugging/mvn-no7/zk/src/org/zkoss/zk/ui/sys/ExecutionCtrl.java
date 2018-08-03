/* ExecutionCtrl.java

	Purpose:
		
	Description:
		
	History:
		Mon Jun  6 14:36:47     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.sys;

import java.util.Collection;

import org.zkoss.xel.XelContext;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.au.AuResponse;

/**
 * Additional interface to {@link org.zkoss.zk.ui.Execution}
 * for implementation.
 *
 * <p>Application developers shall never access any of this methods.
 *
 * @author tomyeh
 */
public interface ExecutionCtrl {
	/** Returns the current page.
	 * Though an execution might process many pages, it processes update
	 * requests one-by-one and each update request is associated
	 * with a page.
	 *
	 * <p>Design decision: we put it here because user need not to know
	 * about the concept of the current page.
	 *
	 * <p>Since 3.6.0, this method returns the first page if
	 * {@link #setCurrentPage} was not called (such as Server Push).
	 *
	 * @see Desktop#getPage
	 */
	public Page getCurrentPage();
	/** Sets the current page.
	 * Though an execution might process many pages, it processes update requests
	 * one-by-one and each update request is associated with a page.
	 */
	public void setCurrentPage(Page page);

	/** Returns the current page definition, which is pushed when
	 * evaluating a page (from a page definition).
	 */
	public PageDefinition getCurrentPageDefinition();
	/** Sets the current page definition.
	 * @param pgdef the page definition. If null, it means it is the same
	 * as getCurrentPage().getPageDefinition().
	 */
	public void setCurrentPageDefinition(PageDefinition pgdef);

	/** Returns the next event queued by
	 * {@link org.zkoss.zk.ui.Execution#postEvent}, or null if no event queued.
	 * <p>Implementation Notes:
	 * {@link org.zkoss.zk.ui.Execution#postEvent(int,Component,Event)}
	 * proxies the event with {@link org.zkoss.zk.ui.impl.ProxyEvent}
	 * if the real target is different from {@link Event#getTarget}.
	 * Of course, it is transparent to the event listeners since the real
	 * event will be passed to the listener (rather than the proxy event).
	 */
	public Event getNextEvent();

	/** Returns whether this execution is activated.
	 */
	public boolean isActivated();
	/** Called when this execution is about to become the current execution
	 * {@link org.zkoss.zk.ui.Executions#getCurrent}.
	 *
	 * <p>Note: an execution might spread over several threads, so
	 * this method might be called several times to activate the states
	 * in each thread. Also, an execution might be activated before another
	 * is deactivate. For example, when a component includes another page,
	 * the second exec is activated to render the included page.
	 *
	 * <p>It is used as callback notification.
	 *
	 * <p>Note: don't throw any exception in this method.
	 */
	public void onActivate();
	/** Called when this execution is about to become a non-current execution.
	 *
	 * <p>It is used as callback notification.
	 *
	 * <p>Note: don't throw any exception in this method.
	 *
	 * @see #onActivate
	 */
	public void onDeactivate();

	/** Returns whether this execution is in recovering.
	 * In other words, it is in the invocation of {@link FailoverManager#recover}.
	 * If in recovering, no response is sent to the client.
	 * It assumes the server is recovering the desktop and all it contains
	 * to match the client's status.
	 */
	public boolean isRecovering();

	/** Returns the {@link Visualizer} for this execution.
	 * It is the same as {@link DesktopCtrl#getVisualizer}.
	 */
	public Visualizer getVisualizer();

	/** Sets the content type.
	 * @since 5.0.0
	 */
	public void setContentType(String contentType);

	/** Sets the desktop associated with this execution.
	 * You rarely need to use this method, since the desktop is associated
	 * when this execution is created.
	 *
	 * <p>Currently, it is used to communicate between WebManager.newDesktop
	 * and DesktopImpl's constructor.
	 *
	 * @exception IllegalArgumentException if desktop is null
	 * @exception IllegalStateException if there is already a desktop
	 * is associated with it.
	 * @since 3.0.0
	 */
	public void setDesktop(Desktop desktop);

	/** Sets the sequence ID of the current request.
	 * @since 3.0.5
	 */
	public void setRequestId(String reqId);
	/** Returns the sequence ID of the current request, or null if not
	 * available. Not all clients support the request ID.
	 * @since 3.0.5
	 */
	public String getRequestId();

	/** Returns the collection of the AU responses ({@link AuResponse})
	 * that shall be generated to the output, or null if not available.
	 * @since 5.0.0
	 */
	public Collection<AuResponse> getResponses();
	/** Sets the collection of the AU responses ({@link AuResponse})
	 * that shall be generated to the output.
	 * @since 5.0.0
	 */
	public void setResponses(Collection<AuResponse> responses);

	/** Returns the information of the event being served, or null
	 * if the execution is not under serving an event.
	 * <p>Unlike most of other methods, this method could be accessed
	 * by another thread.
	 * @since 5.0.6
	 */
	public ExecutionInfo getExecutionInfo();
	/** Sets the information of the event being served, or null if not under
	 * serving an event.
	 * @since 5.0.6
	 */
	public void setExecutionInfo(ExecutionInfo evtinf);

	/** Returns the object, if any, defined in any variable resolver
	 * added by {@link org.zkoss.zk.ui.Execution#addVariableResolver}.
	 * <p>Notice that it looks only for the variables defined
	 * in {@link org.zkoss.zk.ui.Execution#addVariableResolver}. To get a variable an EL expression
	 * can reference, please use {@link org.zkoss.zk.ui.Execution#getVariableResolver} instead.
	 * @since 6.0.0
	 */
	public Object getExtraXelVariable(String name);
	/** Returns the object, if any, defined in any variable resolver
	 * added by {@link org.zkoss.zk.ui.Execution#addVariableResolver}.
	 * <p>Notice that it looks only for the variables defined
	 * in {@link org.zkoss.zk.ui.Execution#addVariableResolver}. To get a variable an EL expression
	 * can reference, please use {@link org.zkoss.zk.ui.Execution#getVariableResolver} instead.
	 * <p>Unlike {@link #getExtraXelVariable(String)}, this method
	 * can utilize {@link org.zkoss.xel.VariableResolverX} if you'd like
	 * to retrieve a property of another object.
	 * @param ctx the XEL context
	 * @param base the base object. If null, it looks for a top-level variable.
	 * If not null, it looks for a member of the base object (such as getter).
	 * @param name the property to retrieve.
	 * @see #getExtraXelVariable(String)
	 * @since 6.0.0
	 */
	public Object getExtraXelVariable(XelContext ctx, Object base, Object name);
}
