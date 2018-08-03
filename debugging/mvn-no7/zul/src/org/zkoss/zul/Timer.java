/* Timer.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep 26 12:45:22     2005, Created by tomyeh

Copyright (C) 2005 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;

/**
 * Fires one or more {@link org.zkoss.zk.ui.event.Event} after
 * a specified delay.
 *
 * <p>{@link Timer} is a special component that is invisible.
 *
 * <p>Notice that the timer won't fire any event until it is attached
 * to a page.
 *
 * @author tomyeh
 */
public class Timer extends HtmlBasedComponent {
	private int _delay;
	private boolean _repeats, _running = true;

	static {
		addClientEvent(Timer.class, Events.ON_TIMER, CE_DUPLICATE_IGNORE);
	}

	public Timer() {
	}
	public Timer(int delay) {
		this();
		_delay = Math.max(0, delay);
	}

	/** Returns the delay, the number of milliseconds between
	 * successive action events.
	 * <p>Default: 0 (immediately).
	 */
	public int getDelay() {
		return _delay;
	}
	/** Sets the delay, the number of milliseconds between
	 * successive action events.
	 * @param delay If negative, 0 is assumed.
	 */
	public void setDelay(int delay) {
		delay = Math.max(0, delay);
		if (delay != _delay) {
			_delay = delay;
			smartUpdate("delay", _delay);
		}
	}
	/** Returns whether the timer shall send Event repeatedly.
	 * <p>Default: false.
	 */
	public boolean isRepeats() {
		return _repeats;
	}
	/** Sets whether the timer shall send Event repeatedly.
	 */
	public void setRepeats(boolean repeats) {
		if (_repeats != repeats) {
			_repeats = repeats;
			smartUpdate("repeats", _repeats);
		}
	}
	/** Returns whether this timer is running.
	 * <p>Default: true.
	 * @see #stop
	 * @see #start
	 */
	public boolean isRunning() {
		return _running;
	}
	/** Start or stops the timer.
	 */
	public void setRunning(boolean running) {
		if (running) start();
		else stop();
	}

	/** Stops the timer.
	 */
	public void stop() {
		if (_running) {
			_running = false;
			smartUpdate("running", Boolean.FALSE, true); //Bug 3155985: shall allow restore
		}
	}
	/** Starts the timer.
	 */
	public void start() {
		if (!_running) {
			_running = true;
			smartUpdate("running", Boolean.TRUE, true); //Bug 3155985: shall allow restore
		}
	}

	//-- Component --//
	/** Not childable. */
	protected boolean isChildable() {
		return false;
	}

	//-- ComponentCtrl --//
	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link HtmlBasedComponent#service},
	 * it also handles onOpen.
	 * @since 5.0.0
	 */
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals(Events.ON_TIMER)) {
			if (!_repeats) _running = false; //Bug 1829397
		}
		super.service(request, everError);
	}

	//super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		render(renderer, "repeats", _repeats);
		if (_delay != 0) renderer.render("delay", _delay);
		if (!_running) renderer.render("running", false);
	}
}
