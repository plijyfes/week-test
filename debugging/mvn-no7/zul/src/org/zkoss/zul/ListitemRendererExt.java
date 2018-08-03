/* ListitemRendererExt.java

	Purpose:
		
	Description:
		
	History:
		Mon Feb  5 10:10:12     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

/**
 * Provides additional control to {@link ListitemRenderer}.
 *
 * @author tomyeh
 * @see ListitemRenderer
 */
public interface ListitemRendererExt {
	/** Creates an instance of {@link Listitem} that will be attached to listbox.
	 * The created component will be passed to {@link ListitemRenderer#render}
	 * to append the required information to show a row of the data.
	 *
	 * <p>Note: remember to invoke {@link Listitem#applyProperties} to initialize
	 * the properties, defined in the component definition, properly.
	 *
	 * <p>If null is returned, the default list item is created as follow.
<pre><code>
final Listitem item = new Listitem();
item.applyProperties();
return item;
</code></pre>
	 *
	 * <p>Note: DO NOT call {@link Listitem#setParent}.
	 *
	 * @return the list item if you'd like to create it differently, or null
	 * if you want {@link Listbox} to create it for you
	 */
	public Listitem newListitem(Listbox listbox);
	/** Create an instance of {@link Listcell} that will be attached to the
	 * <b>unloaded</b> listitem.
	 * By unloaded we mean the listitem that is not loaded with the data
	 * retrieved from the model. That is, {@link ListitemRenderer#render}
	 * is not called yet.
	 *
	 * <p>Notice that this callback shall generate an empty cell,
	 * rather than showing the data retrieved from the model.
	 * The showing of the data from model shall be done
	 * in {@link ListitemRenderer#render}.
	 *
	 * <p>If null is returned, the default list cell is created as follow.
<pre><code>
final Listcell cell = new Listcell();
cell.applyProperties();
return cell;
</code></pre>
	 *
	 * <p>Note: remember to invoke {@link Listcell#applyProperties} to initialize
	 * the properties, defined in the component definition, properly.
	 *
	 * <p>Note: DO NOT call {@link Listitem#setParent}.
	 * Don't create cells for other columns.
	 *
	 * <p>Note: DO NOT call {@link Listcell#setParent}.
	 *
	 * @param item the list item. It is the same as that is returned
	 * by {@link #newListitem}
	 * @return the list cell if you'd like to create it differently, or null
	 * if you want {@link Listbox} to create it for you
	 */
	public Listcell newListcell(Listitem item);

	/** Returned by {@link #getControls} to indicate
	 * that the list cells added by {@link #newListcell} must be
	 * detached before calling {@link ListitemRenderer#render}.
	 *
	 * <p>Default: true.<br/>
	 * If this interface is not specified, this flag is assumed
	 * to be specified.
	 *
	 * <p>If you implement this interface and doesn't return this flag
	 * in {@link #getControls}, the implementation of 
	 * {@link ListitemRenderer#render} must be aware of the existence of
	 * the first cell (of the passed list item).
	 */
	public static final int DETACH_ON_RENDER = 0x0001;
	/** Returns how a listbox shall render the live data.
	 *
	 * <p>Note: if this interface is not implemented, {@link #DETACH_ON_RENDER}
	 * is assumed.
	 *
	 * @return {@link #DETACH_ON_RENDER} or 0 to indicate how to render
	 * the live data.
	 */
	public int getControls();
}
