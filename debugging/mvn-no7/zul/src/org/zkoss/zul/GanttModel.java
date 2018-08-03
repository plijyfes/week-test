/* GanttModel.java

	Purpose:
		
	Description:
		
	History:
		Apr 30, 2008 2:01:47 PM, Created by henrichen

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.event.ChartDataEvent;

/**
 * A data model for Gantt chart.
 *
 * @author henrichen
 * @see GanttTask
 * @see Chart
 * @since 3.5.0
 */
public class GanttModel extends AbstractChartModel {
	private static final long serialVersionUID = 20091008183023L;
	private Map<Comparable<?>, List<GanttTask>> _taskMap = new LinkedHashMap<Comparable<?>, List<GanttTask>>(8); //(series, task list)
	
	public void addValue(Comparable<?> series, GanttTask task) {
		List<GanttTask> tasks = _taskMap.get(series);
		if (tasks == null) {
			tasks = new ArrayList<GanttTask>(13);
			_taskMap.put(series, tasks);
		}
		if (task.getSeries() != null) {
			throw new UiException("A GanttTask in a series cannot be added again: "+ task.getSeries()+":"+task.getDescription());
		}
		task.setSeries(series);
		task.setOwner(this);
		tasks.add(task);
		fireEvent(ChartDataEvent.ADDED, series, task);
	}
	
	public void removeValue(Comparable<?> series, GanttTask task) {
		final List<GanttTask> tasks = _taskMap.get(series);
		if (tasks == null) {
			return;
		}
		tasks.remove(task);
		task.setSeries(null);
		task.setOwner(null);
		fireEvent(ChartDataEvent.REMOVED, series, task);
	}

	/** Return all series of this GanttModel.
	 * @return all series of this GanttModel.
	 */
	public Comparable<?>[] getAllSeries() {
		final Set<Comparable<?>> allseries = _taskMap.keySet();
		return allseries.toArray(new Comparable[allseries.size()]);
	}
	
	public GanttTask[] getTasks(Comparable series) {
		final List<GanttTask> tasks = _taskMap.get(series);
		return tasks == null ? new GanttTask[0] : tasks.toArray(new GanttTask[tasks.size()]);
	}

	/**
	 * A Task in an operation series; a helper class used in {@link GanttModel}.
	 * @author henrichen
	 * @since 3.5.0
	 * @see GanttModel
	 */
	public static class GanttTask implements java.io.Serializable {
		private static final long serialVersionUID = 20091008183314L;
		private Comparable _series;
		private Date _start;
		private Date _end;
		private String _description;
		private double _percent;
		private Collection<GanttTask> _subtasks;
		private GanttModel _owner;
		
		public GanttTask(String description, Date start, Date end, double percent) {
			_description = description;
			_start = start;
			_end = end;
			_percent = percent;
			_subtasks = new LinkedList<GanttTask>();
		}

		public Date getStart() {
			return _start;
		}

		public void setStart(Date start) {
			if (!Objects.equals(start, _start)) {
				this._start = start;
				fireChartChange();
			}
		}

		public Date getEnd() {
			return _end;
		}

		public void setEnd(Date end) {
			if (!Objects.equals(end, _end)) {
				this._end = end;
				fireChartChange();
			}
		}

		public String getDescription() {
			return _description;
		}

		public void setDescription(String description) {
			if (!Objects.equals(description, _description)) {
				this._description = description;
				fireChartChange();
			}
		}

		public double getPercent() {
			return _percent;
		}

		public void setPercent(double percent) {
			if (Double.compare(percent, _percent) != 0) {
				this._percent = percent;
				if (_owner != null)
					_owner.fireEvent(ChartDataEvent.CHANGED, _series, this);
			}
		}

		public void addSubtask(GanttTask task) {
			if (task.getSeries() != null) {
				throw new UiException("A GanttTask in a series cannot be added again: "+ task.getSeries()+":"+task.getDescription());
			}
			task.setSeries(_series);
			task.setOwner(_owner);
			_subtasks.add(task);
			if (_owner != null)
				_owner.fireEvent(ChartDataEvent.CHANGED, _series, this);
		}
		
		public void removeSubtask(GanttTask task) {
			if (_subtasks.remove(task)) {
				task.setSeries(null);
				task.setOwner(null);
				if (_owner != null)
					_owner.fireEvent(ChartDataEvent.CHANGED, _series, this);
			}
		}
		
		public GanttTask[] getSubtasks() {
			return _subtasks.toArray(new GanttTask[_subtasks.size()]);
		}
		
		private Comparable getSeries() {
			return _series;
		}
		
		private void setSeries(Comparable series) {
			_series = series;
		}
		
		private void setOwner(GanttModel owner) {
			_owner = owner;
		}
		
		protected void fireChartChange() {
			if (_owner != null)
				_owner.fireEvent(ChartDataEvent.CHANGED, _series, this);
		}
	}

	
	public Object clone() {
		GanttModel clone = (GanttModel) super.clone();
		if (_taskMap != null)
			clone._taskMap = new LinkedHashMap<Comparable<?>, List<GanttTask>>(_taskMap);
		return clone;
	}
}
