package org.onetwo.plugins.security.common;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.SessionStorer;
import org.onetwo.common.utils.UserDetail;

public class HttpSessionStorer implements SessionStorer {

	public void addUser(String sessionKey, UserDetail userDetail){
		JFishWebUtils.session(sessionKey, userDetail);
	}

	@Override
	public UserDetail getUser(String sessionKey) {
		UserDetail userDetail = JFishWebUtils.session(sessionKey);
		return userDetail;
	}

	@Override
	public UserDetail removeUser(String sessionKey) {
		return JFishWebUtils.removeUserDetail(sessionKey);
	}
	
}
