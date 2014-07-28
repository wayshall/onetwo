package org.onetwo.common.web.login;

public interface ValidatableUser<T> {

	T getUserData();
	String getUserName();
	String getPassword();
	boolean isInvalidUserStatus();
}
