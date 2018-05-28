package org.onetwo.boot.module.oauth2.clientdetails;

import java.io.Serializable;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultOauth2ClientDetailManager implements Oauth2ClientDetailManager {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private ClientDetailsObtainService clientDetailsObtainService;
	
	@Override
	public <T extends Serializable> Optional<T> getCurrentClientDetail() {
		Optional<T> current = getCurrentClientDetail(request);
		return current;
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> Optional<T> getCurrentClientDetail(HttpServletRequest request) {
		Optional<String> at = clientDetailsObtainService.getTokenValue(request);
		if(!at.isPresent()){
			return Optional.empty();
		}
		Optional<T> opt = (Optional<T>)OAuth2Utils.getOrSetClientDetails(request, ()->{
			return clientDetailsObtainService.resolveClientDetails(at.get());
		});
		return opt;
	}
	

}
