package org.onetwo.boot.module.oauth2.result;

import org.onetwo.boot.core.web.view.DefaultDataResultWrapper;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

/**
 * @author wayshall
 * <br/>
 */
public class OAuth2DataResultWrapper extends DefaultDataResultWrapper {

	@Override
	public Object wrapResult(final Object data) {
		if(data instanceof DefaultOAuth2AccessToken){
			DefaultOAuth2AccessToken at = (DefaultOAuth2AccessToken) data;
			return DataResults.map("access_token", at.getValue(),
								   "expires_in", at.getExpiresIn()).build();
		}
		Object newData = super.wrapResult(data);
		return newData;
	}

}
