/**
 * 
 * Base naming rule: The stuff start with "_" means private , end with "_" means
 * protect , others mean public.
 * 
 * All the member field should be private.
 * 
 * Life cycle: (It's very important to know when we bind the event) A widget
 * will do this by order : 1. $init 2. set attributes (setters) 3. rendering
 * mold (@see mold/myImageSlider.js ) 4. call bind_ to bind the event to dom .
 * 
 * this.deskop will be assigned after super bind_ is called, so we use it to
 * determine whether we need to update view manually in setter or not. If
 * this.desktop exist , means it's after mold rendering.
 * 
 */
zk.$package('myImageSlider');
myImageSlider.ImageSlider = zk.$extends(zul.Widget, {
	_viewportSize: 3,
	_target: 0,
	_timer: 0,
	_selectedIndex: -1,
	_imageWidth: 200,
	
	/**
	 * Don't use array/object as a member field, it's a restriction for ZK
	 * object, it will work like a static , share with all the same Widget class
	 * instance.
	 * 
	 * if you really need this , assign it in bind_ method to prevent any
	 * trouble.
	 * 
	 * TODO:check array or object , must be one of them ...I forgot. -_- by Tony
	 */

	$define : {
		/**
		 * The member in $define means that it has its own setter/getter. (It's
		 * a coding sugar.)
		 */
		target : function(timer) {
			
		},
		
		timer : function(timer) {
			
		},
		
		selectedIndex : function(selectedIndex) {
			if (this.desktop) {
				console.log(this.$n('content').children[selectedIndex-1].className)
				this.$n('content').children[selectedIndex-1].className = "z-imageslider-image-selected";
				console.log(this.$n('content').children[selectedIndex-1].className)
			}
		},

		viewportSize : function(viewportSize) {
			if (this.desktop) {
				this.$n().style = "width:"+ (this.$n().imageWidth * viewportSize + 80) + "px;";
				this.$n('scroll-div').style = "width:"+ viewportSize * this.$n().imageWidth + "px;";
			}
		},

		imageWidth : function(imageWidth) {
			var n = this.$n();
			if (n)
				n.imageWidth = imageWidth;
		},
	
	},
	/**
	 * If you don't like the way in $define , you could do the setter/getter by
	 * yourself here.
	 * 
	 * Like the example below, they are the same as we mentioned in $define
	 * section.
	 */
	/*
	 * getText:function(){ return this._text; }, setText:function(val){
	 * this._text = val; if(this.desktop){ //update the UI here. } },
	 */
	bind_ : function() {
		/**
		 * For widget lifecycle , the super bind_ should be called as FIRST
		 * STATEMENT in the function. DONT'T forget to call supers in bind_ , or
		 * you will get error.
		 */
		this.$supers(myImageSlider.ImageSlider, 'bind_', arguments);
		// A example for domListen_ , REMEMBER to do domUnlisten in unbind_.
	},
	/*
	 * A example for domListen_ listener.
	 */

	_doButtonClick : function(evt) {
		// alert("item click event fired");
		// console.log(evt.domTarget);
		var scrollDiv = this.$n('scroll-div'),
		    imageWidth = this.getImageWidth(),
		    scrollLimit = imageWidth * (this.nChildren - this.getViewportSize()),
		    timer = this._timer,
		    target = this.getTarget();
		
//		 console.log(scrollLimit);
		if (timer) {
			clearInterval(timer);
			return false;
		}
		if (evt.domTarget == this.$n('left-button')) {
			if (target >= imageWidth) {
				this.setTarget(target - imageWidth);
			}
			if (scrollDiv.scrollLeft > 0) {
				timer = setInterval(function() {
					if (scrollDiv.scrollLeft > target) {
						scrollDiv.scrollLeft -= (0.02 * imageWidth);
					} else {
						clearInterval(timer);
					}
				}, 10);
			}
		} else {
			if (target <= scrollLimit - imageWidth) {
				this.setTarget(target + imageWidth);
			}
			if (scrollDiv.scrollLeft < scrollLimit) {
				timer = setInterval(function() {
					if (scrollDiv.scrollLeft < target) {
						scrollDiv.scrollLeft += (0.02 * imageWidth);
					} else {
						clearInterval(timer);
					}
				}, 10);
			}
		}
	},

//	_doImageClick : function(evt) {
//		for (var i = 0; i < this.nChildren; i++) {
//			document.getElementById(this.$s('content')).children[i].style.border = "none";
//		}
//		this.setSelectedItem(evt.domTarget);
//		this.getSelectedItem().style.border = "2px green solid;";
//		zAu.send(new zk.Event(this, "onFoo", {
//			foo : this.getSelectedItem().src
//		}, {
//			toServer : true
//		}));
//
//	},

	unbind_ : function() {

		// A example for domUnlisten_ , should be paired with bind_
		this.domUnlisten_(this.$n("leftButton"), "onClick", "_doItemsClick");

		/*
		 * For widget lifecycle , the super unbind_ should be called as LAST
		 * STATEMENT in the function.
		 */
		this.$supers(myImageSlider.ImageSlider, 'unbind_', arguments);
	},
	/*
	 * widget event, more detail please refer to
	 * http://books.zkoss.org/wiki/ZK%20Client-side%20Reference/Notifications
	 */
 doClick_ : function(evt) {
	this.$super('doClick_', evt, true);// the super doClick_ should be called
	if (evt.domTarget == this.$n('left-button') || evt.domTarget == this.$n('right-button')) {
//		console.log(evt.domTarget);
		this._doButtonClick(evt);
	} 
	// make a selecter
  	this.fire('onSelect', {selected : this.getSelectedIndex()+''}, {toServer : true});
 }
});