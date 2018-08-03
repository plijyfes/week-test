/* SortWidget.js

	Purpose:
		
	Description:
		
	History:
		Tue May 26 14:51:17     2009, Created by jumperchen

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
/**
 * A skeletal implementation for a sortable widget.
 */
zul.mesh.SortWidget = zk.$extends(zul.mesh.HeaderWidget, {
	_sortDirection: "natural",
	_sortAscending: "none",
	_sortDescending: "none",

	$define: {
    	/** Returns the sort direction.
    	 * <p>Default: "natural".
    	 * @return String
    	 */
    	/** Sets the sort direction. This does not sort the data, it only serves
    	 * as an indicator as to how the widget is sorted.
    	 *
    	 * <p>If you use {@link #sort(String, jq.Event)} to sort rows,
    	 * the sort direction is maintained automatically.
    	 * If you want to sort it in customized way, you have to set the
    	 * sort direction manually.
    	 *
    	 * @param String sortDir one of "ascending", "descending" and "natural"
    	 */
		sortDirection: function (v) {
			var n = this.$n();
			if (n) {
				var zcls = this.getZclass(),
					$n = jq(n);
				$n.removeClass(zcls + "-sort-dsc").removeClass(zcls + "-sort-asc").addClass(zcls + "-sort");
				switch (v) {
				case "ascending":
					$n.addClass(zcls + "-sort-asc");
					break;
				case "descending":
					$n.addClass(zcls + "-sort-dsc");
				}
			}
		},
		/** Returns the ascending sorter, or null if not available.
		 * @return String
		 */
		/** Sets the ascending sorter with "client", "auto", or null for
		 * no sorter for the ascending order.
		 * @param String sortAscending
		 */
		sortAscending: function (v) {
			if (!v) this._sortAscending = v = "none";
			var n = this.$n(),
				zcls = this.getZclass();
			if (n) {
				var $n = jq(n);
				if (v == "none") {
					$n.removeClass(zcls + "-sort-asc");
					if (this._sortDescending == "none")
						$n.removeClass(zcls + "-sort");					
				} else
					$n.addClass(zcls + "-sort");
			}
		},
		/** Returns the descending sorter, or null if not available.
		 * @return String
		 */
		/** Sets the descending sorter with "client", "auto", or null for
		 * no sorter for the descending order.
		 * @param String sortDescending
		 */
		sortDescending: function (v) {
			if (!v) this._sortDescending = v = "none";
			var n = this.$n(),
				zcls = this.getZclass();
			if (n) {
				var $n = jq(n);
				if (v == "none") {
					$n.removeClass(zcls + "-sort-dsc");
					if (this._sortAscending == "none")
						$n.removeClass(zcls + "-sort");					
				} else
					$n.addClass(zcls + "-sort");
			}
		}
	},
	$init: function () {
		this.$supers('$init', arguments);
		this.listen({onSort: this}, -1000);
	},
	/** Sets the type of the sorter.
	 * You might specify either "auto", "client", or "none".
	 *
	 * <p>If "client" or "client(number)" is specified,
	 * the sort functionality will be done by Javascript at client without notifying
	 * to server, that is, the order of the component in the row is out of sync.
	 * <ul>
	 * <li> "client" : it is treated by a string</li>
	 * <li> "client(number)" : it is treated by a number</li>
	 * </ul>
	 * <p>Note: client sorting cannot work in model case.
	 * @param String type
	 */
	setSort: function (type) {
		if (type && type.startsWith('client')) {
			this.setSortAscending(type);
			this.setSortDescending(type);
		} else {
			this.setSortAscending('none');
			this.setSortDescending('none');
		}
	},
	isSortable_: function () {
		return this._sortAscending != "none" || this._sortDescending != "none";
	},
	/**
	 * Sorts the data.
	 * @param String ascending
	 * @param jq.Event evt
	 * @return boolean
	 * @disable(zkgwt)
	 */
	sort: function (ascending, evt) {
		if (!this.checkClientSort_(ascending))
			return false;
		
		evt.stop();
		
		this.replaceCavedChildrenInOrder_(ascending);
		
		return true;
	},
	/** Check the status whether can be sort in client side.
	 * @param String ascending
	 * @return boolean
	 * @since 5.0.6
	 * @see #sort
	 */
	checkClientSort_: function (ascending) {
		var dir = this.getSortDirection();
		if (ascending) {
			if ("ascending" == dir) return false;
		} else {
			if ("descending" == dir) return false;
		}

		var sorter = ascending ? this._sortAscending: this._sortDescending;
		if (sorter == "fromServer")
			return false;
		else if (sorter == "none") {
			evt.stop();
			return false;
		}
		
		var mesh = this.getMeshWidget();
		if (!mesh || mesh.isModel()) return false;
			// if in model, the sort should be done by server
			
		return true;
	},
	/** Replaced the child widgets with the specified order.
	 * @param String ascending
	 * @since 5.0.6
	 * @see #sort
	 */
	replaceCavedChildrenInOrder_: function (ascending) {
		var mesh = this.getMeshWidget(),
			body = this.getMeshBody(),
			dir = this.getSortDirection(),
			sorter = ascending ? this._sortAscending: this._sortDescending,
			desktop = body.desktop,
			node = body.$n();
		try {
			body.unbind();
			var d = [], col = this.getChildIndex();
			for (var i = 0, z = 0, it = mesh.getBodyWidgetIterator(), w; (w = it.next()); z++) 
				for (var k = 0, cell = w.firstChild; cell; cell = cell.nextSibling, k++) 
					if (k == col) {
						d[i++] = {
							wgt: cell,
							index: z
						};
					}
			
			var dsc = dir == "ascending" ? -1 : 1, fn = this.sorting, isNumber = sorter == "client(number)";
			d.sort(function(a, b) {
				var v = fn(a.wgt, b.wgt, isNumber) * dsc;
				if (v == 0) {
					v = (a.index < b.index ? -1 : 1);
				}
				return v;
			});
			for (var i = 0, k = d.length; i < k; i++) {
				body.appendChild(d[i].wgt.parent);
			}
			this._fixDirection(ascending);
			
		} finally {
			body.replaceHTML(node, desktop);
		}
	},
	/**
	 * The default implementation to compare the data.
     * @param Object o1 the first object to be compared.
     * @param Object o2 the second object to be compared.
     * @param boolean isNumber
     * @return int
	 */
	sorting: function(a, b, isNumber) {
		var v1, v2;
		if (typeof a.getLabel == 'function')
			v1 = a.getLabel();
		else if (typeof a.getValue == 'function')
			v1 = a.getValue();
		else v1 = a;
		
		if (typeof b.getLabel == 'function')
			v2 = b.getLabel();
		else if (typeof b.getValue == 'function')
			v2 = b.getValue();
		else v2 = b;
		
		if (isNumber) return v1 - v2;
		return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
	},
	_fixDirection: function (ascending) {
		//maintain
		for (var w = this.parent.firstChild; w; w = w.nextSibling) {
			w.setSortDirection(
				w != this ? "natural": ascending ? "ascending": "descending");
		}
	},
	onSort: function (evt) {
		var dir = this.getSortDirection();
		if ("ascending" == dir) this.sort(false, evt);
		else if ("descending" == dir) this.sort(true, evt);
		else if (!this.sort(true, evt)) this.sort(false, evt);
	},
	domClass_: function (no) {
		var scls = this.$supers('domClass_', arguments);
		if (!no || !no.zclass) {
			var zcls = this.getZclass(),
				added;
			if (this._sortAscending != "none" || this._sortDescending != "none") {
				switch (this._sortDirection) {
				case "ascending":
					added = zcls + "-sort " + zcls + "-sort-asc";
					break;
				case "descending":
					added = zcls + "-sort " + zcls + "-sort-dsc";
					break;
				default: // "natural"
					added = zcls + "-sort";
					break;
				}
			}
			return scls != null ? scls + (added ? ' ' + added : '') : added || '';
		}
		return scls;
	},
	getColumnMenuPopup_: zk.$void,
	_doMenuClick: function (evt) {
		if (this.parent._menupopup && this.parent._menupopup != 'none') {
			var pp = this.parent._menupopup,
				n = this.$n(),
				btn = this.$n('btn'),
				zcls = this.getZclass();
				
			jq(n).addClass(zcls + "-visi");
			
			if (pp == 'auto' && this.parent._mpop)
				pp = this.parent._mpop;
			else
				pp = this.$f(this.parent._menupopup);

			if (zul.menu.Menupopup.isInstance(pp)) {
				var ofs = zk(btn).revisedOffset(),
					asc = this.getSortAscending() != 'none',
					desc = this.getSortDescending() != 'none',
					mw = this.getMeshWidget();
				if (pp.$instanceof(zul.mesh.ColumnMenupopup)) {
					pp.getAscitem().setVisible(asc);
					pp.getDescitem().setVisible(desc);
					var model = mw.getModel();
					if (zk.feature.pe && pp.getGroupitem()) {
						if (model == 'group' || !model || this.isListen('onGroup', {asapOnly: 1}))
							pp.getGroupitem().setVisible((asc || desc));
						else
							pp.getGroupitem().setVisible(false);
					}
					if (zk.feature.ee && pp.getUngroupitem()) {
						var visible = !model || this.isListen('onUngroup', {asapOnly: 1});
						pp.getUngroupitem().setVisible(visible && mw.hasGroup());
					}
					
					var sep = pp.getDescitem().nextSibling;
					if (sep) 
						sep.setVisible((asc || desc));
				} else {
					pp.listen({onOpen: [this.parent, this.parent._onMenuPopup]});
				}
				pp.open(btn, [ofs[0], ofs[1] + btn.offsetHeight - 4], null, {sendOnOpen: true});
			}
			evt.stop(); // avoid onSort event.
		}
	}
});