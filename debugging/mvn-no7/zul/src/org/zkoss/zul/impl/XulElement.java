/* XulElement.java

	Purpose:
		
	Description:
		
	History:
		Mon Jun 20 16:01:40     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.impl;

import java.io.Serializable;

import org.zkoss.lang.Objects;
import org.zkoss.zk.au.DeferredValue;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.Popup;

/**
 * The fundamental class for XUL elements.
 * 
 * @author tomyeh
 */
abstract public class XulElement extends HtmlBasedComponent {
	/** AuxInfo: use a class (rather than multiple member) to save footprint */
	private AuxInfo _auxinf;

	/** Returns what keystrokes to intercept.
	 * <p>Default: null.
	 * @since 3.0.6
	 */
	public String getCtrlKeys() {
		return _auxinf != null ? _auxinf.ctrlKeys: null;
	}
	/** Sets what keystrokes to intercept.
	 *
	 * <p>The string could be a combination of the following:
	 * <dl>
	 * <dt>^k</dt>
	 * <dd>A control key, i.e., Ctrl+k, where k could be a~z, 0~9, #n</dd>
	 * <dt>@k</dt>
	 * <dd>A alt key, i.e., Alt+k, where k could be a~z, 0~9, #n</dd>
	 * <dt>$n</dt>
	 * <dd>A shift key, i.e., Shift+n, where n could be #n.
	 * Note: $a ~ $z are not supported.</dd>
	 * <dt>#home</dt>
	 * <dd>Home</dd>
	 * <dt>#end</dt>
	 * <dd>End</dd>
	 * <dt>#ins</dt>
	 * <dd>Insert</dd>
	 * <dt>#del</dt>
	 * <dd>Delete</dd>
	 * <dt>#bak</dt>
	 * <dd>Backspace</dd> 
	 * <dt>#left</dt>
	 * <dd>Left arrow</dd>
	 * <dt>#right</dt>
	 * <dd>Right arrow</dd>
	 * <dt>#up</dt>
	 * <dd>Up arrow</dd>
	 * <dt>#down</dt>
	 * <dd>Down arrow</dd>
	 * <dt>#pgup</dt>
	 * <dd>PageUp</dd>
	 * <dt>#pgdn</dt>
	 * <dd>PageDn</dd>
	 * <dt>#f1 #f2 ... #f12</dt>
	 * <dd>Function keys representing F1, F2, ... F12</dd>
	 * </dl>
	 *
	 * <p>For example,
	 * <dl>
	 * <dt>^a^d@c#f10#left#right</dt>
	 * <dd>It means you want to intercept Ctrl+A, Ctrl+D, Alt+C, F10,
	 * Left and Right.</dd>
	 * <dt>^#left</dt>
	 * <dd>It means Ctrl+Left.</dd>
	 * <dt>^#f1</dt>
	 * <dd>It means Ctrl+F1.</dd>
	 * <dt>@#f3</dt>
	 * <dd>It means Alt+F3.</dd>
	 * </dl>
	 *
	 * <p>Note: it doesn't support Ctrl+Alt, Shift+Ctrl, Shift+Alt or Shift+Ctrl+Alt.
	 * @since 3.0.6
	 */
	public void setCtrlKeys(String ctrlKeys) throws UiException {
		if (ctrlKeys != null && ctrlKeys.length() == 0)
			ctrlKeys = null;
		if (!Objects.equals(_auxinf != null ? _auxinf.ctrlKeys: null, ctrlKeys)) {
			initAuxInfo().ctrlKeys = ctrlKeys;
			smartUpdate("ctrlKeys", getCtrlKeys());
		}
	}

