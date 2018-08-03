/* Space.java

	Purpose:
		
	Description:
		
	History:
		Sat Jan 14 00:40:57     2006, Created by tomyeh

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zk.ui.WrongValueException;

/**
 * Space is a {@link Separator} with the orient default to "vertical".
 *
 * <p>In other words, &lt;space&gt; is equivalent to &lt;separator orient="vertical"&gt;
 *
 * @author tomyeh
 */
public class Space extends Separator {
	public Space() {
		try {
			setOrient("vertical");
		} catch (WrongValueException ex) {
			throw new InternalError(); //impossible
		}
	}
}
