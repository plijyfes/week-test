/* FileuploadDlg.java

	Purpose:
		
	Description:
		
	History:
		Wed Aug 17 16:33:06     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.impl;

import java.util.LinkedList;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zul.Window;

/**
 * Used with {@link org.zkoss.zul.Fileupload} to implement
 * the upload feature.
 *
 * @author tomyeh
 */
public class FileuploadDlg extends Window {
	private LinkedList<Media> _result = new LinkedList<Media>();
	private static final String ATTR_FILEUPLOAD_TARGET = "org.zkoss.zul.Fileupload.target";
	private EventListener<UploadEvent> _listener;
	
	/**
	 * Set the upload call back event listener
	 * @since 6.5.3
	 */
	public void setUploadListener(EventListener<UploadEvent> listener) {
		_listener = listener;
	}
	
	public void onClose(Event evt) {
		if (evt.getData() == null)
			_result.clear();
		else {
			final Desktop desktop = Executions.getCurrent().getDesktop();
			final Configuration config = desktop .getWebApp().getConfiguration();
			if (!config.isEventThreadEnabled()) {
				if (_listener != null)
					try {
						_listener.onEvent(new UploadEvent(Events.ON_UPLOAD, null, getResult()));
					} catch (Exception e) {
						throw new UiException(e);
					}
				else
					Events.postEvent(new UploadEvent(Events.ON_UPLOAD, (Component)desktop.getAttribute(ATTR_FILEUPLOAD_TARGET), getResult()));
			}
		}
		detach();
	}
	/**
	 * Called when a file is received.
	 * It is used only for component development.
	 * @since 5.0.0
	 */
	public void onUpload(ForwardEvent evt) {
		_result.add(((UploadEvent)evt.getOrigin()).getMedia());
	}
	
	/** Returns the result.
	 * @return an array of media (length >= 1), or null if nothing.
	 */
	public Media[] getResult() {
		return _result.isEmpty() ? null : _result.toArray(new Media[_result.size()]);
	}
	
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onRemove")) {
			_result.remove(((Integer)request.getData().get("")).intValue());
		} else
			super.service(request, everError);
	}
}
