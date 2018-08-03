/* RichletConfig.java

	Purpose:
		
	Description:
		
	History:
		Thu Oct  5 13:13:16     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui;

/**
 * A richlet configuration object used by a richlet container to pass information
 * to a richlet during initialization.
 *
 * @author tomyeh
 */
public interface RichletConfig {
	/** Returns the web application that the richlet belongs to.
	 */
	public WebApp getWebApp();

	/** Returns a String containing the value of the named initialization
	 * parameter, or null if the parameter does not exist.
	 */
	public String getInitParameter(String name);
	/** Returns the names of the richlet's initialization parameters as
	 * an iterable String objects (never null).
	 * @since 6.0.0
	 */
	public Iterable<String> getInitParameterNames();
}
