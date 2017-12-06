package org.onetwo.boot.dsrouter;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wayshall
 * <br/>
 */
public class CookiesLookupKeyStrategy implements LookupKeyStrategy, InitializingBean {
	
	public static final String DEFAULT_HEADER_KEY = "_d_s";
	private String cookieName = DEFAULT_HEADER_KEY;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isBlank(cookieName)){
			this.cookieName = DEFAULT_HEADER_KEY;
		}
	}

	@Override
	public Object lookup() {
		String value = RequestUtils.getCookieValue(BootWebUtils.request(), cookieName);
		if(StringUtils.isBlank(value)){
			return null;
		}
		return value;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

}
