package org.onetwo.cloud.canary;

import lombok.Data;

import com.netflix.loadbalancer.PredicateKey;

/**
 * @author wayshall
 * <br/>
 */
public interface CanaryContext {

	String getRequestPath();
	String getClientTag();
	String getClientIp();
	
	PredicateKey getPredicateKey();
	
//	String getClientId();
//	String getServiceId();
	
	@Data
	static public class DefaultCanaryContext implements CanaryContext {
		private String requestPath;
		private String clientTag;
		private String clientIp;
		private PredicateKey predicateKey;
	}
}
