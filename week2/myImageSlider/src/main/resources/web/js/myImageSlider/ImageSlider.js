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
		
		selectedIndex : function(selectedIndex) {
			if (this.desktop) {
				var scrollpos = this.$n('scroll-div').scrollLeft;
				for (var i = 0; i < this.nChildren; i++) {
					this.$n('content').children[i].className = "z-imageslider-image";
				}
//				console.log(this.$n('content').children[selectedIndex])
				this.$n('content').children[selectedIndex].className = "z-imageslider-image-selected";
				if (selectedIndex * this.getImageWidth() < scrollpos){
					this.$n('scroll-div').scrollLeft = selectedIndex * this.getImageWidth();
				}else if (selectedIndex * this.getImageWidth() > scrollpos + (this.getViewportSize() * this.getImageWidth())) {
					this.$n('scroll-div').scrollLeft = (selectedIndex - this.getViewportSize() + 1) * this.getImageWidth();
				}
			}
		},

		viewportSize : function(viewportSize) {
			if (this.desktop) {
				this.$n().style.width = (this.getImageWidth() * viewportSize + 80) + "px;";
				if (viewportSize >= this.nChildren) {		
					this.$n('left-button').className = this.$s('left-button-d');
					this.$n('right-button').className = this.$s('left-button-d');
				} else {
					this.$n('left-button').className = this.$s('left-button');
					this.$n('right-button').className = this.$s('right-button');
				}
				this.$n('scroll-div').style.width = viewportSize * this.getImageWidth() + 'px';
			}
		},

		imageWidth : function(imageWidth) {
			if (this.desktop) {
				for (var i = 0; i < this.nChildren; i++) {
					this.$n('content').children[i].style = 'width:' + this.getImageWidth() + 'px;';
				}
				this.$n('content').style = "width:"+ (this.getImageWidth() * this.nChildren) + "px;";
				this.$n('scroll-div').style = "width:"+ (this.getViewportSize() * this.getImageWidth()) + "px;";
				if (this.getViewportSize() >= this.nChildren) {
					this.$n().style = "width:"+ (this.getImageWidth() * this.getViewportSize() + 80) + "px;";
				} else {
					this.$n().style = "width:"+ (this.getImageWidth() * this.getViewportSize() + 160) + "px;";
				}
			}
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
	/*
	 * A example for domListen_ listener.
	 */

	_doButtonClick : function(evt) {
		var scrollDiv = this.$n('scroll-div'),
		    imageWidth = this.getImageWidth(),
		    scrollLimit = imageWidth * (this.nChildren - this.getViewportSize()),
		    timer = this._timer,
		    target = this.getTarget();

		if (this.getTimer()) {
			clearInterval(timer);
		} 
		if (evt.domTarget == this.$n('left-button')) {
			if (target >= imageWidth) {
				this.setTarget(target - imageWidth);
			}
			if (scrollDiv.scrollLeft > 0) {
				this.setTimer(setInterval(function() {
					if (scrollDiv.scrollLeft > target) {
						scrollDiv.scrollLeft -= (0.02 * imageWidth);
					} else {
						clearInterval(timer);
					}
				}, 10));
			}
		} else {
			if (target <= scrollLimit - imageWidth) {
				this.setTarget(target + imageWidth);
			}
			if (scrollDiv.scrollLeft < scrollLimit) {
				this.setTimer(setInterval(function() {
					if (scrollDiv.scrollLeft < target) {
						scrollDiv.scrollLeft += (0.02 * imageWidth);
					} else {
						clearInterval(timer);
					}
				}, 10));
			}
		}
	},

	_doViewClick : function(evt) {
		if (evt.domTarget == this.$n('left-view')){
			if(this.getViewportSize() > 1){
				this.setViewportSize(this.getViewportSize() - 1);
			}
		} else {
			if(this.getViewportSize() < this.nChildren){
				this.setViewportSize(this.getViewportSize() + 1);
			}
		}
		this.fire("onClick", { click : this.getViewportSize() + '' }, {toServer : true});
	},

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
		this._doButtonClick(evt);
	} else if (evt.domTarget.className == 'z-image'){
		var contentposx = this.$n('content').offsetLeft;
		this.setSelectedIndex(Math.floor((evt.domTarget.offsetLeft-contentposx)/this.getImageWidth()));
		this.fire('onSelect', {selected : this.getSelectedIndex()});
	} 
 }
});