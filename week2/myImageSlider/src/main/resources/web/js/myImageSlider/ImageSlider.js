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
	_selectedIndex: -1,
	_imageWidth: 200,
	_target: 0,
//	_timer: clearInterval(0), // not need
	
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
			if (this.desktop && selectedIndex != -1) {
				var scrollpos = this.$n('scroll-div').scrollLeft;
				this._clearSelectedView();
				this.$n('content').children[selectedIndex].className = this.$s('image-selected');
				if(!(scrollpos < selectedIndex * this.getImageWidth() && 
						selectedIndex * this.getImageWidth() < scrollpos + this._viewportSize * this._imageWidth)) {
					this.$n('scroll-div').scrollLeft = selectedIndex * this.getImageWidth();
				}
				this._target = this.$n('scroll-div').scrollLeft;
			} else if (selectedIndex == -1){
			    this._clearSelectedView();
				// -1 的情形
			}
		},

		viewportSize : function (viewportSize) {
			if (this.desktop) {
				var fullview = (viewportSize >= this.nChildren);
				this.$n().style = "width:" + (this.getImageWidth() * viewportSize + (fullview ? 0 : 80)) + 'px;';			
				this.$n('left-button').className = this.$s('left-button' + (fullview ? '-d' : ''));
				this.$n('right-button').className = this.$s('right-button' + (fullview ? '-d' : ''));
				this.$n('scroll-div').style = "width:" + viewportSize * this.getImageWidth() + 'px;';
			}
		},

		imageWidth : function (imageWidth) {
			if (this.desktop) {
				for (var i = 0; i < this.nChildren; i++) {
					this.$n('content').children[i].style = 'width:' + this.getImageWidth() + 'px;';
				}
				this.$n('content').style = "width:" + (this.getImageWidth() * this.nChildren) + 'px;';
				this.$n('scroll-div').style = "width:" + (this.getViewportSize() * this.getImageWidth()) + 'px;';	
				this.$n().style = "width:" + (this.getImageWidth() * this.getViewportSize() + ((this.getViewportSize() >= this.nChildren) ? 0 : 80)) + 'px;';
				this.$n('scroll-div').scrollLeft = Math.floor(this.$n('scroll-div').scrollLeft / this._imageWidth) * this._imageWidth;
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

	_doScrollClick : function(evt) { // 重構
		var scrollDiv = this.$n('scroll-div'),
		    imageWidth = this.getImageWidth(),
		    scrollLimit = imageWidth * (this.nChildren - this.getViewportSize()),
		    target = this._target,
		    self = this;

        if (self._timer) {
        	clearInterval(self._timer);
        }

		if (evt.domTarget == this.$n('left-button')) {
			if (target >= imageWidth) {
				this._target -= imageWidth;
			}
			if (scrollDiv.scrollLeft > 0) {
				this._timer = setInterval(function() { // 閉包 self = this 放外面
					if (scrollDiv.scrollLeft > target) {
						scrollDiv.scrollLeft -= (0.02 * imageWidth);
					} else {
						clearInterval(self._timer);
					}
				}, 10);
			}
		} else {
			if (target <= scrollLimit - imageWidth) {
				this._target += imageWidth;
			}
			if (scrollDiv.scrollLeft < scrollLimit) {
				this._timer = setInterval(function() {
					if (scrollDiv.scrollLeft < target) {
						scrollDiv.scrollLeft += (0.02 * imageWidth);
					} else {
						clearInterval(self._timer);
					}
				}, 10);
			}
		}
	},

	/*
	 * widget event, more detail please refer to
	 * http://books.zkoss.org/wiki/ZK%20Client-side%20Reference/Notifications
	 */
	 doClick_ : function(evt) {
		this.$super('doClick_', evt, true);// the super doClick_ should be called
		if (evt.domTarget == this.$n('left-button') || evt.domTarget == this.$n('right-button')) {
			this._doScrollClick(evt);
		} else if (evt.domTarget.className == 'z-image'){ // 先拿衣服確認是否是衣服
			this.setSelectedIndex(evt.currentTarget.getChildIndex());
			this.fire('onSelect', {selected : this.getSelectedIndex()});
		} 
 },
	
 	removeChild: function (child) {
 		this.$supers('removeChild', arguments);
 		if(this._selectedIndex == child.getChildIndex()){
 			this.setSelectedIndex(-1);
 		}
		jq(this.$n('content').children[child.getChildIndex()]).remove();
		this.$n('content').style = "width:" + (this.getImageWidth() * this.$n('content').children.length) + 'px;';
		if(this._viewportSize == this.nChildren + 1) {
		    this.setViewportSize(this.nChildren);
		}
	},
	
	insertChildHTML_: function (child, before, desktop) {
		divforchild = document.createElement('div');
		divforchild.id = child.uuid;
		divforchild.className = this.$s('image')
		divforchild.style = 'width:' + this._imageWidth + 'px; height:' + this._imageWidth + 'px;'
		divforchild.innerHTML = child.redrawHTML_(); //ZK buffer 串DIV字串  out 給child.redraw 參考Box.js
		this.$n('content').style = "width:" + (this.getImageWidth() * this.nChildren) + 'px;';
		this.$n('content').appendChild(divforchild);
//		this.$n('content').insertBefore(divforchild, (before) ? before.$n() : null); // before.$n() is not a Node
		child.bind(desktop);
		this._resetViewport();
	},// to do: 新增物件多次完處理一次畫面更新

	_resetViewport:function() {
	    var fullview = (this._viewportSize >= this.nChildren);
        this.$n().style = "width:" + (this.getImageWidth() * this._viewportSize + (fullview ? 0 : 80)) + 'px;';
        this.$n('left-button').className = this.$s('left-button' + (fullview ? '-d' : ''));
        this.$n('right-button').className = this.$s('right-button' + (fullview ? '-d' : ''));
        this.$n('scroll-div').style = "width:" + this._viewportSize * this.getImageWidth() + 'px;';
	},

	_clearSelectedView:function() {
	    for (var i = 0; i < this.nChildren; i++) {
            this.$n('content').children[i].className = this.$s('image');
        }
	}
});