	/** Returns the ID of the popup ({@link Popup}) that should appear
	 * when the user right-clicks on the element (a.k.a., context menu).
	 *
	 * <p>Default: null (no context menu).
	 */
	public String getContext() {
		return _auxinf != null && _auxinf.context != null ? (String) _auxinf.context.getValue() : null;
	}
	/** Sets the ID of the popup ({@link Popup}) that should appear
	 * when the user right-clicks on the element (a.k.a., context menu).
	 *
	 * <p>An onOpen event is sent to the context menu if it is going to
	 * appear. Therefore, developers can manipulate it dynamically
	 * (perhaps based on OpenEvent.getReference) by listening to the onOpen
	 * event.
	 *
	 * <p>Note: To simplify the use, it not only searches its ID space,
	 * but also all ID spaces in the desktop.
	 * It first searches its own ID space, and then the other Id spaces
	 * in the same browser window (might have one or multiple desktops).
	 *
	 * <p>(since 3.0.2) If there are two components with the same ID (of course, in
	 * different ID spaces), you can specify the UUID with the following
	 * format:<br/>
	 * <code>uuid(comp_uuid)</code>
	 *
	 * <p>Example:<br/>
	 * <pre><code>
	 * &lt;label context="some"&gt;
	 * &lt;label context="uuid(${some.uuid})"/&gt;
	 * </code></pre>
	 * Both reference a component whose ID is "some".
	 * But, if there are several components with the same ID,
	 * the first one can reference to any of them.
	 * And, the second one reference to the component in the same ID space
	 * (of the label component).
	 * 
	 * 
	 * <p> (since 3.6.3) the context menu can be shown by a position from {@link Popup#open(org.zkoss.zk.ui.Component, String)}
	 * or the location of <code>x</code> and <code>y</code>, you can specify the following format:</br>
	 * <ul>
	 * <li><code>id, position</code></li>
	 * <li><code>id, position=before_start</code></li>
	 * <li><code>id, x=15, y=20</code></li>
	 * <li><code>uuid(comp_uuid), position</code></li>
	 * <li><code>uuid(comp_uuid), x=15, y=20</code></li>
	 * </ul>
	 * For example,
	 * <pre>
	 * &lt;button label="show" context="id, start_before"/>
	 * </pre>
	 * <p> (since 6.5.2) the context menu can also be shown on customized location of <code>x</code> and <code>y</code> by adding parentheses"()", for example,
	 * <pre>
	 * &lt;button label="show" context="id, x=(zk.currentPointer[0] + 10), y=(zk.currentPointer[1] - 10)"/&gt;
	 * </pre>
	 * @see #setContext(Popup)
	 */
	public void setContext(String context) {
		if (!Objects.equals(_auxinf != null ? _auxinf.context: null, context)) {
			initAuxInfo().context = new DeferedUuid(context);
			smartUpdate("context", _auxinf.context);
		}
	}
	/** Sets the UUID of the popup that should appear 
	 * when the user right-clicks on the element (a.k.a., context menu).
	 *
	 * <p>Note: it actually invokes
	 * <code>setContext("uuid(" + popup.getUuid() + ")")</code>
	 * @since 3.0.2
	 * @see #setContext(String)
	 * @see Popup#open(org.zkoss.zk.ui.Component, String)
	 */
	public void setContext(Popup popup) {
		if (!Objects.equals(_auxinf != null ? _auxinf.context: null, popup)) {
			initAuxInfo().context = new DeferedUuid(popup);
			smartUpdate("context", _auxinf.context);
		}
	}
	/** Returns the ID of the popup ({@link Popup}) that should appear
	 * when the user clicks on the element.
	 *
	 * <p>Default: null (no popup).
	 */
	public String getPopup() {
		return _auxinf != null && _auxinf.popup != null ? (String) _auxinf.popup.getValue() : null;
	}
	/** Sets the ID of the popup ({@link Popup}) that should appear
	 * when the user clicks on the element.
	 *
	 * <p>An onOpen event is sent to the popup menu if it is going to
	 * appear. Therefore, developers can manipulate it dynamically
	 * (perhaps based on OpenEvent.getReference) by listening to the onOpen
	 * event.
	 *
	 * <p>Note: To simplify the use, it not only searches its ID space,
	 * but also all ID spaces in the desktop.
	 * It first searches its own ID space, and then the other Id spaces
	 * in the same browser window (might have one or multiple desktops).
	 *
	 * <p>(since 3.0.2) If there are two components with the same ID (of course, in
	 * different ID spaces), you can specify the UUID with the following
	 * format:<br/>
	 * <code>uuid(comp_uuid)</code>
	 * 
	 * <p> (since 3.6.3) the popup can be shown by a position from {@link Popup#open(org.zkoss.zk.ui.Component, String)}
	 * or the location of <code>x</code> and <code>y</code>, you can specify the following format:</br>
	 * <ul>
	 * <li><code>id, position</code></li>
	 * <li><code>id, position=before_start</code></li>
	 * <li><code>id, x=15, y=20</code></li>
	 * <li><code>uuid(comp_uuid), position</code></li>
	 * <li><code>uuid(comp_uuid), x=15, y=20</code></li>
	 * </ul>
	 * For example,
	 * <pre>
	 * &lt;button label="show" popup="id, start_before"/>
	 * </pre>
	 * <p> (since 6.5.2) the popup can also be shown on customized location of <code>x</code> and <code>y</code> by adding parentheses"()", for example,
	 * <pre>
	 * &lt;button label="show" context="id, x=(zk.currentPointer[0] + 10), y=(zk.currentPointer[1] - 10)"/&gt;
	 * </pre>
	 * @see #setPopup(Popup)
	 * @see Popup#open(org.zkoss.zk.ui.Component, String)
	 */
	public void setPopup(String popup) {
		if (!Objects.equals(_auxinf != null ? _auxinf.popup: null, popup)) {
			initAuxInfo().popup = new DeferedUuid(popup);
			smartUpdate("popup", _auxinf.popup);
		}
	}
	/** Sets the UUID of the popup that should appear
	 * when the user clicks on the element.
	 *
	 * <p>Note: it actually invokes
	 * <code>setPopup("uuid(" + popup.getUuid() + ")")</code>
	 * @since 3.0.2
	 * @see #setPopup(String)
	 */
	public void setPopup(Popup popup) {
		if (!Objects.equals(_auxinf != null ? _auxinf.popup: null, popup)) {
			initAuxInfo().popup = new DeferedUuid(popup);
			smartUpdate("popup", _auxinf.popup);
		}
	}
	/** Returns the ID of the popup ({@link Popup}) that should be used
	 * as a tooltip window when the mouse hovers over the element for a moment.
	 * The tooltip will automatically disappear when the mouse is moved away.
	 *
	 * <p>Default: null (no tooltip).
	 */
	public String getTooltip() {
		return _auxinf != null && _auxinf.tooltip != null ? (String)  _auxinf.tooltip.getValue() : null;
	}
	/** Sets the ID of the popup ({@link Popup}) that should be used
	 * as a tooltip window when the mouse hovers over the element for a moment.
	 *
	 * <p>An onOpen event is sent to the tooltip if it is going to
	 * appear. Therefore, developers can manipulate it dynamically
	 * (perhaps based on OpenEvent.getReference) by listening to the onOpen
	 * event.
	 *
	 * <p>Note: To simplify the use, it not only searches its ID space,
	 * but also all ID spaces in the desktop.
	 * It first searches its own ID space, and then the other Id spaces
	 * in the same browser window (might have one or multiple desktops).
	 *
	 * <p>(since 3.0.2) If there are two components with the same ID (of course, in
	 * different ID spaces), you can specify the UUID with the following
	 * format:<br/>
	 * <code>uuid(comp_uuid)</code>
	 * 
	 * <p> (since 3.6.3) the tooltip can be shown by a position from
	 * {@link Popup#open(org.zkoss.zk.ui.Component, String)}
	 * or the location of <code>x</code> and <code>y</code>, and can be specified
	 * with a delay time (in millisecond), you can specify the following format:
	 * </br>
	 * <ul>
	 * <li><code>id, position</code></li>
	 * <li><code>id, position=before_start, delay=500</code></li>
	 * <li><code>id, x=15, y=20</code></li>
	 * <li><code>uuid(comp_uuid2), position</code></li>
	 * <li><code>uuid(comp_uuid), x=15, y=20</code></li>
	 * </ul>
	 * For example,
	 * <pre>
	 * &lt;button label="show" tooltip="id, start_before"/>
	 * </pre>
	 * <p> (since 6.5.2) the tooltip can also be shown on customized location of <code>x</code> and <code>y</code> by adding parentheses"()", for example,
	 * <pre>
	 * &lt;button label="show" context="id, x=(zk.currentPointer[0] + 10), y=(zk.currentPointer[1] - 10)"/&gt;
	 * </pre>
	 * 
	 * @see #setTooltip(Popup)
	 * @see Popup#open(org.zkoss.zk.ui.Component, String)
	 */
	public void setTooltip(String tooltip) {
		// ZK-816
		if (!Objects.equals(_auxinf != null ? _auxinf.tooltip: null, tooltip)) {
			initAuxInfo().tooltip = new DeferedUuid(tooltip);
			smartUpdate("tooltip", _auxinf.tooltip);
		}
	}
	/** Sets the UUID of the popup that should be used
	 * as a tooltip window when the mouse hovers over the element for a moment.
	 *
	 * <p>Note: it actually invokes
	 * <code>setTooltip("uuid(" + popup.getUuid() + ")")</code>
	 * @since 3.0.2
	 * @see #setTooltip(String)
	 */
	public void setTooltip(Popup popup) {
		// ZK-816, component keep wrong tooltip reference if set tooltip before tooltip attached
		if (!Objects.equals(_auxinf != null ? _auxinf.tooltip: null, popup)) {
			initAuxInfo().tooltip = new DeferedUuid(popup);
			smartUpdate("tooltip", _auxinf.tooltip);
		}
	}

