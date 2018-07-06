
ImageSlider.ImageSlider = zk.$extends(zul.Widget, {
	_text:'', 
	
	
	
	$define: {
		
		text: function() { 
		
			if(this.desktop) {
				
			}
		}
	},
	
	
	bind_: function () {
		
		this.$supers(ImageSlider.ImageSlider,'bind_', arguments);
	
		
		
	},
	
	
	unbind_: function () {
	
		
		
		
		
		this.$supers(ImageSlider.ImageSlider,'unbind_', arguments);
	},
	
	doClick_: function (evt) {
		this.$super('doClick_', evt, true);
		this.fire('onFoo', {foo: 'myData'});
	}
});