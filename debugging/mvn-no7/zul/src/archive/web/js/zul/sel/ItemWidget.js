/* ItemWidget.js

	Purpose:
		
	Description:
		
	History:
		Fri May 22 21:50:50     2009, Created by tomyeh

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
(function () {
	function _toggleEffect(wgt, undo) {
		var self = wgt;
		setTimeout(function () {
			if (!self.desktop)
				return;// fixed for B50-3362731.zul
			
			var $n = jq(self.$n()),
				zcls = self.getZclass();
			if (undo) {
   				$n.removeClass(zcls + "-over-seld").removeClass(zcls + "-over");
   					//we have to remove both since _setSelectedDirectly doesn't
   					//remove -over-seld
			} else if (self._musin) {
				$n.addClass(self.isSelected() ? zcls + "-over-seld" : zcls + "-over");
				
				var mesh = self.getMeshWidget(),
					musout = mesh._musout;
				// fixed mouse-over issue for datebox 
				if (musout && $n[0] != musout.$n()) {
					jq(musout.$n()).removeClass(zcls + "-over-seld").removeClass(zcls + "-over");
					musout._musin = false;
					mesh._musout = null;
				}
			}
		});
	}
/**
 * The item widget for {@link Treeitem} and {@link Listitem}
 */
zul.sel.ItemWidget = zk.$extends(zul.Widget, {
	_checkable: true,
	$define: {
		/** Returns whether it is checkable.
		 * <p>Default: true.
		 * @return boolean
		 */
		/** Sets whether it is checkable.
		 * <p>Default: true.
		 * @param boolean checkable
		 */
		checkable: function () {
			if (this.desktop)
				this.rerender();
		},
		/** Returns whether it is disabled.
		 * <p>Default: false.
		 * @return boolean
		 */
		/** Sets whether it is disabled.
		 * @param boolean disabled
		 */
		disabled: function () {
			if (this.desktop)
				this.rerender();
		},
		/** Returns the value.
		 * <p>Default: null.
		 * <p>Note: the value is application dependent, you can place
		 * whatever value you want.
		 * <p>If you are using listitem/treeitem with HTML Form (and with
		 * the name attribute), it is better to specify a String-typed
		 * value.
		 * @return String
		 */
		/** Sets the value.
		 * @param String value the value.
		 * <p>Note: the value is application dependent, you can place
		 * whatever value you want.
		 * <p>If you are using listitem/treeitem with HTML Form (and with
		 * the name attribute), it is better to specify a String-typed
		 * value.
		 */
		value: null
	},
	/** Sets whether it is selected.
	 * @param boolean selected
	 */
	setSelected: function (selected) {
		if (this._selected != selected) {
			var box = this.getMeshWidget();
			if (box)
				box.toggleItemSelection(this);
				
			this._setSelectedDirectly(selected);
		}
	},
	_setSelectedDirectly: function (selected) {
		var n = this.$n();
		if (n) {
			jq(n)[selected ? 'addClass' : 'removeClass'](this.getZclass() + '-seld');
			this._updHeaderCM();
		}
		this._selected = selected;
	},
	/** Returns the label of the {@link Listcell} or {@link Treecell} it contains, or null
	 * if no such cell.
	 * @return String
	 */
	getLabel: function () {
		return this.firstChild ? this.firstChild.getLabel() : null; 
	},
	/** Returns whether it is selected.
	 * <p>Default: false.
	 * @return boolean
	 */
	isSelected: function () {
		return this._selected;
	},
	/**
	 * Returns whether is stripeable or not.
	 * <p>Default: true.
	 * @return boolean
	 */
	isStripeable_: function () {
		return true;
	},
	/**
	 * Returns the mesh widget.
	 * @return zul.mesh.MeshWidget
	 */
	getMeshWidget: function () {
		return this.parent;
	},
	_getVisibleChild: function (row) {
		for (var i = 0, j = row.cells.length; i < j; i++)
			if (zk(row.cells[i]).isVisible()) return row.cells[i];
		return row;
	},
	//super//
	setVisible: function (visible) {
		if (this._visible != visible) { // not to use isVisible()
			this.$supers('setVisible', arguments);
			if (this.isStripeable_()) {
				var p = this.getMeshWidget();
				if (p) p.stripe();
			}
		}
	},
	domClass_: function (no) {
		var scls = this.$supers('domClass_', arguments);
		if (!no || !no.zclass) {
			var zcls = this.getZclass();
			if (this.isDisabled())
				scls += (scls ? ' ': '') + zcls + '-disd';
			if (this.isSelected())
				scls += (scls ? ' ': '') + zcls + '-seld';
		}
		return scls;
	},
	// SelectWidget count on this function
	_toggleEffect: function (undo) {
		_toggleEffect(this, undo);
	},
	focus_: function (timeout) {
		var mesh = this.getMeshWidget();
			mesh._focusItem = this;
		this._doFocusIn();
		mesh._syncFocus(this);
		mesh.focusA_(mesh.$n('a'), timeout);
		return true;
	},
	_doFocusIn: function () {
		var n = this.$n();
		if (n)
			jq(this._getVisibleChild(n)).addClass(this.getZclass() + "-focus");
		
		if (n = this.getMeshWidget())
			n._focusItem = this;			
	},
	_doFocusOut: function () {
		var n = this.$n();
		if (n) {
			var zcls = this.getZclass();
			jq(n).removeClass(zcls + "-focus");
			jq(n.cells).removeClass(zcls + "-focus");
		}
	},
	_updHeaderCM: function (bRemove) { //update header's checkmark
		var box;
		if ((box = this.getMeshWidget()) && box._headercm && box._multiple) {
			if (bRemove) {
				box._updHeaderCM();
				return;
			}

			var zcls = zk.Widget.$(box._headercm).getZclass() + '-img-seld',
				$headercm = jq(box._headercm);

			if (!this.isSelected())
				$headercm.removeClass(zcls);
			else if (!$headercm.hasClass(zcls))
				box._updHeaderCM(); //update in batch since we have to examine one-by-one
		}
	},
	//@Override
	beforeParentChanged_: function (newp) {
		if (!newp) //remove
			this._updHeaderCM(true);
		this.$supers("beforeParentChanged_", arguments);
	},
	//@Override
	afterParentChanged_: function () {
		if (this.parent) //add
			this._updHeaderCM();
		this.$supers("afterParentChanged_", arguments);
	},

	// event
	doSelect_: function(evt) {
		if (this.isDisabled() || !this.isCheckable()) return;
		if (!evt.itemSelected) {
			this.getMeshWidget()._doItemSelect(this, evt);
			evt.itemSelected = true;
		}
		this.$supers('doSelect_', arguments);
	},
	doMouseOver_: function(evt) {
		if (this._musin || this.isDisabled()) return;
		this._musin = true;
		this._toggleEffect();
		evt.stop();
		this.$supers('doMouseOver_', arguments);
	},
	doMouseOut_: function(evt) {
		if (this.isDisabled() || (this._musin &&
					jq.isAncestor(this.$n(), evt.domEvent.relatedTarget ||
								evt.domEvent.toElement))) {
			// fixed mouse-over issue for datebox 
			this.getMeshWidget()._musout = this;
			return;
		}
		this._musin = false;
		this._toggleEffect(true);
		evt.stop({propagation: true});
		this.$supers('doMouseOut_', arguments);
	},
	doKeyDown_: function (evt) {
		var mate = this.getMeshWidget();
		if (!zk.gecko || !jq.nodeName(evt.domTarget, "input", "textarea"))
			zk(mate.$n()).disableSelection();
		mate._doKeyDown(evt);
		this.$supers('doKeyDown_', arguments);
	},
	doKeyUp_: function (evt) {
		var mate = this.getMeshWidget();
		zk(mate.$n()).enableSelection();
		mate._doKeyUp(evt);
		this.$supers('doKeyUp_', arguments);
	},
	deferRedrawHTML_: function (out) {
		out.push('<tr', this.domAttrs_({domClass:1}), ' class="z-renderdefer"></tr>');
	}
});
})();