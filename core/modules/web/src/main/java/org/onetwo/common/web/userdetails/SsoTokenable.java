package org.onetwo.common.web.userdetails;

/***
 *  以前自己实现的一套的sso的遗留接口
 * 已废弃
 * @author wayshall
 *
 */
@Deprecated
public interface SsoTokenable {

	public final static String TOKEN_KEY = "_tk_"; 
	
	public String getToken();
	
	public void setToken(String token);
}
