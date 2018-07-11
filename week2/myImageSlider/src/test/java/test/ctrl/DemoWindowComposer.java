package test.ctrl;

import org.test.myImageSlider.ImageSlider;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;

public class DemoWindowComposer extends SelectorComposer {
	
	@Wire
	private ImageSlider myComp;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	@Listen("onClick = #btn1")
	public void onClick$btn1() {
		Image img1 = new Image("/test_img/40_40_right_wb.PNG");
		myComp.appendChild(img1);
	}
}