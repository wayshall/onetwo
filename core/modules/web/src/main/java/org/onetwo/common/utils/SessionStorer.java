package org.onetwo.common.utils;

import org.onetwo.common.utils.UserDetail;

public interface SessionStorer {

	public void addUser(String sessionKey, UserDetail userDetail);

	public UserDetail getUser(String sessionKey);
	public UserDetail removeUser(String sessionKey);
}
