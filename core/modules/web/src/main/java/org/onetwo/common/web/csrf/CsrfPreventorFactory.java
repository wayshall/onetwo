package org.onetwo.common.web.csrf;

final public class CsrfPreventorFactory {
	

	private static final CsrfPreventor DEFAULT = new SameInSessionCsrfPreventor();//new SessionStoreCsrfPreventor();
	
	
	public static CsrfPreventor getDefault() {
		return DEFAULT;
	}



	private CsrfPreventorFactory(){}

}
