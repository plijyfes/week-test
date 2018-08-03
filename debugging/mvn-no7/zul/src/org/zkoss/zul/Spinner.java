/* Spinner.java

	Purpose:
		
	Description:
		
	History:
		Fri Mar 14 10:26:55 TST 2008, Created by gracelin

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import java.io.IOException;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.impl.NumberInputElement;
import org.zkoss.zul.mesg.MZul;

/**
 * An edit box for holding a constrained integer.
 *
 * <p>Default {@link #getZclass}: z-spinner.
 *
 * <p>spinner supports below key events.
 * <ul>
 *  <li>0-9 : set the value on the inner text box.
 * 	<li>delete : clear the value to empty (null)
 * </ul>
 * @author gracelin
 * @since 3.5.0
 */
public class Spinner extends NumberInputElement {
	private int _step = 1;
	private boolean _btnVisible = true;

	public Spinner() {
		setCols(11);
	}

	public Spinner(int value) throws WrongValueException {
		this();
		setValue(new Integer(value));
	}
	
	/** Returns the value (in Integer), might be null unless
	 * a constraint stops it.
	 * @exception WrongValueException if user entered a wrong value
	 */
	public Integer getValue() throws WrongValueException {
		return (Integer)getTargetValue();
	}
	/** Returns the value in int.
	 * @exception WrongValueException if user entered a wrong value or null value
	 */
	public int intValue() throws WrongValueException {
		return (Integer) getTargetValue();
	}
	protected Object getTargetValue() throws WrongValueException {
		Object val = super.getTargetValue();
		if (val instanceof Integer) {
			return val;
		}
		throw showCustomError(new WrongValueException(this,
				MZul.INTEGER_REQUIRED, val == null ? "null" : val));
	}
	/** Sets the value (in Integer).
	 * @exception WrongValueException if value is wrong
	 */
	public void setValue(Integer value) throws WrongValueException {
		validate(value);
		setRawValue(value);
	}
	
	/**
	 * Return the step of spinner
	 */
	public int getStep(){
		return _step;
	}
	
	/**
	 * Set the step of spinner
	 */
	public void setStep(int step) {
		if (_step != step) {
			_step = step;
			smartUpdate("step", _step);
		}
	}
	
	/** Returns whether the button (on the right of the textbox) is visible.
	 * <p>Default: true.
	 */
	public boolean isButtonVisible() {
		return _btnVisible;
	}
	/** Sets whether the button (on the right of the textbox) is visible.
	 */
	public void setButtonVisible(boolean visible) {
		if (_btnVisible != visible) {
			_btnVisible = visible;
			smartUpdate("buttonVisible", visible);
		}
	}

	// super
	public String getZclass() {
		return _zclass == null ?  "z-spinner" : _zclass;
	}
	
	/**
	 * @param constr a list of constraints separated by comma.
	 * Example: no positive, no zero
	 */
	// -- super --//
	public void setConstraint(String constr) {
		setConstraint(constr != null ? new SimpleSpinnerConstraint(constr): null); //Bug 2564298
	}
	
	protected Object coerceFromString(String value) throws WrongValueException {
		final Object[] vals = toNumberOnly(value);
		final String val = (String) vals[0];
		if (val == null || val.length() == 0)
			return null;

		try {
			int v = Integer.parseInt(val);
			int divscale = vals[1] != null ? ((Integer) vals[1]).intValue() : 0;
			while (v != 0 && --divscale >= 0)
				v /= 10;
			return new Integer(v);
		} catch (NumberFormatException ex) {
			throw showCustomError(new WrongValueException(this,
					MZul.NUMBER_REQUIRED, value));
		}
	}

	protected String coerceToString(Object value) {
		return value != null && getFormat() == null ? value.toString()
				: formatNumber(value, null);
	}
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws IOException {
		super.renderProperties(renderer);
		if(_step != 1)
			renderer.render("step", _step);
		if(!_btnVisible)
			renderer.render("buttonVisible", _btnVisible);
	}
}
