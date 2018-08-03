/* AuClearBusy.java

	Purpose:
		
	Description:
		
	History:
		Thu Jan 21 15:35:51 TST 2010, Created by tomyeh

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zk.au.out;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.au.AuResponse;

/**
 * A response to ask the client to clear the busy message.
 * 
 * @author tomyeh
 * @see AuShowBusy
 * @since 5.0.0
 */
public class AuClearBusy extends AuResponse {
	/** Constructs a command to remove a busy message covering the whole browser.
	 */
	public AuClearBusy() {
		super("clearBusy");
	}
	/** Constructs a command to remove a busy message covering only the specified component.
	 * @param comp the component that the busy message is associated.
	 */
	public AuClearBusy(Component comp) {
		super("clearBusy", comp, comp);
	}

	/** Default: zk.busy (i.e., only one response of this class and {@link AuShowBusy} will
	 * be sent to the client in an execution for the same component, if any)
	 * @since 5.0.2
	 */
	public final String getOverrideKey() {
		return "zk.busy";
	}
}
