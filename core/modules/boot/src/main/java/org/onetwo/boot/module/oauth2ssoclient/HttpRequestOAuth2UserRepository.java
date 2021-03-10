package org.onetwo.boot.module.oauth2ssoclient;

import java.util.Optional;
import java.util.UUID;

import org.onetwo.boot.module.oauth2ssoclient.OAuth2SsoClientUserRepository.OAuth2SsoClientUser;
import org.onetwo.boot.module.redis.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;

/**
 * 
 * @author wayshall
 * <br/>
 */
public class HttpRequestOAuth2UserRepository<T extends OAuth2SsoClientUser> implements OAuth2SsoClientUserRepository<T> {
	@Setter
	private String userInfoKey = Oauth2SsoClients.STORE_USER_INFO_KEY;
	@Autowired
	private TokenValidator tokenValidator;
	
	@SuppressWarnings("unchecked")
	@Override
	public Optional<T> getCurrentUser(OAuth2SsoContext request){
		T userInfo = (T)request.getRequest().getAttribute(userInfoKey);
		return Optional.ofNullable(userInfo);
	}
	@Override
	public void saveCurrentUser(OAuth2SsoContext request, T userInfo, boolean refresh){
		request.getRequest().setAttribute(userInfoKey, userInfo);
	}
	
	public boolean checkOauth2State(OAuth2SsoContext request) {
		String key = getStateKey(request.getState());
		boolean check = tokenValidator.check(key, request.getState(), false);
		return check;
	}

	protected String getStateKey(String state) {
		return "ssoclient_oauth2_state:" + state;
	}
	/****
	 * 生成state参数
	 * @author wayshall
	 * @param request
	 * @return
	 */
	public String generateAndStoreOauth2State(OAuth2SsoContext request){
		String state = UUID.randomUUID().toString();
		String key = getStateKey(state);
		this.tokenValidator.save(key, () -> state);
		return state;
	}
}
