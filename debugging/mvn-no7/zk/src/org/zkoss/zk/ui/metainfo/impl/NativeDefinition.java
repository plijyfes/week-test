/* NativeDefinition.java

	Purpose:
		
	Description:
		
	History:
		Thu Aug 16 12:33:49     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.metainfo.impl;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.metainfo.*;

/**
 * The component definition for the native components.
 * It is used to implement the native namespace
 *
 * @author tomyeh
 * @since 3.0.0
 * @see ComponentDefinitionImpl#newNativeDefinition
 */
/*package*/ class NativeDefinition extends ComponentDefinitionImpl {
	/*package*/ NativeDefinition(LanguageDefinition langdef, String name,
	Class<? extends Component> cls) {
		super(langdef, null, name, cls);

		if (cls == null)
			throw new IllegalArgumentException("null");
	}

	public boolean isNative() {
		return true;
	}
}
