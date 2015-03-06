package org.onetwo.common.utils;


public interface SessionStorer {

	public void addUser(String sessionKey, UserDetail userDetail);

	public UserDetail getUser(String sessionKey);
	public UserDetail removeUser(String sessionKey);
	
	public void put(String key, Object value);
	public <T> T get(String key);
}
