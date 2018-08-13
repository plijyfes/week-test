package org.test.myImageSlider;

import java.util.List;
import java.util.Map;

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

	private int _selectedIndex = -1;

	private int _viewportSize = 3;

	private int _imageWidth = 200;

	public Image getSelectedItem() {
		List<Component> imagelist = getChildren();
		if (_selectedIndex != -1 && _selectedIndex >= 0 && _selectedIndex < imagelist.size()) {	  // 邊界判斷
			return (Image) imagelist.get(_selectedIndex);
		}
		return null;
	}

	public void setSelectedItem(Image selectedItem) {
		if (selectedItem != null) {
			if (selectedItem.getParent() == this) {
				setSelectedIndex(getChildren().indexOf(selectedItem));
			} else {
				throw new UiException("Invalid selectedItem: " + selectedItem);
			}
		} else {
			setSelectedIndex(-1);
		}
	}

	public int getSelectedIndex() {
		return _selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		if (selectedIndex < getChildren().size() && selectedIndex >= -1) {
			if (_selectedIndex != selectedIndex) {
				_selectedIndex = selectedIndex;
				smartUpdate("selectedIndex", _selectedIndex);
			}
		} else {
			throw new UiException("Invalid selectedIndex" + selectedIndex);
		}
	}

	public int getViewportSize() {
		return _viewportSize;
	}

	public void setViewportSize(int viewportSize) { // 可判斷邊界
		if (viewportSize > 0) {
			if (_viewportSize != viewportSize) {
				_viewportSize = viewportSize;
				smartUpdate("viewportSize", _viewportSize);
			}
		} else {
			throw new UiException("Invalid viewportSize" + viewportSize);
		}
	}

	public int getImageWidth() {
		return _imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		if (imageWidth >= 0) {
			if (_imageWidth != imageWidth) {
				_imageWidth = imageWidth;
				smartUpdate("imageWidth", _imageWidth);
			}
		} else {
			throw new UiException("Invalid imageWidth" + imageWidth);
		}
	}

	@Override
	public void onChildAdded(Component child) {
		super.onChildAdded(child);
		List<Component> imagelist = getChildren();
		setSelectedIndex(imagelist.indexOf(getSelectedItem()));
	}

	@Override
	public void beforeChildRemoved(Component child) {
		int childIndex = getChildren().indexOf(child);
		if (getSelectedIndex() == childIndex) {
			setSelectedIndex(-1);
		} else if (getSelectedIndex() > childIndex) {
			setSelectedIndex(getSelectedIndex() - 1);
		}
		if (_viewportSize > getChildren().size()) {
			setViewportSize(getChildren().size() - 1);
		}
		super.beforeChildRemoved(child);
	}

	// super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer) throws java.io.IOException {
		super.renderProperties(renderer);
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
			_selectedIndex = selected;
//			System.out.println("do onSelect, Selecteditem:" + imagelist.get(selected));
//			System.out.println("do onSelect, SelectedIndex:" + selected);
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
			throw new UiException("Unsupported child: " + child + "(Image only)"); // image only
		}
		super.beforeChildAdded(child, refChild);
	}
}
