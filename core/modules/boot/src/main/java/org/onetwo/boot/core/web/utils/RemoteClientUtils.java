package org.onetwo.boot.core.web.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.utils.WebHolder;

/**
 * @author wayshall
 * <br/>
 */
public class RemoteClientUtils {
	public static final String HEADER_CLIENT_TYPE = "X-Client-Type";
	
	public static enum ClientTypes {
		AJAX,
		FEIGN;
		
		public static Optional<ClientTypes> of(String type){
			try {
				return Optional.of(ClientTypes.valueOf(type));
			} catch (Exception e) {
				return Optional.empty();
			}
		}
		
		public static Optional<ClientTypes> parse(HttpServletRequest request){
			if(request==null){
				return Optional.empty();
			}
			String header = request.getHeader(HEADER_CLIENT_TYPE);
			return ClientTypes.of(header);
		}
	}
	
	public static Optional<ClientTypes> getCurrentClientType(){
		HttpServletRequest request = WebHolder.getRequest().orElse(null);
		return ClientTypes.parse(request);
	}
	
	public static boolean isFeign(){
		return isClientType(ClientTypes.FEIGN);
	}
	public static boolean isClientType(ClientTypes clientType){
		Optional<ClientTypes> ct = getCurrentClientType();
		return ct.isPresent() && ct.get().equals(clientType);
	}
	
	
}
