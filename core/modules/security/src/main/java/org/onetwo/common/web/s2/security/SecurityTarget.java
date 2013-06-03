package org.onetwo.common.web.s2.security;

import java.util.Map;

import org.onetwo.common.utils.UserDetail;

public interface SecurityTarget {
	
	/******
	 * 调用action
	 * 由于最初是针对strtus2写的，此方法struts2才有效，实际上在spring mvc里没什么用
	 * @return
	 * @throws Exception
	 */
//	public Object execute() throws Exception;
	
	public SecurityTarget addMessage(String msg);
	
	public Object getInvocation();
	
	public UserDetail getAuthoritable();
	
	/***
	 * 如果token不是放在cookie里，直接返回session对象里的token 
	 * @return
	 */
	public String getCookieToken();
	
	public void removeCurrentLoginUser();
	void setCurrentLoginUser(UserDetail userDetail);
	public void removeCookieToken();
	
	public String[] getRoles();
	public boolean containsRole(String role);
	public void setCookieToken(String token);
	
	public Map<String, Object> getDatas();
	public void putData(String key, Object value);
	public Object getData(String key);
	
}
