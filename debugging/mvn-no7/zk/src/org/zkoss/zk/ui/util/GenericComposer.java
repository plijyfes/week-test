/* GenericComposer.java

	Purpose:
		
	Description:
		
	History:
		Nov 21, 2007 6:22:00 PM , Created by robbiecheng

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.util;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.ExecutionCtrl;
import org.zkoss.zk.ui.event.GenericEventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;

/**
 * <p>A skeletal composer that you can extend and write intuitive onXxx event handler methods;
 * this class will registers onXxx events to the supervised component automatically.</p>
 *
 * <P>Alternatives: in most cases, you don't extend from {@link GenericComposer} directly.
 * Rather, you can extend from one of the following skeletons.
 * <dl>
 * <dt>{@link org.zkoss.zk.ui.select.SelectorComposer}</dt>
 * <dd>It supports the autowiring based on Java annotation and a CSS3-based selector.
 * If you don't know which one to use, use {@link org.zkoss.zk.ui.select.SelectorComposer}.</dd>
 * <dt>{@link GenericForwardComposer}</dt>
 * <dd>It supports the autowiring based on naming convention.
 * You don't need to specify annotations explicitly, but it is error-prone if
 * it is used properly.</dd>
 * </dl>
 *
 * <p>The following is an example. The onOK and onCancel event listener is registered into 
 * the target main window automatically.</p>
 * 
 * <pre><code>
 * &lt;zscript>&lt;!-- both OK in zscript or a compiled Java class -->
 * public class MyComposer extends GenericComposer {
 *    public void onOK() {
 *        //doOK!
 *        //...
 *    }
 *    public void onCancel() {
 *        //doCancel
 *        //...
 *    } 
 * }
 * &lt;/zscript>
 *
 * &lt;window id="main" apply="MyComposer">
 *     ...
 * &lt;/window>
 * </code></pre>
 * <p>since 3.6.1, this composer would be assigned as an attribute of the given component 
 * per the naming convention composed of the component id and composer Class name. e.g.
 * If the applied component id is "xwin" and this composer class is 
 * org.zkoss.MyComposer, then the variable name would be "xwin$MyComposer". You can
 * reference this composer with {@link Component#getAttributeOrFellow} or via EL as ${xwin$MyComposer}
 * of via annotate data binder as @{xwin$MyComposer}, etc. If this composer is the 
 * first composer applied to the component, a shorter variable name
 * composed of the component id and a String "composer" would be also available for use. 
 * Per the above example, you can also reference this composer with the name "xwin$composer".
 * <p>In general, <code>xwin$composer</code> is suggested because EL expressions won't
 * depend on the composer's class name. However, <code>xwin$MyComposer</code> is
 * useful if you apply multiple composers to the same component.
 *
 * <p>Notice that, since 3.6.2, this composer becomes serializable.
 * 
 * @author robbiecheng
 * @since 3.0.1
 */
abstract public class GenericComposer<T extends Component> extends GenericEventListener
implements Composer<T>, ComposerExt<T>, java.io.Serializable {
	private static final long serialVersionUID = 20091006115555L;
	protected String _applied; //uuid of the applied component (for serialization back)
	
	/** Returns the current page.
	 * @since 5.0.10
	 */
	protected Page getPage() {
		final Execution exec = Executions.getCurrent();
		return exec != null ? ((ExecutionCtrl)exec).getCurrentPage(): null;
	}
	/**
	 * Registers onXxx events to the supervised component; a subclass that override
	 * this method should remember to call super.doAfterCompose(comp) or it will not 
	 * work.
	 */
	public void doAfterCompose(T comp) throws Exception {
		//bind this GenericEventListener to the supervised component
		_applied = comp.getUuid();
		bindComponent(comp);
	}
	
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) { //do nothing
		return compInfo;
	}
	
	public void doBeforeComposeChildren(T comp) throws Exception {
		//assign this composer as a variable
		//feature #2778508
		ConventionWires.wireController(comp, this);
	}
	
	public boolean doCatch(Throwable ex) throws Exception { //do nothing
		return false;
	}
	
	public void doFinally() throws Exception { //do nothing
	}
}
