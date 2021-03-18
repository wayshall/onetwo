package org.onetwo.boot.module.oauth2.ssoclient.tokeninfo;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 单点登录用户信息加载接口
 * 接入单点登录的业务系统可以扩展此接口，根据单点登录token 接口返回的用户数据，加载对应本地系统的用户的登录信息
 * @author weishao zeng
 * <br/>
 */
public interface SSoUserDetailsService {
	
	/****
	 * 
	 * @author weishao zeng
	 * @param oauthUserDataMap 从oauth2 tokenInfo 接口获取到的用户数据 
	 * @return
	 */
	UserDetails loadUserByOAuth2User(Map<String, ?> oauthUserDataMap);

}
