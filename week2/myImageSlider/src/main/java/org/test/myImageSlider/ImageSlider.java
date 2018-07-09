package org.test.myImageSlider;

import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.impl.XulElement;

public class ImageSlider extends XulElement {

	static {
		addClientEvent(ImageSlider.class, "onFoo", 0);
	}

	/* Here's a simple example for how to implements a member field */

	private String _text;

	private Image _selectedItem;

	private int _selectedIndex;

	private int _viewportSize;

	private int _imageWidth;

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		if (!Objects.equals(_text, text)) {
			_text = text;
			smartUpdate("text", _text);
		}
	}

	public Image getSelectedItem() {
		return _selectedItem;
	}

	public void setSelectedItem(Image selectedItem) {
		if (!Objects.equals(_selectedItem, selectedItem)) {
			_selectedItem = selectedItem;
			smartUpdate("selectedItem", _selectedItem);
		}
	}

	public int getSelectedIndex() {
		return _selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		if (!Objects.equals(_selectedIndex, selectedIndex)) {
			_selectedIndex = selectedIndex;
			smartUpdate("selectedIndex", _selectedIndex);
		}
	}

	public int getViewportSize() {
		return _viewportSize;
	}

	public void setViewportSize(int viewportSize) {
		if (!Objects.equals(_viewportSize, viewportSize)) {
			_viewportSize = viewportSize;
			smartUpdate("viewportSize", _viewportSize);
		}
	}

	public int getImageWidth() {
		return _imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		if (!Objects.equals(_imageWidth, imageWidth)) {
			_imageWidth = imageWidth;
			smartUpdate("imageWidth", _imageWidth);
		}
	}

	// super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "text", _text);
		render(renderer, "selectedItem", _selectedItem);
		render(renderer, "selectedIndex", _selectedIndex);
		render(renderer, "viewportSize", _viewportSize);
		render(renderer, "imageWidth", _imageWidth);
		System.out.print("render");
	}

	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		final Map data = request.getData();

		if (cmd.equals("onFoo")) {
			final String foo = (String) data.get("foo");
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

	// public void beforeChildAdded(Component child,Component refChild) {
	// if(!(child instanceof Listheader)) {
	// throw new UiException();
	// }
	// super.beforeChildAdded(child, refChild);
	// }
}
