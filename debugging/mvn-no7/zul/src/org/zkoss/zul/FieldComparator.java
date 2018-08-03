/* FieldComparator.java

	Purpose:
		
	Description:
		
	History:
		Jan 8, 2009 5:49:21 PM, Created by henrichen

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.zul;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.zkoss.lang.Strings;
import org.zkoss.lang.reflect.Fields;
import org.zkoss.util.CollectionsX;
import org.zkoss.zk.ui.UiException;

/**
 * <p>Based on the the given field names to compare the field value of the object 
 * that is passed in {@link #compare} method.</p> 
 * <p>The field names accept compound "a.b.c" expression. It also
 * accept multiple field names that you can give expression in the form 
 * of e.g. "name, age, salary" and this comparator will compare in that sequence.</p>
 * 
 * @author henrichen
 * @since 3.6.0
 */
public class FieldComparator implements Comparator, Serializable {
	private static final long serialVersionUID = 20090120111922L;
	/** The field names collection. */
	private Collection<FieldInfo> _fieldnames;
	/** The cached field name string. */
	private transient String _orderBy;
	/** The original orderBy passed to the constructor. */
	private String _rawOrderBy;
	/** Whether to treat null as the maximum value. */
	private boolean _maxnull;
	private boolean _ascending;
	
	/** Compares with the fields per the given "ORDER BY" clause.
	 * <p>Note: It assumes null as minimum value.
	 *  If not, use {@link #FieldComparator(String, boolean, boolean)}
	 * instead.</p>
	 * 
	 * @param orderBy the "ORDER BY" clause to be compared upon for the given object in {@link #compare}.
	 * @param ascending whether to sort as ascending (or descending).
	 */
	public FieldComparator(String orderBy, boolean ascending) {
		this(orderBy, ascending, false);
	}
	
	/** Compares with the fields per the given "ORDER BY" clause.
	 *
	 * @param orderBy the "ORDER BY" clause to be compared upon for the given object in {@link #compare}.
	 * @param ascending whether to sort as ascending (or descending).
	 * @param nullAsMax whether to consider null as the maximum value.
	 * If false, null is considered as the minimum value.
	 */
	public FieldComparator(String orderBy, boolean ascending, boolean nullAsMax) {
		if (Strings.isBlank(orderBy)) {
			throw new UiException("Empty fieldnames: "+ orderBy);
		}
		_fieldnames = parseFieldNames(orderBy, ascending);
		_maxnull = nullAsMax;
		_rawOrderBy = orderBy;
		_ascending = ascending;
	}
	
	public int compare(Object o1, Object o2) {
		try {
			for(FieldInfo fi: _fieldnames) {
				final int res = compare0(o1, o2, fi.fieldname, fi.asc, fi.func);
				if (res != 0) {
					return res;
				}
			}
			return 0; 
		} catch (NoSuchMethodException ex) {
			throw UiException.Aide.wrap(ex);
		}
	}
	/** Returns the order-by clause.
	 * Notice that is the parsed result, such as <code>name=category ASC</code>.
	 * For the original format, please use {@link #getRawOrderBy}.
	 */
	public String getOrderBy() {
		if (_orderBy == null) {
			final StringBuffer sb = new StringBuffer(_fieldnames.size() * 16);
			final Iterator<FieldInfo> it = _fieldnames.iterator();
			if (it.hasNext()) {
				appendField(sb, it.next());
			}
			while(it.hasNext()) {
				sb.append(',');
				appendField(sb, it.next());
			}
			_orderBy = sb.toString();
		}
		return _orderBy;
	}
	/** Returns the original order-by clause passed to the constructor.
	 * It is usually the field's name, such as <code>category</code>,
	 * or a concatenation of field names, such as <code>category.name</code>.
	 * <p>Notice that, with the field's name, you could retrieve the value
	 * by use of {@link Fields#getByCompound}.
	 * @since 5.0.6
	 */
	public String getRawOrderBy() {
		return _rawOrderBy;
	}
	/** Returns whether the sorting is ascending.
	 * @since 5.0.6
	 */
	public boolean isAscending() {
		return _ascending;
	}

