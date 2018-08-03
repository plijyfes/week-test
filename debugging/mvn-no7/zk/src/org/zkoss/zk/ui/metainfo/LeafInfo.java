/* LeafInfo.java

	Purpose:
		
	Description:
		
	History:
		Wed Jul  6 19:46:33 TST 2011, Created by tomyeh

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

*/
package org.zkoss.zk.ui.metainfo;

import java.util.List;
import java.util.Collections;

import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.xel.Evaluator;
import org.zkoss.zk.xel.EvaluatorRef;

/**
 * A skeleton used to implement a leaf node that does not allow any children.
 * <p>Notice that it is serializable.
 * Also notice that the implementation assumes all nodes except the root
 * must extend from {@link LeafInfo} or its derives.
 * @author tomyeh
 * @since 6.0.0
 */
/*package*/ abstract class LeafInfo implements NodeInfo, java.io.Serializable {
	//Note: getEvaluatorRef() is the same as getPageDefintion().getEvaluatorRef()
	//However, we store _evalr since PageDefinition is not serializable
	//Also notice that _evalr will be initialized later when this node is added
	/*package*/ EvaluatorRef _evalr;
	//transient since it is maintained by BranchInfo.readObject()
	private transient NodeInfo _parent;

	/*package*/ LeafInfo() {
		//_evalr will be added later when this node is added as child (see BranchInfo)
	}
	/*package*/ LeafInfo(NodeInfo parent) {
		if (parent != null)
			parent.appendChild(this);
	}
	/** Used only by {@link ComponentInfo#duplicate} to make a virtual copy.
	 */
	/*package*/ LeafInfo(LeafInfo from) {
		_parent = from._parent; //direct copy since it is 'virtual'
		_evalr = from._evalr;
	}

	
	public EvaluatorRef getEvaluatorRef() {
		return _evalr;
	}
	
	public PageDefinition getPageDefinition() {
		return _evalr != null ? _evalr.getPageDefinition(): null;
	}
	
	public Evaluator getEvaluator() {
		return _evalr != null ? _evalr.getEvaluator(): null;
	}

	
	public NodeInfo getParent() {
		return _parent;
	}
	
	public List<NodeInfo> getChildren() {
		return Collections.emptyList();
	}

	/** Sets a parent directly without maintaining the parent/child relationship.
	 */
	/*package*/ void setParentDirectly(NodeInfo parent) {
		_parent = parent;
	}

	
	public void appendChild(NodeInfo child) {
		throw new UiException(this+" does not allow any children");
	}
	
	public boolean removeChild(NodeInfo child) {
		return false;
	}
}
