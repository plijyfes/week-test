/* TreeDataListener.java

	Purpose:
		
	Description:
		
	History:
		Aug 10 2007, Created by Jeff Liu

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.event;

/**
 * Defines the methods used to listener when the content of
 * {@link org.zkoss.zul.TreeModel} is changed.
 *
 * @author Jeff Liu
 * @since 3.0.0
 * @see org.zkoss.zul.TreeModel
 * @see TreeDataEvent
 */
public interface TreeDataListener {
	/** Sent when the contents of the tree has changed.
	 */
	public void  onChange(TreeDataEvent event);
}
