/* Errorbox.js

	Purpose:
		
	Description:
		
	History:
		Sun Jan 11 21:17:56     2009, Created by tomyeh

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
/**
 * A error message box that is displayed as a popup.
 */
zul.inp.Errorbox = zk.$extends(zul.wgt.Popup, {
	$init: function () {
		this.$supers('$init', arguments);
		this.setWidth("260px");
		this.setSclass('z-errbox');
	},
	getZclass: function () {
		return this._zclass == null ? 'z-popup' : this._zclass;
	},
	/** Opens the popup.
	 * @param zk.Widget owner the owner widget
	 * @param String msg the error message
	 * @see zul.wgt.Popup#open
	 */
	show: function (owner, msg) {
		this.parent = owner; //fake
		this.parent.__ebox = this;
		this.msg = msg;
		jq(document.body).append(this);

		// Fixed IE6/7 issue in B50-2941554.zul
		var self = this, cstp = owner._cst && owner._cst._pos;
		setTimeout(function() {
			if (self.parent) //Bug #3067998: if 
				self.open(owner, null, cstp || "end_before", 
						{dodgeRef: !cstp});
		}, 0);
		zWatch.listen({onHide: [this.parent, this.onParentHide]});
	},
	/** 
	 * Destroys the errorbox
	 */
	destroy: function () {
		if (this.parent) {
			zWatch.unlisten({onHide: [this.parent, this.onParentHide]});
			delete this.parent.__ebox;
		}
		this.close();
		this.unbind();
		jq(this).remove();
		this.parent = null;
	},
	onParentHide: function () {
		if (this.__ebox) {
			this.__ebox.setFloating_(false);
			this.__ebox.close();
		}
	},
	//super//
	bind_: function () {
		this.$supers(zul.inp.Errorbox, 'bind_', arguments);

		var Errorbox = zul.inp.Errorbox;
		this._drag = new zk.Draggable(this, null, {
			starteffect: zk.$void,
			endeffect: Errorbox._enddrag,
			ignoredrag: Errorbox._ignoredrag,
			change: Errorbox._change
		});
		zWatch.listen({onScroll: this});
	},
	unbind_: function () {
		// bug ZK-1143
		var drag = this._drag;
		this._drag = null;
		drag.destroy();
		zWatch.unlisten({onScroll: this});
		
		// just in case
		if (this.parent)
			zWatch.unlisten({onHide: [this.parent, this.onParentHide]});
		
		this.$supers(zul.inp.Errorbox, 'unbind_', arguments);
		this._drag = null;
	},
	/** Reset the position on scroll
	 * @param zk.Widget wgt
	 */
	onScroll: function (wgt) {
		if (wgt) { //scroll requires only if inside, say, borderlayout
			if (zk(this.parent).isScrollIntoView()) {// B65-ZK-1632
				this.position(this.parent, null, "end_before", {overflow:true});
				this._fixarrow();
			} else {
				this.close();
			}
		}
	},
	setDomVisible_: function (node, visible) {
		this.$supers('setDomVisible_', arguments);
		var stackup = this._stackup;
		if (stackup) stackup.style.display = visible ? '': 'none';
	},
	doMouseMove_: function (evt) {
		var el = evt.domTarget;
		if (el == this.$n('c')) {
			var y = evt.pageY,
				$el = jq(el),
				size = zk.parseInt($el.css('padding-right')),
				offs = $el.zk.revisedOffset();
			if (zul.inp.InputCtrl.isPreservedMouseMove(this))
				$el[y >= offs[1] && y < offs[1] + size ? 'addClass':'removeClass']('z-errbox-close-over');
		} else this.$supers('doMouseMove_', arguments);
	},
	doMouseOut_: function (evt) {
		var el = evt.domTarget;
		if (el == this.$n('c'))
			jq(el).removeClass('z-errbox-close-over');
		else
			this.$supers('doMouseOut_', arguments);
	},
	doClick_: function (evt) {
		var p = evt.domTarget;
		if (p == this.$n('c')) {
			if ((p = this.parent) && p.clearErrorMessage) {
				p.clearErrorMessage(true, true);
				p.focus(0); // Bug #3159848
			} else
				zAu.wrongValue_(p, false);
		} else {
			this.$supers('doClick_', arguments);
			this.parent.focus(0);
		}
	},
	open: function () {
		this.$supers('open', arguments);
		this.setTopmost();
		this._fixarrow();
	},
	prologHTML_: function (out) {
		var id = this.uuid;
		out.push('<div id="', id);
		out.push('-a" class="z-errbox-left z-arrow" title="')
		out.push(zUtl.encodeXML(msgzk.GOTO_ERROR_FIELD));
		out.push('"><div id="', id, '-c" class="z-errbox-right z-errbox-close"><div class="z-errbox-center">');
		out.push(zUtl.encodeXML(this.msg, {multiline:true})); //Bug 1463668: security
		out.push('</div></div></div>');
	},
	onFloatUp: function (ctl) {
		var wgt = ctl.origin;
		if (wgt == this) {
			this.setTopmost();
			return;
		}
		if (!wgt || wgt == this.parent || !this.isVisible())
			return;

		var top1 = this, top2 = wgt;
		while ((top1 = top1.parent) && !top1.isFloating_())
			if (top1 == wgt) //wgt is parent
				return;
		for (; top2 && !top2.isFloating_(); top2 = top2.parent)
			;
		if (top1 == top2) { //uncover if sibling
			var n = wgt.$n();
			if (n) this._uncover(n);
		}
	},
	_uncover: function (el) {
		var elofs = zk(el).cmOffset(),
			node = this.$n(),
			nodeofs = zk(node).cmOffset();

		if (jq.isOverlapped(
		elofs, [el.offsetWidth, el.offsetHeight],
		nodeofs, [node.offsetWidth, node.offsetHeight])) {
			var parent = this.parent.$n(), y;
			var ptofs = zk(parent).cmOffset(),
				pthgh = parent.offsetHeight,
				ptbtm = ptofs[1] + pthgh;
			y = elofs[1] + el.offsetHeight <=  ptbtm ? ptbtm: ptofs[1] - node.offsetHeight;
				//we compare bottom because default is located below

			var ofs = zk(node).toStyleOffset(0, y);
			node.style.top = ofs[1] + "px";
			this._fixarrow();
		}
	},
	_fixarrow: function () {
		var parent = this.parent.$n(),
			node = this.$n(),
			pointer = this.$n('p'),
			ptofs = zk(parent).revisedOffset(),
			nodeofs = zk(node).revisedOffset(),
			dx = nodeofs[0] - ptofs[0], 
			dy = nodeofs[1] - ptofs[1], 
			dir;
		
		// conditions of direction
		if (dx >= parent.offsetWidth - 22)
			dir = dy < 6 - node.offsetHeight ? "ld": dy >= parent.offsetHeight - 7 ? "lu": "l";
		else if (dx < 20 - node.offsetWidth)
			dir = dy < 6 - node.offsetHeight ? "rd": dy >= parent.offsetHeight - 7 ? "ru": "r";
		else
			dir = dy < 0 ? "d": "u";
		
		// for setting the pointer position
		if(dir == "d" || dir == "u") {
			var md = (Math.max(dx, 0) + Math.min(node.offsetWidth + dx, parent.offsetWidth))/2 - dx - 6,
				mx = node.offsetWidth - 11;
			pointer.style.left = (md > mx ? mx : md < 1 ? 1 : md) + "px";
			if(dir == "d") { 
				pointer.style.top = null;
				pointer.style.bottom = zk.ie6_? "-14px" : "-5px"; 
			} else 
				pointer.style.top = "-5px";
			
		} else if(dir == "l" || dir == "r") {
			var md = (Math.max(dy, 0) + Math.min(node.offsetHeight + dy, parent.offsetHeight))/2 - dy - 6,
				mx = node.offsetHeight - 11;
			pointer.style.top = (md > mx ? mx : md < 1 ? 1 : md) + "px";
			if(dir == "r") { 
				pointer.style.left = null;
				pointer.style.right = "-5px"; 
			} else
				pointer.style.left = "-5px";
			
		} else {
			if(dir == "lu" || dir== "ld")
				pointer.style.left = "5px";
			else {
				pointer.style.left = null;
				pointer.style.right = "5px";
			}
			if(dir == "ru" || dir == "lu")
				pointer.style.top = "-10px";
			else {
				pointer.style.top = null;
				pointer.style.bottom = zk.ie6_? "-14px" : "-10px";
			}
		}
		
		pointer.className = 'z-pointer z-pointer-' + dir;
	}
},{
	_enddrag: function (dg) {
		var errbox = dg.control;
		errbox.setTopmost();
		errbox._fixarrow();
	},
	_ignoredrag: function (dg, pointer, evt) {
		return zul.inp.InputCtrl.isIgnoredDragForErrorbox(dg, pointer, evt);
	},
	_change: function (dg) {
		var errbox = dg.control,
			stackup = errbox._stackup;
		if (stackup) {
			var el = errbox.$n();
			stackup.style.top = el.style.top;
			stackup.style.left = el.style.left;
		}
		errbox._fixarrow();
	}
});
