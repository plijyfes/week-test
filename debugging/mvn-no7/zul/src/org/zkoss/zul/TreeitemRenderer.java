/* TreeitemRenderer.java

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
package org.zkoss.zul;

import org.zkoss.zul.event.TreeDataEvent;

/**
 * Identifies components that can be used as "rubber stamps" to paint
 * the cells in a {@link Tree}.
 *
 * <p>Note: changing a render will not cause the tree to re-render.
 * If you want it to re-render, you could assign the same model again 
 * (i.e., setModel(getModel())), or fire an {@link TreeDataEvent} event.
 *
 * @author Jeff Liu
 * @since 3.0.0
 * @see TreeModel
 * @see Tree
 */
public interface TreeitemRenderer<T> {
	/** 
	 * Renders the data to the specified tree item.
	 * 
	 * 
	 *
	 * @param item the Treeitem to render the result.
	 * <br>Note: 
	 * 
	 *<ol>
	 * <li>When this method is called, the treeitem should have no child
	 * at all, unless you don't return</li>
	 * <li>Treeitem and Treerow are only components that allowed to be
	 * <b>item</b>'s children.</li>
	 * <li>A new treerow should be constructed and append to <b>item</b>, when
	 * treerow of <b>item</b> is null.<br/> Otherwise, when treerow of <b>item</b> is not null, 
	 * modify the content of the treerow or detach the treerow's children first, since that only one treerow is allowed</li>
	 * <li>Do not append any treechildren to <b>item</b> in this method, a treechildren will be appended afterward.</li>
	 * <li>When a treerow is not appended to <b>item</b>,  generally label of <b>item</b> is displayed.</li> 
	 * </ol>
	 * @param data that is used to render the Treeitem
	 * @param index the index of the data that is currently being rendered.
	 * Notice the index is the order of the siblings (i.e., data that belongs
	 * to the same parent).
	 */
	public void render(Treeitem item, T data, int index) throws Exception;
}
