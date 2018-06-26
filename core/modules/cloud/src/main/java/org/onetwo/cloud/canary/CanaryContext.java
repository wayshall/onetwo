package org.onetwo.cloud.canary;

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
}
