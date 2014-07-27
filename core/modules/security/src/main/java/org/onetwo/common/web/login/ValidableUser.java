package org.onetwo.common.web.login;

public interface ValidableUser<T> {

	T getUserData();
	String getUserName();
	String getPassword();
	boolean isInvalidUserStatus();
}
