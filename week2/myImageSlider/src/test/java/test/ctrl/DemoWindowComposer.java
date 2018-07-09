package test.ctrl;

import org.test.myImageSlider.ImageSlider;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;

public class DemoWindowComposer extends SelectorComposer {
	
	@Wire
	private ImageSlider myComp;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
//		myComp.setText("Hello ZK Component!! Please click me.");
		myComp.setViewportSize(600);
		System.out.println(myComp.getViewportSize());
	}
	
	public void onFoo$myComp (ForwardEvent event) {
		Event mouseEvent = (Event) event.getOrigin();
		System.out.println("onFOO");
		alert("You listen onFoo: " + mouseEvent.getTarget());
	}
}