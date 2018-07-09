package org.test.myImageSlider;

import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.impl.XulElement;

public class ImageSlider extends XulElement {

	static {
		addClientEvent(ImageSlider.class, "onFoo", 0);
	}
	
	/* Here's a simple example for how to implements a member field */

	private String _text;
	
	private String _sliderClass;

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		if (!Objects.equals(_text, text)) {
			_text = text;
			smartUpdate("text", _text);
		}
	}
	
	public String get_sliderClass() {
		return _sliderClass;
	}

	public void set_sliderClass(String sliderClass) {
		if (!Objects.equals(_sliderClass, sliderClass)) {
			_sliderClass = sliderClass;
			smartUpdate("sliderClass", _sliderClass);
		}
	}

	//super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);
//		render(renderer, "text", _text);
		render(renderer, "sliderClass", _sliderClass);
	}
	
	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		final Map data = request.getData();
		
		if (cmd.equals("onFoo")) {
			final String foo = (String)data.get("foo");
			System.out.println("do onFoo, data:" + foo);
			Events.postEvent(Event.getEvent(request));
		} else
			super.service(request, everError);
	}

	/**
	 * The default zclass is "z-imageSlider"
	 */
	public String getZclass() {
		return (this._zclass != null ? this._zclass : "z-imageSlider");
	}
	
	public void beforeParentChange(Component parent) {
		if(parent != null && !(parent instanceof Listbox)) {
			throw new UiException();			
		}
		super.beforeParentChanged(parent);
	}
	
//	public void beforeChildAdded(Component child,Component refChild) {
//		if(!(child instanceof Listheader)) {
//			throw new UiException();			
//		}
//		super.beforeChildAdded(child, refChild);
//	}
}

