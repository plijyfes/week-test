package org.exam.Forum.services.impl;

import java.util.Map;

import org.exam.Forum.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

@Service
public class AuthenticationInit implements Initiator {

	AuthenticationService authenticationService = new AuthenticationServiceImpl();

	@Override
	public void doInit(Page page, Map<String, Object> arg) throws Exception {
		UserCredential cre = authenticationService.getUserCredential();
		if (cre == null || cre.isAnonymous()) {
			Executions.sendRedirect("/pages/login.zul");
			return;
		}
	}
}
