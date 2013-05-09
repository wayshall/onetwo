package org.onetwo.common.web.s2.security;



/*****
 * 验证服务
 * @author wayshall
 *
 */
public interface AuthenticationInvocation {
	
	public String NAME = "defaultSecurityAuthentication"; 
	
//	public UserDetail getCurrentLoginUser(HttpServletRequest request);

	public void authenticate(AuthenticationContext context) throws Exception;
}
