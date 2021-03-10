package org.onetwo.boot.module.oauth2ssoclient;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.module.oauth2ssoclient.OAuth2SsoClientUserRepository.OAuth2SsoClientUser;

/**
 * 对应 WechatOAuth2UserRepository
 * 
 * T OAuth2UserInfo
 * @author wayshall
 * <br/>
 */
public interface OAuth2SsoClientUserRepository<T extends OAuth2SsoClientUser> {

	Optional<T> getCurrentUser(OAuth2SsoContext request);

	void saveCurrentUser(OAuth2SsoContext request, T userInfo, boolean refresh);

	default boolean checkOauth2State(OAuth2SsoContext request) {
		HttpSession session = request.getRequest().getSession();
		if(session!=null){
			String state = request.getState();
			String storedState = (String)session.getAttribute(Oauth2SsoClients.STORE_STATE_KEY);
//			Logger logger = JFishLoggerFactory.getCommonLogger();
//			logger.info("storedState: {}, param state: {}", storedState, state);
			session.removeAttribute(Oauth2SsoClients.STORE_STATE_KEY);
			return StringUtils.isNotBlank(state) && state.equals(storedState);
		}
		return false;
	}

	/****
	 * 生成state参数
	 * @author wayshall
	 * @param request
	 * @return
	 */
	default String generateAndStoreOauth2State(OAuth2SsoContext request){
		String state = UUID.randomUUID().toString();
		HttpSession session = request.getRequest().getSession();
		if(session!=null){
//			Logger logger = JFishLoggerFactory.getCommonLogger();
//			logger.info("storedState: {}", state);
			session.setAttribute(Oauth2SsoClients.STORE_STATE_KEY, state);
		}
		return state;
	}

	/***
	 * OAuth2User
	 * @author way
	 *
	 */
	public interface OAuth2SsoClientUser {
		/***
		 * 是否过时
		 * @author weishao zeng
		 * @return
		 */
		default boolean isAccessTokenExpired() {
			return false;
		}
		/****
		 * accesstoken已过期时，是否采用刷新token的方式更新token和用户信息
		 * 
		 * @author weishao zeng
		 * @return
		 */
		default boolean isRefreshToken() {
			return false;
		}
	}
}