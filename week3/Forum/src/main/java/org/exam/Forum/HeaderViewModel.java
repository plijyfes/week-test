package org.exam.Forum;

import org.exam.Forum.services.AuthenticationService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class HeaderViewModel {
	
	@WireVariable
	private AuthenticationService authenticationService;
	private String message;
	private String title;
	
	@Init
	public void init() {
		title = "Wellcome to week3!";
		message = "Hi, " + authenticationService.getUserCredential().getName();
	}

	public String getMessage() {
		return message;
	}
	
	public String getTitle() {
		return title;
	}

	@Command("logout")
	public void logout() {
		authenticationService.logout();
		Executions.sendRedirect("/index.zul");
	}
	
}
