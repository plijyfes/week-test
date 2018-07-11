/**
 * Here's the mold file , a mold means a HTML struct that the widget really
 * presented. yep, we build html in Javascript , that make it more clear and
 * powerful.
 */
function (out) {

	// Here you call the "this" means the widget instance. (@see ImageSlider.js)
	
	var zcls = this.getZclass(),
		uuid = this.uuid,
		viewportSize = this.getViewportSize(),
		imageWidth = this.getImageWidth(),
		selectedIndex = this.getSelectedIndex();

	// The this.domAttrs_() means it will prepare some dom attributes,
	// like the pseudo code below
	/*
	 * class="${zcls} ${this.getSclass()}" id="${uuid}"
	 */
		out.push('<div ', this.domAttrs_(), 'style="width:', imageWidth * viewportSize + 160, 'px;;height:', imageWidth, 'px;">'); 
		if (viewportSize > this.nChildren) {
			out.push('<div id="', uuid, '-left-button" class="', this.$s('left-button-d'), '"/>');
		} else {
			out.push('<div id="', uuid, '-left-button" class="', this.$s('left-button'), '"/>');
		}
		out.push('<div id="', uuid, '-left-view" class="', this.$s('left-view'), '"/>');
		out.push('<div id="', uuid, '-scroll-div" class="', this.$s('scroll-div'), '" style="width:', imageWidth * viewportSize, 'px;height:', imageWidth, 'px;">'); 
		out.push('<div id="', uuid, '-content" class="', this.$s('content'), '" style="width:', imageWidth * this.nChildren, 'px;;height:', imageWidth, 'px;">');
		for (var w = this.firstChild; w; w=w.nextSibling){
			out.push('<div id="', uuid, '-image" class="', this.$s('image'), '" style="width:', imageWidth, 'px;height:', imageWidth, 'px;">')
			w.redraw(out);
			out.push('</div>');
		}
		out.push('</div>', '</div>');
		out.push('<div id="', uuid, '-right-view" class="', this.$s('right-view'), '"/>');
		if (viewportSize > this.nChildren) {
			out.push('<div id="', uuid, '-right-button" class="', this.$s('right-button-d'), '"/>');
		} else {
			out.push('<div id="', uuid, '-right-button" class="', this.$s('right-button'), '"/>');
		}
	

}