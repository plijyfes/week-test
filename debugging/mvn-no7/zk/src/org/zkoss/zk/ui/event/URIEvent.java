/* URIEvent.java

	Purpose:
		
	Description:
		
	History:
		Thu Aug  7 09:19:15     2008, Created by tomyeh

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.event;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.au.AuRequest;

/**
 * The URI update event used with <code>onURIChange</code>
 * to notify that the associated URI
 * is changed by the user. Currently it is supported only by ZUL's
 * iframe component (and only if iframe contains a ZK page).
 *
 * <p>A typical use of this event is to support a better bookmarking
 * for a page containing iframe.
 *
 * <p>Unlike {@link BookmarkEvent}, this event is sent to the component
 * (iframe) directly.
 *
 * @author tomyeh
 * @since 3.5.0
 */
public class URIEvent extends Event {
	/** The URI. */
	private final String _uri;

	/** Converts an AU request to an URI event.
	 * @since 5.0.0
	 */
	public static final URIEvent getURIEvent(AuRequest request) {
		final Map<String, Object> data = request.getData();
		String uri = (String)data.get("");
		int urilen = uri.length();
		if (urilen > 0 && uri.charAt(0) == '/') {
			//Convert URL to URI if starting with the context path
			String ctx = Executions.getCurrent().getContextPath();
			int ctxlen = ctx != null ? ctx.length(): 0;
			if (ctxlen > 0 && !"/".equals(ctx)) {
				if (ctx.charAt(0) != '/') { //just in case
					ctx = '/' + ctx;
					++ctxlen;
				}
				if (uri.startsWith(ctx)
				&& (urilen == ctxlen || uri.charAt(ctxlen) == '/'))
					uri = uri.substring(ctxlen);
			}
		}

		return new URIEvent(request.getCommand(), request.getComponent(), uri);
	}

	/** Constructs an URI update event.
	 * @param target the component to receive the event.
	 * @param uri the URI. Note: it doesn't include the context path
	 * unless it starts with a protocol (such as http://).
	 */
	public URIEvent(String name, Component target, String uri) {
		super(name, target);
		_uri = uri != null ? uri: "";
	}

	/** Returns the URI (never null).
	 * Notice that it does not include the context path, unless
	 * it starts with a protocol (such as http://).
	 */
	public String getURI() {
		return _uri;
	}
}
