package org.springframework.cloud.netflix.feign;

import feign.Feign;
import feign.Target;


/**
 * @author wayshall
 * <br/>
 */
public class EnhanceHystrixTargeter implements Targeter {
	
	private HystrixTargeter hystrixTargeter = new HystrixTargeter();
	
	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context, Target.HardCodedTarget<T> target) {

		return hystrixTargeter.target(factory, feign, context, target);
	}

}
