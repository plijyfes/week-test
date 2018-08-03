/* Treefoot.js

	Purpose:
		
	Description:
		
	History:
		Wed Jun 10 15:32:41     2009, Created by jumperchen

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
/**
 * A row of {@link Treefooter}.
 *
 * <p>Like {@link Treecols}, each tree has at most one {@link Treefoot}.
 * <p>Default {@link #getZclass}: z-treefoot
 */
zul.sel.Treefoot = zk.$extends(zul.Widget, {
	/** Returns the tree that it belongs to.
	 * @return Tree
	 */
	getTree: function () {
		return this.parent;
	},
	//bug #3014664
	setVflex: function (v) { //vflex ignored for Treefoot
		v = false;
		this.$super(zul.sel.Treefoot, 'setVflex', v);
	},
	//bug #3014664
	setHflex: function (v) { //hflex ignored for Treefoot
		v = false;
		this.$super(zul.sel.Treefoot, 'setHflex', v);
	},
	deferRedrawHTML_: function (out) {
		out.push('<tr', this.domAttrs_({domClass:1}), ' class="z-renderdefer"></tr>');
	}
});
