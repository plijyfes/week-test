/* SimpleCategoryModel.java

	Purpose:
		
	Description:
		
	History:
		Thu Aug 14 11:25:51     2006, Created by henrichen

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.lang.Objects;
import org.zkoss.zul.event.ChartDataEvent;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A Category data model implementation of {@link CategoryModel}.
 * A Category model is an N series of (category, value) data objects.
 *
 * @author henrichen
 * @see CategoryModel
 * @see Chart
 */
public class SimpleCategoryModel extends AbstractChartModel implements CategoryModel {
	private static final long serialVersionUID = 20091008183445L;
	private Map<Comparable<?>, Integer> _seriesMap = new HashMap<Comparable<?>, Integer>(); // (series, usecount)
	private List<Comparable<?>> _seriesList = new ArrayList<Comparable<?>>();

	private Map<Comparable<?>, Integer> _categoryMap = new HashMap<Comparable<?>, Integer>(); // (category, usecount)
	private List<Comparable<?>> _categoryList = new ArrayList<Comparable<?>>();
	
	private Map<List<Comparable<?>>, Number> _valueMap = new LinkedHashMap<List<Comparable<?>>, Number>();
	
	//-- CategoryModel --//
	public Comparable<?> getSeries(int index) {
		return _seriesList.get(index);
	}

	public Comparable<?> getCategory(int index) {
		return _categoryList.get(index);
	}
	
	public Collection<Comparable<?>> getSeries() {
		return _seriesList;
	}

	public Collection<Comparable<?>> getCategories() {
		return _categoryList;
	}
	
	public Collection<List<Comparable<?>>> getKeys() {
		return _valueMap.keySet();
	}
	
	public Number getValue(Comparable<?> series, Comparable<?> category) {
		List<Comparable<?>> key = new ArrayList<Comparable<?>>(2);
		key.add(series);
		key.add(category);
		Number num = _valueMap.get(key);
		return num;
	}

	public void setValue(Comparable<?> series, Comparable<?> category, Number value) {
		List<Comparable<?>> key = new ArrayList<Comparable<?>>(2);
		key.add(series);
		key.add(category);
		
		if (!_valueMap.containsKey(key)) {
			if (!_categoryMap.containsKey(category)) {
				_categoryMap.put(category, new Integer(1));
				_categoryList.add(category);
			} else {
				Integer count = _categoryMap.get(category);
				_categoryMap.put(category, new Integer(count.intValue()+1));
			}
			
			if (!_seriesMap.containsKey(series)) {
				_seriesMap.put(series, new Integer(1));
				_seriesList.add(series);
			} else {
				Integer count = _seriesMap.get(series);
				_seriesMap.put(series, new Integer(count.intValue()+1));

			}
		} else {
			Number ovalue = _valueMap.get(key);
			if (Objects.equals(ovalue, value)) {
				return;
			}
		}
		
		_valueMap.put(key, value);
		//bug 2555730: Unnecessary String cast on 'series' in SimpleCategoryModel
		fireEvent(ChartDataEvent.CHANGED, series, category);
	}
	
	public void removeValue(Comparable<?> series, Comparable<?> category) {
		List<Comparable<?>> key = new ArrayList<Comparable<?>>(2);
		key.add(series);
		key.add(category);
						
		if (_valueMap.remove(key) == null)
			return;
		
		int ccount = _categoryMap.get(category).intValue();
		if (ccount > 1) {
			_categoryMap.put(category, new Integer(ccount-1));
		} else {
			_categoryMap.remove(category);
			_categoryList.remove(category);
		}
		
		int scount = _seriesMap.get(series).intValue();
		if (scount > 1) {
			_seriesMap.put(series, new Integer(scount-1));
		} else {
			_seriesMap.remove(series);
			_seriesList.remove(series);
		}
		
		//bug 2555730: Unnecessary String cast on 'series' in SimpleCategoryModel
		fireEvent(ChartDataEvent.REMOVED, series, category);
	}
	
	public void clear() {
		_seriesMap.clear();
		_seriesList.clear();
		_categoryMap.clear();
		_categoryList.clear();
		_valueMap.clear();
		fireEvent(ChartDataEvent.REMOVED, null, null);
	}
	
	public Object clone() {
		SimpleCategoryModel clone = (SimpleCategoryModel) super.clone();
		if (_seriesMap != null)
			clone._seriesMap = new HashMap<Comparable<?>, Integer>(_seriesMap);
		if (_seriesList != null)
			clone._seriesList = new ArrayList<Comparable<?>>(_seriesList);
		if (_categoryMap != null)
			clone._categoryMap = new HashMap<Comparable<?>, Integer>(_categoryMap);
		if (_categoryList != null)
			clone._categoryList = new ArrayList<Comparable<?>>(_categoryList);
		if (_valueMap != null)
			clone._valueMap = new LinkedHashMap<List<Comparable<?>>, Number>(_valueMap);
		return clone;
	}
}
