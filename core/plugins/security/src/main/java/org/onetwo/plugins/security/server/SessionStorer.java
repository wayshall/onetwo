package org.onetwo.plugins.security.server;

import org.onetwo.common.utils.UserDetail;

public interface SessionStorer {

	public void addUser(UserDetail userDetail);
	
	public UserDetail getUser(String token);
}
