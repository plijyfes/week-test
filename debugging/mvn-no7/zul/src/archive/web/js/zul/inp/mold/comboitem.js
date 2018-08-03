/* comboitem.js

	Purpose:
		
	Description:
		
	History:
		Sun Mar 29 20:59:06     2009, Created by tomyeh

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
function (out) {
	var zcls = this.getZclass();
	// ZK-945
	// fine tune the style of image's td,
	// add spacer so can customize easier
	out.push('<tr', this.domAttrs_({text:true}), '><td class="',
		zcls, '-img">', this.domImage_(), '</td><td class="',
		zcls, '-text"><span class="',
		zcls, '-spacer">&nbsp;</span>', this.domLabel_());

	var v;
	if (v = this._description)
		out.push('<br/><span class="', zcls, '-inner">',
			zUtl.encodeXML(v), '</span>');
	if (v = this._content)
		out.push('<span class="', zcls, '-cnt">', v, '</span>');

	out.push('</td></tr>');
}
