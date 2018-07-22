package org.exam.Forum.services.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.exam.Forum.dao.UserDao;
import org.exam.Forum.entity.User;
import org.exam.Forum.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

@Service("authenticationService")
public class AuthenticationServiceImpl implements Serializable, AuthenticationService {

	final static Logger logger = Logger.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	UserDao userDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.exam.Forum.services.AuthenticationService#getUserCredential()
	 */
	@Override
	public UserCredential getUserCredential() {
		Session sess = Sessions.getCurrent();
		UserCredential cre = (UserCredential) sess.getAttribute("userCredential");
		if (cre == null) {
			cre = new UserCredential();// new a anonymous user and set to session
			sess.setAttribute("userCredential", cre);
		}
		return cre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.exam.Forum.services.AuthenticationService#login(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean login(String account, String password) throws Exception {
		User user = userDao.findByAccount(account);
		if (user == null || !user.getPassword().equals(Md5Util.md5(password))) {
			return false;
		}
		Session sess = Sessions.getCurrent();
		UserCredential cre = new UserCredential(user.getAccount(), user.getNickName());

		sess.setAttribute("userCredential", cre);
		logger.warn("login account : " + cre.getAccount());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.exam.Forum.services.AuthenticationService#logout()
	 */
	@Override
	public void logout() {
		Session sess = Sessions.getCurrent();
		logger.warn("logout account : " + ((UserCredential) sess.getAttribute("userCredential")).getAccount());
		sess.removeAttribute("userCredential");
	}
}
