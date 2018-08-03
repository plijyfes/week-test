/* ClientConstraint.java

	Purpose:
		
	Description:
		
	History:
		Wed Apr 11 18:11:54     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zul;

/**
 * Addition interface implemented with {@link Constraint} to handle
 * the validation at the client.
 *
 * <p>Note: this interface is ignored if {@link CustomConstraint}
 * is also implemented, since {@link CustomConstraint} causes
 * all validations are processed at the server.
 *
 * @author tomyeh
 * @see Constraint
 * @see CustomConstraint
 */
public interface ClientConstraint {
	/** Returns the JavaScript snippet that will be evaluated at client
	 * to return a validator, or null if no client constraint is supported.
	 * The validator is later used to validate an input.
	 *
	 * <p>For example,
	 * <pre><code>String getClientConstraint() {
	 *  return "new foo.MyValidator()";
	 *}</code></pre>
	 *
	 * <p>Instead of return the snippet of JavaScript codes, it can return
	 * an instance of JavaScript string (enclosed with quotation),
	 * if the validator is zul.inp.SimpleConstraint.
	 *
	 * <p>For example,
	 * <pre><code>String getClientConstraint() {
	 *  return "'no empty'";
	 *}</code></pre>
	 *
	 * <p>The validator could implement the <code>validate</code>,
	 * and <code>showCustomError</code> methods, and an optional property,
	 * <code>serverValidate</code>
	 * methods as follow. <code>validate</code> is required,
	 * while <code>showCustomError</code> and <code>serverValidate</code> are optional.
	 *
	 * <pre><code>String validate(Widget wgt, String value);
	 *Object showCustomError(Widget wgt, String errmsg);
	 *boolean serverValidate;</code></pre>
	 *
	 * <p>Please refer to
	 * <a href="http://www.zkoss.org/javadoc/latest/jsdoc/zul/inp/SimpleConstraint.html">zul.inp.SimpleConstraint</a>
	 * for details.
	 *
	 * <p>Notice that {@link CustomConstraint} has the higher priority than
	 * {@link ClientConstraint}. In other words, {@link ClientConstraint}
 	 * is ignored if both defined.
	 *
	 * @return the code snippet that will be evaluated at client to
	 * return a validator.
	 * @since 5.0.0
	 */
	public String getClientConstraint();
	/** Returns a list of packages separated by comma that ZK client
	 * engine has to load before evaluating {@link #getClientConstraint}.
	 * <p>For example,
	 * <pre><code>com.foo,com.foo.more</code></pre>
	 * @since 5.0.0
	 */
	public String getClientPackages();
}
