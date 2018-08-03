/* PageSizeEvent.java

	Purpose:
		
	Description:
		
	History:
		Sat Jun 30 21:02:06     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

import org.zkoss.zul.ext.Pageable;
import org.zkoss.zul.ext.Paginal;
import org.zkoss.zul.Grid;

/**
 * Used to notify the paging size has been changed 
 * when the autopaging ({@link Grid#setAutopaging}) is enabled 
 * and user changed the size of the content. 
 * 
 * @author tomyeh
 * @since 2.4.1
 */
public class PageSizeEvent extends Event {
	private final Pageable _pgi;
	private final int _pgsz;

	/** Construct a page size event.
	 *
	 * @param target the target must be a paginal component, i.e.,
	 * implements {@link Pageable}.
	 * @param pgsz the new page size
	 */
	public PageSizeEvent(String name, Component target, int pgsz) {
		super(name, target);
		_pgi = (Pageable)target;
		_pgsz = pgsz;
	}
	/** Construct a page size event that the target is different
	 * from the page controller.
	 *
	 * @param target the event target
	 * @param pageable the paging controller. In other words,
	 * it is usually {@link Paginal}.
	 */
	public PageSizeEvent(String name, Component target, Pageable pageable,
	int pgsz) {
		super(name, target);
		_pgi = pageable;
		_pgsz = pgsz;
	}

	/** Returns the pageable controller.
	 */
	public Pageable getPageable() {
		return _pgi;
	}

	/** Returns the page size.
	 * <p>It is the same as {@link #getPageable}'s {@link Pageable#getPageSize}.
	 */
	public int getPageSize() {
		return _pgsz;
	}
}
