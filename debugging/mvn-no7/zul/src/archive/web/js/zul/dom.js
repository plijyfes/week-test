/* dom.js

	Purpose:
		
	Description:
		
	History:
		Fri Jul 31 16:37:36     2009, Created by tomyeh

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
(function () {
	//handle theme
	jq(function () {
		var cookies = document.cookie.split(";"),
			len = cookies.length,
			tname = 'breeze'; // shall sync with default theme name
		for (var i = 0, c, j; i < len; i++) {
			c = cookies[i];
			j = c.indexOf("=");
			if ("zktheme" == jq.trim(c.substr(0, j))) { // shall sync with zkplus Themes.java
				tname = jq.trim(c.substr(j+1));
				break;
			}
		}
		jq(document.body).addClass(tname);
	});

	var $alert = jq.alert,
		icons = {QUESTION: "z-msgbox z-msgbox-question",
			EXCLAMATION: "z-msgbox z-msgbox-exclamation",
			INFORMATION: "z-msgbox z-msgbox-information",
			ERROR: "z-msgbox z-msgbox-error",
			NONE: 'z-msgbox z-msgbox-none'
		};

	function newButton(nm, f) {
		return new zul.wgt.Button({
			label: msgzul[nm.toUpperCase()]||nm,
			listeners: {
				onClick: function (evt) {
					if (typeof f == 'function')
						f.call(this, evt);
					this.$o().detach();
				}
			}
		});
	}
	function getButtons(opts) {
		var btns = [];
		for (var nm in opts) {
			var f = opts[nm];
			btns.push(newButton(nm, typeof f == 'function' ? f: null));
		}
		if (!btns.length)
			btns.push(newButton('OK'));
		return btns;
	}

	jq.alert = function (msg, opts) {
		if (opts && opts.mode == 'os')
			return $alert(msg);

		opts = opts || {};
		zk.load("zul.wnd,zul.wgt,zul.box", function () {
			var wnd = new zul.wnd.Window({
				closable: true,
				width: '250pt',
				title: opts.title||zk.appName,
				border: 'normal',
				children: [
					new zul.box.Box({
						mold: 'horizontal',
						children: [
							new zul.wgt.Div({sclass: icons[(opts.icon||'').toUpperCase()]||opts.icon||icons.INFORMATION}),
							new zul.wgt.Div({
								sclass: 'z-messagebox',
								width: '210pt',
								children: [
									new zul.wgt.Label({
										value: msg,
										multiline: true
									})
								]
							})
						]
					}),
					new zul.wgt.Separator({bar: true}),
					new zul.box.Box({
						mold: 'horizontal',
						style: 'margin-left:auto; margin-right:auto',
						children: getButtons(opts.button)
					})
				],
				mode: opts.mode||'modal'
			});

			var p = opts.desktop || zk.Desktop.$();
			if (p && (p = p.firstChild) && p.desktop)
				p.appendChild(wnd);
			else
				jq(document.body).append(wnd);
		});
  	};
	zAu.wrongValue_ = function(wgt, msg) {
		var efs = wgt.effects_;
		if (efs.errMesg) {
			efs.errMesg.destroy();
			delete efs.errMesg;
		}
		if (msg !== false) {
			efs.errMesg = {destroy: zk.$void};
			zk.load("zul.inp", function () {
				if (efs.errMesg) //not destroyed yet
					(efs.errMesg = new zul.inp.Errorbox()).show(wgt, msg);
			});
		}
	};
})();
