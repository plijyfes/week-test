/* math.js

	Purpose:
		
	Description:
		
	History:
		Sun Dec 14 17:16:17     2008, Created by tomyeh

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
/** A big decimal.
 * @disable(zkgwt)
 */
zk.BigDecimal = zk.$extends(zk.Object, {
	_precision: 0,
	$define: {
		/** Returns the precision.
		 * <p>Default: 0
		 * @return int
		 */
		/** Sets the precision
		 * @param int precision the precision
		 */
		precision: null
	},
	/** Constructor.
	 * @param Object value a number or a string
	 */
	$init: function (value) {
		value = value ? '' + value: '0';
		var jdot = -1;
		for (var j = 0, len = value.length; j < len; ++j) {
			var cc = value.charAt(j);
			if (((cc < '0' || cc > '9') && cc != '-' && cc != '+') || 
				(j && (cc == '-' || cc == '+')))
				if (jdot < 0 && cc == '.') {
					jdot = j;
				} else {
					value = value.substring(0, j);
					break;
				}
		}
		if (jdot >= 0) {
			value = value.substring(0, jdot) + value.substring(jdot + 1);
			this._precision = value.length - jdot;
			this._dot = true;
		}
		this._value = value;
	},
	$toNumber: function () {
		var v = parseFloat(this._value), p;
		if (p = this._precision)
			v /= Math.pow(10, p);
		return v;
	},
	/** Returns a string for this big decimal (per the original form).
	 * To have a Locale-dependent string, use {@link #$toLocaleString}
	 * instead.
	 * @return String
	 */
	$toString: function() { //toString is reserved keyword for IE
		if (this._value.length == 0) return ''; 
		var j = this._value.length - this._precision,
			valFixed = '';
		if (j < 0)
			for(var len = -j; len-- > 0;)
				valFixed += '0';
		return this._value.substring(0, j) + (this._dot || this._precision ? '.' + valFixed + this._value.substring(j) : '');
	},
	/** Returns a Locale-dependent string for this big decimal(for human's eye).
	 * @return String
	 */
	$toLocaleString: function() { //toLocaleString is reserved keyword for IE
		if (this._value.length == 0) return ''; 
		var j = this._value.length - this._precision;
		if (j <= 0) {
			var valFixed = '';
			for(var len = -j; len-- > 0;)
				valFixed += '0';
			return '0' + (this._precision ? zk.DECIMAL + valFixed + this._value : '');
		}
		return this._value.substring(0, j) + (this._precision ? zk.DECIMAL + this._value.substring(j) : '');
	}
});

/** A long integer.
 * @disable(zkgwt)
 */
zk.Long = zk.$extends(zk.Object, {
	/** Constructor.
	 * @param Object value a number or a string
	 */
	$init: function (value) {
	//Note: it shall work like parseInt:
	//1) consider '.' rather than zkDecimal
	//2) ignore unrecognized characters
		value = value ? '' + value: '0';
		var len = value.length;
		for (var j = 0; j < len; ++j) {
			var cc = value.charAt(j);
			if ((cc < '0' || cc > '9') && (j > 0 || (cc != '-' && cc != '+'))) {
				value = value.substring(0, j);
				break;
			}
		}
		if(len == 1) {
			var c = value.charAt(0);
			if(cc < '0' || cc > '9')
				value = 'NaN';
		}
		this._value = value;
	},
	/** Scales the number as value * 10 ^ digits.
	 * @param int digits the number of digits to scale.
	 * If zero, nothing changed.
	 * @since 5.0.10.
	 */
	scale: function (digits) {
		var val = this._value||'',
			n = val.length;
		if (n)
			if (digits > 0) {
				if (n > 1 || val.charAt(0) != '0')
					while (digits-- > 0) //in case if digits is not an integer
						val += '0';
			} else if (digits < 0)
				this._value = (n += digits) <= 0 ? '0': val.substring(0, n);
	},
	$toNumber: function () {
		return parseFloat(this._value)
	},
	/** Returns a string for this long integer
	 * To have a Locale-dependent string, use {@link #$toLocaleString}
	 * instead.
	 * @return String
	 */
	$toString: zkf = function() { //toString is reserved keyword for IE
		return this._value;
	},
	/** Returns a Locale-dependent string for this long integer.
	 * @return String
	 */
	$toLocaleString: zkf
});
