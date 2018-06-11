package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;


/**
 * @author wayshall
 * <br/>
 */
public interface Oauth2ClientDetailManager {

	<T extends ClientDetails> Optional<T> getCurrentClientDetail();
	
}
