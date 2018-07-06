
function (out) {

	

	var zcls = this.getZclass(),
		uuid = this.uuid;

	
	
	
	out.push('<span ', this.domAttrs_(), '>');
	out.push(this._text);
	out.push('</span>');

}