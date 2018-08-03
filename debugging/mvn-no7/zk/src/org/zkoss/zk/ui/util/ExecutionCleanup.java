/* ExecutionCleanup.java

	Purpose:
		
	Description:
		
	History:
		Thu Aug 24 23:31:23     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.util;

import java.util.List;
import org.zkoss.zk.ui.Execution;

/**
 * Used to clean up an execution.
 *
 * <p>How this interface is used.
 * <ol>
 * <li>First, you specify a class that implements this interface
 * in WEB-INF/zk.xml as a listener.
 * </li>
 * <li>Then, even time ZK loader is destroying an execution, an instance of
 * the specified class is instantiated and {@link #cleanup} is called.</li>
 * </ol>
 * 
 * @author tomyeh
 */
public interface ExecutionCleanup {
	/** called when an execution is about to be destroyed.
	 *
	 * <p>If this method throws an exception, the stack trace will be logged,
	 * and the error message will be displayed at the client.
	 *
	 * <p>When this method is invoked, the execution is still activated,
	 * so you can create components here.
	 *
	 * @param exec the execution to clean up.
	 * @param parent the previous execution, or null if no previous at all
	 * @param errs a list of exceptions (java.lang.Throwable) if any exception
	 * occurred before this method is called, or null if no exception at all.
	 * Note: you can manipulate the list directly to add or clean up exceptions.
	 * For example, if exceptions are fixed correctly, you can call errs.clear()
	 * such that no error message will be displayed at the client.
	 */
	public void cleanup(Execution exec, Execution parent, List<Throwable> errs)
	throws Exception;
}
