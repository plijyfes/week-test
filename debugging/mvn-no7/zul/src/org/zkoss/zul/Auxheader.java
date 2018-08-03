/* Auxheader.java

	Purpose:
		
	Description:
		
	History:
		Wed Oct 24 10:07:12     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;

import org.zkoss.zul.impl.HeaderElement;

/**
 * An auxiliary header.
 * <p>Default {@link #getZclass}: z-auxheader.(since 3.5.0)
 *
 * @author tomyeh
 * @since 3.0.0
 */
public class Auxheader extends HeaderElement {
	private int _colspan = 1, _rowspan = 1;

	public Auxheader() {
	}
	public Auxheader(String label) {
		super(label);
	}
	public Auxheader(String label, String src) {
		super(label, src);
	}

	/** Returns number of columns to span this header.
	 * Default: 1.
	 */
	public int getColspan() {
		return _colspan;
	}
	/** Sets the number of columns to span this header.
	 * <p>It is the same as the colspan attribute of HTML TD tag.
	 */
	public void setColspan(int colspan) throws WrongValueException {
		if (colspan <= 0)
			throw new WrongValueException("Positive only");
		if (_colspan != colspan) {
			_colspan = colspan;
			smartUpdate("colspan", _colspan);
		}
	}

	/** Returns number of rows to span this header.
	 * Default: 1.
	 */
	public int getRowspan() {
		return _rowspan;
	}
	/** Sets the number of rows to span this header.
	 * <p>It is the same as the rowspan attribute of HTML TD tag.
	 */
	public void setRowspan(int rowspan) throws WrongValueException {
		if (rowspan <= 0)
			throw new WrongValueException("Positive only");
		if (_rowspan != rowspan) {
			_rowspan = rowspan;
			smartUpdate("rowspan", _rowspan);
		}
	}

	//super//
	public String getZclass() {
		return _zclass == null ? "z-auxheader" : _zclass;
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		if (_colspan != 1)
			renderer.render("colspan", _colspan);
		if (_rowspan != 1)
			renderer.render("rowspan", _rowspan);
	}

	//Component//
	public void beforeParentChanged(Component parent) {
		if (parent != null && !(parent instanceof Auxhead))
			throw new UiException("Wrong parent: "+parent);
		super.beforeParentChanged(parent);
	}
}
