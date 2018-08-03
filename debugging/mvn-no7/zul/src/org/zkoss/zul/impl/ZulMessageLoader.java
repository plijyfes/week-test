/* ZulMessageLoader.java

{{IS_NOTE
 Purpose:
  
 Description:
  
 History:
  Feb 9, 2012 11:48:53 AM , Created by simonpai
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zul.impl;

import java.io.IOException;

import org.zkoss.zk.device.Devices;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.metainfo.MessageLoader;

/**
 * ZUL implementation of MessageLoader
 * @author simonpai
 * @since 5.0.11
 */
public class ZulMessageLoader implements MessageLoader {
	
	public void load(StringBuffer out, Execution exec) throws IOException {
		out.append(Devices.loadJavaScript(exec, "~./js/zul/lang/msgzul*.js"));
		out.append(Utils.outLocaleJavaScript());
	}	
}