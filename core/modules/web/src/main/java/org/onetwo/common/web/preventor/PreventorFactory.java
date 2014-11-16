package org.onetwo.common.web.preventor;

import org.onetwo.common.web.csrf.SameInSessionCsrfPreventor;

final public class PreventorFactory {
	
	
	private static final PreventRequestInfoManager preventRequestInfoManager = new DefaultPreventRequestInfoManager();
	private static final RequestPreventor CSRF_PREVENTOR = new SameInSessionCsrfPreventor(preventRequestInfoManager);//new SessionStoreCsrfPreventor();
	private static final RequestPreventor REPEATE_SUBMIT_PREVENTOR = new SessionRepeateSubmitPreventor(preventRequestInfoManager);//new SessionStoreCsrfPreventor();

	
	public static RequestPreventor getCsrfPreventor() {
		return CSRF_PREVENTOR;
	}
	
	public static RequestPreventor getRepeateSubmitPreventor() {
		return REPEATE_SUBMIT_PREVENTOR;
	}

	private PreventorFactory(){}

}
