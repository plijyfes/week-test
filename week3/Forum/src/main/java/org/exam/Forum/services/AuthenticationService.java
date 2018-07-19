package org.exam.Forum.services;

import org.exam.Forum.services.impl.UserCredential;

public interface AuthenticationService {

	UserCredential getUserCredential();

	boolean login(String account, String password) throws Exception;

	void logout();

}