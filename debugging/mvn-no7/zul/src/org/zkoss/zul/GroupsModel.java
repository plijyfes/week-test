/* GroupsModel.java

	Purpose:
		
	Description:
		
	History:
		Mon Sep  1 10:10:34     2008, Created by tomyeh

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

import org.zkoss.zul.event.GroupsDataListener;
import org.zkoss.zul.ext.GroupsSortableModel;

/**
 * The interface defines a suitable data model for grouping {@link Listbox}
 * and {@link Grid}.
 *
 * <p>H: the class representing a group header,
 * F: the class representing a group footer,
 * and D: the class representing each item of data (in a group).
 *
 * <p>If the data model is used with sortable listbox or grid,
 * the developer must also implement {@link GroupsSortableModel}.
 *
 * <p>For more information, please refer to
 * <a href="http://books.zkoss.org/wiki/ZK_Developer%27s_Reference/MVC/Model/Groups_Model">ZK Developer's Reference: Groups Model</a>
 *
 * @author tomyeh
 * @since 3.5.0
 * @see GroupsSortableModel
 * @see ListModel
 */
public interface GroupsModel<D, H, F> {
	/** Returns the group value at the specified index.
	 * It is used to render {@link Group} and {@link Listgroup}.
	 * @param groupIndex the index of the group.
	 */
	public H getGroup(int groupIndex);
	/** Returns the number of groups.
	 */
	public int getGroupCount();

	/** Returns the child value of the specified group at the specified index.
	 * @param groupIndex the index of the group.
	 * @param index the index of the element in the group.
	 */
	public D getChild(int groupIndex, int index);
	/** Returns the number of children of the specified group.
	 * <p>Note: it does <i>not</i> include the group foot ({@link #getGroupfoot}).
	 * @param groupIndex the index of the group.
	 */
	public int getChildCount(int groupIndex);

	/** Returns the foot value of the specified group, or null if the specified group
	 * does not have any foot.
	 * It is used to render {@link Groupfoot} and {@link Listgroupfoot}.
	 *
	 * <p>Note: it is ignored if {@link #hasGroupfoot} returns false.
	 * @param groupIndex the index of the group.
	 */
	public F getGroupfoot(int groupIndex);
	/** Returns if the specified group has a foot value.
	 * @param groupIndex the index of the group.
	 */
	public boolean hasGroupfoot(int groupIndex);

	/** Adds a listener to the groups that's notified each time a change
	 * to the data model occurs. 
	 */
	public void addGroupsDataListener(GroupsDataListener l);
    /** Removes a listener from the groups that's notified each time
     * a change to the data model occurs. 
     */
	public void removeGroupsDataListener(GroupsDataListener l) ;

	/** Whether the group is open at the specified index.
	 * It is used to render {@link Group} and {@link Listgroup}.
	 * <p> Default: true
	 * @param groupIndex the index of the group.
	 * @since 6.0.0
	 */
	public boolean isGroupOpened(int groupIndex);
	/** Opens the group at the specified index.
	 * @param groupIndex the index of the group.
	 * @return if it has been added successfully; false if it was opened.
	 * @since 6.0.0
	 */
	public boolean addOpenGroup(int groupIndex);
	/** Closes the group at the specified index.
	 * @param groupIndex the index of the group.
	 * @return if it has been removed successfully; false if it was closed.
	 * @since 6.0.0
	 */
	public boolean removeOpenGroup(int groupIndex);
}
