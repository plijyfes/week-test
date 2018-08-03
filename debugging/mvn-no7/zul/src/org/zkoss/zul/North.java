/* North.java

	Purpose:
		
	Description:
		
	History:
		Feb 10, 2009 4:17:16 PM , Created by jumperchen

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

/**
 * A north region of a border layout.
 * <p>Default {@link #getZclass}: z-north.(since 3.5.0)
 * 
 * @author jumperchen
 * @since 5.0.0
 */
public class North extends LayoutRegion {
	public North() {
	}

	/**
	 * Returns {@link Borderlayout#NORTH}.
	 */
	public String getPosition() {
		return Borderlayout.NORTH;
	}

	/**
	 * The width can't be specified in this component because its width is
	 * determined by other region components ({@link West} or {@link East}).
	 */
	public void setWidth(String width) {
		throw new UnsupportedOperationException("readonly");
	}

	/**
	 * Returns the size of this region. This method is shortcut for
	 * {@link #getHeight()}.
	 */
	public String getSize() {
		return getHeight();
	}

	/**
	 * Sets the size of this region. This method is shortcut for
	 * {@link #setHeight(String)}.
	 */
	public void setSize(String size) {
		setHeight(size);
	}

	/** Returns the default collapsed margin ([3, 0, 0, 3]).
	 * @since 5.0.5
	 */
	protected int[] getDefaultCmargins() {
		return new int[] { 3, 0, 0, 3 };
	}
}
