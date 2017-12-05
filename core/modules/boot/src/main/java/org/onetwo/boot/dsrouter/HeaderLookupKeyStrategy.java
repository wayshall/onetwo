package org.onetwo.boot.dsrouter;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wayshall
 * <br/>
 */
public class HeaderLookupKeyStrategy implements LookupKeyStrategy, InitializingBean {
	
	public static final String DEFAULT_HEADER_KEY = "X-Request-Datasource";
	private String headerName = DEFAULT_HEADER_KEY;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isBlank(headerName)){
			this.headerName = DEFAULT_HEADER_KEY;
		}
	}

	@Override
	public Object lookup() {
		String value = BootWebUtils.getHeader(headerName, "");
		if(StringUtils.isBlank(value)){
			return null;
		}
		return value;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	

}
