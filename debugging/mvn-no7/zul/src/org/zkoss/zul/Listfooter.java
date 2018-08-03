/* Listfooter.java

	Purpose:
		
	Description:
		
	History:
		Fri Jan 13 12:42:38     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import java.util.List;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.impl.FooterElement;

/**
 * A column of the footer of a list box ({@link Listbox}).
 * Its parent must be {@link Listfoot}.
 *
 * <p>Unlike {@link Listheader}, you could place any child in a list footer.
 * <p>Note: {@link Listcell} also accepts children.
 * <p>Default {@link #getZclass}: z-listfooter.(since 5.0.0)
 * 
 * @author tomyeh
 */
public class Listfooter extends FooterElement {

	public Listfooter() {
	}
	public Listfooter(String label) {
		super(label);
	}
	public Listfooter(String label, String src) {
		super(label, src);
	}

	/** Returns the listbox that this belongs to.
	 */
	public Listbox getListbox() {
		final Component comp = getParent();
		return comp != null ? (Listbox)comp.getParent(): null;
	}
	/** Returns the column index, starting from 0.
	 */
	public int getColumnIndex() {
		int j = 0;
		for (Iterator it = getParent().getChildren().iterator();
		it.hasNext(); ++j)
			if (it.next() == this)
				break;
		return j;
	}
	/** Returns the list header that is in the same column as
	 * this footer, or null if not available.
	 */
	public Listheader getListheader() {
		final Listbox listbox = getListbox();
		if (listbox != null) {
			final Listhead lcs = listbox.getListhead();
			if (lcs != null) {
				final int j = getColumnIndex();
				final List<Component> lcschs = lcs.getChildren();
				if (j < lcschs.size())
					return (Listheader)lcschs.get(j);
			}
		}
		return null;
	}

	//-- Component --//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);
		org.zkoss.zul.impl.Utils.renderCrawlableText(getLabel());
	}
	public String getZclass() {
		return _zclass == null ? "z-listfooter" : _zclass;
	}
	public void beforeParentChanged(Component parent) {
		if (parent != null && !(parent instanceof Listfoot))
			throw new UiException("Wrong parent: "+parent);
		super.beforeParentChanged(parent);
	}
}
