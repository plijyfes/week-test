package org.exam.Forum;

import org.exam.Forum.services.AuthenticationService;
import org.exam.Forum.services.impl.UserCredential;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class LoginViewModel {

	@WireVariable
	private AuthenticationService authenticationService;
	private String account;
	private String password;
	private String message = "";

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Command("logIn")
	public void logIn() throws Exception {
		if (!authenticationService.login(account, password)) {
			setMessage("account or password are not correct.");
			return;
		}
		// UserCredential cre = authenticationService.getUserCredential();
		// setMessage("Welcome, " + cre.getName());
		Executions.sendRedirect("/index.zul");
	}
}
