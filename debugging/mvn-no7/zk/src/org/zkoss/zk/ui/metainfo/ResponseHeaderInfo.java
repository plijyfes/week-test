/* ResponseHeaderInfo.java

	Purpose:
		
	Description:
		
	History:
		Tue Apr 27 16:19:54 TST 2010, Created by tomyeh

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zk.ui.metainfo;

import java.util.Date;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ConditionImpl;
import org.zkoss.zk.xel.ExValue;
import org.zkoss.zk.xel.Evaluator;

/**
 * Represents a setting of a response header.
 * The setting specified here will become the response's header.
 * <p>It is a bit confusing that {@link ResponseHeaderInfo} represents a setting of the response's header.
 * {@link HeaderInfo} represents a tag located in the header of the generated content of a page.
 * For example, if the client is a HTTP browser, then {@link ResponseHeaderInfo} is equivalent
 * to invoke {@link Execution#setResponseHeader} and 
 * {@link Execution#addResponseHeader}.
 * And, {@link HeaderInfo} represents the &lt;link&gt;, &lt;meta&gt; and &lt;script&gt; HTML tags
 * 
 * <p>It is not serializable.
 *
 * @author tomyeh
 * @since 5.0.2
 */
public class ResponseHeaderInfo { //directive
	private final String _name;
	private final ExValue _value, _append;
	private final ConditionImpl _cond;

	/** Constructor.
	 *
	 * @param name the header's name, such as Refresh.
	 * @param value the header's value. It could contain EL expressions.
	 * It could be evaluated to a string, or a date ({@link Date}).
	 * @param append whether to append the header, or to set the header. It could contain EL expressions.
	 */
	public ResponseHeaderInfo(String name, String value, String append, ConditionImpl cond) {
		if (name == null || name.length() == 0 || value == null)
			throw new IllegalArgumentException();

		_name = name;
		_value = new ExValue(value, Object.class);
		_append = append != null ? new ExValue(append, Boolean.class): null;
		_cond = cond;
	}
	/** Returns the response header's name.
	 */
	public String getName() {
		return _name;
	}
	/** Returns the value of the response header.
	 * @return the value which is an instance of {@link Date} or {@link String}
	 * (and never null).
	 */
	public Object getValue(PageDefinition pgdef, Page page) {
		final Evaluator eval = pgdef.getEvaluator();
		if (_cond == null || _cond.isEffective(eval, page)) {
			final Object val = _value.getValue(eval, page);
			return val != null ? val instanceof Date ? val: val.toString(): "";
		}
		return "";
	}
	/** Returns whether to append the response header, rather than replace.
	 */
	public boolean shallAppend(PageDefinition pgdef, Page page) {
		final Evaluator eval = pgdef.getEvaluator();
		final Boolean bAppend = _append != null ? (Boolean)_append.getValue(eval, page): null;
		return bAppend != null && bAppend.booleanValue();
	}
}
