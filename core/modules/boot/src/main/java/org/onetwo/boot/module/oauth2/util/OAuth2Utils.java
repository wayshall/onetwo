package org.onetwo.boot.module.oauth2.util;

import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wayshall
 * <br/>
 */
public abstract class OAuth2Utils {
	public static final String CLIENT_DETAILS_ATTR_KEY = "__CLIENT_DETAILS__";

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> getOrSetClientDetails(HttpServletRequest request, Supplier<T> supplier) {
		Object data = request.getAttribute(CLIENT_DETAILS_ATTR_KEY);
		if(data!=null){
			return Optional.of((T)data);
		}
		
		T details = supplier.get();
		request.setAttribute(CLIENT_DETAILS_ATTR_KEY, details);
		return Optional.ofNullable(details);
	}
	
	private OAuth2Utils(){
	}
	
}
