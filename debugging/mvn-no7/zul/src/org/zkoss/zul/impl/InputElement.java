/* InputElement.java

	Purpose:
		
	Description:
		
	History:
		Tue Jul  5 08:49:30     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.impl;

import java.util.Map;

import org.zkoss.lang.Objects;

import org.zkoss.lang.Exceptions;
import org.zkoss.util.logging.Log;
import org.zkoss.json.JavaScriptValue;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.ext.Scopes;
import org.zkoss.zk.ui.ext.Disable;
import org.zkoss.zk.ui.ext.Readonly;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.au.out.AuSelect;
import org.zkoss.zk.au.out.AuWrongValue;

import org.zkoss.zul.mesg.MZul;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.ClientConstraint;
import org.zkoss.zul.CustomConstraint;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.ext.Constrainted;

/**
 * A skeletal implementation of an input box.
 * 
 * <p>Events: onChange, onChanging, onFocus, onBlur, onSelection.
 *
 * @author tomyeh
 */
abstract public class InputElement extends XulElement
implements Constrainted, Readonly, Disable {
	private static final Log log = Log.lookup(InputElement.class);

	static {
		addClientEvent(InputElement.class, Events.ON_CHANGE, CE_IMPORTANT|CE_REPEAT_IGNORE);
		addClientEvent(InputElement.class, Events.ON_CHANGING, CE_DUPLICATE_IGNORE);
		addClientEvent(InputElement.class, Events.ON_FOCUS, CE_DUPLICATE_IGNORE);
		addClientEvent(InputElement.class, Events.ON_BLUR, CE_DUPLICATE_IGNORE);
		addClientEvent(InputElement.class, Events.ON_SELECTION, CE_REPEAT_IGNORE);
		addClientEvent(InputElement.class, Events.ON_ERROR, CE_DUPLICATE_IGNORE|CE_IMPORTANT);
	}

	/** The value. */
	protected Object _value;
	private int _cols;
	private AuxInfo _auxinf;
	private boolean _disabled, _readonly;
	/** Whether this input is validated (Feature 1461209). */
	private boolean _valided;
	private boolean _inplace;
	private String _placeholder;
	
	/**
	 * Returns the placeholder text
	 * @since 6.5.0
	 */
	public String getPlaceholder() {
		return _placeholder;
	}
	
	/**
	 * Sets the placeholder text that is displayed when input is empty.
	 * Only works for browsers supporting HTML5.
	 * @since 6.5.0
	 */
	public void setPlaceholder(String placeholder) {
		if (_placeholder != placeholder) {
			_placeholder = placeholder;
			smartUpdate("placeholder", _placeholder);
		}
	}

	/**
	 * Sets to enable the inplace-editing function that the look and feel is
	 * like a label.
	 * @since 5.0.0 
	 */
	public void setInplace(boolean inplace) {
		if (_inplace != inplace) {
			_inplace = inplace;
			smartUpdate("inplace", _inplace);
		}
	}
	
	/**
	 * Returns whether enable the inplace-editing.
	 * <p>default: false.
	 * @since 5.0.0
	 */
	public boolean isInplace() {
		return _inplace;
	}
	
	/** Returns whether it is disabled.
	 * <p>Default: false.
	 */
	public boolean isDisabled() {
		return _disabled;
	}
	/** Sets whether it is disabled.
	 */
	public void setDisabled(boolean disabled) {
		if (_disabled != disabled) {
			_disabled = disabled;
			smartUpdate("disabled", _disabled);
		}
	}
	/** Returns whether it is readonly.
	 * <p>Default: false.
	 */
	public boolean isReadonly() {
		return _readonly;
	}
	/** Sets whether it is readonly.
	 */
	public void setReadonly(boolean readonly) {
		if (_readonly != readonly) {
			_readonly = readonly;
			smartUpdate("readonly", _readonly);
		}
	}

	/** Returns the name of this component.
	 * <p>Default: null.
	 * <p>Don't use this method if your application is purely based
	 * on ZK's event-driven model.
	 * <p>The name is used only to work with "legacy" Web application that
	 * handles user's request by servlets.
	 * It works only with HTTP/HTML-based browsers. It doesn't work
	 * with other kind of clients.
	 */
	public String getName() {
		return _auxinf != null ? _auxinf.name: null;
	}
	/** Sets the name of this component.
	 * <p>Don't use this method if your application is purely based
	 * on ZK's event-driven model.
	 * <p>The name is used only to work with "legacy" Web application that
	 * handles user's request by servlets.
	 * It works only with HTTP/HTML-based browsers. It doesn't work
	 * with other kind of clients.
	 *
	 * @param name the name of this component.
	 */
	public void setName(String name) {
		if (name != null && name.length() == 0) name = null;
		if (!Objects.equals(_auxinf != null ? _auxinf.name: null, name)) {
			initAuxInfo().name = name;
			smartUpdate("name", getName());
		}
	}

	/** Returns the error message that is caused when user entered
	 * invalid value, or null if no error at all.
	 *
	 * <p>The error message is set when user has entered a wrong value,
	 * or setValue is called with a wrong value.
	 * It is cleared once a correct value is assigned.
	 *
	 * <p>If the error message is set, we say this input is in the error mode.
	 * Any following invocation to {@link #getText} or getValue will throw
	 * any exception.
	 * Example, {@link org.zkoss.zul.Textbox#getValue} and
	 * {@link org.zkoss.zul.Intbox#getValue}.
	 */
	public String getErrorMessage() {
		return _auxinf != null ? _auxinf.errmsg: null;
	}
	/** Associates an error message to this input.
	 * It will cause the given error message to be shown at the client.
	 * <p>Notice that the application rarely invokes this method. Rather,
	 * throw {@link WrongValueException} instead.
	 * <p>Notice it does not invoke {@link CustomConstraint#showCustomError}
	 * even if {@link #getConstraint} implements  {@link CustomConstraint}.
	 * @since 5.0.4
	 */
	public void setErrorMessage(String errmsg) {
		if (errmsg != null && errmsg.length() > 0) {
			initAuxInfo().errmsg = errmsg;
			response(new AuWrongValue(this, errmsg));
		} else {
			clearErrorMessage();
		}
	}
	/** Clears the error message.
	 *
	 * <p>The error message is cleared automatically, so you rarely need
	 * to call this method.
	 * However, if a constraint depends on multiple input fields and
	 * the error can be corrected by changing one of these fields,
	 * then you may have to clear the error message manually by invoking
	 * this method.
	 *
	 * <p>For example, assume you have two {@link org.zkoss.zul.Intbox}
	 * and want the value of the first one to be smaller than that of the
	 * second one. Then, you have to call this method for the second intbox
	 * once the validation of the first intbox succeeds, and vice versa.
	 * Otherwise, the error message for the second intbox remains if
	 * the user fixed the error by lowering down the value of the first one
	 * Why? The second intbox got no idea to clear the error message
	 * (since its content doesn't change).
	 *
	 * @param revalidateRequired whether to re-validate the current value
	 * when {@link #getText} or others (such as {@link org.zkoss.zul.Intbox#getValue})
	 * is called.
	 * If false, the current value is assumed to be correct and
	 * the following invocation to {@link #getText} or others (such as {@link org.zkoss.zul.Intbox#getValue})
	 * won't check the value again.
	 * Note: when an input element is constructed, the initial value
	 * is assumed to be "not-validated-yet".
	 * @since 3.0.1
	 */
	public void clearErrorMessage(boolean revalidateRequired) {
		if (_auxinf != null && _auxinf.errmsg != null) {
			_auxinf.errmsg = null;
			Clients.clearWrongValue(this);
		}
		_valided = !revalidateRequired;
	}
	/** Clears the error message.
	 * It is the same as clearErrorMessage(false). That is, the current
	 * value is assumed to be correct. {@link #getText} or others (such as {@link org.zkoss.zul.Intbox#getValue})
	 * won't re-validate it again.
	 *
	 * <p>The error message is cleared automatically, so you rarely need
	 * to call this method.
	 *
	 * @see #clearErrorMessage(boolean)
	 */
	public void clearErrorMessage() {
		clearErrorMessage(false);
	}

	/** Returns the value in the String format.
	 * In most case, you shall use the setValue method instead, e.g.,
	 * {@link org.zkoss.zul.Textbox#getValue} and
	 * {@link org.zkoss.zul.Intbox#getValue}.
	 *
	 * <p>It invokes {@link #checkUserError} to ensure no user error.
	 *
	 * <p>It invokes {@link #coerceToString} to convert the stored value
	 * into a string.
	 *
	 * @exception WrongValueException if user entered a wrong value
	 */
	public String getText() throws WrongValueException {
		checkUserError();
		return coerceToString(_value);
	}

	/** Sets the value in the String format.
	 * In most case, you shall use the setValue method instead, e.g.,
	 * {@link org.zkoss.zul.Textbox#setValue} and
	 * {@link org.zkoss.zul.Intbox#setValue}.
	 *
	 * <p>It invokes {@link #coerceFromString} first and then {@link #validate}.
	 * Derives might override them for type conversion and special
	 * validation.
	 *
	 * @param value the value; If null, it is considered as empty.
	 */
	public void setText(String value) throws WrongValueException {
		if (_auxinf != null && _auxinf.maxlength > 0 && value != null && value.length() > _auxinf.maxlength)
			throw new WrongValueException(this, MZul.STRING_TOO_LONG, new Integer(_auxinf.maxlength));

		final Object val = coerceFromString(value);
		final boolean same = Objects.equals(_value, val);
		boolean errFound = false;
		if (!same || !_valided || (_auxinf != null && _auxinf.errmsg != null)) { //note: the first time (!_valided) must always validate
			validate(val); //Bug 2946917: don't validate if not changed

			errFound = _auxinf != null && _auxinf.errmsg != null;
			clearErrorMessage(); //no error at all
		}

		if (!same) {
			_value = val;
			smartUpdate("_value", marshall(val));
		} else if (errFound) {
			smartUpdate("_value", marshall(_value)); //send back original value
				//Bug 1876292: make sure client see the updated value
		}
	}

	/** Coerces the value passed to {@link #setText}.
	 *
	 * <p>Deriving note:<br>
	 * If you want to store the value in other type, say BigDecimal,
	 * you have to override {@link #coerceToString} and {@link #coerceFromString}
	 * to convert between a string and your targeting type.
	 *
	 * <p>Moreover, when {@link org.zkoss.zul.Textbox} is called, it calls this method
	 * with value = null. Derives shall handle this case properly.
	 */
	abstract protected
	Object coerceFromString(String value) throws WrongValueException;
	/** Coerces the value passed to {@link #setText}.
	 *
	 * <p>Default: convert null to an empty string.
	 *
	 * <p>Deriving note:<br>
	 * If you want to store the value in other type, say BigDecimal,
	 * you have to override {@link #coerceToString} and {@link #coerceFromString}
	 * to convert between a string and your targeting type.
	 */
	abstract protected String coerceToString(Object value);

	/** Validates the value returned by {@link #coerceFromString}.
	 * <p>Default: use  {@link #getConstraint}'s {@link Constraint#validate},
	 * if not null.
	 * <p>You rarely need to override this method.
	 */
	protected void validate(Object value) throws WrongValueException {
		final Constraint constr = getConstraint();
		if (constr != null) { //then _auxinf must be non-null
			//Bug 1698190: constructor might be zscript
			Scopes.beforeInterpret(this);
			try {
				constr.validate(this, value);
				if (!_auxinf.checkOnly && (constr instanceof CustomConstraint)) {
					try {
						((CustomConstraint)constr).showCustomError(this, null);
						//not call thru showCustomError(Wrong...) for better performance
					} catch (Throwable ex) {
						log.realCauseBriefly(ex);
					}
				}
			} finally {
				Scopes.afterInterpret();
			}
		}
	}
	/** Shows the error message in the custom way by calling
	 * ({@link CustomConstraint#showCustomError}, if the constraint
	 * implements {@link CustomConstraint}.
	 *
	 * <p>Derived class shall call this method before throwing
	 * {@link WrongValueException}, such that the constraint,
	 * if any, has a chance to show the error message in a custom way.
	 *
	 * @param ex the exception, or null to clean up the error.
	 * @return the exception (ex)
	 */
	protected WrongValueException showCustomError(WrongValueException ex) {
		if (_auxinf != null && _auxinf.constr instanceof CustomConstraint) {
			Scopes.beforeInterpret(this);
			try {
				((CustomConstraint)_auxinf.constr).showCustomError(this, ex);
			} catch (Throwable t) {
				log.realCause(t); //and ignore it
			} finally {
				Scopes.afterInterpret();
			}
		}
		return ex;
	}

	/** Returns the maxlength.
	 * <p>Default: 0 (non-positive means unlimited).
	 */
	public int getMaxlength() {
		return _auxinf != null ? _auxinf.maxlength: 0;
	}
	/** Sets the maxlength.
	 * <p> The length includes the format, if specified.
	 */
	public void setMaxlength(int maxlength) {
		if ((_auxinf != null ? _auxinf.maxlength: 0) != maxlength) {
			initAuxInfo().maxlength = maxlength;
			smartUpdate("maxlength", getMaxlength());
		}
	}
	/** Returns the cols.
	 * <p>Default: 0 (non-positive means the same as browser's default).
	 */
	public int getCols() {
		return _cols;
	}
	/** Sets the cols.
	 */
	public void setCols(int cols) throws WrongValueException {
		if (cols <= 0)
			throw new WrongValueException("Illegal cols: "+cols);

		if (_cols != cols) {
			_cols = cols;
			smartUpdate("cols", _cols);
		}
	}
	/** Returns the tab order of this component.
	 * <p>Default: 0 (means the same as browser's default).
	 */
	public int getTabindex() {
		return _auxinf != null ? _auxinf.tabindex: 0;
	}
	/** Sets the tab order of this component.
	 */
	public void setTabindex(int tabindex) throws WrongValueException {
		if ((_auxinf != null ? _auxinf.tabindex: 0) != tabindex) {
			initAuxInfo().tabindex = tabindex;
			smartUpdate("tabindex", getTabindex());
		}
	}
	/** Returns true if onChange event is sent as soon as user types in the input 
	 * component.
	 * <p>Default: false
	 * @since 6.0.0
	 */
	public boolean getInstant() {
		return _auxinf != null && _auxinf.instant;
	}
	/** Sets the instant attribute. When the attribute is true, onChange event 
	 * will be fired as soon as user type in the input component.
	 * @since 6.0.0
	 */
	public void setInstant(boolean instant) {
		if (getInstant() != instant) {
			initAuxInfo().instant = instant;
			smartUpdate("instant", getInstant());
		}
	}
	/** Returns whether it is multiline.
	 * <p>Default: false.
	 */
	public boolean isMultiline() {
		return false;
	}
	/** Returns the type.
	 * <p>Default: text.
	 */
	public String getType() {
		return "text";
	}

	/** Selects the whole text in this input.
	 */
	public void select() {
		response(new AuSelect(this));
	}

	//-- Constrainted --//
	public void setConstraint(String constr) {
		setConstraint(constr != null ? SimpleConstraint.getInstance(constr): null); //Bug 2564298
	}
	public void setConstraint(Constraint constr) {
		if (!Objects.equals(_auxinf != null ? _auxinf.constr: null, constr)) {
			initAuxInfo().constr = constr;
			_valided = false;

			if (_auxinf.constr instanceof CustomConstraint) { //client ignored if custom
				smartUpdate("constraint", "[c"); //implies validated at server
				return;
			} else if (_auxinf.constr instanceof ClientConstraint) {
				final ClientConstraint cc = (ClientConstraint)_auxinf.constr;
				final JavaScriptValue cpkgs = getClientPackages(cc);
				if (cpkgs != null)
					smartUpdate("_0", cpkgs); //name doesn't matter

				final Object code = getClientConstraintCode(cc);
				if (code != null) {
					if (code instanceof JavaScriptValue)
						smartUpdate("z$al", code);
					else //must be string
						smartUpdate("constraint", new JavaScriptValue((String)code));
					return;
				}
			}
			smartUpdate("constraint", _auxinf.constr != null ? "[s": null);
		}
	}
	private static JavaScriptValue getClientPackages(ClientConstraint cc) {
		final String cpkg = cc.getClientPackages();
		return cpkg != null ? new JavaScriptValue("zk.load('" + cpkg + "')"): null;
	}
	private static Object getClientConstraintCode(ClientConstraint cc) {
		final String js = cc.getClientConstraint();
		if (js != null && js.length() > 0) {
			final char c =  js.charAt(0);
			if (c != '\'' && c != '"')
				return new JavaScriptValue("{constraint:function(){\nreturn "+js+";}}");
					//some JavaScript code => z$al
			return js;
		}
		return null;
	}
	public Constraint getConstraint() {
		return _auxinf != null ? _auxinf.constr: null;
	}

	/** Returns the value in the targeting type.
	 * It is used by the deriving class to implement the getValue method.
	 * For example, {@link org.zkoss.zul.Intbox#getValue} is the same
	 * as this method except with a different signature.
	 *
	 * <p>It invokes {@link #checkUserError} to ensure no user error.
	 * @exception WrongValueException if the user entered a wrong value
	 * @see #getText
	 */
	protected Object getTargetValue() throws WrongValueException {
		checkUserError();
		return _value;
	}

	/** Returns the raw value directly with checking whether any
	 * error message not yet fixed. In other words, it does NOT invoke
	 * {@link #checkUserError}.
	 *
	 * <p>Note: if the user entered an incorrect value (i.e., caused
	 * {@link WrongValueException}), the incorrect value doesn't
	 * be stored so this method returned the last correct value.
	 *
	 * @see #getRawText
	 * @see #getText
	 * @see #setRawValue
	 */
	public Object getRawValue() {
		return _value;
	}
	/** Returns the text directly without checking whether any error
	 * message not yet fixed. In other words, it does NOT invoke
	 * {@link #checkUserError}.
	 *
	 * <p>Note: if the user entered an incorrect value (i.e., caused
	 * {@link WrongValueException}), the incorrect value doesn't
	 * be stored so this method returned the last correct value.
	 *
	 * @see #getRawValue
	 * @see #getText
	 */
	public String getRawText() {
		return coerceToString(_value);
	}
	/** Sets the raw value directly. The caller must make sure the value
	 * is correct (or intend to be incorrect), because this method
	 * doesn't do any validation.
	 *
	 * <p>If you feel confusing with setValue, such as {@link org.zkoss.zul.Textbox#setValue},
	 * it is usually better to use setValue instead. This method
	 * is reserved for developer that really want to set an 'illegal'
	 * value (such as an empty string to a textbox with no-empty constraint).
	 *
	 * <p>Note: since 3.0.1, the value will be re-validate again if
	 * {@link #getText} or others (such as {@link org.zkoss.zul.Intbox#getValue})
	 * is called. In other words, it is assumed that the specified value
	 * is not validated yet -- the same state when this component is
	 * created. If you want to avoid the re-validation, you have to invoke
	 * {@link #clearErrorMessage()}.
	 *
	 * <p>Like setValue, the result is returned back to the server
	 * by calling {@link #getText}.
	 *
	 * @see #getRawValue
	 */
	public void setRawValue(Object value) {
		if ((_auxinf != null && _auxinf.errmsg != null)
		|| !Objects.equals(_value, value)) {
			clearErrorMessage(true);
			_value = value;
			smartUpdate("_value", marshall(_value));
		}
	}
	/** Sets the value directly.
	 * Note: Unlike {@link #setRawValue} (nor setValue), this method
	 * assigns the value directly without clearing error message or
	 * synchronizing with the client.
	 *
	 * <p>It is usually used only the constructor.
	 * Though it is also OK to use {@link #setRawValue} in the constructor,
	 * this method has better performance.
	 * @since 3.0.3
	 */
	protected void setValueDirectly(Object value) {
		_value = value;
	}

	/** Returns the current content of this input is correct.
	 * If the content is not correct, next call to the getValue method will
	 * throws WrongValueException.
	 */
	public boolean isValid() {
		if (_auxinf != null && _auxinf.errmsg != null)
			return false;

		if (!_valided && _auxinf != null && _auxinf.constr != null) {
			_auxinf.checkOnly = true;
			try {
				validate(_value);
			} catch (Throwable ex) {
				return false;
			} finally {
				_auxinf.checkOnly = false;
			}
		}
		return true;
	}

	/**
	 * Sets the text of this InputElement to the specified text which is
	 * beginning with the new start point and ending with the new end point.
	 * 
	 * @param start the start position of the text (included)
	 * @param end the end position of the text (excluded)
	 * @param newtxt the new text to be set.
	 * @param isHighLight
	 *            Sets whether it will represent highlight style or cursor
	 *            style.If the start point same with the end point always
	 *            represent cursor style.
	 */
	public void setSelectedText(int start, int end, String newtxt,
			boolean isHighLight) {
		if (start <= end) {
			final String txt = getText();
			final int len = txt.length();
			if (start < 0) start = 0;
			if (start > len) start = len;
			if (end < 0) end = 0;
			if (end > len) end = len;

			if (newtxt == null)
				newtxt = "";

			setText( txt.substring(0, start) + newtxt + txt.substring(end));
			setSelectionRange(start,
				isHighLight ? start + newtxt.length(): start);
		}
	}

	/**
	 * Sets the selection end to the specified position and the selection start
	 * to the specified position. The new end point is constrained to be at or
	 * after the current selection start. If the new start point is different
	 * with the new end point, then will represent the result of highlight in
	 * this text.
	 *
	 * <p>Set both arguments to the same value to move the cursor to
	 * the corresponding position without selecting text.
	 * 
	 * @param start the start position of the text (included)
	 * @param end the end position of the text (excluded)
	 */
	public void setSelectionRange(int start, int end) {
		response(new AuSelect(this, start, end));
	}
	
	/** Checks whether user entered a wrong value (and not correct it yet).
	 * Since user might enter a wrong value and moves on to other components,
	 * this method is called when {@link #getText} or {@link #getTargetValue} is
	 * called.
	 *
	 * <p>Derives rarely need to access this method if they use only
	 * {@link #getText} and {@link #getTargetValue}.
	 */
	protected void checkUserError() throws WrongValueException {
		if (_auxinf != null && _auxinf.errmsg != null)
			throw new WrongValueException(this, _auxinf.errmsg);
				//Note: we still throw exception to abort the exec flow
				//It's client's job NOT to show the error box!
				//(client checks z.srvald to decide whether to open error box)

		if (!_valided && _auxinf != null && _auxinf.constr != null)
			setText(coerceToString(_value));
	}

	//-- Component --//
	/** Not childable. */
	protected boolean isChildable() {
		return false;
	}

	//-- ComponentCtrl --//
	public WrongValueException onWrongValue(WrongValueException ex) {
		initAuxInfo().errmsg = Exceptions.getMessage(ex);
		return showCustomError(ex);
	}
	/** Marshall value to be sent to the client if needed.
	 *
	 * <p>Overrides it if the value to be sent to the client is not JSON Compatible.
	 * @param value the value to be sent to the client
	 * @return the marshalled value
	 * @since 5.0.5
	 */
	protected Object marshall(Object value) {
		return value;
	}
	/** Unmarshall value returned from client if needed.
	 *
	 * <p>Overrides it if the value returned is not JSON Compatible.
	 * @param value the value returned from client
	 * @return the unmarshalled value
	 * @since 5.0.5
	 */
	protected Object unmarshall(Object value) {
		return value;
	}
	private void setValueByClient(Object value, String valstr) {
		if (_auxinf != null && _auxinf.maxlength > 0 && valstr != null && valstr.length() > _auxinf.maxlength)
			throw new WrongValueException(this, MZul.STRING_TOO_LONG, new Integer(_auxinf.maxlength));

		final boolean same = Objects.equals(_value, value);
		boolean errFound = false;
		if (!same || !_valided || (_auxinf != null && _auxinf.errmsg != null)) { //note: the first time (!_valided) must always validate
			validate(value); //Bug 2946917: don't validate if not changed

			errFound = _auxinf != null && _auxinf.errmsg != null;
			clearErrorMessage(); //no error at all
		}

		if (!same) {
			_value = value;
		} else if (errFound) {
			smartUpdate("_value", marshall(_value)); //send back original value
				//Bug 1876292: make sure client see the updated value
		}
	}
	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link XulElement#service},
	 * it also handles onChange, onChanging and onError.
	 * @since 5.0.0
	 */
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals(Events.ON_CHANGE)) {
			try {
				final Object oldval = _value;
				Object value = null;
				final Map<String, Object> data = request.getData();
				final String rawValue = (String)data.get("rawValue");
				if (rawValue != null) {
					value = coerceFromString(rawValue);
				} else {
					final Object clientv = data.get("value");
					try {
						value = unmarshall(clientv);
					} catch (NumberFormatException ex) {
						throw new WrongValueException(this, MZul.NUMBER_REQUIRED, clientv);
					}
				}

				final String valstr = coerceToString(value);
				setValueByClient(value, valstr); //always since it might have func even not change
				if (rawValue != null && !rawValue.equals(valstr))
					smartUpdate("_value", marshall(_value));

				if (Objects.equals(oldval, _value))
					return; //Bug 1881557: don't post event if not modified

				final InputEvent evt = new InputEvent(cmd, this,
					valstr, oldval, //20101022, henrichen: for backward compatible, must coerceToString
					AuRequests.getBoolean(data, "bySelectBack"),
					AuRequests.getInt(data, "start", 0));
				Events.postEvent(evt);
			} catch (WrongValueException ex) {
				initAuxInfo().errmsg = ex.getMessage();
				throw ex; //No need to go through onWrongValue since UiEngine will do it
			}
		} else if (cmd.equals(Events.ON_CHANGING)) {
			final Map<String, Object> data = request.getData();
			final Object clientv = data.get("value");
			final Object oldval = _value;
			final InputEvent evt = new InputEvent(cmd, this,
				clientv == null ? "" : clientv.toString(), oldval, //clientv is what user input (not marshal)
				AuRequests.getBoolean(data, "bySelectBack"),
				AuRequests.getInt(data, "start", 0));
			Events.postEvent(evt);
		} else if (cmd.equals(Events.ON_ERROR)) {
			ErrorEvent evt = ErrorEvent.getErrorEvent(request, _value);
			final String msg = evt.getMessage();
			initAuxInfo().errmsg = msg != null && msg.length() > 0 ? msg: null;
			Events.postEvent(evt);
		} else if (cmd.equals(Events.ON_SELECTION)) {
			Events.postEvent(SelectionEvent.getSelectionEvent(request));
		} else
			super.service(request, everError);
	}

	//super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		render(renderer, "_value", marshall(_value));
			//ZK-658: we have to render the value before constraint
		render(renderer, "readonly", _readonly);
		render(renderer, "disabled", _disabled);
		render(renderer, "name", getName());
		render(renderer, "inplace", _inplace);
		
		if (_placeholder != null) render(renderer, "placeholder", _placeholder);
		
		int v;
		if ((v = getMaxlength()) > 0) renderer.render("maxlength", v);
		if (_cols > 0) renderer.render("cols", _cols);
		if ((v = getTabindex()) != 0) renderer.render("tabindex", v);
		if (getInstant()) renderer.render("instant", true);

		boolean constrDone = false;
		final Constraint constr = _auxinf != null ? _auxinf.constr: null;
		if (constr instanceof CustomConstraint) { //client ignored if custom
			renderer.render("constraint", "[c"); //implies validated at server
			constrDone = true;
		} else if (constr instanceof ClientConstraint) {
			final ClientConstraint cc = (ClientConstraint)constr;
			render(renderer, "_0", getClientPackages(cc)); //name doesn't matter

			final Object code = getClientConstraintCode(cc);
			if (code != null) {
				if (code instanceof JavaScriptValue)
					renderer.render("z$al", code);
				else //must be string
					renderer.renderDirectly("constraint", code);
				constrDone = true;
			}
		}
		if (!constrDone && constr != null)
			renderer.render("constraint", "[s");

		Utils.renderCrawlableText(coerceToString(_value));
	}

	//Cloneable//
	public Object clone() {
		final InputElement clone = (InputElement)super.clone();
		if (_auxinf != null)
			clone._auxinf = (AuxInfo)_auxinf.clone();
		return clone;
	}

	private AuxInfo initAuxInfo() {
		if (_auxinf == null)
			_auxinf = new AuxInfo();
		return _auxinf;
	}
	private static class AuxInfo implements java.io.Serializable, Cloneable {
		/** The error message. Not null if users entered a wrong data (and
		 * not correct it yet).
		 */
		private String errmsg;
		/** The name. */
		private String name;
		private int maxlength;
		private int tabindex;
		/** Whether to send onChange as soon as possible */
		private boolean instant;
		private Constraint constr;
		/** Whether the validation is caused by {@link #isValid}. */
		private transient boolean checkOnly;

		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
				throw new InternalError();
			}
		}
	}
}
