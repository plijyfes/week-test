/* grid.js

	Purpose:
		
	Description:
		
	History:
		Tue Dec 23 15:24:01     2008, Created by jumperchen

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
function (out) {
	var uuid = this.uuid,
		zcls = this.getZclass(),
		innerWidth = this.getInnerWidth(),
		wdAttr = innerWidth == '100%' ? ' width="100%"' : '', //bug#3183182
		wdStyle = innerWidth != '100%' ? 'width:' + innerWidth : '',
		inPaging = this.inPagingMold(), pgpos;

	out.push('<div', this.domAttrs_(), '>');

	if (inPaging && this.paging) {
		pgpos = this.getPagingPosition();
		if (pgpos == 'top' || pgpos == 'both') {
			out.push('<div id="', uuid, '-pgit" class="', zcls, '-pgi-t">');
			this.paging.redraw(out);
			out.push('</div>');
		}
	}

	if (this.columns) {
		out.push('<div id="', uuid, '-head" class="', zcls, '-header">',
			'<table', wdAttr, zUtl.cellps0,
			' style="table-layout:fixed;', wdStyle,'">');
		this.domFaker_(out, '-hdfaker', zcls);
		
		for (var hds = this.heads, j = 0, len = hds.length; j < len;)
			hds[j++].redraw(out);
	
		out.push('</table></div><div class="', zcls, '-header-bg"></div>');
	}
	out.push('<div id="', uuid, '-body" class="', zcls, '-body');
	if (this._autopaging)
		out.push(' ', zcls, '-autopaging');
	out.push('"');

	var hgh = this.getHeight();
	if (hgh) out.push(' style="height:', hgh, '"');
	
	out.push('>');
	if (this.rows && this.domPad_ && !this.inPagingMold())
		this.domPad_(out, '-tpad');
	out.push('<table', wdAttr, zUtl.cellps0, ' style="table-layout:fixed;', wdStyle,'">');
	
	if (this.columns)
		this.domFaker_(out, '-bdfaker', zcls);

	if (this.rows)
		this.rows.redraw(out);
	
	this.redrawEmpty_(out);
	
	out.push('</table>');
	if (this.rows && this.domPad_ && !this.inPagingMold())
		this.domPad_(out, '-bpad');
	
	out.push('</div>');
	
	if (this.foot) {
		out.push('<div id="', uuid, '-foot" class="', zcls, '-footer">',
			'<table', wdAttr, zUtl.cellps0, ' style="table-layout:fixed;', wdStyle,'">');
		if (this.columns) 
			this.domFaker_(out, '-ftfaker', zcls);
			
		this.foot.redraw(out);
		out.push('</table></div>');
	}
	
	if (this.frozen) {
		out.push('<div id="', uuid, '-frozen" class="', zcls, '-frozen">');
		this.frozen.redraw(out);
		out.push('</div>');
	}
	
	if (pgpos == 'bottom' || pgpos == 'both') {
		out.push('<div id="', uuid, '-pgib" class="', zcls, '-pgi-b">');
		this.paging.redraw(out);
		out.push('</div>');
	}
	out.push('</div>');
}
