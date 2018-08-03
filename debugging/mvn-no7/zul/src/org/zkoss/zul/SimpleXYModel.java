/* SimpleXYModel.java

	Purpose:
		
	Description:
		
	History:
		Thu Aug 14 11:30:51     2006, Created by henrichen

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.event.ChartDataEvent;

/**
 * A XY data model implementation of {@link XYModel}.
 * A XY model is an N series of (X, Y) data objects .
 *
 * @author henrichen
 * @see XYModel
 * @see Chart
 */
public class SimpleXYModel extends AbstractChartModel implements XYModel {
	private static final long serialVersionUID = 20091008182904L;
	protected Map<Comparable<?>, List<XYPair>> _seriesMap = new HashMap<Comparable<?>, List<XYPair>>(8); //(series, XYPair)
	protected List<Comparable<?>> _seriesList = new ArrayList<Comparable<?>>();
	private boolean _autoSort = true;

	//-- XYModel --//
	public Comparable<?> getSeries(int index) {
		return _seriesList.get(index);
	}
	
	public Collection<Comparable<?>> getSeries() {
		return _seriesList;
	}
	
	public int getDataCount(Comparable<?> series) {
		final List<XYPair> xyPairs =  _seriesMap.get(series);
		return xyPairs != null ? xyPairs.size() : 0;
	}

	public Number getX(Comparable<?> series, int index) {
		final List<XYPair> xyPairs = _seriesMap.get(series);
		
		if (xyPairs != null) {
			return xyPairs.get(index).getX();
		}
		return null;
	}

	public Number getY(Comparable<?> series, int index) {
		final List<XYPair> xyPairs = _seriesMap.get(series);
		
		if (xyPairs != null) {
			return xyPairs.get(index).getY();
		}
		return null;
	}
	
	public void setValue(Comparable<?> series, Number x, Number y, int index) {
		removeValue0(series, index);
		addValue0(series, x, y, index);
		fireEvent(ChartDataEvent.CHANGED, series, null);
	}
	
	public void addValue(Comparable<?> series, Number x, Number y) {
		addValue(series, x, y, -1);
	}
	
	public void addValue(Comparable<?> series, Number x, Number y, int index) {
		addValue0(series, x, y, index);
		fireEvent(ChartDataEvent.CHANGED, series, null);
	}
	
	private void addValue0(Comparable<?> series, Number x, Number y, int index) {
		List<XYPair> xyPairs = _seriesMap.get(series);
		if (xyPairs == null) {
			xyPairs = new ArrayList<XYPair>(15);
			_seriesMap.put(series, xyPairs);
			_seriesList.add(series);
		}
		if (index >= 0)
			xyPairs.add(index, new XYPair(x, y));
		else
			xyPairs.add(new XYPair(x, y));
	}

	public void setAutoSort(boolean auto) {
		_autoSort = auto;
	}

	public boolean isAutoSort() {
		return _autoSort;
	}
	
	public void removeSeries(Comparable<?> series) {
		_seriesMap.remove(series);
		_seriesList.remove(series);
		//bug 2555730: Unnecessary String cast on 'series' in SimpleCategoryModel
		fireEvent(ChartDataEvent.REMOVED, series, null);
	}
	
	public void removeValue(Comparable<?> series, int index) {
		removeValue0(series, index);
		fireEvent(ChartDataEvent.REMOVED, series, null);
	}
	
	private void removeValue0(Comparable<?> series, int index) {
		List<XYPair> xyPairs = _seriesMap.get(series);
		if (xyPairs == null) {
			return;
		}
		xyPairs.remove(index);
	}
	
	public void clear() {
		_seriesMap.clear();
		_seriesList.clear();
		fireEvent(ChartDataEvent.REMOVED, null, null);
	}
	
	//-- internal class --//
	protected static class XYPair implements java.io.Serializable {
		private static final long serialVersionUID = 20091008182941L;
		private Number _x;
		private Number _y;
		
		protected XYPair(Number x, Number y) {
			_x = x;
			_y = y;
		}
		
		public Number getX() {
			return _x;
		}
		
		public Number getY() {
			return _y;
		}
	}

	
	public Object clone() {
		SimpleXYModel clone = (SimpleXYModel) super.clone();
		if (_seriesMap != null)
			clone._seriesMap = new HashMap<Comparable<?>, List<XYPair>>(_seriesMap);
		if (_seriesList != null)
			clone._seriesList = new ArrayList<Comparable<?>>(_seriesList);
		return clone;
	}
}
