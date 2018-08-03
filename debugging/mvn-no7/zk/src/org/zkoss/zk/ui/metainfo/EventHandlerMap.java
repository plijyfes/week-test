/* EventHandlerMap.java

	Purpose:
		
	Description:
		
	History:
		Wed Mar 28 16:28:40     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.metainfo;

import java.util.Set;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;

/**
 * A map of instances of {@link EventHandler}.
 *
 * <p>Note: it is not thread safe. Thus, it is better to {@link #clone}
 * and then modifying the cloned instance if you want to change it
 * concurrently.
 *
 * @author tomyeh
 */
public class EventHandlerMap implements Cloneable, java.io.Serializable {
	/** The event handler map, (String evtnm, EventHandler evthd).
	 */
	private Map<String, List<EventHandler>> _evthds;

	/** Returns whether no event handler at all.
	 */
	public boolean isEmpty() {
		return _evthds == null || _evthds.isEmpty();
	}
	/** Returns the first effective event handler of the specified event name,
	 * or null if not available.
	 *
	 * <p>It checks whether an event handler is effective by calling
	 * {@link EventHandler#isEffective(Component)}.
	 *
	 * @param comp the component used to evaluate whether an event handler
	 * is effective.
	 * @see EventHandler#isEffective(Component)
	 * @since 3.0.0
	 */
	public EventHandler get(Component comp, String evtnm) {
		if (_evthds != null) {
			final List<EventHandler> ehl = _evthds.get(evtnm);
			if (ehl != null)
				for (EventHandler eh: ehl)
					if (eh.isEffective(comp))
						return eh;
		}
		return null;
	}
	/** Returns a readonly collection of event names (String), or
	 * an empty collection if no event name is registered.
	 * @since 3.0.2
	 */
	public Set<String> getEventNames() {
		if (_evthds != null)
			return _evthds.keySet();
		return Collections.emptySet();
	}
	/** Returns a readonly list of all event handlers associated
	 * with the specified event name, or null if no handler is associated
	 * with.
	 *
	 * <p>Unlike {@link #get(Component,String)}, it returns all
	 * event handlers no matter whether they are effective.
	 *
	 * @since 3.0.0
	 */
	public List<EventHandler> getAll(String evtnm) {
		if (_evthds != null)
			return _evthds.get(evtnm);
		return null;
	}

	/** Adds the event handler for the specified event name.
	 * <p>Note: the new handler won't overwrite the previous one,
	 * unless {@link EventHandler#getCondition} is the same.
	 * Rather, the new handler is appended to the list. You can retreive
	 * list by invoking {@link #getAll}.
	 *
	 * @see #getAll
	 * @since 3.0.0
	 */
	public void add(String evtnm, EventHandler evthd) {
		if (evtnm == null || evthd == null)
			throw new IllegalArgumentException("null");

		if (_evthds == null)
			_evthds = new HashMap<String, List<EventHandler>>(4);

		List<EventHandler> ehl = _evthds.get(evtnm);
		if (ehl == null) {
			_evthds.put(evtnm, ehl = new LinkedList<EventHandler>());
		} else {
			for (Iterator<EventHandler> it = ehl.iterator(); it.hasNext();) {
				final EventHandler eh = it.next();
				if (Objects.equals(eh.getCondition(), evthd.getCondition()))
					it.remove(); //replicate
			}
		}

		ehl.add(evthd);
	}
	/** Adds all event handlers of the specified map to this map.
	 */
	public void addAll(EventHandlerMap src) {
		if (src != null && !src.isEmpty()) {
			for (Map.Entry<String, List<EventHandler>> me: src._evthds.entrySet()) {
				final String evtnm = me.getKey();
				for (EventHandler eh: me.getValue())
					add(evtnm, eh);
			}
		}
	}

	//Cloneable//
	/** Clones this event handler map.
	 */
	public Object clone() {
		final EventHandlerMap clone = new EventHandlerMap();
		clone.addAll(this);
		return clone;
	}
	//Object//
	public String toString() {
		return "[evthd:" + _evthds + ']';
	}
}
