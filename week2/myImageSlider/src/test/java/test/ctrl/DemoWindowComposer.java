package test.ctrl;

import org.test.myImageSlider.ImageSlider;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;

public class DemoWindowComposer extends SelectorComposer {

	@Wire
	private ImageSlider myImageSlider;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}

	@Listen("onClick = #btn1")
	public void onClick$btn1() {
		Image img1 = new Image("/test_img/ironman-01.jpg");
		myImageSlider.appendChild(img1);
	}

	@Listen("onClick = #btn2")
	public void onClick$btn2() {
		myImageSlider.removeChild(myImageSlider.getLastChild());
		// myImageSlider.removeChild(myImageSlider.getFirstChild());
	}
}