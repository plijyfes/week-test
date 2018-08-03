/* LabelElement.java

	Purpose:
		
	Description:
		
	History:
		Fri Jun 17 09:45:54     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.impl;

import org.zkoss.lang.Objects;

/**
 * A XUL element with a label.
 *
 * @author tomyeh
 */
abstract public class LabelElement extends XulElement {
	/** The label. */
	private String _label = "";

	/** Returns the label (never null).
	 * <p>Default: "".
	 */
	public String getLabel() {
		return _label;
	}
	/** Sets the label.
	 * <p>If label is changed, the whole component is invalidate.
	 * Thus, you want to smart-update, you have to override this method.
	 */
	public void setLabel(String label) {
		if (label == null) label = "";
		if (!Objects.equals(_label, label)) {
			_label = label;
			smartUpdate("label", _label);
		}
	}

	//super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		render(renderer, "label", _label);
		renderCrawlable(_label);
	}
	/** Renders the crawlable information.
	 * It is called by {@link #renderProperties},
	 * and designed to be overridden if the deriving class wants to generate
	 * it differently.
	 * <p>Default: <code>org.zkoss.zul.impl.Utils.renderCrawlableText(label)</code>
	 * @since 5.0.5
	 */
	protected void renderCrawlable(String label) throws java.io.IOException {
		org.zkoss.zul.impl.Utils.renderCrawlableText(label);
	}
}
