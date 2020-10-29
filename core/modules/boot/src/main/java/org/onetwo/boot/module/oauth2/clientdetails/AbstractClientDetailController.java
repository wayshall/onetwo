package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.module.oauth2.util.OAuth2Errors;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class AbstractClientDetailController extends AbstractBaseController {
	@Autowired(required=false)
	protected Oauth2ClientDetailManager oauth2ClientDetailManager;
	
	protected ClientDetails requiredCurrentClientDetail(){
		Optional<ClientDetails> opt = getOauth2ClientDetailManager().getCurrentClientDetail();
		return opt.orElseThrow(()->new ServiceException(OAuth2Errors.CLIENT_ACCESS_TOKEN_NOT_FOUND));
	}
	
	protected <T extends OAuth2ClientDetail> Optional<T> getCurrentClientDetail(Class<T> clazz){
		return getOauth2ClientDetailManager().getCurrentClientDetail();
	}
	
	protected <T extends OAuth2ClientDetail> T requiredCurrentClientDetail(Class<T> clazz){
		Optional<T> opt = getOauth2ClientDetailManager().getCurrentClientDetail();
		return opt.map(d -> clazz.cast(d)).orElseThrow(()->new ServiceException(OAuth2Errors.CLIENT_ACCESS_TOKEN_NOT_FOUND));
	}

	protected Oauth2ClientDetailManager getOauth2ClientDetailManager() {
		return oauth2ClientDetailManager;
	}
	
}
