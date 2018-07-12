package org.test.myImageSlider;

import java.util.List;
import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Image;
import org.zkoss.zul.impl.XulElement;

public class ImageSlider extends XulElement {

	static {
		addClientEvent(ImageSlider.class, Events.ON_SELECT, CE_IMPORTANT);
	}
	// to DO :selectedItem
	private Image _selectedItem;

	private int _selectedIndex = -1;

	private int _viewportSize = 3;

	private int _imageWidth = 200;

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
		if (_selectedIndex != selectedIndex) {
			_selectedIndex = selectedIndex;
			smartUpdate("selectedIndex", _selectedIndex);
		}
	}

	public int getViewportSize() {
		return _viewportSize;
	}

	public void setViewportSize(int viewportSize) {
		if (_viewportSize != viewportSize) {
			_viewportSize = viewportSize;
			smartUpdate("viewportSize", _viewportSize);
		}
	}

	public int getImageWidth() {
		return _imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		if (_imageWidth != imageWidth) {
			_imageWidth = imageWidth;
			smartUpdate("imageWidth", _imageWidth);
		}
	}

	// super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "selectedItem", _selectedItem);
		if (_selectedIndex != -1) {
			render(renderer, "selectedIndex", _selectedIndex);
		}
		if (_viewportSize != 3) {
			render(renderer, "viewportSize", _viewportSize);
		}
		if (_imageWidth != 200) {
			render(renderer, "imageWidth", _imageWidth);
		}
	}

	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		final Map data = request.getData();

		if (cmd.equals(Events.ON_SELECT)) {
			SelectEvent<Image, Object> evt = SelectEvent.getSelectEvent(request);
			final int selected = (Integer) data.get("selected");
			List<Component> imagelist = getChildren();
			_selectedIndex = selected;
			_selectedItem = (Image) imagelist.get(selected);
			System.out.println("do onSelect, Selecteditem:" + imagelist.get(selected));
			System.out.println("do onSelect, SelectedIndex:" + selected);
			Events.postEvent(evt);
		} else {
			super.service(request, everError);
		}
	}

	/**
	 * The default zclass is "z-imageSlider"
	 */
	public String getZclass() {
		return (this._zclass != null ? this._zclass : "z-imageSlider");
	}

	public void beforeChildAdded(Component child, Component refChild) {
		if (!(child instanceof Image)) {
			throw new UiException("Unsupported child: " + child); // image only
		}
		super.beforeChildAdded(child, refChild);
	}
}
