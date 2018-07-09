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
var timer;
var target = 0;
zk.$package('myImageSlider');
myImageSlider.ImageSlider = zk.$extends(zul.Widget, {
	_text : '', // default value for text attribute
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
		 * 
		 * If you don't get this , you could see the comment below for another
		 * way to do this.
		 * 
		 * It's more clear.
		 * 
		 */
		text : function() { // this function will be called after setText() .

			if (this.desktop) {
				// updated UI here.
			}
		},

		selectedIndex : function(selectedIndex) {
			var n = this.$n();
			if (n)
				n.selectedIndex = selectedIndex;
		},

		selectedItem : function(selectedItem) {
			var n = this.$n();
			if (n)
				n.selectedItem = selectedItem;
		},

		viewportSize : function(viewportSize) {
			var n = this.$n();
			if (n)
				n.viewportSize = viewportSize;
		},

		imageWidth : function(imageWidth) {
			var n = this.$n();
			if (n)
				n.imageWidth = imageWidth;
		},
	// sliderClass: [
	// function(v) {
	// return !v ? '' : v;
	// },
	// function() {
	// var n = this.$n;
	// if(n){
	// jq(n).addClass(this._sliderClass)
	// }
	// }
	// ]
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
		this.domListen_(document.getElementById(this.$s('leftButton')),
				"onClick", "_doItemsClick");
		this.domListen_(document.getElementById(this.$s('rightButton')),
				"onClick", "_doItemsClick");
	},
	/*
	 * A example for domListen_ listener.
	 */

	_doItemsClick : function(evt) {
		// alert("item click event fired");
//		console.log(evt.domTarget);
		var scrollDiv = document.getElementById(this.$s('scrollDiv'));

		if (timer) {
			clearInterval(timer);
		}
		if (evt.domTarget.id == this.$s('leftButton')) {
			if (target >= 200) {
				target -= 200;
			}
			if (scrollDiv.scrollLeft > 0) {
				timer = setInterval(function() {
					if (scrollDiv.scrollLeft > target) {
						scrollDiv.scrollLeft -= 4;
					} else {
						clearInterval(timer);
					}
				}, 10);
			}
		} else {
			if (target <= 200) {
				target += 200;
			}
			if (scrollDiv.scrollLeft < 400) {
				timer = setInterval(function() {
					if (scrollDiv.scrollLeft < target) {
						scrollDiv.scrollLeft += 4;
					} else {
						clearInterval(timer);
					}
				}, 10);
			}
		}
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
		this.$super('doClick_', evt, true);// the super doClick_ should be
											// called
		this.fire('onFoo', {
			foo : 'myData'
		});
	}
});