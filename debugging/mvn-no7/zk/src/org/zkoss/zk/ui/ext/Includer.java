/* Includer.java

	Purpose:
		
	Description:
		
	History:
		Mon Oct 20 14:02:51     2008, Created by tomyeh

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.ext;

import org.zkoss.zk.ui.Page;

/**
 * Implemented by a component to indicate that
 * it might include another ZUML page.
 *
 * <p>The owner of the included page is this page
 * (see {@link org.zkoss.zk.ui.sys.PageCtrl#getOwner}).
 *
 * @author tomyeh
 * @since 5.0.0
 * @see org.zkoss.zk.ui.sys.PageCtrl#getOwner
 */
public interface Includer {
	/** Returns the child page.
	 */
	public Page getChildPage();
	/** Sets the child page.
	 * <p>Used only for implementing an includer component (such as
	 * {@link org.zkoss.zul.Include}).
	 * <P>Note: the child page is actually maintained by
	 * the included page, so the implementation of this method
	 * needs only to store the page in a transient member.
	 * <p>Notice that, like {@link #setRenderingResult}, it is called only if
	 * the included page is a ZUML document.
	 */
	public void setChildPage(Page page);

	/** Sets the rendering result
	 * It is called when the child page ({@link #getChildPage}) has
	 * been rendered. The includer can then generate it to the output
	 * in the way it'd like.
	 * <p>Used only for implementing an includer component (such as
	 * {@link org.zkoss.zul.Include}).
	 * <p>Notice that, like {@link #setChildPage}, it is called only if
	 * the included page is a ZUML document.
	 * @since 5.0.6
	 */
	public void setRenderingResult(String result);
}
