/* Calendar.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Apr 24 17:12:27     2006, Created by tomyeh
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zul;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;
import java.util.TimeZone;

import org.zkoss.lang.Objects;
import org.zkoss.util.Dates;
import org.zkoss.util.Locales;
import org.zkoss.util.TimeZones;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.au.AuRequests;

import org.zkoss.zul.impl.XulElement;

/**
 * A calendar.
 *
 * <p>Default {@link #getZclass}: z-calendar. (since 3.5.0)
 *
 * <h3>Support display the week number within the current year</h3>
 * Events: onWeekClick
 * <p>
 * For example
 * <pre><code>
 * &lt;calendar weekOfYear=&quot;true&quot; onWeekClick='alert(event.data)'/&gt;
 * </code></pre>
 * [ZK EE]
 * [Since 6.5.0]
 * 
 * @author tomyeh
 */
public class Calendar extends XulElement {
	private Date _value;
	private boolean _weekOfYear;
	
	/** The name. */
	private String _name;

	/** Constructs a calendar whose value is default to today.
	 */
	static {
		addClientEvent(Calendar.class, Events.ON_CHANGE, CE_IMPORTANT|CE_REPEAT_IGNORE);
		addClientEvent(Calendar.class, "onWeekClick", CE_REPEAT_IGNORE);
	}
	public Calendar() {
		this(null);
	}
	public Calendar(Date value) {
		_value = value != null ? value: Dates.today();
	}

	/**
	 * Sets whether enable to show the week number within the current year or
	 * not.
	 * [ZK EE]
	 * @since 6.5.0
	 */
	public void setWeekOfYear(boolean weekOfYear) {
		if (_weekOfYear != weekOfYear) {
			_weekOfYear = weekOfYear;
			smartUpdate("weekOfYear", _weekOfYear);
		}
	}
    
    /**
     * Returns whether enable to show the week number within the current year or not.
     * <p>Default: false
     * @since 6.5.0
     */
    public boolean isWeekOfYear() {
		return _weekOfYear;
    }


	/** @deprecated As of release 5.0.5, it is meaningless to set time zone for a calendar.
	 */
	public TimeZone getTimeZone() {
		return null;
	}
	/** As of release 5.0.5, it is meaningless to set time zone for a calendar.
	 */
	public void setTimeZone(TimeZone tzone) {
	}

	/** Returns the value that is assigned to this component, never null.
	 */
	public Date getValue() {
		return _value;
	}
	/** Assigns a value to this component.
	 * @param value the date to assign. If null, today is assumed.
	 */
	public void setValue(Date value) {
		if (value == null) value = Dates.today();
		if (!value.equals(_value)) {
			_value = value;
			smartUpdate("value", _value);
		}
	}

	private DateFormat getDateFormat() {
		final DateFormat df =
			new SimpleDateFormat("yyyy/MM/dd", Locales.getCurrent());
		df.setTimeZone(TimeZones.getCurrent());
		return df;
	}

	/** Returns the name of this component.
	 * <p>Default: null.
	 * <p>The name is used only to work with "legacy" Web application that
	 * handles user's request by servlets.
	 * It works only with HTTP/HTML-based browsers. It doesn't work
	 * with other kind of clients.
	 * <p>Don't use this method if your application is purely based
	 * on ZK's event-driven model.
	 * @since 3.0.0
	 */
	public String getName() {
		return _name;
	}
	/** Sets the name of this component.
	 * <p>The name is used only to work with "legacy" Web application that
	 * handles user's request by servlets.
	 * It works only with HTTP/HTML-based browsers. It doesn't work
	 * with other kind of clients.
	 * <p>Don't use this method if your application is purely based
	 * on ZK's event-driven model.
	 *
	 * @param name the name of this component.
	 * @since 3.0.0
	 */
	public void setName(String name) {
		if (name != null && name.length() == 0) name = null;
		if (!Objects.equals(_name, name)) {
			_name = name;
			smartUpdate("name", _name);
		}
	}

	//-- super --//
	public String getZclass() {
		return _zclass == null ? "z-calendar" : _zclass;
	}

	//-- ComponentCtrl --//
	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link XulElement#service},
	 * it also handles onChange, onChanging and onError.
	 * @since 5.0.0
	 */
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals(Events.ON_CHANGE)) {
			final Map<String, Object> data = request.getData();
			final Object value = data.get("value");
			if (Objects.equals(_value, value))
				return; //nothing happen

			_value = (Date)value;
			final InputEvent evt = new InputEvent(cmd, this,
				getDateFormat().format(value), value,
				AuRequests.getBoolean(data, "bySelectBack"),
				AuRequests.getInt(data, "start", 0));
			Events.postEvent(evt);
		} else {
			super.service(request, everError);
		}
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
			throws java.io.IOException {
		super.renderProperties(renderer);
		if (_name != null)
			render(renderer, "name", _name);
		render(renderer, "weekOfYear", _weekOfYear);
		render(renderer, "value", _value);
	}
}
