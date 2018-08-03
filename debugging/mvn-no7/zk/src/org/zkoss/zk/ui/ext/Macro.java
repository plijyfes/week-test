/* Macro.java

	Purpose:
		
	Description:
		
	History:
		Wed Feb 21 23:05:29     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.ext;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.IdSpace;

/**
 * Implemented with {@link org.zkoss.zk.ui.Component} to represent
 * a macro component.
 *
 * @author tomyeh
 */
public interface Macro extends AfterCompose, IdSpace, DynamicPropertied {
	/** Sets the macro URI.
	 * It affects only this component.
	 *
	 * <p>Note: this method calls {@link #recreate} automatically
	 * if uri is changed.
	 *
	 * @param uri the URI of this macro. If null, the default is used.
	 */
	public void setMacroURI(String uri);
	/** Returns the macro URI.
	 * <p>If {@link #setMacroURI} wasn't called, it returns the URI
	 * defined in the macro definition.
	 * @since 3.6.0
	 */
	public String getMacroURI();
	/** Detaches all child components and then recreate them.
	 *
	 * <p>It is used if you have assigned new values to dynamic properties
	 * and want to re-create child components to reflect the new values.
	 * Note: it is convenient but the performance is better if you can manipulate
	 * only the child components that need to be changed.
	 * Refer to <a href="http://books.zkoss.org/wiki/ZK_Developer%27s_Reference/UI_Composing/Macro_Component">ZK Developer's Reference</a> for details.
	 */
	public void recreate();
	/** Returns whether this is an inline macro.
	 * The only way to create an inline macro is by use of
	 * {@link org.zkoss.zk.ui.metainfo.ComponentDefinition#newInstance}.
	 *
	 * @since 2.4.0
	 */
	public boolean isInline();
	/** Sets the parent to the given one and insert the children of
	 * the inline macro right before the given sibling (beforeSibling).
	 *
	 * <p>This method is used only internally.
	 *
	 * <p>Notice that when {@link org.zkoss.zk.ui.AbstractComponent#insertBefore}
	 * is called to insert an inline macro ({@link #isInline}),
	 * the invocation will be forwarded to this method.
	 * It is called only {@link #isInline} is true.
	 *
	 * @param parent the parent
	 * @param beforeSibling a child of the parent that the macro component
	 * will be inserted before
	 * @exception IllegalStateException if {@link #isInline} is false.
	 * @return if it has been added successfully
	 * @since 5.0.4
	 */
	public boolean setInlineParent(Component parent, Component beforeSibling);
}
