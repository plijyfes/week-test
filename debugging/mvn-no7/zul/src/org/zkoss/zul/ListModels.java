/* ListModels.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Aug 25, 2010 4:37:32 PM , Created by jumperchen
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zul;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.lang.Objects;
import org.zkoss.zul.event.ListDataListener;
import org.zkoss.zul.ext.Selectable;

/**
 * A utility for handling {@link ListModel}.
 *  
 * @author jumperchen
 * @since 5.0.4
 */
public class ListModels {
	
	private ListModels() {}
	
	/**
	 * A comparator for {@link ListSubModel#getSubModel} to check if
	 * a value retrieved from the model matches the user typed.
	 * To compare, it will convert them to String instances, and
	 * return 0 (i.e., matched), when the value starts with
	 * the user typed, and both of them are not empty.
	 */
	public final static Comparator STRING_COMPARATOR = new Comparator() {
		public int compare(Object key, Object value) {
			String idx = Objects.toString(key);
			return idx != null && value != null && idx.length() > 0 &&
					Objects.toString(value).startsWith(idx) ? 0 : 1;
		}
	};

	/**
	 * A comparator for {@link ListSubModel#getSubModel} to check if
	 * a value retrieved from the model matches the user typed.
	 * It assumes the model is {@link Map}, and the value is
	 * Map.Entry.
	 * To compare, it will convert them to String instances, and
	 * return 0 when the value (Map.Entry's getValue())
	 * starts with the user typed, and both of them are not empty.
	 */
	public final static Comparator MAP_COMPARATOR = new Comparator() {
		public int compare(Object key, Object value) {
			String idx = Objects.toString(key);
			return idx != null && value != null && idx.length() > 0 &&
					Objects.toString(((Map.Entry) value).getValue()).startsWith(idx) ? 0 : 1;
		}
	};
	
	/**
	 * Returns a proxy instance of the given model that implements
	 * {@link ListSubModel} and {@link ListModel} interface.
	 * @param model a model
	 * @param comparator used to compare the value typed by user and
	 * the value from the model. The first argument is the value typed by user,
	 * and the second argument is the value retrieved from the model.
	 * It shall return 0 if they matched (i.e., shall be shown).
	 * @param nRows the maximal allowed number of matched items.
	 */
	public static <T> ListModel<T> toListSubModel(ListModel<T> model, Comparator<T> comparator, int nRows) {
		return new SubModel<T>(model, comparator, nRows);
	}
	
	/**
	 * Returns a proxy instance of the given model that implements
	 * {@link ListSubModel} and {@link ListModel} interface.
	 * <p>The default comparator depends on the type of the model, if the
	 * model is an instance of {@link ListModelMap}, {@link #MAP_COMPARATOR} is used.
	 * Otherwise, {@link #STRING_COMPARATOR} is used.
	 * <p>In additions, the maximal allowed number of matched items is 15.
	 * <p>If you want more control, use {@link #toListSubModel(ListModel, Comparator, int)}
	 * instead.
	 * @param model a {@link ListModel}
	 * @see #toListSubModel(ListModel, Comparator, int)
	 */
	@SuppressWarnings("unchecked")
	public static <T> ListModel<T> toListSubModel(ListModel<T> model) {
		return new SubModel<T>(model, (model instanceof ListModelMap) ? MAP_COMPARATOR
					: STRING_COMPARATOR, 15);
	}
	
	private static class SubModel<E> implements ListModel<E>, ListSubModel<E>, 
	Selectable<E>, java.io.Serializable {
		private final ListModel<E> _model;

		private final Comparator<E> _comparator;

		private final int _nRows;

		private SubModel(ListModel<E> model, Comparator<E> comparator, int nRows) {
			_model = model;
			_comparator = comparator;
			_nRows = nRows;
		}

		/**
		 * Returns the subset of the list model data that matches the specified
		 * value. It is usually used for implementation of auto-complete.
		 * 
		 * @param value
		 *            the value to retrieve the subset of the list model. It is
		 *            the key argument when invoking {@link #inSubModel}. this
		 *            string.
		 * @param nRows
		 *            the maximal allowed number of matched items. If negative,
		 *            it means the caller allows any number, but the
		 *            implementation usually limits to a certain number (for
		 *            better performance).
		 */
		@SuppressWarnings("unchecked")
		public ListModel<E> getSubModel(Object value, int nRows) {
			final List<E> data = new LinkedList<E>();
			final Set<E> selection = new LinkedHashSet<E>();
			nRows = nRows < 0 ? _nRows : nRows;
			for (int i = 0, j = _model.getSize(); i < j; i++) {
				E o = _model.getElementAt(i);
				if (((Comparator)_comparator).compare(value, o) == 0) {
					data.add(o);
					if (_model instanceof Selectable && ((Selectable<?>)_model).isSelected(o)) {
						selection.add(o);
					}
					if (--nRows <= 0)
						break; // done
				}
			}
			final Selectable<E> subm =  new ListModelList<E>(data, true);
			// may multiple selected item in subModel, sync the multiple state
			subm.setMultiple(((Selectable)_model).isMultiple());
			subm.setSelection(selection);
			return (ListModel<E>) subm;
		}

		public E getElementAt(int index) {
			return _model.getElementAt(index);
		}

		public int getSize() {
			return _model.getSize();
		}

		public void addListDataListener(ListDataListener l) {
			_model.addListDataListener(l);
		}

		public void removeListDataListener(ListDataListener l) {
			_model.removeListDataListener(l);
		}

		@SuppressWarnings("unchecked")
		private Selectable<E> getSelectModel() {
			return (Selectable<E>) _model;
		}
		
		public Set<E> getSelection() {
			return getSelectModel().getSelection();
		}

		
		public void setSelection(Collection<? extends E> selection) {
			getSelectModel().setSelection(selection);
		}

		
		public boolean isSelected(Object obj) {
			return getSelectModel().isSelected(obj);
		}

		
		public boolean isSelectionEmpty() {
			return getSelectModel().isSelectionEmpty();
		}

		
		public boolean addToSelection(E obj) {
			return getSelectModel().addToSelection(obj);
		}

		
		public boolean removeFromSelection(Object obj) {
			return getSelectModel().removeFromSelection(obj);
		}

		
		public void clearSelection() {
			getSelectModel().clearSelection();
			
		}

		
		public void setMultiple(boolean multiple) {
			getSelectModel().setMultiple(multiple);
			
		}

		
		public boolean isMultiple() {
			return getSelectModel().isMultiple();
		}
	}
}
