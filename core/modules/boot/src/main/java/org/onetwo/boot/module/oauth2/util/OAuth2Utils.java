package org.onetwo.boot.module.oauth2.util;

import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.web.utils.BootWebUtils;

/**
 * @author wayshall
 * <br/>
 */
public abstract class OAuth2Utils {
	public static final String CLIENT_DETAILS_ATTR_KEY = "__CLIENT_DETAILS__";

	public static <T> Optional<T> getCurrentClientDetails() {
		HttpServletRequest req = BootWebUtils.request();
		if(req==null){
			return Optional.empty();
		}
		return getClientDetails(req);
	}
	public static <T> Optional<T> getClientDetails(HttpServletRequest request) {
		return getOrSetClientDetails(request, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Optional<T> getOrSetClientDetails(HttpServletRequest request, Supplier<T> supplier) {
		Object data = request.getAttribute(CLIENT_DETAILS_ATTR_KEY);
		if(data!=null){
			return Optional.of((T)data);
		}
		
		if(supplier==null){
			return Optional.empty();
		}
		
		T details = supplier.get();
		request.setAttribute(CLIENT_DETAILS_ATTR_KEY, details);
		return Optional.ofNullable(details);
	}
	
	private OAuth2Utils(){
	}
	
}
