package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class AbstractClientDetailController extends AbstractBaseController {
	@Autowired(required=false)
	protected Oauth2ClientDetailManager oauth2ClientDetailManager;
	
	protected Optional<ClientDetails> getCurrentClientDetail(){
		return oauth2ClientDetailManager.getCurrentClientDetail();
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends ClientDetails> Optional<T> getCurrentClientDetail(Class<T> clazz){
		return (Optional<T>)oauth2ClientDetailManager.getCurrentClientDetail();
	}
}
