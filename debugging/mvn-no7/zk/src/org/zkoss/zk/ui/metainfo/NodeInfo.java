/* NodeInfo.java

	Purpose:
		
	Description:
		
	History:
		Wed Jul  6 19:18:11 TST 2011, Created by tomyeh

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zk.ui.metainfo;

import java.util.List;

import org.zkoss.zk.xel.Evaluator;
import org.zkoss.zk.xel.EvaluatorRef;

/**
 * Represent a node in a ZUML metainfo tree.
 * @author tomyeh
 * @since 6.0.0
 */
public interface NodeInfo {
	/** Returns the parent, or null if it has no parent.
	 */
	public NodeInfo getParent();
	/** Returns a readonly list of children.
	 *
	 * <p>Note: the returned list is readonly. To modify, please use
	 * {@link #appendChild} and {@link #removeChild} instead.
	 */
	public List<NodeInfo> getChildren();

	/** Append a child
	 */
	public void appendChild(NodeInfo child);
	/** Removes a child.
	 */
	public boolean removeChild(NodeInfo child);

	/** Returns the page definition, i.e., the root node, or null if not available.
	 */
	public PageDefinition getPageDefinition();
	/** Returns the evaluator.
	 * <p>All nodes in the same ZUML tree has the same evaluator reference
	 * (inherited from the root node, {@link PageDefinition#getEvaluatorRef}).
	 */
	public Evaluator getEvaluator();
	/** Returns the evaluator reference.
	 */
	public EvaluatorRef getEvaluatorRef();
}