	private void appendField(StringBuffer sb, FieldInfo fi) {
		if (fi.func != null) {
			sb.append(fi.func).append('(').append(fi.fieldname).append(')');
		} else {
			sb.append(fi.fieldname);
		}
		sb.append(fi.asc ? " ASC" : " DESC");
	}
	@SuppressWarnings("unchecked")
	private int compare0(Object o1, Object o2, String fieldname, boolean asc, String func) throws NoSuchMethodException {
		// Bug B50-3183438: Access to bean shall be consistent
		final Object f1 = o1 instanceof Map ? ((Map)o1).get(fieldname) : 
			Fields.getByCompound(getCompareObject(o1), fieldname);
		final Object f2 = o2 instanceof Map ? ((Map)o2).get(fieldname) : 
			Fields.getByCompound(getCompareObject(o2), fieldname);
		final Object v1 = handleFunction(f1, func);
		final Object v2 = handleFunction(f2, func);
		
		if (v1 == null) return v2 == null ? 0: (asc == _maxnull) ? 1 : -1;
		if (v2 == null) return (asc == _maxnull) ? -1 : 1;
		final int v = ((Comparable)v1).compareTo(v2);
		return asc ? v : -v;
	}

	private Object getCompareObject(Object o) {
		if (o instanceof TreeNode)
			return ((TreeNode) o).getData();
		return o;
	}

	private Object handleFunction(Object c, String func) {
		if ("UPPER".equals(func)) {
			if (c instanceof String)
				return ((String)c).toUpperCase();
			if (c instanceof Character)
				return new Character(Character.toUpperCase(
					((Character)c).charValue()));
		} else if ("LOWER".equals(func)) {
			if (c instanceof String)
				return ((String)c).toLowerCase(java.util.Locale.ENGLISH);
			if (c instanceof Character)
				return new Character(Character.toLowerCase(
					((Character)c).charValue()));
		}
		return c;
	}

	private Collection<FieldInfo> parseFieldNames(String fieldnames, boolean ascending) {
		final Collection<String> fields = CollectionsX.parse(new ArrayList<String>(), fieldnames, ',');
		final List<FieldInfo> results = new ArrayList<FieldInfo>(fields.size());
		for (final Iterator<String> it = fields.iterator(); it.hasNext();) {
			final String field = it.next().trim();
			String fieldname;
			String ascstr = "asc";
			//whether a String
			String func = null;
			final String ufn = field.toUpperCase(); 
			if (ufn.startsWith("UPPER(") || ufn.startsWith("LOWER(")) { //with function
				final int k = field.lastIndexOf(')');
				if (k == 0) {
					throw new UiException("No closing function ')' mark: "+field);
				} else if (k == 6) {
					throw new UiException("No data inside function: "+field);
				} else {
					fieldname = field.substring(6, k);
					if ((k+1) < field.length()) { //with asc
						ascstr = field.substring(k+1);
						if (Strings.isBlank(ascstr)) {
							ascstr = "asc";
						}
					}
					func = ufn.substring(0,5);
				}
			} else {
				final int j = field.indexOf(' ');
				if (j < 0) {
					fieldname = field;
				} else {
					fieldname = field.substring(0, j);
					ascstr = field.substring(j+1);
				}
			}
			
			boolean asc;
			if ("asc".equalsIgnoreCase(ascstr)) {
				asc = ascending;
			} else if ("desc".equalsIgnoreCase(ascstr)) {
				asc = !ascending;
			} else {
				throw new UiException("field must be in the form of \"field ASC\" or \"field DESC\":" + ascstr);
			}
				
			results.add(new FieldInfo(fieldname, asc, func));
		}
		return results;
	}
		
	private static class FieldInfo implements Serializable {
		private String fieldname;
		private boolean asc;
		private String func;
		
		public FieldInfo(String fieldname, boolean asc, String func) {
			this.fieldname = fieldname;
			this.asc = asc;
			this.func = func;
		}
	}
}
