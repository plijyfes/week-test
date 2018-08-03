/* No1Composer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jan 28, 2011 12:51:15 PM , Created by jumperchen
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package testzk;

import java.util.Iterator;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;

/**
 * @author jumperchen
 */
public class No1Composer extends GenericForwardComposer {
	Button btn;
	Listbox listbox;

	public void onClick$btn(Event evt) {
//		for (Iterator it = listbox.getChildren().iterator(); it.hasNext();)
//			listbox.getChildren().remove(it.next());
//		
//		int times = listbox.getChildren().size();
//		for (int i = 0; i < times; i++)
//			listbox.getChildren().remove(listbox.getChildren().get(0));
//		
		Iterator it = listbox.getChildren().iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
	}
}
