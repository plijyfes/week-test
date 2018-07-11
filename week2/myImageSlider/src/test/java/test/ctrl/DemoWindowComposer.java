package test.ctrl;

import org.test.myImageSlider.ImageSlider;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

public class DemoWindowComposer extends SelectorComposer {
	
	@Wire
	private ImageSlider myComp;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		myComp.setViewportSize(3);
		myComp.setImageWidth(200);
		myComp.setSelectedIndex(4);
	}

}