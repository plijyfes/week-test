/* HeaderInfo.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Sep 14 22:02:29     2006, Created by tomyeh
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zk.ui.metainfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zkoss.html.HTMLs;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.ConditionImpl;
import org.zkoss.zk.xel.Evaluator;
import org.zkoss.zk.xel.ExValue;

/**
 * Represents a header element, such as &lt;?link&gt; and &lt;?meta&gt;
 * directives on the ZUML page.
 * They are usually represented as directives in ZUML.
 * For example, the link and meta directives represent &lt;link&gt;
 * and &lt;meta&gt; HTML tags, respectively.
 *
 * <p>Notice
 * <ul>
 * <li>Directives are evaluated before all other tags. Thus, it is not
 * possible to set a value in zscript and then reference it in an attribute
 * of the directive.</li>
 * <li>If a page is included by another page with the instant mode
 * ({@link org.zkoss.zul.Include#getMode}), the directives will be ignored.</li>
 * <li>EL is allowed for every attribute.</li>
 * <li>Since 5.0.0, the if and unless attributes are supported.</li>
 * </ul>
 *
 * <p>It is not serializable.
 *
 * @author tomyeh
 * @see ResponseHeaderInfo
 */
public class HeaderInfo { //directive
	private final String _name;
	/** A list of AttrInfo. */
	private final List<AttrInfo> _attrs;
	private final ConditionImpl _cond;

	/** Constructor.
	 *
	 * <p>Note: it detects the href attribute (from the attrs argument), and
	 * encodes it with {@link Executions#encodeURL}.
	 *
	 * @param name the tag name, such as link (never null or empty).
	 * @param attrs a map of (String, String) attributes.
	 */
	public HeaderInfo(String name, Map<String, String> attrs, ConditionImpl cond) {
		if (name == null || name.length() == 0)
			throw new IllegalArgumentException("empty");

		_name = name;
		_cond = cond;
		if (attrs == null || attrs.isEmpty()) {
			_attrs = Collections.emptyList();
		} else {
			_attrs = new LinkedList<AttrInfo>();
			for (Map.Entry<String, String> me: attrs.entrySet()) {
				final String nm = me.getKey(), val = me.getValue();
				_attrs.add(new AttrInfo(nm, new ExValue(val, String.class)));
			}
		}
	}
	/** Returns the tag name of this header element.
	 */
	public String getName() {
		return _name;
	}

	/** Returns as HTML tag(s) representing this header element.
	 *
	 * @param page the page containing this header element.
	 * It is used to evaluate EL expression, if any, contained in the value.
	 * @since 6.0.0
	 */
	public String toHTML(PageDefinition pgdef, Page page) {
		final Evaluator eval = pgdef.getEvaluator();
		if (_cond != null && !_cond.isEffective(eval, page))
			return "";

		final boolean bScript = "script".equals(_name),
			bStyle = !bScript && "style".equals(_name);

		//1. scan content
		final StringBuffer sb = new StringBuffer(128);
		if (bScript || bStyle) {
			String content = null;
			boolean srcFound = false;
			for (AttrInfo attr: _attrs) {
				final String nm = attr.name;
				if ("content".equals(nm)) {
					content = (String)attr.value.getValue(eval, page);
				} else {
					srcFound = srcFound || "src".equals(nm) || "href".equals(nm);
				}
			}

			if (content != null) {
				sb.append('<').append(_name).append(" type=\"text/")
					.append(bScript ? "javascript": "css").append("\">\n")
					.append(content)
					.append("\n</").append(_name).append('>');
				if (srcFound) sb.append('\n');
			}
			if (!srcFound)
				return sb.toString(); //no more to generate
		}

		
		sb.append('<').append(bStyle ? "link": _name);
		boolean relFound = false, typeFound = false;
		for (AttrInfo attr: _attrs) {
			final String nm = attr.name;
			if ((bScript || bStyle) && "content".equals(nm))
				continue; //skip

			relFound = bStyle && (relFound || "rel".equals(nm));
			typeFound = (bScript || bStyle) && (typeFound || "type".equals(nm));

			String val = (String)attr.value.getValue(eval, page);
			if (val == null || val.length() == 0)
				val = "";
			else if ("href".equals(nm) || ((bScript || bStyle) && "src".equals(nm)))
				val = Executions.encodeURL(val);

			HTMLs.appendAttribute(sb,
				bStyle && "src".equals(nm) ? "href":
				bScript && "href".equals(nm) ? "src": nm, val);
		}

		if (bStyle && !relFound)
				sb.append(" rel=\"stylesheet\"");
		if ((bScript || bStyle) && !typeFound)
			sb.append(" type=\"text/")
				.append(bScript ? "javascript": "css").append('"');
		return sb.append(bScript ? ">\n</script>": "/>").toString();
	}

	private static class AttrInfo {
		private final String name;
		private final ExValue value;
		private AttrInfo(String name, ExValue value) {
			this.name = name;
			this.value = value;
		}
	}
}
