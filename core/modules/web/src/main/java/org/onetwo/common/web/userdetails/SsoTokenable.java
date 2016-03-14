package org.onetwo.common.web.userdetails;

public interface SsoTokenable {

	public final static String TOKEN_KEY = "_tk_"; 
	
	public String getToken();
	
	public void setToken(String token);
}