	//super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		render(renderer, "popup", getPopup());
		render(renderer, "context", getContext());
		// ZK-816
		render(renderer, "tooltip", getTooltip());
		render(renderer, "ctrlKeys", getCtrlKeys());
	}

	//Cloneable//
	public Object clone() {
		final XulElement clone = (XulElement)super.clone();
		if (_auxinf != null)
			clone._auxinf = (AuxInfo)_auxinf.clone();
		return clone;
	}

	private final AuxInfo initAuxInfo() {
		if (_auxinf == null)
			_auxinf = new AuxInfo();
		return _auxinf;
	}
	/** Merge multiple members into an single object (and create on demand)
	 * to minimize the footprint
	 * @since 5.0.4
	 */
	private static class AuxInfo implements java.io.Serializable, Cloneable {
		/** The popup ID that will be shown when click. */
		private DeferredValue popup;
		/** The context ID that will be shown when right-click. */
		private DeferredValue context;
		/** The tooltip ID that will be shown when mouse-over. */
		private DeferredValue tooltip;
		/** What control and function keys to intercepts. */
		private String ctrlKeys;

		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
				throw new InternalError();
			}
		}
	}
	
	private static class DeferedUuid implements DeferredValue,Serializable{
		private static final long serialVersionUID = -122378869909137783L;
		
		private Popup popup;
		private String popupString;
		public DeferedUuid(String popupString) {
			super();
			this.popupString = popupString;
		}
		
		public DeferedUuid(Popup tooltip) {
			super();
			this.popup = tooltip;
		}
		
		public Object getValue() {
			if( popupString != null){
				return popupString;
			}else if(popup != null){
				return "uuid(" + popup.getUuid() + ")";
			}else{
				return null;
			}
		}
		
		public int hashCode() {
			if(popupString != null) {
				return popupString.hashCode();
			}else if(popup != null){
				return popup.hashCode();
			}else{
				return super.hashCode();
			}
		}
		
		public boolean equals(Object obj) {
			
			if(obj instanceof String){
				return Objects.equals(popupString, obj);
			}else if(obj instanceof Popup){
				return Objects.equals(popup, obj);
			}else if(obj == null){
				return popup == null && popupString == null;
			}else{
				return false;
			}
		}
	}
}
