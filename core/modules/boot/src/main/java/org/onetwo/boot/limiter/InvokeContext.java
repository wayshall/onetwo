package org.onetwo.boot.limiter;

import java.util.Optional;

import lombok.Builder;
import lombok.Data;


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
	
	default Optional<String> getAttribute(String key) {
		return Optional.empty();
	}
	
	public enum InvokeType {
		BEFORE,
		AFTER
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
	}
}
