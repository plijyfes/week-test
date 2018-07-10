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
		imageWidth = this.getImageWidth();

	// The this.domAttrs_() means it will prepare some dom attributes,
	// like the pseudo code below
	/*
	 * class="${zcls} ${this.getSclass()}" id="${uuid}"
	 */
		out.push('<div ', this.domAttrs_(), 'style="width:', imageWidth * viewportSize + 80, 'px;">'); 
		if (viewportSize > this.nChildren) {
			out.push('<div id="', uuid, '-left-button" class="', this.$s('left-button-d'), '"/>');
		} else {
			out.push('<div id="', uuid, '-left-button" class="', this.$s('left-button'), '"/>');
		}
		out.push('<div id="', uuid, '-scroll-div" class="', this.$s('scroll-div'), '" style="width:', imageWidth * viewportSize, 'px;">', 
				'<div id="', uuid, '-content" class="', this.$s('content'), '" style="width:', imageWidth * this.nChildren,'px;">');
		for (var w = this.firstChild; w; w=w.nextSibling){
			out.push('<div id="', uuid, '-image" class="', this.$s('image'), '">')
			w.redraw(out);
			out.push('</div>');
		}
		out.push('</div>', 
				'</div>');
		if (viewportSize > this.nChildren) {
			out.push('<div id="', uuid, '-right-button" class="', this.$s('right-button-d'), '"/>');
		} else {
			out.push('<div id="', uuid, '-right-button" class="', this.$s('right-button'), '"/>');
		}
	
	

}