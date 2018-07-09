/**
 * Here's the mold file , a mold means a HTML struct that the widget really
 * presented. yep, we build html in Javascript , that make it more clear and
 * powerful.
 */
function (out) {

	// Here you call the "this" means the widget instance. (@see ImageSlider.js)
	
	var zcls = this.getZclass(),
		uuid = this.uuid,
		viewportSize = this.getViewportSize();
	console.log(this.domAttrs_());

	// The this.domAttrs_() means it will prepare some dom attributes,
	// like the pseudo code below
	/*
	 * class="${zcls} ${this.getSclass()}" id="${uuid}"
	 */
// out.push('<div ', this.domAttrs_(), '>');
// for(var w = this.firstChild; w; w=w.nextSibling){
// w.redraw(out);
// }
// out.push('</div>');
	out.push('<div ', this.domAttrs_(), '>', '<image id="', this.$s('leftButton'), '" src="test_img/40_40_left_wb.PNG"/>', '<div id="', this.$s('scrollDiv'), '" style="width:', viewportSize, 'px;">', '<div class="', this.$s('content'), '">');
	for(var w = this.firstChild; w; w=w.nextSibling){
		w.redraw(out);
	}
	out.push('</div>', '</div>', '<image id="', this.$s('rightButton'), '" src="test_img/40_40_right_wb.PNG"/>');

}