package org.onetwo.common.web.csrf;

final public class CsrfPreventorFactory {
	

	private static final CsrfPreventor DEFAULT = new SessionStoreCsrfPreventor();
	
	
	public static CsrfPreventor getDefault() {
		return DEFAULT;
	}



	private CsrfPreventorFactory(){}

}
