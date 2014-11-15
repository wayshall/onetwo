package org.onetwo.common.web.preventor;


public class SessionRepeateSubmitPreventor extends SessionStoreRequestPreventor {
	public static final String DEFAULT_CSRF_TOKEN_FIELD = "_jfish.submit.token";
	
	public SessionRepeateSubmitPreventor() {
		super(DEFAULT_CSRF_TOKEN_FIELD);
	}

}
