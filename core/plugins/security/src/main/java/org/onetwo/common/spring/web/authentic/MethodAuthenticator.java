package org.onetwo.common.spring.web.authentic;

import org.onetwo.common.web.s2.security.AuthenticationContext;
import org.onetwo.common.web.s2.security.AuthenticationInvocation;

/********
 * 简便的验证接口
 * 直接在controller实现此接口，即可绕过AuthenticationInvocation的authenticate验证方法，直接在controller里实现验证逻辑
 * 注意，如果实现了此接口，意味着所有Authentic注解的逻辑都不会 被执行，而由你自己去验证。
 * @author wayshall
 *
 */
public interface MethodAuthenticator {
	
	public void authenticate(AuthenticationInvocation authentication, AuthenticationContext context) throws Exception;
	
}
