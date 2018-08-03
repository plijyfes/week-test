/* Merger.java

	Purpose:
		
	Description:
		
	History:
		Fri Jul 30 18:53:19 TST 2010, Created by tomyeh

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zk.ui.ext.render;

import org.zkoss.zk.ui.Component;

/**
 * Implemented by the object returned by {@link org.zkoss.zk.ui.sys.ComponentCtrl#getExtraCtrl}
 * to indicate a component is capable to merge with its sibling and children.
 * 
 * @author tomyeh
 * @since 5.0.4
 * @deprecated As of release 6.5.1, please use stubonly instead.
 */
public interface Merger {
	/** Merges the next sibling into one component, if possible.
	 * @return the component after merged, or null if it is not able
	 * to merge the next sibling.
	 */
	public Component mergeNextSibling();
	/** Merges the children into one component, if possible.
	 * <p>Notice that {@link #mergeNextSibling} is called first to merge
	 * all adjacent siblings if possible, and then {@link #mergeChildren}
	 * is called to merge parent and children.
	 * @return the component after merged, or null if it is not able
	 * to merge the children.
	 */
	public Component mergeChildren();
}
