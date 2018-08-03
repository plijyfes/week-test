/* GenericForwardComposer.java

	Purpose:
		
	Description:
		
	History:
		Jun 26, 2008 2:30:30 PM, Created by henrichen

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.ui.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.ConventionWires;

/**
 * <p>A skeletal composer that you can extend and write intuitive onXxx$myid 
 * event handler methods with auto event forwarding and "auto-wired" accessible 
 * variable objects such as embedded objects, components, and external 
 * resolvable variables in a ZK zuml page; this class will add forward
 * condition to the myid source component and forward source onXxx 
 * event received by the source myid component to the target onXxx$myid event
 * (as defined in this composer) of the supervised target component; of course
 * it will also registers onXxx$myid events to the supervised 
 * component and wire all accessible variable objects to this composer by 
 * calling setXxx() method or set xxx field value directly per the variable 
 * name.</p>
 * 
 * <P>Alternatives: in most cases, you can extend from one of the following skeletons.
 * <dl>
 * <dt>{@link org.zkoss.zk.ui.select.SelectorComposer}</dt>
 * <dd>It supports the autowiring based on Java annotation and a CSS3-based selector.
 * If you don't know which one to use, use {@link org.zkoss.zk.ui.select.SelectorComposer}.</dd>
 * <dt>{@link GenericForwardComposer}</dt>
 * <dd>It supports the autowiring based on naming convention.
 * You don't need to specify annotations explicitly, but it is error-prone if
 * it is used improperly.</dd>
 * </dl>
 *
 * <p>Notice that since this composer kept references to the components, single
 * instance object cannot be shared by multiple components.</p>
 *  
 * <p>The following is an example. The onChange event received by Textbox 
 * mytextbox will be forwarded to target Window mywin as a new target event 
 * onChange$mytextbox and the Textbox component with id name "mytextbox" and
 * Label with id name mylabel are injected into the "mytextbox" and "mylabel"
 * fields respectively(so you can use mytextbox and mylabel variable directly 
 * in onChange_mytextbox without problem).</p>
 * 
 * <pre><code>
 * MyComposer.java
 * 
 * public class MyComposer extends GenericForwardComposer {
 *     private Textbox mytextbox;
 *     private Window self; //embedded object, the supervised window "mywin"
 *     private Page page; //the ZK zuml page
 *     private Label mylabel;
 *     
 *     public void onChange$mytextbox(Event event) {
 *         mylabel.setValue("You just entered: "+ mytextbox.getValue());
 *     }
 * }
 * 
 * test.zul
 * 
 * &lt;window id="mywin" apply="MyComposer">
 *     &lt;textbox id="mytextbox"/>
 *     &lt;label id="mylabel"/>
 * &lt;/window>
 * </code></pre>
 * 
 * @author henrichen
 * @since 3.0.7
 * @see ConventionWires
 */
abstract public class GenericForwardComposer<T extends Component> extends GenericAutowireComposer<T> {
	private static final long serialVersionUID = 20091006115726L;

	/** The default constructor.
	 * <p>It is a shortcut of <code>GenericForwardComposer('$',
	 * !"true".equals(Library.getProperty("org.zkoss.zk.ui.composer.autowire.zscript")),
	 * !"true".equals(Library.getProperty("org.zkoss.zk.ui.composer.autowire.xel")))</code>.
	 * <p>In other words, whether to ignore variables defined in ZSCRIPT and XEL depends
	 * on the library variables called <code>org.zkoss.zk.ui.composer.autowire.zscript</code>
	 * and <code>org.zkoss.zk.ui.composer.autowire.xel</code>.
	 * Furthermore, if not specified, their values are default to <b>false</b>, i.e., 
	 * they shall <tt>not</tt> be wired (i.e., shall be ignored)
	 * <p>If you want to control whether to wire ZSCRIPT's or XEL's variable
	 * explicitly, you could use
	 * {@link #GenericForwardComposer(char,boolean,boolean)} instead.
	 *
	 * <h2>Version Difference</h2>
	 * <p>ZK 5.0 and earlier, this constructor is the same as
	 * <code>GenericForwardComposer('$', false, false)</code><br/>
	 * In other words, it is default to wire (i.e., shall <i>not</i> ignore).
	 */
	protected GenericForwardComposer() {
	}
	/** Constructor with a custom separator.
	 * The separator is used to separate the component ID and event name.
	 * By default, it is '$'. For Groovy and other environment that '$'
	 * is not applicable, you can specify '_'.
	 * <p>It is a shortcut of <code>GenericForwardComposer('$',
	 * !"true".equals(Library.getProperty("org.zkoss.zk.ui.composer.autowire.zscript")),
	 * !"true".equals(Library.getProperty("org.zkoss.zk.ui.composer.autowire.xel")))</code>.
	 * <p>In other words, whether to ignore variables defined in ZSCRIPT and XEL depends
	 * on the library variables called <code>org.zkoss.zk.ui.composer.autowire.zscript</code>
	 * and <code>org.zkoss.zk.ui.composer.autowire.xel</code>.
	 * Furthermore, if not specified, their values are default to <b>false</b>, i.e., 
	 * they shall <tt>not</tt> be wired (i.e., shall be ignored)
	 * <p>If you want to control whether to wire ZSCRIPT's or XEL's variable
	 * explicitly, you could use
	 * {@link #GenericForwardComposer(char,boolean,boolean)} instead.
	 *
	 * <h2>Version Difference</h2>
	 * <p>ZK 5.0 and earlier, this constructor is the same as
	 * <code>GenericForwardComposer('$', false, false)</code><br/>
	 * In other words, it is default to wire (i.e., shall <i>not</i> ignore).
	 * @since 3.6.0
	 */
	protected GenericForwardComposer(char separator) {
		super(separator);
	}
	/** Constructor with full control.
	 * @param separator the separator used to separate the component ID and event name.
	 * Refer to {@link #_separator} for details.
	 * @param ignoreZScript whether to ignore variables defined in zscript when wiring
	 * a member.
	 * @param ignoreXel whether to ignore variables defined in variable resolver
	 * ({@link org.zkoss.zk.ui.Page#addVariableResolver}) when wiring a member.
	 * @since 5.0.3
	 */
	protected GenericForwardComposer(char separator, boolean ignoreZScript,
	boolean ignoreXel) {
		super(separator, ignoreZScript, ignoreXel);
	}

	/**
	 * Auto forward events and wire accessible variables of the specified 
	 * component into a controller Java object; a subclass that 
	 * override this method should remember to call super.doAfterCompose(comp) 
	 * or it will not work.
	 */
	public void doAfterCompose(T comp) throws Exception {
		super.doAfterCompose(comp);
		
		//add forward conditions to the components as defined in this composer
		//onXxx$myid
		ConventionWires.addForwards(comp, this, _separator);
	}
}
