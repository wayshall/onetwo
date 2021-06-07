package org.springframework.cloud.netflix.feign;

import java.util.function.Supplier;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author weishao zeng
 * <br/>
 */
public class LocalFeignTransactionWrapper {
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Object wrapRequiresNew(Supplier<Object> action) {
		return action.get();
	}

}
