package org.onetwo.common.web.s2.security.config;

import org.onetwo.common.web.s2.security.AuthenticationInvocation;
import org.onetwo.common.web.s2.security.SecurityTarget;

/**********
 * 验证配置和获取实际验证执行者的服务接口
 * @author wayshall
 *
 */
public interface AuthenticConfigService {
	public static final String NAME = "authenticConfigService";
	
	/****
	 * 获取验证配置
	 * @param target
	 * @return
	 */
	public AuthenticConfig getConfig(SecurityTarget target);
	
	public AuthenticationInvocation getAuthenticationInvocation(AuthenticConfig config);
	
}