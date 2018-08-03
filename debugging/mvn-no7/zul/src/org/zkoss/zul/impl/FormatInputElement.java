/* FormatInputElement.java

	Purpose:
		
	Description:
		
	History:
		Tue Jul  5 09:27:34     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.impl;

import org.zkoss.lang.Objects;

import org.zkoss.zk.ui.WrongValueException;

/**
 * A skeletal implementation for an input box with format.
 *
 * @author tomyeh
 */
abstract public class FormatInputElement extends InputElement {
	private String _format;

	/** Returns the format.
	 * <p>Default: null (used what is defined in the format sheet).
	 */
	public String getFormat() {
		return _format;
	}
	/** Sets the format.
	 */
	public void setFormat(String format) throws WrongValueException {
		if (!Objects.equals(_format, format)) {
			_format = format;
			smartUpdate("format", getRealFormat());
			smartUpdate("_value", marshall(_value));
				//Technically, it shall be independent of format, but it is
				//safer to send again (since some implementation might not good)
				//See also bug 2998196.
		}
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		render(renderer, "format", getRealFormat());//value might depend on format (though it shall not)
		super.renderProperties(renderer);
	}
	/** Returns the real format.
	 * <p>Default: return {@link #getFormat}.
	 * It is designed to allow the deriving class to provide another layer of
	 * abstraction. For example, {@link org.zkoss.zul.Datebox#setFormat}
	 * accepts short to denote the short format.
	 * @since 5.0.7
	 */
	protected String getRealFormat() {
		return _format;
	}
}
