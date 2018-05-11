package org.onetwo.boot.limiter;

import java.util.Map;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;

import com.google.common.collect.Maps;


/**
 * @author wayshall
 * <br/>
 */
public interface InvokeContext {
	
	InvokeType getInvokeType();
	String getRequestPath();
	String getClientUser();
	String getClientIp();
	String getServiceId();
	int getInvokeTimes();
	
	default <T> Optional<T> getAttributeOpt(String key) {
		return Optional.empty();
	}
	
	void setAttribute(String key, Object value);
	
	public enum InvokeType {
		BEFORE,
		AFTER,
		ALL
	}
	
	@Data
	@Builder
	public class DefaultInvokeContext implements InvokeContext {
		InvokeType invokeType;
		String requestPath;
		String clientUser;
		String clientIp;
		String serviceId;
		int invokeTimes = 1;
		
		Map<String, Object> attributes = Maps.newHashMap();

		@SuppressWarnings("unchecked")
		@Override
		public <T> Optional<T> getAttributeOpt(String key) {
			return Optional.ofNullable((T)attributes.get(key));
		}
		
		@Override
		public void setAttribute(String key, Object value) {
			attributes.put(key, value);
		}
	}
}
